package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Data.CartProduct
import edu.utsa.cs3443.skyboltecommerceapp.Firebase.FirebaseCommon
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.CART_SUBCOLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.USER_COLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    val authenticator: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
): ViewModel() {
    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.Idle())
    val addToCart = _addToCart.asStateFlow()

    fun addUpdateProductInCart(cartProduct: CartProduct)
    {
        viewModelScope.launch {
            _addToCart.emit(Resource.Loading())
        }

        firestore.collection(USER_COLLECTION)
            .document(authenticator.uid!!).collection(CART_SUBCOLLECTION)
            .whereEqualTo("product.id", cartProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let {
                    //Add the new product
                    if(it.isEmpty())
                    {
                        addNewProduct(cartProduct)
                    }
                    else
                    {
                        val product = it.first().toObject(CartProduct::class.java)
                        //Increase quantity of product
                        if(product!!.areEqualIgnoreQuantity(product, cartProduct))
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
                viewModelScope.launch {
                    _addToCart.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun addNewProduct(cartProduct: CartProduct)
    {
        firebaseCommon.addProductToCart(cartProduct) { addedProduct, e ->
            viewModelScope.launch {
                if(e == null)
                {
                    _addToCart.emit(Resource.Success(addedProduct!!))
                }
                else
                {
                    _addToCart.emit(Resource.Error(e.message.toString()))
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
                    _addToCart.emit(Resource.Success(cartProduct))
                }
                else
                {
                    _addToCart.emit(Resource.Error(e.message.toString()))
                }
            }
        }
    }
}