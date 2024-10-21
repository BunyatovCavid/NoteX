package com.example.notex.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notex.data.models.LoginEntity
import com.example.notex.data.interfaces.Dao.LoginDao
import com.example.notex.data.interfaces.AuthorizationInterface
import com.example.notex.data.interfaces.UserInterface
import com.example.notex.data.models.UserModel
import com.example.notex.data.repositories.UserRepository
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor( private val repository:AuthorizationInterface, private val userRepository: UserInterface,
                                                  private val loginDao: LoginDao, ) : ViewModel() {

    private val crashlytics : FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    private val _loginResult = MutableLiveData<String?>()
    val loginResult: LiveData<String?> get() = _loginResult

    private val _loginEntity = MutableLiveData<String>()
    val loginEntity:LiveData<String> get() = _loginEntity

    private val _signUpResult = MutableLiveData<String?>()
    val signUpResult: LiveData<String?> get() = _signUpResult

    private val _resetPasswordResult = MutableLiveData<String?>()
    val resetPasswordResult: LiveData<String?> get() = _resetPasswordResult

    fun singOut(context: Context)
    {
        viewModelScope.launch {
            try {
                repository.singOut(context)
            } catch (e: Exception) {
                _loginResult.postValue("The process failed while Updated")
                crashlytics.recordException(e)
                Log.d("TestCostume", "The process failed while Updated")
            }
        }
    }

    fun logIn(email: String, password: String) {
        viewModelScope.launch {
           try
           {
               repository.logIn(email, password) { success, message ->
                   if (success) {
                       _loginResult.postValue(message)
                   } else {
                       _loginResult.postValue(null)
                   }
               }
           }catch (e:Exception)
           {
               crashlytics.recordException(e)
               Log.d("TestCostume", "Login de xeta oldu")
               _loginResult.postValue("Login de xeta oldu")
           }

        }
    }



    private suspend fun getUserFromLocalDatabase(): LoginEntity? {
        return loginDao.getLoginData()
    }


    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            repository.singUp(email, password) { success, message ->
                _signUpResult.postValue(message)
            }
        }
    }

    fun resetPassword(email: String)
    {
        viewModelScope.launch {
            repository.resetPassword(email){ success, message->
                _resetPasswordResult.postValue(message)
            }
        }
    }

    fun updateProfile(){
        val defaultUser = UserModel(
            name = "New User"
        )

        val storageRef = FirebaseStorage.getInstance().reference
        val defaultImageRef =
            storageRef.child("default_images/default_profile_image.jpg")
        defaultImageRef.downloadUrl.addOnSuccessListener { uri ->
            val defaultImageUrl = uri.toString()
            defaultUser.imageUrl = defaultImageUrl

            userRepository.updateUser(defaultUser) { updateResult ->
                if (updateResult == "Success") {
                    _signUpResult.postValue("Success")
                } else {
                    _signUpResult.postValue("Failed")
                }
            }


        }.addOnFailureListener { exception ->
            crashlytics.recordException(exception)
        }
    }


}

