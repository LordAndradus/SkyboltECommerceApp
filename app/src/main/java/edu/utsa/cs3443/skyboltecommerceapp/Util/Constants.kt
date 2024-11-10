package edu.utsa.cs3443.skyboltecommerceapp.Util

/**
 * This handles project specific constants
 *
 * Example: We want a Singleton variable, that is easily modifiable that tells Firestore what collection we're handling
 * At a specific instance. Instead of hunting through the program files to change "user" to something else, we have one
 * consolidated location to change it for all
 */
object Constants
{
    const val USER_COLLECTION = "user"
}