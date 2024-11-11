package edu.utsa.cs3443.skyboltecommerceapp.Util

import edu.utsa.cs3443.skyboltecommerceapp.databinding.FragmentRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Utilities
{
    //Companion object acts as a static variable that we can use to store static functions
    companion object
    {
        /**
         * Literally does not nothing at all. This is to waste clock cycles
         *
         * @param null
         * @return Nothing at all
         */
        fun nop()
        {

        }

        /**
         * Takes the text from an EditText field, and converts it to a String data-type that is trimmed
         *
         * @param android.widget.EditText string
         * @return Trimmed String
         */
        fun input(et: android.widget.EditText): String
        {
            return et.text.toString().trim();
        }

        /**
         * Applies a Resource Operation to determine what Loading, on Success, on Error, or while Idle
         *
         * @param Resource The resource we want to check on
         * @param Lambda onLoading - Operations while the loading
         * @param Lambda onSuccess - Operations on success callback
         * @param Lambda onError - Operations on failed/error callback
         * @return null
         */
        fun <T>ResourceOperation(
            operation: Resource<T>,
            onLoading: () -> Unit = { },
            onSuccess: () -> Unit = { },
            onError: () -> Unit = { },
        ) {
            when (operation)
            {
                is Resource.Loading -> {
                    onLoading()
                }

                is Resource.Success -> {
                    onSuccess()
                }

                is Resource.Error -> {
                    onError()
                }

                else -> nop()
            }
        }
    }
}