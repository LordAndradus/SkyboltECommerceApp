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

open class ResourceSignaler<T>(
    private val viewModel: ViewModel
) {
    protected val _signal = MutableStateFlow<Resource<T>>(Resource.Idle())
    val signal = _signal.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    /**
     * This is to tell the ViewModel that the firebase is currently loading the information
     */
    fun start()
    {
        viewModel.viewModelScope.launch {
            _signal.emit(Resource.Loading())
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
     * @param View Can be any view, but mostly useful for anything related to display progress
     * @param Lambda A function to handle loading
     * @param Lambda A function to handle success
     * @param Lambda A function to handle errors/failures
     * @param Boolean A toggle to set the passed view invisible or completely gone
     */
    fun handleScope(frag: Fragment, progressBar: View ?= null,
        onLoading: (() -> Unit) ?= null,
        onSuccess: (() -> Unit) ?= null,
        onError: (() -> Unit) ?= null,
        progressGone: Boolean = false
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
                        else progressBar?.visibility = if(!progressGone) View.INVISIBLE else View.GONE
                        onSuccess?.invoke()
                        onSuccessIterator?.invoke(it)
                    },
                    {
                        if(progressBar is CircularProgressButton) progressBar.revertAnimation()
                        else progressBar?.visibility = if(!progressGone) View.INVISIBLE else View.GONE
                        Utilities.showToast(frag.requireContext(), it.message.toString())
                        onError?.invoke()
                        onFailureIterator?.invoke(it)
                    }
                )
            }
        }
    }

    /**
     * Method overload to handle when I only want to deal with hiding the progress bar completely
     *
     * @param Fragment
     * @param View Can be any view, but mostly useful for anything related to display progress
     * @param Boolean Toggles whether to make progressBar invisible or gone
     */
    fun handleScope(frag: Fragment, progressBar: View ?= null, progressGone: Boolean = false)
    {
        handleScope(frag, progressBar, null, null, null, progressGone)
    }

    /**
     * Separate invokable delegates for handling specific resource cases
     * when it requires "it" (which is a unit of data)
     */
    var onLoadingIterator: ((Resource<T>) -> Unit) ?= null
    var onSuccessIterator: ((Resource<T>) -> Unit) ?= null
    var onFailureIterator: ((Resource<T>) -> Unit) ?= null
}