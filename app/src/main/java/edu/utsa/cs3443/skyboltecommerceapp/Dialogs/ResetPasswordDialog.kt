package edu.utsa.cs3443.skyboltecommerceapp.Dialogs

import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import edu.utsa.cs3443.skyboltecommerceapp.R

/**
 * Instead of using a whole fragment, this is a dialog box that will pop up from the bottom to prompt
 * the user on entering an email to reset a password
 *
 * @param Lambda onSendClick - The operation we will enact when tapping the send button, takes a string arg
 */

fun Fragment.setupBottomSheetDialog(
    onSendClick: (String) -> Unit,
) {
    val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_password_dialog, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val EnterEmail = view.findViewById<EditText>(R.id.EnterEmail)
    val SendEmailButton = view.findViewById<AppCompatButton>(R.id.SendEmailButton)
    val CancelResetButton = view.findViewById<AppCompatButton>(R.id.CancelResetButton)

    SendEmailButton.setOnClickListener {
        val Email = EnterEmail.text.toString().trim()
        onSendClick(Email)
        dialog.dismiss()
    }

    CancelResetButton.setOnClickListener {
        dialog.dismiss()
    }
}