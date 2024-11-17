package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Firebase.FirebaseCommon
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    val firestore: FirebaseFirestore,
    val authenticator: FirebaseAuth,
    val fCommon: FirebaseCommon
): ViewModel() {



}