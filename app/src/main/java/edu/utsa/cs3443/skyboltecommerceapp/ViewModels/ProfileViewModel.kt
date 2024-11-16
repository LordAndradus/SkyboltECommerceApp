package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Data.User
import edu.utsa.cs3443.skyboltecommerceapp.Helper.ResourceSignaler
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.USER_COLLECTION
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authenticator: FirebaseAuth
): ViewModel() {
    val user = ResourceSignaler<User>(this)

    init {
        getUser()
    }

    fun getUser()
    {
        user.start()

        firestore.collection(USER_COLLECTION).document(authenticator.uid!!)
            .addSnapshotListener { value, e ->
                if(e != null)
                {
                    user.error(e)
                    return@addSnapshotListener
                }

                val userItem = value?.toObject(User::class.java)
                userItem?.let {
                    user.success(userItem)
                }
            }
    }

    fun logout()
    {
        authenticator.signOut()
    }
}