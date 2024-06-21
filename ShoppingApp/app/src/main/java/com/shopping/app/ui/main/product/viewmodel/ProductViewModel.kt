package com.shopping.app.ui.main.product.viewmodel

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shopping.app.data.model.DataState
import com.shopping.app.data.model.MealzProductWrapper
import com.shopping.app.data.model.Product
import com.shopping.app.data.preference.UserPref
import com.shopping.app.data.repository.product.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel(val context: Context, private val productRepository: ProductRepository) : ViewModel() {

    private var _productLiveData = MutableLiveData<DataState<List<Product>?>>()
    val productLiveData: LiveData<DataState<List<Product>?>>
        get() = _productLiveData

    init {
        getProducts()
    }

    private fun getProducts(){
        _productLiveData.postValue(DataState.Loading())
        CoroutineScope(Dispatchers.Main).launch {
           val storeId = UserPref(context).getStoreId()
            productRepository.getProducts(storeId).enqueue(object: Callback<MealzProductWrapper>{

                override fun onResponse(call: Call<MealzProductWrapper>, response: Response<MealzProductWrapper>) {

                    if (response.isSuccessful) {
                        response.body()?.let {
                            _productLiveData.postValue(DataState.Success(it.data))
                        } ?: kotlin.run {
                            _productLiveData.postValue(DataState.Error("Data Empty"))
                        }
                    } else {
                        _productLiveData.postValue(DataState.Error(response.message()))
                    }

                }

                override fun onFailure(call: Call<MealzProductWrapper>, t: Throwable) {
                    _productLiveData.postValue(DataState.Error(t.message.toString()))
                }

            })
        }
    }
}