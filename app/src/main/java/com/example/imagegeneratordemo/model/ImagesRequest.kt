package com.example.imagegeneratordemo.model

import com.google.gson.annotations.SerializedName

data class ImagesRequest(
    val prompt: String,
    val n: Int,
    val size: String,
    @SerializedName("response_format") val format: String
)