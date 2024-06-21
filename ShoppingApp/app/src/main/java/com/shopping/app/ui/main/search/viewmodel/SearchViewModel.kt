package com.shopping.app.ui.main.search.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shopping.app.data.model.CategoryModel
import com.shopping.app.data.model.DataState
import com.shopping.app.data.model.MealzProductWrapper
import com.shopping.app.data.model.Product
import com.shopping.app.data.preference.UserPref
import com.shopping.app.data.repository.search.SearchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(private val context: Context, private val searchRepository: SearchRepository) : ViewModel() {

    private lateinit var productList:List<Product>
    private lateinit var categoryList:List<CategoryModel>

    private var _searchLiveData = MutableLiveData<DataState<List<Product>?>>()
    val searchLiveData: LiveData<DataState<List<Product>?>>
        get() = _searchLiveData

    private var _categoryLiveData = MutableLiveData<DataState<List<CategoryModel>?>>()
    val categoryLiveData: LiveData<DataState<List<CategoryModel>?>>
        get() = _categoryLiveData

    init {
        getCategories()
        getProducts()
    }

    private fun getCategories(){

        _categoryLiveData.postValue(DataState.Loading())
        categoryList =
            listOf( CategoryModel(
                "Pomme",
                false
            ),CategoryModel(
                "Steak",
                false
            ),CategoryModel(
                "Mayonaise",
                false
            ))

        _categoryLiveData.postValue(DataState.Success(categoryList))
    }

    fun getProductsByCategoryCheck(categoryModel: CategoryModel){

        val isSelectedCategory = categoryModel.isSelected
        categoryList.map {
            if(isSelectedCategory) it.isSelected = false
            else it.isSelected = it.categoryName == categoryModel.categoryName

        }

        _categoryLiveData.postValue(DataState.Success(categoryList))

        if(isSelectedCategory) getProducts()
        else getProductsByCategory(categoryModel)

    }

    private fun getProducts(){

        _searchLiveData.postValue(DataState.Loading())

        CoroutineScope(Dispatchers.Main).launch {

            val storeId = UserPref(context).getStoreId()

            searchRepository.getProducts(storeId).enqueue(object : Callback<MealzProductWrapper> {

                override fun onResponse(
                    call: Call<MealzProductWrapper>,
                    response: Response<MealzProductWrapper>
                ) {

                    if (response.isSuccessful) {
                        response.body()?.let {
                            productList = it.data!!
                            _searchLiveData.postValue(DataState.Success(productList))

                        } ?: kotlin.run {
                            _searchLiveData.postValue(DataState.Error("Data Empty"))
                        }
                    } else {
                        _searchLiveData.postValue(DataState.Error(response.message()))
                    }

                }

                override fun onFailure(call: Call<MealzProductWrapper>, t: Throwable) {
                    _searchLiveData.postValue(DataState.Error(t.message.toString()))
                }

            })
        }

    }

    private fun getProductsByCategory(categoryModel: CategoryModel){

        _searchLiveData.postValue(DataState.Loading())
        CoroutineScope(Dispatchers.Main).launch {

            val storeId = UserPref(context).getStoreId()
            searchRepository.getProductsByCategory(categoryModel.categoryName,storeId)
                .enqueue(object : Callback<MealzProductWrapper> {

                    override fun onResponse(
                        call: Call<MealzProductWrapper>,
                        response: Response<MealzProductWrapper>
                    ) {

                        if (response.isSuccessful) {
                            response.body()?.let {

                                productList = it.data!!
                                _searchLiveData.postValue(DataState.Success(productList))

                            } ?: kotlin.run {
                                _searchLiveData.postValue(DataState.Error("Data Empty"))
                            }
                        } else {
                            _searchLiveData.postValue(DataState.Error(response.message()))
                        }

                    }

                    override fun onFailure(call: Call<MealzProductWrapper>, t: Throwable) {
                        _searchLiveData.postValue(DataState.Error(t.message.toString()))
                    }

                })
        }

    }

    fun searchProducts(isSearch:Boolean = false, query:String = ""){

        if(productList.isNotEmpty()){

            if(isSearch){
                _searchLiveData.postValue(DataState.Loading())

                CoroutineScope(Dispatchers.Main).launch {
                    val storeId = UserPref(context).getStoreId()
                searchRepository.getProductsByCategory(storeId,query).enqueue(object: Callback<MealzProductWrapper>{

                    override fun onResponse(call: Call<MealzProductWrapper>, response: Response<MealzProductWrapper>) {

                        if (response.isSuccessful) {
                            response.body()?.let {
                                productList = it.data!!
                                _searchLiveData.postValue(DataState.Success(productList))

                            } ?: run {
                                _searchLiveData.postValue(DataState.Error("Data Empty"))
                            }
                        } else {
                            _searchLiveData.postValue(DataState.Error(response.message()))
                        }
                    }
                    override fun onFailure(call: Call<MealzProductWrapper>, t: Throwable) {
                        _searchLiveData.postValue(DataState.Error(t.message.toString()))
                    }
                })
                }
            } else {
                _searchLiveData.postValue(DataState.Success(productList))
            }
        }else{
            getProducts()
        }
    }
}