package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Data.Address
import edu.utsa.cs3443.skyboltecommerceapp.Helper.ResourceSignaler
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.ADDRESS_SUBCOLLECTION
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.USER_COLLECTION
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authenticator: FirebaseAuth
): ViewModel() {

    val addressHandler = ResourceSignaler<Address>(this)
    val addressCollection = ResourceSignaler<List<Address>>(this)

    var addressDocuments = emptyList<DocumentSnapshot>()
    var userID: String = ""

    init {
        userID = authenticator.uid!!
        retrieveAddressSnapshots()
    }

    private fun retrieveAddressSnapshots()
    {
        addressHandler.start()

        firestore.collection(USER_COLLECTION).document(userID)
            .collection(ADDRESS_SUBCOLLECTION)
            .addSnapshotListener { address, e ->
                if(e != null || address == null)
                {
                    if(e != null) addressHandler.error(e.message.toString())
                    if(address == null) addressHandler.error("No addresses found!")

                    return@addSnapshotListener
                }

                addressDocuments = address.documents
                addressCollection.success(address.toObjects(Address::class.java))
            }
    }

    fun setAddress(oldAddress: Address, address: Address)
    {
        val index = addressCollection.signal.value.data?.indexOf(oldAddress)
        addressHandler.start()

        if(addressHandler.isLoading())
        {
            return
        }

        addressHandler.start()

        if(index != null)
        {
            val addrDocRef = addressDocuments[index].id

            firestore.collection(USER_COLLECTION).document(userID)
                .collection(ADDRESS_SUBCOLLECTION).document(addrDocRef).set(address)
                .addOnSuccessListener {
                    retrieveAddressSnapshots()
                }
                .addOnFailureListener {
                    addressCollection.error("Failed to set address!")
                }
        }
    }

    fun addAddress(address: Address)
    {
        val validateInputs = validateInputs(address)

        if(!validateInputs)
        {
            addressHandler.error("All fields are required!")
            return
        }

        addressHandler.start()

        firestore.collection(USER_COLLECTION).document(userID)
            .collection(ADDRESS_SUBCOLLECTION).document().set(address)
            .addOnSuccessListener {
                addressHandler.success(address)
            }
            .addOnFailureListener {
                addressHandler.error(it)
            }
    }

    fun deleteAddress(address: Address)
    {
        val index = addressCollection.signal.value.data?.indexOf(address)
        addressHandler.start()

        if(addressHandler.isLoading())
        {
            return
        }

        addressHandler.start()

        if(index != null)
        {
            val addrDocRef = addressDocuments[index].id

            firestore.collection(USER_COLLECTION).document(userID)
                .collection(ADDRESS_SUBCOLLECTION).document(addrDocRef).delete()
                .addOnSuccessListener {
                    retrieveAddressSnapshots()
                }
                .addOnFailureListener {
                    addressCollection.error("Failed to delete address!")
                }
        }
    }

    private fun validateInputs(address: Address): Boolean
    {
        return address.addressTitle.trim().isNotEmpty()
                && address.fullName.trim().isNotEmpty()
                && address.street.trim().isNotEmpty()
                && address.phoneNumber.trim().isNotEmpty()
                && address.city.trim().isNotEmpty()
                && address.state.trim().isNotEmpty()
    }

}
