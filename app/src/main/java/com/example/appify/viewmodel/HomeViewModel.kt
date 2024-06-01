package com.example.appify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appify.database.UserDatabase
import com.example.appify.database.model.User
import com.example.appify.database.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application): AndroidViewModel(application){

    private val repository: UserRepository

    init {
        val userDao = UserDatabase.getDatabase(application).getUserDao()
        repository = UserRepository(userDao)
    }

    // Add a user if the user ID does not already exist in the list
    fun addUser(user: User) {
        // Fetch the list of all users
        val allUsersLiveData = getAllUsers()

        // Observe the LiveData to get the current list of users
        allUsersLiveData.observeForever { allUsers ->
            // Check if the user ID is already in the list
            val isUserExists = allUsers.any { it.id == user.id }

            // If the user does not exist, add the user
            if (!isUserExists) {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.addUser(user)
                }
            }
        }
    }

    fun getAllUsers() = repository.getAllUsers()




}