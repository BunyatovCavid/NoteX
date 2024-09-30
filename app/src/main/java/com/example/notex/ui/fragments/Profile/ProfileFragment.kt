package com.example.notex.ui.fragments.Profile

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.data.models.CheckLoginData
import com.example.notex.databinding.FragmentProfileBinding
import com.example.notex.ui.MainActivity
import com.example.notex.viewmodels.AuthorizationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding :FragmentProfileBinding? = null
    private val binding get() =_binding!!

    private val authViewModel :AuthorizationViewModel by viewModels()

    private lateinit var  nav:replaceFragments

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        nav = replaceFragments()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signOutButton.setOnClickListener{
            AlertDialog.Builder(activity).apply {
                setTitle("SingOut Note")
                setMessage("Are you sure you want to log out?")
                setPositiveButton("SignOut") { _, _ ->
                    authViewModel.singOut()

                    authViewModel.loginEntity.observe(viewLifecycleOwner, {result->
                        if(result =="Deleted") {
                            val checkLoginData = CheckLoginData(true)
                            findNavController().navigate(
                                R.id.action_profileFragment2_to_onBoardingFragment,
                                Bundle().apply {
                                    putParcelable("checkLoginData", checkLoginData)
                                })
                        }
                    })
                }
                setNegativeButton("CANCEL", null)
            }.create().show()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}