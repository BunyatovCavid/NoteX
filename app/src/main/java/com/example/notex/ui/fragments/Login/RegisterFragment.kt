package com.example.notex.ui.fragments.Login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.notex.Independents.helper.toast
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.databinding.FragmentRegisterBinding
import com.example.notex.ui.MainActivity
import com.example.notex.viewmodels.AuthorizationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding

    private val authViewModel: AuthorizationViewModel by viewModels()
    private lateinit var replacefrg: replaceFragments


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        replacefrg = replaceFragments()

        binding?.registerbackbutton?.setOnClickListener(){
            clearInputs()
            replacefrg.replace(this, R.id.action_registerFragment_to_welcomingFragment)
        }

        binding?.registerLoginaccounttext?.setOnClickListener{
            replacefrg.replace(this, R.id.action_registerFragment_to_loginFragment2)
        }



        binding?.registerRegisterbutton?.setOnClickListener {
            try {
                val email = binding?.registeremailinput?.text.toString()
                val password = binding?.registerpasswordinput?.text.toString()
                val rePassword = binding?.registerRepasswordinput?.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty() && rePassword.isNotEmpty()) {
                    if (password == rePassword) {
                        authViewModel.signUp(email, password)
                        authViewModel.signUpResult.observe(viewLifecycleOwner) { result ->
                            Toast.makeText(context, result, Toast.LENGTH_LONG).show()
                            if (result == "Successful") {
                                clearInputs()
                                saveLoginState(true)
                                authViewModel.updateProfile()
                                replacefrg.replace(this, R.id.action_registerFragment_to_homeFragment)
                            }
                        }
                    } else {
                       context?.toast("Password and Re-Password don't equal.")
                    }
                } else {
                    context?.toast("You can't send empty field")
                }
            } catch (e: Exception) {
                crashlytics.recordException(e)
                context?.toast("An error occurred: ${e.message}")
            }
        }

    }



    private fun saveLoginState(isLoggedIn: Boolean) {
        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
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

    private fun clearInputs(){
        binding?.registeremailinput?.text?.clear()
        binding?.registerpasswordinput?.text?.clear()
        binding?.registerRepasswordinput?.text?.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}