package com.shopping.app.ui.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.shopping.app.R
import com.shopping.app.data.api.ApiClient
import com.shopping.app.data.model.DataState
import com.shopping.app.data.repository.store.StoreRepositoryImpl
import com.shopping.app.databinding.FragmentStoreBinding
import com.shopping.app.ui.loadingprogress.LoadingProgressBar
import com.shopping.app.ui.main.search.adapter.SearchAdapter
import com.shopping.app.ui.store.adapter.StoreAdapter
import com.shopping.app.ui.store.viewmodel.StoreViewModel
import com.shopping.app.ui.store.viewmodel.StoreViewModelFactory

class StoreFragment: Fragment() {

    private lateinit var fs: FragmentStoreBinding
    private val viewModel: StoreViewModel by viewModels(){
        StoreViewModelFactory(
            StoreRepositoryImpl(
                ApiClient.getApiService()
            )
        )
    }
    private lateinit var storeAdapter: StoreAdapter
    private lateinit var loadingProgressBar: LoadingProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fs = DataBindingUtil.inflate(inflater, R.layout.fragment_store, container, false)
        return fs.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingProgressBar = LoadingProgressBar(requireContext())

        viewModel.storeLiveData.observe(viewLifecycleOwner){

            when (it) {
                is DataState.Success -> {
                    loadingProgressBar.hide()
                    it.data?.let { safeData ->
                        storeAdapter = StoreAdapter(requireContext(), safeData, findNavController())
                        fs.storeAdapter.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        fs.storeAdapter.setHasFixedSize(true)
                        fs.storeAdapter.adapter = storeAdapter
                    } ?: run {
                        Snackbar.make(fs.root, getString(R.string.no_data), Snackbar.LENGTH_LONG).show()
                    }
                }
                is DataState.Error -> {
                    loadingProgressBar.hide()
                    Snackbar.make(fs.root, it.message, Snackbar.LENGTH_LONG).show()
                }
                is DataState.Loading -> {
                    loadingProgressBar.show()
                }
            }
        }
    }
}