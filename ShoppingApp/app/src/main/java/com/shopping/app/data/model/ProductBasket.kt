package com.shopping.app.data.model

import ai.mealz.core.model.SupplierProduct

class ProductBasket(

    var id: String? = "",
    var title: String? = "",
    var image: String? = "",
    var price: Double? = 0.0,
    var piece: Int? = 0
) {
    /**
     * Step 8: Implement the toSupplierProduct method
     * https://miamtech.github.io/mealz-documentation/docs/android/overview/supplierInit#basket-synchronization-setup
     */
    fun toSupplierProduct(): SupplierProduct {
        return SupplierProduct(
            id = id!!.toString(),
            quantity = piece!!,
            name = title!!,
            imageURL = image!!
        )
    }
}
