package com.example.appify.network

import com.example.appify.network.response.UserResponse

class UserRepository: BaseRepository() {


    suspend fun getUsers(): BaseResponse<List<UserResponse>>{
        return safeApiCall {
            ApiClient.apiService.getUsers()
        }
    }


}