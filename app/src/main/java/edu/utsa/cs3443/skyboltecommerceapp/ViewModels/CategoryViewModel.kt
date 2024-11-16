package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import edu.utsa.cs3443.skyboltecommerceapp.Data.Product
import edu.utsa.cs3443.skyboltecommerceapp.Helper.ResourceSignaler
import edu.utsa.cs3443.skyboltecommerceapp.Util.Categories
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.PRODUCT_COLLECTION

class CategoryViewModel (
    private val firestore: FirebaseFirestore,
    private val category: Categories
): ViewModel() {
    val offerProducts = ResourceSignaler<List<Product>>(this)
    val bestProducts = ResourceSignaler<List<Product>>(this)

    init {
        fetchOfferProducts()
        fetchBestProducts()
    }

    //TODO: Implement Paging

    fun fetchOfferProducts()
    {
        offerProducts.start()

        firestore.collection(PRODUCT_COLLECTION)
            .whereEqualTo("category", category.category)
            .whereNotEqualTo("offerPercentage", null)
            .get()
            .addOnSuccessListener {
                val productList = it.toObjects(Product::class.java)
                offerProducts.success(productList)
            }
            .addOnFailureListener {
                offerProducts.error(it)
            }
    }

    fun fetchBestProducts()
    {
        bestProducts.start()

        firestore.collection(PRODUCT_COLLECTION)
            .whereEqualTo("category", category.category)
            .whereEqualTo("offerPercentage", null)
            .get()
            .addOnSuccessListener {
                val productList = it.toObjects(Product::class.java)
                bestProducts.success(productList)
            }
            .addOnFailureListener {
                bestProducts.error(it)
            }
    }
}