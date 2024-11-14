package edu.utsa.cs3443.skyboltecommerceapp.Firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.utsa.cs3443.skyboltecommerceapp.Data.CartProduct
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.CART_SUBCOLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.USER_COLLECTION

class FirebaseCommon(
    private val firestore: FirebaseFirestore,
    private val authenticator: FirebaseAuth
){
    private val cartCollection = firestore.collection(USER_COLLECTION)
        .document(authenticator.uid!!).collection(CART_SUBCOLLECTION)

    fun addProductToCart(cartProduct: CartProduct, onResult: (CartProduct?, Exception?) -> Unit)
    {
        cartCollection.document().set(cartProduct)
            .addOnSuccessListener {
                onResult(cartProduct, null)
            }
            .addOnFailureListener {
                onResult(null, it)
            }
    }

    fun increaseQuantity(documentID: String, onResult: (String?, Exception?) -> Unit)
    {
        firestore.runTransaction { transaction ->
            val documentReference = cartCollection.document(documentID)
            val document = transaction.get(documentReference)
            val productObject = document.toObject(CartProduct::class.java)
            productObject?.let { cartProduct ->
                val newQuantity = cartProduct.quantity + 1
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transaction.set(documentReference, newProductObject)
            }
        }
        .addOnSuccessListener {
            onResult(documentID, null)
        }
        .addOnFailureListener {
            onResult(null, it)
        }
    }
}