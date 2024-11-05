package com.example.notex.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.notex.Independents.replaceFragments
import com.example.notex.R


class NoInternetFragment : Fragment() {

    private lateinit var nav: replaceFragments

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        nav = replaceFragments()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                nav.replace(this@NoInternetFragment, R.id.action_noInternetFragment_to_noteFragment)
            }
        })

        return inflater.inflate(R.layout.fragment_no_internet, container, false)
    }

}