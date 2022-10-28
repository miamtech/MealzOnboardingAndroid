package com.shopping.app.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.shopping.app.R
import com.shopping.app.data.model.DataState
import com.shopping.app.databinding.FragmentSignInBinding
import com.shopping.app.ui.auth.viewmodel.SignInViewModel
import com.shopping.app.ui.loadingprogress.LoadingProgressBar

class SignInFragment : Fragment() {

    private lateinit var bnd: FragmentSignInBinding
    private lateinit var loadingProgressBar: LoadingProgressBar
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        bnd = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        init()
        return bnd.root

    }

    private fun init(){

        bnd.viewModel = viewModel
        loadingProgressBar = LoadingProgressBar(requireContext())

        viewModel.userLiveData.observe(viewLifecycleOwner){

            handleSignIn(it)

        }

    }

    private fun handleSignIn(it: DataState<FirebaseUser?>){

        when(it){

            is DataState.Loading -> {
                loadingProgressBar.show()
            }

            is DataState.Success -> {
                loadingProgressBar.cancel()
            }

            is DataState.Error -> {
                loadingProgressBar.cancel()
                showAlertDialogMessage(it.message)
            }

        }

    }

    private fun showAlertDialogMessage(message: String){

        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(resources.getString(R.string.close)) { dialog, which ->
                dialog.cancel()
            }
            .show()

    }

}