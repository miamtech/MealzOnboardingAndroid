package com.shopping.app.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class Rating(
    @SerializedName("count")
    val count: Int = Random.nextInt(0, 1000),
    @SerializedName("rate")
    val rate: Double = Random.nextDouble(0.0, 5.0)
) : Parcelable {

    val stringCount: String
        get() {
            return  count.toString()
        }

    val floatRate: Float
        get() {
            return  rate.toFloat()
        }
    val stringRate: String
        get() {
            return  rate.toString()
        }
}