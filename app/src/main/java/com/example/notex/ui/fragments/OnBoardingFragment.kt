package com.example.notex.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.ui.MainActivity
import com.example.notex.viewmodels.authorizationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {

    private val authViewModel: authorizationViewModel by viewModels()
    private lateinit var replacefrg: replaceFragments

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_on_boarding, container, false)

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        replacefrg = replaceFragments()
        var check:String = "noLogin"


        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.checkSavedUser()
            authViewModel.loginResult.observe(viewLifecycleOwner) { result ->
                if (result == "Welcome") {
                    check = result
                    replacefrg.replace(
                        this@OnBoardingFragment,
                        R.id.action_onBoardingFragment_to_homeFragment
                    )
                }
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
                if(check =="noLogin")
                {
                    replacefrg.replace(this@OnBoardingFragment, R.id.action_onBoardingFragment_to_welcomingFragment)
                }
        }, 3000)
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