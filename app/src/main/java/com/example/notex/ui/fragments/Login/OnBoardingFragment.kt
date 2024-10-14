package com.example.notex.ui.fragments.Login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notex.Independents.helper.toast
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.data.models.CheckLoginData
import com.example.notex.ui.MainActivity
import com.example.notex.ui.fragments.Notes.UpdateNoteFragmentArgs
import com.example.notex.viewmodels.AuthorizationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    private val authViewModel: AuthorizationViewModel by viewModels()

    private var backPressedOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_on_boarding, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedOnce) {
                    requireActivity().finish()
                } else {
                    backPressedOnce = true
                    context?.toast("Press back again to exit")
                    view?.postDelayed({
                        backPressedOnce = false
                    }, 2000)
                }
            }
        })

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            var check: String = "noLogin"
            var loginCheck = arguments?.getParcelable<CheckLoginData>("checkLoginData")

            if (loginCheck == null) {
                viewLifecycleOwner.lifecycleScope.launch {
                    authViewModel.checkSavedUser()
                    authViewModel.loginResult.observe(viewLifecycleOwner) { result ->
                        if (result == "Welcome") {
                            check = result
                            loginCheck = null
                            activity?.findNavController(R.id.nav_host_fragment)
                                ?.navigate(R.id.action_onBoardingFragment_to_homeFragment)
                        }
                    }
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    if (check == "noLogin") {
                        activity?.findNavController(R.id.nav_host_fragment)
                            ?.navigate(R.id.action_onBoardingFragment_to_welcomingFragment)
                    }
                }, 3000)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (check == "noLogin") {
                        activity?.findNavController(R.id.nav_host_fragment)
                            ?.navigate(R.id.action_onBoardingFragment_to_welcomingFragment)
                    }
                }, 3000)
            }
        } catch (e: Exception) {
            crashlytics.recordException(e)
        }
    }


    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.let {
            it.findViewById<BottomNavigationView>(R.id.bottomNav).visibility = View.GONE
        }
    }


    override fun onStop() {
        super.onStop()
        (activity as? MainActivity)?.let {
            it.findViewById<BottomNavigationView>(R.id.bottomNav).visibility = View.VISIBLE
        }
    }


}