package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Data.CartProduct
import edu.utsa.cs3443.skyboltecommerceapp.Firebase.FirebaseCommon
import edu.utsa.cs3443.skyboltecommerceapp.Helper.ResourceSignaler
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.CART_SUBCOLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.USER_COLLECTION
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    val authenticator: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
): ViewModel() {

    val addToCart = ResourceSignaler<CartProduct>(this)

    fun addUpdateProductInCart(cartProduct: CartProduct)
    {
        addToCart.start()

        firestore.collection(USER_COLLECTION)
            .document(authenticator.uid!!).collection(CART_SUBCOLLECTION)
            .whereEqualTo("product.id", cartProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let { product ->
                    //Add the new product
                    if(product.isEmpty())
                    {
                        addNewProduct(cartProduct)
                    }
                    else
                    {
                        val cartP = it.first().toObject(CartProduct::class.java)
                        //Increase quantity of product
                        if(cartP.areEqualIgnoreQuantity(cartP, cartProduct))
                        {
                            val documentID = it.first().id
                            increaseQuantity(documentID, cartProduct)
                        }
                        //Add as a new product
                        else
                        {
                            addNewProduct(cartProduct)
                        }
                    }
                }
            }
            .addOnFailureListener {
                addToCart.error(it)
            }
    }

    private fun addNewProduct(cartProduct: CartProduct)
    {
        firebaseCommon.addProductToCart(cartProduct) { addedProduct, e ->
            viewModelScope.launch {
                if(e == null)
                {
                    addToCart.success(addedProduct!!)
                }
                else
                {
                    addToCart.error(e)
                }
            }
        }
    }

    private fun increaseQuantity(documentID: String, cartProduct: CartProduct)
    {
        firebaseCommon.increaseQuantity(documentID) { _, e ->
            viewModelScope.launch {
                if(e == null)
                {
                    addToCart.success(cartProduct)
                }
                else
                {
                    addToCart.error(e)
                }
            }
        }
    }
}