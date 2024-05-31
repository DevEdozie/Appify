package com.example.appify.network.response

data class UserResponse(
    val address: Address,
    val email: String,
    val id: Int,
    val name: String,
    val username: String
)