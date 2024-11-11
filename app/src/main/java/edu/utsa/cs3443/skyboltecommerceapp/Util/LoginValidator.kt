package edu.utsa.cs3443.skyboltecommerceapp.Util

/**
 * This class will tell us if the Login process should proceed
 *
 * Upon failure, it will fill a message and export it for the EditText to show the user what went wrong
 */

sealed class LoginValidator
{
    object success : LoginValidator()
    data class Failed(val message: String) : LoginValidator()
}

data class LoginFieldState(
    val email: LoginValidator,
    val password: LoginValidator
)