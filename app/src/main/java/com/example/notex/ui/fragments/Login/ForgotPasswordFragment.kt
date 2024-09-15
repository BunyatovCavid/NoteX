package com.example.notex.ui.fragments.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.notex.Independents.replaceFragments
import com.example.notex.databinding.FragmentForgotPasswordBinding
import com.example.notex.viewmodels.authorizationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : DialogFragment() {


    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: authorizationViewModel by viewModels()
    private lateinit var replacefrg: replaceFragments


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.forgotsendbutton.setOnClickListener{
            if (!binding.forgotemailinput.text.isNullOrEmpty())
            {
                val email =binding.forgotemailinput.text.toString()

                authViewModel.resetPassword(email)
                binding.forgotemailinput.text.clear()
                dismiss()
                authViewModel.resetPasswordResult.observe(viewLifecycleOwner, { result ->
                    Toast.makeText(context, result, Toast.LENGTH_LONG).show()
            })
            }
            else
            {
                Toast.makeText(context, "Please write Email.", Toast.LENGTH_LONG).show()
            }
        }

    }

}