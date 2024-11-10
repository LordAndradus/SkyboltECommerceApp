package edu.utsa.cs3443.skyboltecommerceapp.Data

data class User(
    val FirstName: String,
    val LastName: String,
    val Email: String,
    val ImagePath: String = "" //Profile picture
){
    constructor() : this("", "", "", "")
}