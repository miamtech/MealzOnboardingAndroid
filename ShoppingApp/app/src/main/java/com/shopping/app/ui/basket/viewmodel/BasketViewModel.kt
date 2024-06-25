package com.shopping.app.ui.basket.viewmodel


import ai.mealz.core.Mealz
import ai.mealz.core.model.SupplierProduct
import ai.mealz.core.subscription.publisher.BasketPublisher
import ai.mealz.core.subscription.subscriber.BasketSubscriber
import android.database.Observable
import android.provider.CallLog.Calls
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.shopping.app.R
import com.shopping.app.data.model.DataState
import com.shopping.app.data.model.ProductBasket
import com.shopping.app.data.preference.UserPref
import com.shopping.app.data.repository.basket.BasketRepository
import com.shopping.app.data.repository.product.ProductRepository


/**
 * Step 9: Implement the BasketPublisher, BasketSubscriber interface.
 * https://miamtech.github.io/mealz-documentation/docs/android/overview/supplierInit#basket-synchronization-setup
 */
class BasketViewModel(private val basketRepository: BasketRepository, private val productRepository: ProductRepository) : BasketPublisher, BasketSubscriber,  ViewModel() {

    /**
     * /!\/!\ IMPORTANT /!\/!\
     * Don't initialize this variable to empty list it will cause the lost of previous basket
     * You should wait for your basket first value or current value
     */
    override lateinit var initialValue: List<SupplierProduct>

    /**
     * We call this function to listen to your basket once we have fetch our
     */
    override fun onBasketUpdate(sendUpdateToSDK: (List<SupplierProduct>) -> Unit) {
        _basketLiveData.observeForever {
            sendUpdateToSDK(basketList.map { it.toSupplierProduct() })
        }
    }

    /**
     * We call this when you should receive our basket modifications
     */
    override fun receive(event: List<SupplierProduct>) {
        val allTasks = emptyList<Task<Void>>()
        val tmpBasket = basketList.toMutableList()
        Thread {
            try {
                event.map { sp -> productRepository.getProductsById(sp.id).execute() }.forEachIndexed { index, response  ->
                    if (!response.isSuccessful) return@Thread
                    val product = response.body()?.data?.get(0)?.toBasketProduct(event[index].quantity)
                    product?.let {
                        val productToUpdateIdx = basketList.indexOfFirst { it.id == product.id }

                        if (productToUpdateIdx == -1) {
                            // Product to add to your basket
                            allTasks.plus(basketRepository.addProductsToBasket(product))
                            tmpBasket.add(product)
                        } else if (product.piece == 0) {
                            //remove the product from the list
                            allTasks.plus(basketRepository.deleteProducts(product))
                            tmpBasket.removeAt(productToUpdateIdx)
                        } else {
                            //update the product quantity
                            allTasks.plus(basketRepository.updateProductsPiece(product))
                            tmpBasket[productToUpdateIdx].piece = product.piece
                        }

                        // /!\ Emit your changes only once to avoid synchronisation issues
                        Tasks.whenAllComplete(allTasks).addOnCompleteListener {
                            updateLiveData(tmpBasket)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }.start()
    }


    private var _basketTotalLiveData = MutableLiveData<Double>()
    val basketTotalLiveData: LiveData<Double>
        get() = _basketTotalLiveData


    var basketList = mutableListOf<ProductBasket>()
    private var _basketLiveData = MutableLiveData<DataState<List<ProductBasket>?>>()
    val basketLiveData: LiveData<DataState<List<ProductBasket>?>>
        get() = _basketLiveData


    private var _updateProductPieceLiveData = MutableLiveData<DataState<Int>>()
    val updateProductPieceLiveData: LiveData<DataState<Int>>
        get() = _updateProductPieceLiveData


    private var _purchaseLiveData = MutableLiveData<DataState<Int>>()
    val purchaseLiveData: LiveData<DataState<Int>>
        get() = _purchaseLiveData


    init {
        _basketTotalLiveData.value = 0.0
        getProductsBasket()
    }

    private fun getProductsBasket(){

        basketRepository.getAllProductsBasket()
            .addSnapshotListener{ value, error ->

                if(error == null){
                    basketList = mutableListOf()
                    value?.let {
                        updateLiveData(it.map { it.toObject(ProductBasket::class.java) })
                    }
                    initialValue = basketList.map { it.toSupplierProduct() }

                }else{
                    _basketLiveData.value = DataState.Error(error.message!!)
                }

            }
        }

    private fun updateLiveData(products : List<ProductBasket>){
        var total = 0.0
        products.forEach {
            total += it.price!! * it.piece!!
        }
         basketList.addAll(products)
        _basketLiveData.value = DataState.Success(basketList)
        _basketTotalLiveData.value = total
    }

    fun increaseProduct(productBasket: ProductBasket){

        if(productBasket.piece!! < 100){

            productBasket.piece = productBasket.piece!! + 1
            updateProductPiece(productBasket, true)

        }

    }


    fun reduceProduct(productBasket: ProductBasket){

        if(productBasket.piece!! > 1){

            productBasket.piece = productBasket.piece!! - 1
            updateProductPiece(productBasket, false)

        }else{
            deleteProduct(productBasket)
        }

    }

    private fun updateProductPiece(productBasket: ProductBasket, isIncrease: Boolean){

        basketRepository.updateProductsPiece(productBasket)
            .addOnSuccessListener {

                if(isIncrease) _updateProductPieceLiveData.value = DataState.Success(R.string.product_increased_message)
                else _updateProductPieceLiveData.value = DataState.Success(R.string.product_reduce_message)

            }
            .addOnFailureListener { e ->
                _updateProductPieceLiveData.value = DataState.Error(e.message!!)
            }

    }

    private fun deleteProduct(productBasket: ProductBasket){

        basketRepository.deleteProducts(productBasket)
            .addOnSuccessListener {
                _updateProductPieceLiveData.value = DataState.Success(R.string.product_deleted_message)
            }
            .addOnFailureListener { e ->
                _updateProductPieceLiveData.value = DataState.Error(e.message!!)
            }

    }

    fun clearTheBasket(){
        /**
         * Step 10: Handle basket state
         */
        basketList.forEach {
            deleteProduct(it)
        }
        Mealz.basket.clear()
        _basketLiveData.value = DataState.Success(basketList)
        _purchaseLiveData.value = DataState.Success(R.string.clear_success_message)
    }

    fun purchase(){
        /**
         * Step 10: Handle basket state
         */
        Mealz.basket.handlePayment(_basketTotalLiveData.value!!)
        basketList.forEach {
            deleteProduct(it)
        }
        _basketLiveData.value = DataState.Success(basketList)
        _purchaseLiveData.value = DataState.Success(R.string.purchase_success_message)
    }

}