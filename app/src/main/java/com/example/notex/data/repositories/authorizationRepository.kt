package com.example.notex.data.repositories

import android.util.Log
import com.example.notex.data.interfaces.AuthorizationInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.crashlytics.FirebaseCrashlytics
import android.content.Context

class AuthorizationRepository :AuthorizationInterface {

    private val firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    private  val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    override fun logIn(email: String, password: String, callback: (Boolean, String?) -> Unit) {
               firebaseAuth.signInWithEmailAndPassword(email,password)
                   .addOnCompleteListener { task ->
                       if (task.isSuccessful) {
                           callback(true, "Welcome")
                       }
                   }
                   .addOnFailureListener { e ->
                       crashlytics.recordException(e)
                       Log.d("CostumeExceptionHandle", e.message.toString())
                       callback(false, "Enter email and password correctly")
                   }
    }

    override  fun singUp(email: String, password: String, callback: (Boolean, String?) -> Unit ) {
              firebaseAuth.createUserWithEmailAndPassword(email,password)
                  .addOnCompleteListener { task ->
                      if (task.isSuccessful) {
                          callback(true, "Successful")
                      } else {
                          val errorMessage = when (task.exception) {
                              is FirebaseAuthUserCollisionException -> "Email is now available."
                              is FirebaseAuthWeakPasswordException -> "Very weak password: ${task.exception?.message}"
                              is FirebaseAuthInvalidCredentialsException -> "Wrong email format."
                              else -> task.exception?.message.toString()
                          }
                          task.exception?.let { crashlytics.recordException(it) }
                          callback(false, errorMessage)
                      }
                  }
                  .addOnFailureListener { e ->
                      crashlytics.recordException(e)
                      Log.d("CostumeExceptionHandle", e.message.toString())
                      callback(false, "Could not register")
                  }
    }

    override fun singOut(context: Context) {
        firebaseAuth.signOut()

        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("isLoggedIn", false) // Login vəziyyətini false olaraq təyin et
        editor.apply()
    }

    override fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthSuccess", "Password reset email sent successfully.")
                    onResult(true, "Password reset email sent successfully.")
                } else {
                    task.exception?.let { crashlytics.recordException(it) }
                    onResult(false, "Didn't know the link was sent. Check email accuracy")
                }
            }
            .addOnFailureListener { e ->
                crashlytics.recordException(e)
                Log.d("CostumeExceptionHandle", e.message.toString())
                onResult(false, "Didn't know the link was sent. Check email accuracy")
            }
    }

}