package com.example.appify.database.repository

import com.example.appify.database.UserDao
import com.example.appify.database.model.User

class UserRepository(
    private val userDao: UserDao
) {

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }

    fun getAllUsers() = userDao.getAllUsers()


}