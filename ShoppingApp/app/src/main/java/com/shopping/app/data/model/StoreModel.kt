package com.shopping.app.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MealzStoreWrapper(
    @SerializedName("data")
    val data: List<Store>?)
    : Parcelable

@Parcelize
class Store (
    @SerializedName("id")
    val id: String,
    @SerializedName("attributes")
    val attributes: StoreAttributes
): Parcelable {
    val name: String
        get() {
            return this.attributes.name
        }

    val adress: String
        get() {
            return this.attributes.adress
        }
}

@Parcelize
class StoreAttributes(
    @SerializedName("name")
    val name: String ,
    val adress: String,
): Parcelable

