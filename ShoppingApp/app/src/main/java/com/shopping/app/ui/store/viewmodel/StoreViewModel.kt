package com.shopping.app.ui.store.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shopping.app.data.model.DataState
import com.shopping.app.data.model.MealzStoreWrapper
import com.shopping.app.data.model.Store
import com.shopping.app.data.repository.store.StoreRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreViewModel(private val storeRepository: StoreRepository) : ViewModel(){


    private var _storeLiveData = MutableLiveData<DataState<List<Store>?>>()

    val storeLiveData: LiveData<DataState<List<Store>?>>
        get() = _storeLiveData

    init {
        getStores()
    }

    private fun getStores(){
        _storeLiveData.postValue(DataState.Loading())
        storeRepository.getProducts().enqueue(object: Callback<MealzStoreWrapper> {

            override fun onResponse(call: Call<MealzStoreWrapper>, response: Response<MealzStoreWrapper>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _storeLiveData.postValue(DataState.Success(it.data))
                    } ?: kotlin.run {
                        _storeLiveData.postValue(DataState.Error("Data Empty"))
                    }
                } else {
                    _storeLiveData.postValue(DataState.Error(response.message()))
                }
            }

            override fun onFailure(call: Call<MealzStoreWrapper>, t: Throwable) {
                _storeLiveData.postValue(DataState.Error(t.message.toString()))
            }
        })
    }
}