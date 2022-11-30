package com.example.imagegeneratordemo.repository

import com.example.imagegeneratordemo.data.Result
import com.example.imagegeneratordemo.data.local.PromptDao
import com.example.imagegeneratordemo.data.remote.OpenAiService
import com.example.imagegeneratordemo.model.ImagesRequest
import com.example.imagegeneratordemo.model.ImagesResponse
import com.example.imagegeneratordemo.model.ImagesResponseErrorBody
import com.example.imagegeneratordemo.model.Prompt
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class OpenAiRepository @Inject constructor(
    private val service: OpenAiService,
    private val promptDao: PromptDao,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun getImages(request: ImagesRequest) = flow {
        try {
            val response = service.generateImage(request)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                promptDao.insertPrompt(body.toPrompt(request.prompt))
                emit(Result.Success(body))
            } else {
                if (response.code() == 400 && response.errorBody() != null) {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()!!.charStream(),
                        ImagesResponseErrorBody::class.java
                    )
                    emit(Result.Error(errorResponse.error.message))
                }

            }
        } catch (e: Throwable) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(ioDispatcher)

    private fun ImagesResponse.toPrompt(prompt: String): Prompt = Prompt(
        prompt = prompt,
        timeStamp = this.created,
        images = this.imageList.map { it.url }
    )
}