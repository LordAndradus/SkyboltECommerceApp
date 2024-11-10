package edu.utsa.cs3443.skyboltecommerceapp.Util

/**
 * This class will tell us if the registration process failed or succeeded.
 *
 * Upon failure, it will fill a message and export it for a class to read it
 */

sealed class RegistrationValidator
{
    object Success : RegistrationValidator()
    data class Failed(val message: String) : RegistrationValidator()
}

data class RegisterFieldState(
    val email : RegistrationValidator,
    val password : RegistrationValidator,
    val firstname : RegistrationValidator,
    val lastname : RegistrationValidator
)