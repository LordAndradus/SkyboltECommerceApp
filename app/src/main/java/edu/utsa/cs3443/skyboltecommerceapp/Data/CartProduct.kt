package edu.utsa.cs3443.skyboltecommerceapp.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * A data class for handling the sub-collection in a user, which is to add products to a cart.
 */
@Parcelize
data class CartProduct(
    val product: Product,
    val quantity: Int,
    val selectedColor: Int ?= null,
    val selectedSize: String ?= null
): Parcelable {
    constructor(): this(Product(), 1, null, null)

    fun areEqualIgnoreQuantity(product1: CartProduct, product2: CartProduct): Boolean
    {
        return product1.copy(quantity = 0) == product2.copy(quantity = 0)
    }
}