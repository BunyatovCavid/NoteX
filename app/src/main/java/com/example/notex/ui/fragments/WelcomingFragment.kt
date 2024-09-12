package com.example.notex.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.databinding.FragmentRegisterBinding
import com.example.notex.databinding.FragmentWelcomingBinding
import com.example.notex.ui.MainActivity
import com.example.notex.viewmodels.authorizationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WelcomingFragment : Fragment() {

    private var _binding: FragmentWelcomingBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: authorizationViewModel by viewModels()
    private lateinit var replacefrg: replaceFragments


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWelcomingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        replacefrg = replaceFragments()

        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.checkSavedUser()
            authViewModel.loginResult.observe(viewLifecycleOwner) { result ->
                if (result == "Welcome") {
                    replacefrg.replace(this@WelcomingFragment, R.id.action_welcomingFragment_to_homeFragment)
                }
            }
        }

        binding.welcomingLoginButton.setOnClickListener{
            replacefrg.replace(this,R.id.action_welcomingFragment_to_loginFragment)
        }

        binding.welcomingRegisterButton.setOnClickListener{
            replacefrg.replace(this, R.id.action_welcomingFragment_to_registerFragment)
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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}