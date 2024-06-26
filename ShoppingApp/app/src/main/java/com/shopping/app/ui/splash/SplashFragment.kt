package com.shopping.app.ui.splash

import ai.mealz.core.Mealz
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.shopping.app.R
import com.shopping.app.data.preference.UserPref
import com.shopping.app.databinding.FragmentOnboardingBinding
import com.shopping.app.databinding.FragmentSplashBinding
import com.shopping.app.ui.onboarding.adapter.OnboardAdapter
import com.shopping.app.ui.onboarding.viewmodel.OnboardViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private lateinit var bnd: FragmentSplashBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bnd = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){

        val userPref = UserPref(requireContext())
        CoroutineScope(Dispatchers.Main).launch {

            delay(1000)

            if(FirebaseAuth.getInstance().currentUser != null && userPref.getEmail().isNotEmpty()){
                /**
                 * Step 6 : Pass user identifier to Mealz when logged in
                 *  https://miamtech.github.io/mealz-documentation/docs/android/overview/supplierInit#user-setup
                 */
                FirebaseAuth.getInstance().currentUser?.uid?.let { Mealz.user.updateUserId(it) }
                if (userPref.getStoreId().isEmpty()) {
                    findNavController().navigate(R.id.action_splashFragment_to_storeFragment)
                    return@launch
                }
                /**
                 * Step 7 :  Pass selected store to Mealz
                 *  https://miamtech.github.io/mealz-documentation/docs/android/overview/supplierInit#store-setup
                 */
                // /!\ IMPORTANT /!\ here we are working for the sake of demo with mealz api then we passe and internal id
                //  you should use Mealz.user.setStoreId(store.id) with your own store id
                Mealz.user.setStoreWithMealzId(userPref.getStoreId())
                findNavController().navigate(R.id.action_splashFragment_to_mainMenuFragment)
            }else{
                if(userPref.isFirstUsage()){
                    findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
                } else if (userPref.getStoreId().isNotEmpty()) {
                    findNavController().navigate(R.id.action_splashFragment_to_authFragment)
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_storeFragment)
                }
            }
        }
    }
}