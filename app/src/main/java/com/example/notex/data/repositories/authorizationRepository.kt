package com.example.notex.data.repositories

import android.util.Log
import com.example.notex.data.models.LoginEntity
import com.example.notex.data.interfaces.Dao.LoginDao
import com.example.notex.data.interfaces.AuthorizationInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthorizationRepository @Inject constructor(private val loginDao: LoginDao) :AuthorizationInterface {

    override var firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()
        set(value) {}

    override fun logIn(email: String, password: String, callback: (Boolean, String?) -> Unit) {
               firebaseAuth.signInWithEmailAndPassword(email,password)
                   .addOnCompleteListener { task ->
                       if (task.isSuccessful) {
                           callback(true, "Welcome")
                       } else {
                           callback(false, "Login failed: ${task.exception?.message}")
                       }
                   }
                   .addOnFailureListener { exception ->
                       callback(false, "Error: ${exception.message}")
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
                              is FirebaseAuthUserCollisionException -> "Email artıq istifadə olunub."
                              is FirebaseAuthWeakPasswordException -> "Çox zəif parol: ${task.exception?.message}"
                              is FirebaseAuthInvalidCredentialsException -> "Yanlış email formatı."
                              else -> "Digər xəta: ${task.exception?.message}"
                          }
                          Log.e("AuthError", errorMessage)
                          callback(false, errorMessage)
                      }
                  }
                  .addOnFailureListener { exception ->
                      Log.e("AuthError", "Firebase ilə əlaqə problemi: ${exception.message}")
                      callback(false, "Firebase ilə əlaqə problemi: ${exception.message}")
                  }
    }

    override fun singOut() {
        firebaseAuth.signOut()
    }

    override fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthSuccess", "Parolu sıfırlamaq üçün email göndərildi.")
                    onResult(true, "Password reset email sent successfully.")
                } else {
                    Log.e("AuthError", "Parolu sıfırlamaq üçün email göndərilmədi: ${task.exception?.message}")
                    onResult(false, task.exception?.message)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("AuthError", "Xəta: ${exception.message}")
                onResult(false, exception.message)
            }
    }

    override suspend fun saveUserToLocalDatabase(loginEntity: LoginEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            loginDao.insertLoginData(loginEntity)
        }
    }


}