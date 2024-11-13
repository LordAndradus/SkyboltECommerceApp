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
    const val PRODUCT_COLLECTION = "products"

    const val INTRODUCTION_SHARED_PREFERENCES = "IntroductionSP"
    const val INTRODUCTION_KEY = "IntroductionKey"
}

/**
 * A constant class
 *
 * Keeps track of what categories have been added to the project
 */
enum class Categories(val category: String)
{
    Special("special product"),
    Fashion("fashion"),
    Electronics("electronics"),
    Furniture("furniture"),
    Medical("medical"),
    Food("food"),
    Pets("pets")
}