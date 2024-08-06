package com.example.notex.data.repositories

import android.widget.Toast
import androidx.collection.emptyLongSet
import androidx.fragment.app.Fragment
import com.example.notex.Independents.BaseDatas
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.data.interfaces.authorizationInterface
import com.example.notex.ui.fragments.HomeFragment
import com.example.notex.ui.fragments.LoginFragment
import com.example.notex.ui.fragments.SetUpProfileFragment
import com.google.firebase.auth.FirebaseAuth

class authorizationRepository :authorizationInterface {

    override var firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()
        set(value) {}

    private var replacefrg:replaceFragments = replaceFragments()

    override  fun logIn(email: String, password: String,f:Fragment) {
               firebaseAuth.signInWithEmailAndPassword(email,password)
                   .addOnCompleteListener{
                       if(!it.isSuccessful)
                       {
                           Toast.makeText(f.context, "Email or Password is wrong", Toast.LENGTH_SHORT).show()
                       }
                       else {
                           BaseDatas.checkLogin = true
                           Toast.makeText(f.context, "Welcome", Toast.LENGTH_LONG).show()
                           replacefrg.replace(f, HomeFragment())
                       }
                   }

    }

    override  fun singUp(email: String, password: String, f:Fragment ) {
              firebaseAuth.createUserWithEmailAndPassword(email,password)
                  .addOnCompleteListener{
                      if (it.isSuccessful)
                      {
                          BaseDatas.checkRegister=true
                          Toast.makeText(f.context, "Please Log In", Toast.LENGTH_LONG).show()
                          replacefrg.replace(f,LoginFragment())
                      }
                  }
    }

    override fun resetPassword(email: String) {
       firebaseAuth.sendPasswordResetEmail(email)
           .addOnCompleteListener{
               if(it.isSuccessful)
               {

               }
           }
    }


}