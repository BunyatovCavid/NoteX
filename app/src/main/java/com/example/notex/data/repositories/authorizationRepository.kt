package com.example.notex.data.repositories

import android.util.Log
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

class AuthorizationRepository @Inject constructor(private val loginDao: LoginDao, private  val firebaseAuth: FirebaseAuth, private  val crashlytics: FirebaseCrashlytics) :AuthorizationInterface {

    override fun logIn(email: String, password: String, callback: (Boolean, String?) -> Unit) {
               firebaseAuth.signInWithEmailAndPassword(email,password)
                   .addOnCompleteListener { task ->
                       if (task.isSuccessful) {
                           callback(true, "Welcome")
                       } else {
                           task.exception?.let { crashlytics.recordException(it) }
                           callback(false, "Enter email and password correctly")
                       }
                   }
                   .addOnFailureListener { exception ->
                       crashlytics.recordException(exception)
                       callback(false, "The process failed.")
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
                              else -> "The process failed"
                          }
                          task.exception?.let { crashlytics.recordException(it) }
                          callback(false, errorMessage)
                      }
                  }
                  .addOnFailureListener { exception ->
                      crashlytics.recordException(exception)
                      callback(false, "The process failed")
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
                    onResult(false, "The process failed")
                }
            }
            .addOnFailureListener { exception ->
                crashlytics.recordException(exception)
                onResult(false, "The process failed")
            }
    }

    override suspend fun saveUserToLocalDatabase(loginEntity: LoginEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            loginDao.insertLoginData(loginEntity)
        }
    }


}