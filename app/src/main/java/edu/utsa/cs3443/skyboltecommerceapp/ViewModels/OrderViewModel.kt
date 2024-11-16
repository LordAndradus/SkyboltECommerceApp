package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Data.Order
import edu.utsa.cs3443.skyboltecommerceapp.Helper.ResourceSignaler
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.CART_SUBCOLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.ORDER_COLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.USER_COLLECTION
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authenticator: FirebaseAuth
): ViewModel() {
    val orders = ResourceSignaler<Order>(this)

    fun placeOrder(order: Order)
    {
        orders.start()

        firestore.runBatch { batch ->
            //Add the order into a sub-collection for users
            firestore.collection(USER_COLLECTION).document(authenticator.uid!!)
                .collection(ORDER_COLLECTION).document()
                .set(order)

            //Add the order into a general collection for administrators
            firestore.collection(ORDER_COLLECTION).document().set(order)

            //Delete each cart items as we placed the order
            firestore.collection(USER_COLLECTION).document(authenticator.uid!!)
                .collection(CART_SUBCOLLECTION).get()
                .addOnSuccessListener {
                    it.documents.forEach {
                        it.reference.delete()
                    }
                }
        }
            .addOnSuccessListener {
                orders.success(order)
            }
            .addOnFailureListener {
                orders.error(it)
            }
    }
}