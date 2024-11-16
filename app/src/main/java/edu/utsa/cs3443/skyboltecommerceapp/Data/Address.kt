package edu.utsa.cs3443.skyboltecommerceapp.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val addressTitle: String,
    val fullName: String,
    val street: String,
    val phoneNumber: String,
    val city: String,
    val state: String
): Parcelable {
    constructor(): this ("", "", "" ,"", "", "")

    fun equals(other: Address): Boolean {
        return addressTitle == other.addressTitle
                && fullName == other.fullName
                && street == other.street
                && phoneNumber == other.phoneNumber
                && city == other.city
                && state == other.state
    }
}
