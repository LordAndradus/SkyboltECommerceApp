package edu.utsa.cs3443.skyboltecommerceapp.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random.Default.nextLong

@Parcelize
data class Order(
    val orderStatus: OrderStatus = OrderStatus.PLACED,
    val totalPrice: Float = 0f,
    val products: List<CartProduct> = emptyList(),
    val address: Address = Address(),
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date()),
    val orderID: Long = nextLong(0, Long.MAX_VALUE)
):Parcelable

enum class OrderStatus(val item: String)
{
    PLACED("placed"),
    CONFIRMED("confirmed"),
    SHIPPED("shipped"),
    DELIVERED("delivered"),

    //Extraneous
    CANCELLED("cancelled"),
    RETURNED("returned");

    companion object
    {
        fun fromString(value: String): OrderStatus? {
            return entries.find { it.item.equals(value, ignoreCase = true)}
        }
    }
}
