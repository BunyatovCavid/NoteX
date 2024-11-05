package com.example.notex.ui.fragments.Login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.notex.Independents.helper.toast
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.databinding.FragmentWelcomingBinding
import com.example.notex.ui.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomingFragment : Fragment(R.layout.fragment_welcoming) {

    private val crashlytics:FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()


    private var _binding: FragmentWelcomingBinding? = null
    private val binding get() = _binding

    private lateinit var replacefrg: replaceFragments

    private var backPressedOnce = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWelcomingBinding.inflate(inflater, container, false)
        val view = binding?.root

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedOnce) {
                    requireActivity().finish()
                } else {
                    backPressedOnce = true
                    context?.toast("Press back again to exit")
                    view?.postDelayed({
                        backPressedOnce = false
                    }, 2000)
                }
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        replacefrg = replaceFragments()


        binding?.welcomingLoginButton?.setOnClickListener {
            try {
                replacefrg.replace(this, R.id.action_welcomingFragment_to_loginFragment)
            } catch (e: Exception) {
                crashlytics.recordException(e)
                context?.toast("An error occurred while navigating: ${e.message}")
            }
        }

        binding?.welcomingRegisterButton?.setOnClickListener {
            try {
                replacefrg.replace(this, R.id.action_welcomingFragment_to_registerFragment)
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
                context?.toast("An error occurred while navigating: ${e.message}")
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



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}