package com.example.appify.network

import com.example.appify.network.response.UserResponse
import retrofit2.Response
import retrofit2.http.GET

interface UserService {

    // GET ALL USERS AS A LIST
    @GET("users")
    suspend fun getUsers(): Response<List<UserResponse>>
}