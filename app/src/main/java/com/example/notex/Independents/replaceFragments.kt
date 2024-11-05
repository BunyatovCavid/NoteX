package com.example.notex.Independents

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class replaceFragments {

     fun replace(f:Fragment, g:Int)=
         f.findNavController().navigate(g)

    fun replace(f: Fragment, g:Int, b:Bundle)
    {
        f.findNavController().navigate(g, b)
    }

}