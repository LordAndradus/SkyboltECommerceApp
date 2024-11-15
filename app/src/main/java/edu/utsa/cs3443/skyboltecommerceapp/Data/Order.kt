package edu.utsa.cs3443.skyboltecommerceapp.Data

data class Order(
    val orderStatus: OrderStatus,
    val totalPrice: Float,
    val products: List<CartProduct>,
    val address: Address
)

enum class OrderStatus(val item: String)
{
    PLACED("placed"),
    PAYMENT("payment"),
    SHIPPED("shipped"),
    DELIVERED("delivered"),

    //Extraneous
    CANCELLED("cancelled"),
    RETURNED("returned")
}
