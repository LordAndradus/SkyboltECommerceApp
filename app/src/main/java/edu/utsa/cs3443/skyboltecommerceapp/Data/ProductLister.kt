package edu.utsa.cs3443.skyboltecommerceapp.Data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import edu.utsa.cs3443.skyboltecommerceapp.Helper.ResourceSignaler
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.PRODUCT_COLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Resource
import kotlinx.coroutines.launch

class ProductLister(
    private val model: ViewModel,
    private val firestore: FirebaseFirestore,
): ResourceSignaler<List<Product>>(model) {

    private val page: Pager = Pager()

    private lateinit var params: FetchParams;
    private var paramsSet: Boolean = false

    fun setFetch(params: FetchParams = FetchParams.WithoutFilter())
    {
        paramsSet = true
        this.params = params
        fetch()
    }

    fun fetch()
    {
        if(!paramsSet) throw Exception("You need to set the params dunkhead!")

        if(!page.isPagingFinished)
        {
            start()

            var limit = -1L
            if(params is FetchParams.WithoutFilter && (params as FetchParams.WithoutFilter).limit != -1L) limit = (params as FetchParams.WithoutFilter).limit
            if(params is FetchParams.WithFilter && (params as FetchParams.WithFilter).limit != -1L) limit = (params as FetchParams.WithFilter).limit

            firestore.collection(PRODUCT_COLLECTION)
                .let { if(params is FetchParams.WithFilter) it.whereEqualTo((params as FetchParams.WithFilter).field, (params as FetchParams.WithFilter).fieldValue) else it }
                .let { if(limit > 0L) it.limit(page.currentPage * limit) else it }
                .get()
                .addOnSuccessListener { result ->
                    val productList = result.toObjects(Product::class.java)
                    model.viewModelScope.launch {
                        page.isPagingFinished = productList == page.oldList
                        page.oldList = productList
                        _signal.emit(Resource.Success(productList))
                    }
                    page.currentPage++
                    Log.d("Fetching", "Current page: ${page.currentPage}")
                }
                .addOnFailureListener {
                    error(it)
                }
        }
    }

    sealed class FetchParams
    {
        data class WithFilter(
            val limit: Long = -1L,
            val field: String,
            val fieldValue: Any?
        ) : FetchParams()

        data class WithoutFilter(
            val limit: Long = -1
        ) : FetchParams()
    }

    internal data class Pager(
        var currentPage: Long = 1,
        var oldList: List<Product> = emptyList(),
        var isPagingFinished: Boolean = false
    )
}