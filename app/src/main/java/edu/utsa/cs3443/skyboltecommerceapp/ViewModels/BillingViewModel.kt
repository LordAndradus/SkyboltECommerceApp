package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Data.Address
import edu.utsa.cs3443.skyboltecommerceapp.Helper.ResourceSignaler
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.ADDRESS_SUBCOLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.USER_COLLECTION
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authenticator: FirebaseAuth
): ViewModel() {

    val address = ResourceSignaler<List<Address>>(this)

    init {
        getUserAddresses()
    }

    fun getUserAddresses()
    {
        address.start()

        firestore.collection(USER_COLLECTION).document(authenticator.uid!!)
            .collection(ADDRESS_SUBCOLLECTION)
            .addSnapshotListener { value, e ->
                if(e != null)
                {
                    address.error(e)

                    return@addSnapshotListener
                }

                val addresses = value?.toObjects(Address::class.java)
                address.success(addresses)
            }
    }
}