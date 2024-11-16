package edu.utsa.cs3443.skyboltecommerceapp.Helper

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import edu.utsa.cs3443.skyboltecommerceapp.Util.Utilities

class PhoneEntry(private val textEditor: EditText) : TextWatcher {
    private var isFormatting = false
    private var previousText = ""

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) {
        previousText = s.toString()
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
    {
        Utilities.nop()
    }

    override fun afterTextChanged(s: Editable?)
    {
        if(isFormatting) return

        isFormatting = true

        val formatted = formatPhoneNumber(s.toString().replace("\\D".toRegex(), ""))

        if(formatted.isEmpty()) return

        if(formatted != s.toString())
        {
            textEditor.setText(formatted)
        }

        if(formatted.length <= 5) textEditor.setSelection(formatted.length - 1)
        else if(formatted.length < 12) textEditor.setSelection(formatted.length - 3)
        else textEditor.setSelection(formatted.length)

        isFormatting = false
    }



    private fun formatPhoneNumber(digits: String): String
    {
        val builder = StringBuilder()

        if(digits.isEmpty()) return ""

        if(digits.length <= 3)
        {
            builder.append(String.format("(%s)", digits))
        }
        else if(digits.length <= 6)
        {
            builder.append(String.format("(%s) %s - ", digits.substring(0, 3), digits.substring(3)))
        }
        else
        {
            builder.append(String.format("(%s) %s - %s", digits.substring(0, 3), digits.substring(3, 6), digits.substring(6)))
        }

        return builder.toString()
    }
}

