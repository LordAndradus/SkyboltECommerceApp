package edu.utsa.cs3443.skyboltecommerceapp.ViewModels.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import edu.utsa.cs3443.skyboltecommerceapp.Util.Categories
import edu.utsa.cs3443.skyboltecommerceapp.ViewModels.CategoryViewModel

class BaseCategoryViewModelFactory(
    private val firestore: FirebaseFirestore,
    private val category: Categories
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        return CategoryViewModel(firestore, category) as T
    }
}