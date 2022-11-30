package com.example.imagegeneratordemo.model

import com.google.gson.annotations.SerializedName

data class ImagesResponse(
    val created: String,
    @SerializedName("data") val imageList: List<ImageUrl>
)