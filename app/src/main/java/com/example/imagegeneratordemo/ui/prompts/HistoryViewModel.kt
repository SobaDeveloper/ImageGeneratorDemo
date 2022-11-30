package com.example.imagegeneratordemo.ui.prompts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagegeneratordemo.model.Prompt
import com.example.imagegeneratordemo.repository.PromptsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: PromptsRepository
) : ViewModel() {

    private val _promptsLiveData = MutableLiveData<MutableList<Prompt>>()
    val promptsLiveData: LiveData<MutableList<Prompt>> = _promptsLiveData

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> = _isLoadingLiveData

    fun getPrompts() {
        viewModelScope.launch {
            repository.getPrompts().onStart {
                _isLoadingLiveData.value = true
            }.collect() {
                _promptsLiveData.value = it.toMutableList()
                _isLoadingLiveData.value = false
            }
        }
    }

    fun deletePrompt(position: Int) {
        viewModelScope.launch {
            promptsList()?.let {
                val deleted = it[position]
                it.removeAt(position)
                repository.deletePrompt(deleted)
            }
        }
    }

    fun clearPrompts() {
        viewModelScope.launch {
            promptsList()?.clear()
            repository.deleteAll()
        }
    }

    fun promptsList() = promptsLiveData.value
}