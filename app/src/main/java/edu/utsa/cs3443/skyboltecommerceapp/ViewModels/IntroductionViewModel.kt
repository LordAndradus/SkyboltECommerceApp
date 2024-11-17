package edu.utsa.cs3443.skyboltecommerceapp.ViewModels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.INTRODUCTION_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _navigate = MutableStateFlow(0)
    val navigate: StateFlow<Int> = _navigate

    companion object {
        const val SHOPPING_ACTIVITY = -1
    }

    init {
        val isButtonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY, false)
        val user = firebaseAuth.currentUser

        if(user != null)
        {
            viewModelScope.launch {
                _navigate.emit(SHOPPING_ACTIVITY)
            }
        }
        else if(isButtonClicked)
        {
            viewModelScope.launch {
                _navigate.emit(R.id.accountOptionsFragment)
            }
        }
        else
        {
            Unit
        }
    }

    fun startButtonClick()
    {
        sharedPreferences.edit().putBoolean(INTRODUCTION_KEY, true).apply()
    }

    fun finishedIntroduction(): Boolean
    {
        return sharedPreferences.getBoolean(INTRODUCTION_KEY, false)
    }
}