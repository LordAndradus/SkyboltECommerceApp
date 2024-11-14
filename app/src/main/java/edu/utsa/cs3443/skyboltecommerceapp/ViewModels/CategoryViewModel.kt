package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Data.Product
import edu.utsa.cs3443.skyboltecommerceapp.Util.Categories
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.PRODUCT_COLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel (
    private val firestore: FirebaseFirestore,
    private val category: Categories
): ViewModel() {
    private val _offerProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Idle())
    val offerProducts = _offerProducts.asStateFlow()

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Idle())
    val bestProducts = _bestProducts.asStateFlow()

    init {
        fetchOfferProducts()
        fetchBestProducts()
    }

    //TODO: Implement Paging

    fun fetchOfferProducts()
    {
        viewModelScope.launch {
            _offerProducts.emit(Resource.Loading())
        }

        firestore.collection(PRODUCT_COLLECTION)
            .whereEqualTo("category", category.category)
            .whereNotEqualTo("offerPercentage", null)
            .get()
            .addOnSuccessListener {
                val productList = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Success(productList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestProducts()
    {
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
        }

        firestore.collection(PRODUCT_COLLECTION)
            .whereEqualTo("category", category.category)
            .whereEqualTo("offerPercentage", null)
            .get()
            .addOnSuccessListener {
                val productList = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(productList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}