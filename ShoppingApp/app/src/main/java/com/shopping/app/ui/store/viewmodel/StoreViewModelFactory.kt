package com.shopping.app.ui.store.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shopping.app.data.repository.store.StoreRepository

class StoreViewModelFactory(private val storeRepository: StoreRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StoreViewModel(storeRepository) as T
    }
}