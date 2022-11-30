package com.example.imagegeneratordemo.data.local

import androidx.room.*
import com.example.imagegeneratordemo.model.Prompt

@Dao
interface PromptDao {

    @Query("SELECT * FROM prompt_table ORDER BY timeStamp DESC")
    fun getAll(): List<Prompt>

    @Query("SELECT * FROM prompt_table WHERE prompt LIKE :prompt ")
    fun getPrompt(prompt: String): Prompt

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPrompt(prompts: Prompt)

    @Delete
    fun delete(prompt: Prompt)

    @Query("DELETE FROM prompt_table")
    fun deleteAll()
}