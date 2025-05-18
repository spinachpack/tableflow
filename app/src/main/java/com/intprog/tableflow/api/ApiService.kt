package com.intprog.tableflow.api

import com.intprog.tableflow.model.User
import retrofit2.Call
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("addUser")
    fun addUser(@Body user: User): Call<ResponseBody>
}
