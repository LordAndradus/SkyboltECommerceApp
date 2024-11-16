package edu.utsa.cs3443.skyboltecommerceapp.Helper

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton
import edu.utsa.cs3443.skyboltecommerceapp.Util.Resource
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * This class helps cut down on repetitive code when working with firebase.
 * Essentially, it works with both the viewModel and the fragment it's assigned to
 *
 */

class ResourceSignaler<T>(
    private val viewModel: ViewModel
) {
    private val _signal = MutableStateFlow<Resource<T>>(Resource.Idle())
    val signal = _signal.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    /**
     * This is to tell the ViewModel that the firebase is currently loading the information
     */
    fun start()
    {
        viewModel.viewModelScope.launch {
            _signal.emit(Resource.Idle())
        }
    }

    fun isLoading(): Boolean
    {
        return _signal.value == Resource.Loading<T>()
    }

    /**
     * This is to alert the ViewModel that the firebase has successfully retrieved the information
     * If it happens to pass null data, then we can send an alert in the error signaler
     *
     * @param T A unit of data from the firebase, it's a generic in that it passes what we need
     */
    fun success(data: T ?= null)
    {
        if(data == null)
        {
            error(Exception("Data returned null!"))
            return
        }

        viewModel.viewModelScope.launch {
            _signal.emit(Resource.Success(data))
        }
    }

    /**
     * If there is any error or exception, this function will handle that, there is the String and Exception version
     * This version emits the string error for Toast to read out the user
     *
     * @param String An error message
     */
    fun error(e: String)
    {
       viewModel.viewModelScope.launch {
           _signal.emit(Resource.Error(e))
       }
    }

    /**
     * If there is any error or exception, this function will handle that, there is the String and Exception version
     * This version converts the exception into a readable string and passes it to the String version
     *
     * @param String An error message
     */
    fun error(e: Exception)
    {
        error(e.message.toString())
    }

    /**
     * Handles the instance of the lifecycle scope and its accompanying logic. Takes 3 lambda arguments that can be null.
     *
     * @param Fragment The fragment instance we want to launch the lifecycleScope in
     * @param View A progress bar view. If anything else is passed in, I can't guarantee it won't bug out.
     */
    fun handleScope(frag: Fragment, progressBar: View ?= null,
        onLoading: (() -> Unit) ?= null,
        onSuccess: (() -> Unit) ?= null,
        onError: (() -> Unit) ?= null
    ) {
        frag.lifecycleScope.launchWhenStarted {
            signal.collectLatest {
                Utilities.ResourceOperation(it,
                    {
                        if(progressBar is CircularProgressButton) progressBar.startAnimation()
                        else progressBar?.visibility = View.VISIBLE
                        onLoading?.invoke()
                        onLoadingIterator?.invoke(it)
                    },
                    {
                        if(progressBar is CircularProgressButton) progressBar.revertAnimation()
                        else progressBar?.visibility = View.INVISIBLE
                        onSuccess?.invoke()
                        onSuccessIterator?.invoke(it)
                    },
                    {
                        if(progressBar is CircularProgressButton) progressBar.revertAnimation()
                        else progressBar?.visibility = View.INVISIBLE
                        Utilities.showToast(frag.requireContext(), it.message.toString())
                        onError?.invoke()
                        onFailureIterator?.invoke(it)
                    }
                )
            }
        }
    }

    var onLoadingIterator: ((Resource<T>) -> Unit) ?= null
    var onSuccessIterator: ((Resource<T>) -> Unit) ?= null
    var onFailureIterator: ((Resource<T>) -> Unit) ?= null
}