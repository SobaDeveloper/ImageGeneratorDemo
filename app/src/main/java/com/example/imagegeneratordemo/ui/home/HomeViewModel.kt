package com.example.imagegeneratordemo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagegeneratordemo.data.Result
import com.example.imagegeneratordemo.model.ImagesRequest
import com.example.imagegeneratordemo.model.ImagesResponse
import com.example.imagegeneratordemo.repository.OpenAiRepository
import com.example.imagegeneratordemo.ui.common.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: OpenAiRepository
) : ViewModel() {

    private val _viewStateLiveData = MutableLiveData<ViewState<ImagesResponse>>()
    val viewStateLiveData: LiveData<ViewState<ImagesResponse>> = _viewStateLiveData

    // Pair ([url], [prompt])
    private var _currentImage = Pair("", "")
    val currentImage: Pair<String, String>
        get() = _currentImage

    fun getImages(prompt: String) {
        viewModelScope.launch {
            repository.getImages(
                ImagesRequest(
                    prompt = prompt, n = 1, size = "512x512",
                    format = "b64_json"
                )
            ).onStart { _viewStateLiveData.value = ViewState.Loading }
                .map {
                    if (it is Result.Success) {
                        _currentImage = _currentImage.copy(
                            first = it.data.imageList[0].url,
                            second = prompt
                        )
                    }
                    ViewState.setState(it)
                }
                .collect() {
                    _viewStateLiveData.value = it
                }
        }
    }
}