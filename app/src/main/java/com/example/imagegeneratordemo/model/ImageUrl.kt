package com.example.imagegeneratordemo.model

import com.google.gson.annotations.SerializedName

data class ImageUrl(
    @SerializedName("b64_json") val url: String
)