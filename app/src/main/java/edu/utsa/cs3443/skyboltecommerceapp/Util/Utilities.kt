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
         * Literally does not nothing at all.
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
    }
}