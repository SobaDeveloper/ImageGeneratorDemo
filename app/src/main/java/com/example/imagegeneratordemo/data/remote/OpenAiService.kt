package com.example.imagegeneratordemo.data.remote

import com.example.imagegeneratordemo.model.ImagesRequest
import com.example.imagegeneratordemo.model.ImagesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAiService {

    @POST("images/generations")
    suspend fun generateImage(@Body request: ImagesRequest): Response<ImagesResponse>
}