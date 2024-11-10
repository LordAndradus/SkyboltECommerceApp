package edu.utsa.cs3443.skyboltecommerceapp.Util

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