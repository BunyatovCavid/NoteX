package com.example.notex.ui.fragments.Profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.notex.Independents.helper.toast
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.databinding.FragmentChangePasswordBinding
import com.example.notex.databinding.FragmentForgotPasswordBinding
import com.example.notex.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.zip.Inflater

@AndroidEntryPoint
class ChangePasswordFragment : DialogFragment(R.layout.fragment_change_password){


    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    private val userViewModel:UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.forgotsendbutton.setOnClickListener{
            var password = binding.changepasswordinput.text.toString()


            if(password.isNotEmpty() && password.isNotBlank()) {
                userViewModel.changePassword(password)
                userViewModel.response.observe(viewLifecycleOwner, { result ->
                      if(result=="Success")
                      {
                          context?.toast("password has been changed successfully")
                      }
                    else
                      {
                          context?.toast(result)
                      }
                    this.dismiss()
                })
            }
            else
            {
                context?.toast("You must write a password.")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}