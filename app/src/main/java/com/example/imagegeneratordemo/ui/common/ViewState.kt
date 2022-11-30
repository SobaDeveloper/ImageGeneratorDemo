package com.example.imagegeneratordemo.ui.common

import com.example.imagegeneratordemo.data.Result

sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    data class Success<out T>(val data: T) : ViewState<T>()
    data class Error(val message: String) : ViewState<Nothing>()

    companion object {
        fun <T> setState(result: Result<T>): ViewState<T> {
            return when (result) {
                is Result.Success -> Success(result.data)
                is Result.Error -> Error(result.message)
            }
        }
    }
}