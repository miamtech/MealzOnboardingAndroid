package com.shopping.app.ui.store.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.shopping.app.R
import com.shopping.app.data.model.Store
import com.shopping.app.data.model.User
import com.shopping.app.data.preference.UserPref
import com.shopping.app.databinding.ItemStoreBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class StoreAdapter(private val context: Context, private val storeList: List<Store>, private val navController: NavController)
    : RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {


    private lateinit var binding: ItemStoreBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreAdapter.StoreViewHolder {
        binding = ItemStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoreViewHolder(binding)
    }

    override fun getItemCount(): Int = storeList.size
    override fun onBindViewHolder(holder: StoreAdapter.StoreViewHolder, position: Int) {
        val store = storeList[position]
        holder.bind(store)
    }

     fun goHome(store: Store) {
         saveSelectedStore(store)
    }

    private fun saveSelectedStore(store: Store){
        val userPref = UserPref(context)
        CoroutineScope(Dispatchers.Main).launch {
            /**
             * TODO (Step 7): Pass selected store to Mealz
             * https://miamtech.github.io/mealz-documentation/docs/android/overview/supplierInit#store-setup
             */
            userPref.setStoreId(store.id)
            userPref.setStoreName(store.name)
            navController.navigate(R.id.action_storeFragment_to_mainMenuFragment)
        }
    }

    inner class StoreViewHolder(private val binding: ItemStoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(store: Store) {
            binding.dataHolder = store
            binding.storeAdapter = this@StoreAdapter
            binding.executePendingBindings()
        }
    }
}
