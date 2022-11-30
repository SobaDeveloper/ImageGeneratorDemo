package com.example.imagegeneratordemo.repository

import com.example.imagegeneratordemo.data.local.PromptDao
import com.example.imagegeneratordemo.model.Prompt
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class PromptsRepository @Inject constructor(
    private val promptDao: PromptDao,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getPrompts() = flow {
        emit(promptDao.getAll())
    }.flowOn(ioDispatcher)

    fun deletePrompt(prompt: Prompt) = CoroutineScope(ioDispatcher).launch {
        promptDao.delete(prompt)
    }

    fun deleteAll() = CoroutineScope(ioDispatcher).launch {
        promptDao.deleteAll()
    }
}