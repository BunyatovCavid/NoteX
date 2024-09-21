package com.example.notex.ui.fragments.Login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.databinding.FragmentWelcomingBinding
import com.example.notex.ui.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomingFragment : Fragment(R.layout.fragment_welcoming) {

    private var _binding: FragmentWelcomingBinding? = null
    private val binding get() = _binding!!

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