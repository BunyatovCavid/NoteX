package com.example.notex.ui.fragments.Login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.databinding.FragmentForgotPasswordBinding
import com.example.notex.ui.MainActivity
import com.example.notex.viewmodels.AuthorizationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : DialogFragment(R.layout.fragment_forgot_password) {

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthorizationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

                try {
                    authViewModel.resetPassword(email)
                    binding.forgotemailinput.text.clear()
                    dismiss()

                    authViewModel.resetPasswordResult.observe(viewLifecycleOwner, { result ->
                        Toast.makeText(context, result, Toast.LENGTH_LONG).show()
                    })
                } catch (e: Exception) {
                    crashlytics.recordException(e)
                    Toast.makeText(context, "An error occurred while resetting the password.", Toast.LENGTH_LONG).show()
                }
            }
            else
            {
                Toast.makeText(context, "Please write Email.", Toast.LENGTH_LONG).show()
            }
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