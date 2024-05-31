package com.example.appify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appify.network.BaseResponse
import com.example.appify.network.UserRepository
import com.example.appify.network.response.UserResponse
import kotlinx.coroutines.launch

class LoginViewModel(application: Application): AndroidViewModel(application) {

    val userRepo by lazy {
        UserRepository()
    }

    private val _usersResult: MutableLiveData<BaseResponse<List<UserResponse>>> = MutableLiveData()
    val usersResult: LiveData<BaseResponse<List<UserResponse>>> = _usersResult

    fun getUsers(){
        _usersResult.postValue(BaseResponse.Loading())
        viewModelScope.launch {
            val response = userRepo.getUsers()
            if(response is BaseResponse.Success){
                _usersResult.postValue(response)
            }else if(response is BaseResponse.Error){
                _usersResult.postValue(response)
            }
        }
    }

}