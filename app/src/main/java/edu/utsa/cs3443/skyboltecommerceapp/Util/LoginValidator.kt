package edu.utsa.cs3443.skyboltecommerceapp.Util

sealed class LoginValidator
{
    object success : LoginValidator()
    data class Failed(val message: String) : LoginValidator()
}

data class LoginFieldState(
    val email: LoginValidator,
    val password: LoginValidator
)