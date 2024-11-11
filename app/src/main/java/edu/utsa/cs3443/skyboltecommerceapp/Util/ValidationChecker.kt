package edu.utsa.cs3443.skyboltecommerceapp.Util

import android.util.Patterns

/**
 * Validates an email, in that it has the right syntax (IE it has an @, a domain, and a name, for example => HeySisters@hotmail.com)
 *
 * Luckily, Patterns has a built in function specifically to validate emails
 *
 * @param String An email
 * @return returns a validator that reports a success or failure, upon failure it sends a message
 */
fun ValidateEmail(email: String): RegistrationValidator
{
    if(email.isEmpty()) return RegistrationValidator.Failed("Email cannot be empty")
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return RegistrationValidator.Failed("Wrong email address")

    return RegistrationValidator.Success
}

/**
 * Validates a password, in that it has the right syntax
 *
 *  Rules for a password:
 *  Must contain at least 6 characters
 *  Must contain at least 1 uppercase letter
 *  Must contain at least 1 number
 *  Must contain at least 1 special character
 *
 *  TODO: Maybe encrypt the password before sending it off the firebase database
 *
 * @param String A password
 * @return returns a validator that reports a success or failure, upon failure it sends a message
 */
fun ValidatePassword(password: String): RegistrationValidator
{
    if(password.isEmpty()) return RegistrationValidator.Failed("Password cannot be empty")
    if(password.length < 6) return RegistrationValidator.Failed("Password must be at least 6 characters long");
    if(!password.any {it.isUpperCase()}) return RegistrationValidator.Failed("Password must contain at least 1 uppercase letter")
    if(!password.any {it.isDigit()}) return RegistrationValidator.Failed("Password must contain at least 1 number")
    if(!password.contains("[^a-zA-Z0-9]".toRegex())) return RegistrationValidator.Failed("Password must contain at least 1 special character")

    //If Password does not contain at least 1 special character

    return RegistrationValidator.Success
}

/**
 * Validates a First Name, in that it has the right syntax (IE it can be anything really. Looking at Elon's child X Æ A-Xii. Poor kid)
 *
 * @param String A first name
 * @return returns a validator that reports a success or failure, upon failure it sends a message
 */
fun ValidateFirstName(firstName: String): RegistrationValidator
{
    if(firstName.isEmpty()) return RegistrationValidator.Failed("First name cannot be empty")

    return RegistrationValidator.Success
}

/**
 * Validates a Last Name, in that it has the right syntax (IE it can be anything really. Like Charles the 4th or Jameson XII)
 *
 * @param String A last name
 * @return returns a validator that reports a success or failure, upon failure it sends a message
 */
fun ValidateLastName(lastName: String): RegistrationValidator
{
    if(lastName.isEmpty()) return RegistrationValidator.Failed("Last name cannot be empty")

    return RegistrationValidator.Success
}

fun ValidateLoginEmail(email: String) : LoginValidator
{
    if(email.isEmpty()) return LoginValidator.Failed("Enter your email")
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return LoginValidator.Failed("Wrong email address")

    return LoginValidator.success;
}

fun ValidateLoginPassword(password: String) : LoginValidator
{
    if(password.isEmpty()) return LoginValidator.Failed("Enter your password")

    return LoginValidator.success
}