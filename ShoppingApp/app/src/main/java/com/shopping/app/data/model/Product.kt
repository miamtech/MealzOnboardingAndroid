package com.shopping.app.data.model

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
 data class MealzProductWrapper(
    @SerializedName("data")
    val data: List<Product>?)
    : Parcelable

@Parcelize
data class Product(
    @SerializedName("category")
    val category: String?,
    @SerializedName("id")
    val stringId: String?,
    @SerializedName("rating")
    val rating: Rating = Rating(),
    @SerializedName("attributes")
    val attributes: Attributes?
): Parcelable {

    // json convert method
    fun toJson(): String {
        return Gson().toJson(this)
    }

    val id: Int
        get() {
            return this.stringId?.toInt() ?: 0
        }

    val image: String
        get() {
            return this.attributes?.image ?: ""
        }

    val description: String
        get() {
            return this.attributes?.description ?: ""
        }

    val title: String
        get() {
            return this.attributes?.name ?: ""
        }

    val price: Double
        get() {
            return this.attributes?.unitPrice?.toDouble() ?: 0.0
        }

    // static json object
    companion object {
        fun fromJson(jsonValue: String): Product {
            return Gson().fromJson(jsonValue, Product::class.java)
        }
    }

}

@Parcelize
data class Attributes(
    @SerializedName("ext-id")
    val extId: String? = null,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("unit-price")
    val unitPrice: String? = null,
    @SerializedName("capacity-unit")
    val capacityUnit: String? = null,
    @SerializedName("capacity-volume")
    val capacityVolume: String? = null,
    @SerializedName("capacity-factor")
    val capacityFactor: Int? = 1,
    @SerializedName("variable-capacity")
    val variableCapacity: Boolean
) : Parcelable