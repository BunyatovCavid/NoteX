package com.example.notex.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.notex.Independents.BaseDatas
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.databinding.FragmentLoginBinding
import com.example.notex.databinding.FragmentRegisterBinding
import com.example.notex.ui.MainActivity
import com.example.notex.viewmodels.authorizationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {


    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: authorizationViewModel by viewModels()
    private lateinit var replacefrg: replaceFragments


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        replacefrg = replaceFragments()

        binding.registerbackbutton.setOnClickListener(){
            clearInputs()
            replacefrg.replace(this, WelcomingFragment())
        }

        binding.registerLoginaccounttext.setOnClickListener{
            replacefrg.replace(this, LoginFragment())
        }

        binding.registerRegisterbutton.setOnClickListener(){
            if(!binding.registeremailinput.text.isNullOrEmpty() && !binding.registerpasswordinput.text.isNullOrEmpty()  && !binding.registerRepasswordinput.text.isNullOrEmpty()) {
              if(binding.registerpasswordinput.text.toString() == binding.registerRepasswordinput.text.toString()){
                  authViewModel.singUp(
                    binding.registeremailinput.text.toString(),
                    binding.registerpasswordinput.text.toString(),
                    this
                )
                if (BaseDatas.checkRegister) {
                    clearInputs()
                }
             }
              else
               Toast.makeText(context, "Password and Re-Password don't equal.", Toast.LENGTH_LONG).show()

            }
            else
                Toast.makeText(context, "You can't send empty field", Toast.LENGTH_LONG).show()

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
        binding.registeremailinput.text.clear()
        binding.registerpasswordinput.text.clear()
        binding.registerRepasswordinput.text.clear()
    }


}