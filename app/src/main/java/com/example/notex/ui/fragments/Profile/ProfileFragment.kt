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
import com.bumptech.glide.Glide
import com.example.notex.Independents.helper.toast
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.data.models.CheckLoginData
import com.example.notex.data.models.UserModel
import com.example.notex.databinding.FragmentProfileBinding
import com.example.notex.ui.MainActivity
import com.example.notex.ui.fragments.Login.ForgotPasswordFragment
import com.example.notex.viewmodels.AuthorizationViewModel
import com.example.notex.viewmodels.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()


    private var _binding :FragmentProfileBinding? = null
    private val binding get() =_binding!!

    private val authViewModel :AuthorizationViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var  nav:replaceFragments

    var imageUrl:String?=""

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

        try {
            userViewModel.getUser()
            userViewModel.user.observe(viewLifecycleOwner) { result ->
                try {
                    imageUrl = result.imageUrl

                    if (!imageUrl.isNullOrEmpty()) {
                        try {
                            Glide.with(this)
                                .load(imageUrl)
                                .into(binding.userImage)
                        } catch (e: Exception) {
                            crashlytics.recordException(e)
                            context?.toast("Failed to load image")
                        }
                    }

                    binding.editTextTextName.setText(result.name)
                    binding.editTextTextEmailAddress.setText(result.email)
                } catch (e: Exception) {
                    crashlytics.recordException(e)
                    context?.toast("Failed to load user data")
                }
            }
        } catch (e: Exception) {
            crashlytics.recordException(e)
            context?.toast("Failed to load user data")
        }

        binding.editProfilebutton.setOnClickListener{
            var name = binding.editTextTextName.text.toString()
            var email = binding.editTextTextEmailAddress.text.toString()

            var bundle = Bundle()
            bundle.putParcelable("User", UserModel(name, imageUrl, email))
            nav.replace(this, R.id.action_profileFragment2_to_setUpProfileFragment, bundle)
        }

        binding.changePasswordbutton.setOnClickListener{
            val showpopUp = ChangePasswordFragment()
            fragmentManager?.let { it1 -> showpopUp.show(it1,"Hello") }
        }


        binding.signOutButton.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Sign Out")
                setMessage("Are you sure you want to log out?")
                setPositiveButton("Sign Out") { _, _ ->
                    try {
                        authViewModel.singOut(context)
                        findNavController().navigate(R.id.welcomingFragment)
                    } catch (e: Exception) {
                        crashlytics.recordException(e)
                        context?.toast("Failed to sign out")
                    }
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