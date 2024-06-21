package com.shopping.app.ui.main.search.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shopping.app.data.repository.search.SearchRepository

class SearchViewModelFactory(private val context: Context, private val searchRepository: SearchRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(context,searchRepository) as T
    }
}