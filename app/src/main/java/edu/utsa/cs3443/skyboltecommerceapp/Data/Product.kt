package edu.utsa.cs3443.skyboltecommerceapp.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val category: String,
    val price: Float,
    val offerPercentage: Float? = 0f,
    val description: String? = null,
    val colors: List<Int>? = null,
    val special: Boolean? = false,
    val bestDeal: Boolean? = false,
    val bestProduct : Boolean? = false,
    val sizes: List<String>? = null,
    val images: List<String>
): Parcelable {
    constructor(): this("0", "", "", 0f, 0f, images = emptyList())
}