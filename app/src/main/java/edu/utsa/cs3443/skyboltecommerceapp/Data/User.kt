package edu.utsa.cs3443.skyboltecommerceapp.Data

/**
 * Data container for user data on the Firebase Auth service
 */

data class User(
    val FirstName: String,
    val LastName: String,
    val Email: String,
    val ImagePath: String = "" //Profile picture
){
    constructor() : this("", "", "", "")
}