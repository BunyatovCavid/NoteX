package com.example.notex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notex.data.models.LoginEntity
import com.example.notex.data.interfaces.Dao.LoginDao
import com.example.notex.data.interfaces.authorizationInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class authorizationViewModel @Inject constructor( private val repository:authorizationInterface, private val loginDao: LoginDao) : ViewModel() {

    private val _loginResult = MutableLiveData<String?>()
    val loginResult: LiveData<String?> get() = _loginResult

    private val _loginEntity = MutableLiveData<String>()
    val loginEntity:LiveData<String> get() = _loginEntity

    private val _signUpResult = MutableLiveData<String?>()
    val signUpResult: LiveData<String?> get() = _signUpResult

    private val _resetPasswordResult = MutableLiveData<String?>()
    val resetPasswordResult: LiveData<String?> get() = _resetPasswordResult


    fun singOut()
    {
        viewModelScope.launch {
            try {
                repository.singOut()

                 val user = getUserFromLocalDatabase()
                 if (user != null) {
                     loginDao.deleteUser(user)
                     _loginEntity.postValue("Deleted")
                 }
            } catch (e: Exception) {
                _loginResult.postValue("Çıxma zamanı bir hata baş verdi: ${e.message}")
            }
        }
    }

    fun logIn(email: String, password: String) {
        viewModelScope.launch {
            repository.logIn(email, password) { success, message ->
                if (success) {
                    viewModelScope.launch {
                        repository.saveUserToLocalDatabase(LoginEntity(0, email, password))
                    }
                    _loginResult.postValue(message)
                } else {
                    _loginResult.postValue(message)
                }
            }
        }
    }



    private suspend fun getUserFromLocalDatabase(): LoginEntity? {
        return loginDao.getLoginData()
    }

    fun checkSavedUser() {
        viewModelScope.launch {
            val user = getUserFromLocalDatabase()
            if (user != null) {
                logIn(user.email, user.password)
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            repository.singUp(email, password) { success, message ->
                if (success) {
                    viewModelScope.launch {
                        repository.saveUserToLocalDatabase(LoginEntity(0,email,password))
                    }
                }
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


}

