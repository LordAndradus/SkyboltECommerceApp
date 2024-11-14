package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Data.ProductLister
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {

    val specialProductsLister = ProductLister(this, firestore)
    val bestDealsLister = ProductLister(this, firestore)
    val bestProductsLister = ProductLister(this, firestore)
    val allProductsLister = ProductLister(this, firestore)

    init {
        //Set fetch type
        specialProductsLister.setFetch(ProductLister.FetchParams.WithFilter(1L, "special", true))
        bestDealsLister.setFetch(ProductLister.FetchParams.WithFilter(1L, "bestDeal", true))
        bestProductsLister.setFetch(ProductLister.FetchParams.WithFilter(-1L, "bestProduct", true))
        allProductsLister.setFetch(ProductLister.FetchParams.WithoutFilter(10L))
    }
}