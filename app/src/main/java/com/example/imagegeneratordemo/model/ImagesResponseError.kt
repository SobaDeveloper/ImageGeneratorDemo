package com.example.imagegeneratordemo.model

import com.google.gson.annotations.SerializedName

data class ImagesResponseErrorBody(
    @SerializedName("error") val error: ImagesResponseError
)


data class ImagesResponseError(
    val code: String,
    val message: String,
    val param: String,
    val type: String
)