package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.Data.Product
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants
import edu.utsa.cs3443.skyboltecommerceapp.Util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Idle())
    val specialProducts: StateFlow<Resource<List<Product>>> = _specialProducts

    private val _bestDeals = MutableStateFlow<Resource<List<Product>>>(Resource.Idle())
    val bestDeals: StateFlow<Resource<List<Product>>> = _bestDeals

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Idle())
    val bestProducts: StateFlow<Resource<List<Product>>> = _bestProducts

    init {
        fetchSpecialProducts()
        fetchBestDeals()
        fetchBestProducts()
    }

    fun fetchSpecialProducts()
    {
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())
        }

        firestore.collection(Constants.PRODUCT_COLLECTION)
            .whereEqualTo("special", true).get()
            .addOnSuccessListener { result ->
                val specialProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Success(specialProductsList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestDeals()
    {
        viewModelScope.launch {
            _bestDeals.emit(Resource.Loading())
        }

        firestore.collection(Constants.PRODUCT_COLLECTION)
            .whereEqualTo("bestDeal", true).get()
            .addOnSuccessListener { result ->
                val specialProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestDeals.emit(Resource.Success(specialProductsList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _bestDeals.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestProducts()
    {
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
        }

        firestore.collection(Constants.PRODUCT_COLLECTION)
            .whereEqualTo("bestProduct", true).get()
            .addOnSuccessListener { result ->
                val specialProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(specialProductsList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}