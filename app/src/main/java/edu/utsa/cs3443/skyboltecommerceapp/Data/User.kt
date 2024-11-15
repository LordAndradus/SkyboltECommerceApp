package edu.utsa.cs3443.skyboltecommerceapp.Data

/**
 * Data container for user data on the Firebase Auth service
 */

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val imagePath: String = "" //Profile picture
){
    constructor() : this("", "", "", "")

    override fun toString(): String {
        return String.format("First name: $firstName\nLast name: $lastName\nEmail: $email\nImage URL: $imagePath")
    }
}