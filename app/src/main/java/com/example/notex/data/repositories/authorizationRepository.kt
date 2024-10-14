package com.example.notex.data.repositories

import android.util.Log
import android.widget.Toast
import com.example.notex.data.models.LoginEntity
import com.example.notex.data.interfaces.Dao.LoginDao
import com.example.notex.data.interfaces.AuthorizationInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthorizationRepository @Inject constructor(private val loginDao: LoginDao) :AuthorizationInterface {

    private val firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    private  val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    override fun logIn(email: String, password: String, callback: (Boolean, String?) -> Unit) {
               firebaseAuth.signInWithEmailAndPassword(email,password)
                   .addOnCompleteListener { task ->
                       if (task.isSuccessful) {
                           callback(true, "Welcome")
                       } else {
                           task.exception?.let { crashlytics.recordException(it) }
                           Log.d("TestCostum", task.exception?.message.toString())
                           callback(false, "Enter email and password correctly")
                       }
                   }
                   .addOnFailureListener { exception ->
                       crashlytics.recordException(exception)
                       Log.d("TestCostum", exception.message.toString())
                       callback(false, "Enter email and password correctly")
                   }
    }

    override  fun singUp(email: String, password: String, callback: (Boolean, String?) -> Unit ) {
              firebaseAuth.createUserWithEmailAndPassword(email,password)
                  .addOnCompleteListener { task ->
                      if (task.isSuccessful) {
                          Log.d("AuthSuccess", "Yeni istifadəçi yaradıldı: ${firebaseAuth.currentUser?.email}")
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
                  .addOnFailureListener { exception ->
                      crashlytics.recordException(exception)
                      callback(false, "Could not register")
                  }
    }

    override fun singOut() {
        firebaseAuth.signOut()
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
            .addOnFailureListener { exception ->
                crashlytics.recordException(exception)
                onResult(false, "Didn't know the link was sent. Check email accuracy")
            }
    }

    override suspend fun saveUserToLocalDatabase(loginEntity: LoginEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            loginDao.insertLoginData(loginEntity)
        }
    }


}