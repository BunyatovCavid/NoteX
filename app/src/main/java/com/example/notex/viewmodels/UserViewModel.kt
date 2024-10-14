package com.example.notex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notex.data.interfaces.UserInterface
import com.example.notex.data.models.UserModel
import com.example.notex.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserInterface): ViewModel() {

    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> get() = _user

    private var _response:MutableLiveData<String> = MutableLiveData<String>()
    val response:LiveData<String> get() = _response


    fun getUser()
    {
        var user = repository.getUser()
        _user.value = user
    }

    fun updateUser(userModel: UserModel) {
        repository.updateUser(userModel){result->
            _response.postValue(result)
        }
    }


    fun changePassword(password:String)=
        repository.changePassword(password){result->
            var i:Int =0
            _response.postValue(result)
        }

}