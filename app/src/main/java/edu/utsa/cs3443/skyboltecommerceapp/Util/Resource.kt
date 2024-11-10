package edu.utsa.cs3443.skyboltecommerceapp.Util

/**
 * A class that will specifically be used for reporting when authenticating, creating, and storing
 * Google's Firebase User information
 */
sealed class Resource<T>(
    val data: T ?= null,
    val message: String ?= null
){
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String) : Resource<T>(message = message)
    class Loading<T>() : Resource<T>()
    class Idle<T>() : Resource<T>()
}