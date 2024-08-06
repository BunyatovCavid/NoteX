package com.example.notex.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.notex.data.interfaces.authorizationInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class authorizationViewModel @Inject constructor( private val repository:authorizationInterface) : ViewModel() {

    fun logIn(email:String, password:String, f:Fragment)=
        repository.logIn(email, password, f)

    fun singUp(email: String, password: String, f: Fragment) =
        repository.singUp(email, password, f)

    fun resetPassword(email: String)=
        repository.resetPassword(email)
}