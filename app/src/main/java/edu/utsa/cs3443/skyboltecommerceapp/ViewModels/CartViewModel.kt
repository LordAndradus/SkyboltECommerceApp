package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Data.CartProduct
import edu.utsa.cs3443.skyboltecommerceapp.Firebase.FirebaseCommon
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.CART_SUBCOLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.USER_COLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Resource
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities.Companion.getProductPrice
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authenticator: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
): ViewModel() {
    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Idle())
    val cartProducts = _cartProducts.asStateFlow()

    private var cartProductDocuments = emptyList<DocumentSnapshot>()

    val productsPrice = cartProducts.map {
        when(it) {
            is Resource.Success -> {
                calculatePrice(it.data!!)
            }
            else -> null
        }
    }

    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()

    init {
        getCartProducts()
    }

    private fun getCartProducts()
    {
        viewModelScope.launch {
            _cartProducts.emit(Resource.Loading())
        }

        firestore.collection(USER_COLLECTION).document(authenticator.uid!!).collection(CART_SUBCOLLECTION)
            .addSnapshotListener { value, error ->
                if(error != null || value == null)
                {
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Error(error?.message.toString()))
                    }
                }
                else
                {
                    cartProductDocuments = value.documents
                    val cartProducts = value.toObjects(CartProduct::class.java)
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Success(cartProducts))
                        Resource.Success(cartProducts)
                    }
                }
            }
    }

    private fun calculatePrice(data: List<CartProduct>): Any
    {
        return data.sumByDouble { cartProduct ->
            (cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price) * cartProduct.quantity).toDouble()
        }.toFloat()
    }

    /**
     * Index could be equal be below positive if [getCartProducts] is repeatedly called before firestore is complete
     * We expect to be inside the [_cartProducts], and thus to prevent the app from crashing, we make a check for a positive value
     *
     * @param CartProduct
     * @param FirebaseCommon.QuantityChanging
     */
    fun changeQuantity(cartProduct: CartProduct, quantityChanging: FirebaseCommon.QuantityChanging)
    {
        val index = cartProducts.value.data?.indexOf(cartProduct)

        if(index != null && index >= 0)
        {
            val documentID = cartProductDocuments[index].id

            when(quantityChanging)
            {
                FirebaseCommon.QuantityChanging.INCREASED -> {
                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
                    increaseQuantity(documentID)
                }
                FirebaseCommon.QuantityChanging.DECREASED -> {
                    if(cartProduct.quantity == 1)
                    {
                        viewModelScope.launch {
                            _deleteDialog.emit(cartProduct)
                        }
                        return
                    }

                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
                    decreaseQuantity(documentID)
                }
            }
        }
    }

    private fun increaseQuantity(documentID: String)
    {
        firebaseCommon.increaseQuantity(documentID) { result, e ->
            if(e != null)
            {
                viewModelScope.launch {
                    _cartProducts.emit(Resource.Error(e.message.toString()))
                }
            }


        }
    }

    private fun decreaseQuantity(documentID: String)
    {
        firebaseCommon.decreaseQuantity(documentID) { result, e ->
            if(e != null)
            {
                viewModelScope.launch {
                    _cartProducts.emit(Resource.Error(e.message.toString()))
                }
            }


        }
    }

    fun deleteCartProduct(cartProduct: CartProduct)
    {
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if(index != null && index >= 0)
        {
            val documentID = cartProductDocuments[index].id

            firestore.collection(USER_COLLECTION).document(authenticator.uid!!)
                .collection(CART_SUBCOLLECTION).document(documentID).delete()
        }
    }
}