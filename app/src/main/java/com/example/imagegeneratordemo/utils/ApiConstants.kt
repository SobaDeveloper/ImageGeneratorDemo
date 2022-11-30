package com.example.imagegeneratordemo.utils

object ApiConstants {
    const val BASE_URL = "https://api.openai.com/v1/"

    const val API_KEY = "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"
    const val KEY_HEADER_AUTHORIZATION = "Authorization"
    const val KEY_HEADER_CONTENT_TYPE = "Content-Type"
    const val VALUE_HEADER_BEARER = "Bearer $API_KEY"
    const val VALUE_HEADER_JSON = "application/json"
}