package com.example.notex.Independents

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.notex.R


class replaceFragments {

     fun replace(f:Fragment, g:Fragment)=
      f.parentFragmentManager.beginTransaction().replace(R.id.container, g).commit()

    fun replace(f: Fragment, g:Fragment, b:Bundle)
    {
        f.arguments = b
        f.parentFragmentManager.beginTransaction().replace(R.id.container, g).commit()
    }

}