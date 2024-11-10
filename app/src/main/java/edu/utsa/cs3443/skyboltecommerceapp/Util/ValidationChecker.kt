package edu.utsa.cs3443.skyboltecommerceapp.Util

import android.util.Patterns

fun ValidateEmail(email: String): RegistrationValidator
{
    if(email.isEmpty()) return RegistrationValidator.Failed("Email cannot be empty")
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return RegistrationValidator.Failed("Wrong email address")

    return RegistrationValidator.Success
}

fun ValidatePassword(password: String): RegistrationValidator
{
    if(password.isEmpty()) return RegistrationValidator.Failed("Password cannot be empty")
    if(password.length < 6) return RegistrationValidator.Failed("Password must be at least 6 characters long");

    //If Password does not contain at least 1 special character

    return RegistrationValidator.Success
}

fun ValidateFirstName(firstName: String): RegistrationValidator
{
    if(firstName.isEmpty()) return RegistrationValidator.Failed("First name cannot be empty")

    return RegistrationValidator.Success
}

fun ValidateLastName(lastName: String): RegistrationValidator
{
    if(lastName.isEmpty()) return RegistrationValidator.Failed("Last name cannot be empty")

    return RegistrationValidator.Success
}