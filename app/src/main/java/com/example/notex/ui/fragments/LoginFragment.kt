package com.example.notex.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.notex.Independents.BaseDatas
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.databinding.FragmentLoginBinding
import com.example.notex.ui.MainActivity
import com.example.notex.viewmodels.authorizationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: authorizationViewModel by viewModels()
    private lateinit var replacefrg: replaceFragments

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        replacefrg = replaceFragments()


        binding.loginforgotpassText.setOnClickListener(){
            val showpopUp = ForgotPasswordFragment()
            fragmentManager?.let { it1 -> showpopUp.show(it1,"Hello") }

        }

        binding.loginbackbutton.setOnClickListener(){
            clearInputs()
            replacefrg.replace(this, WelcomingFragment())
          //  getView()?.let { it1 -> Navigation.findNavController(it1).navigate(R.layout.fragment_welcoming) }
        }

        binding.loginLogInbutton.setOnClickListener(){
            if(!binding.loginemailinput.text.isNullOrEmpty() && !binding.loginpasswordinput.text.isNullOrEmpty()) {
                authViewModel.logIn(
                    binding.loginemailinput.text.toString(),
                    binding.loginpasswordinput.text.toString(),
                    this
                )
                if (BaseDatas.checkLogin) {
                    clearInputs()
                }
            }
            else
                Toast.makeText(context, "You can't send empty field", Toast.LENGTH_LONG).show()
        }

        binding.logincreateaccounttext.setOnClickListener() {
            clearInputs()
            replacefrg.replace(this, RegisterFragment())
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.let {
            it.findViewById<BottomNavigationView>(R.id.bottomNav).visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainActivity)?.let {
            it.findViewById<BottomNavigationView>(R.id.bottomNav).visibility = View.VISIBLE
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clearInputs(){
        binding.loginemailinput.text.clear()
        binding.loginpasswordinput.text.clear()
    }

}