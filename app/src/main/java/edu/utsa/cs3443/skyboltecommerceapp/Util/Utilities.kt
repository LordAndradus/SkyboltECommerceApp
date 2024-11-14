package edu.utsa.cs3443.skyboltecommerceapp.Util

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import edu.utsa.cs3443.skyboltecommerceapp.Activities.ShoppingActivity
import edu.utsa.cs3443.skyboltecommerceapp.R
import edu.utsa.cs3443.skyboltecommerceapp.Util.Constants.CURRENCY_SYMBOL

/**
 * Primarily a static class, this handles function calls that are helpful for constructing the app
 *
 * May or may not include variables in the future.
 */

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

        fun showToast(context: Context, message: String)
        {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun showSnackbar(view: View, message: String)
        {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
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

        /**
         * Converts a float to an appropriate decimal form of money, with a the appropriate currency sign (Will change later)
         *
         * @param Float The price as a decimal value
         * @return A monetary representation of the price
         */
        @SuppressLint("DefaultLocale") //Suppresses the warning about using default locale. For now we assume everyone is American
        fun price(p: Float): String
        {
            return CURRENCY_SYMBOL + String.format("%.02f", p)
        }

        /**
         * Displays the bottom navigation bar when the activity is the shopping activity
         */
        fun Fragment.showBottomNavigation()
        {
            val bnv = (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.BottomNavigator)
            bnv.visibility = View.VISIBLE
        }

        /**
         * Hides the bottom navigation bar when the activity is the shopping activity
         */
        fun Fragment.hideBottomNavigation()
        {
            val bnv = (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.BottomNavigator)
            bnv.visibility = View.GONE
        }
    }
}