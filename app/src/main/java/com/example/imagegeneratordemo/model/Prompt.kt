package com.example.imagegeneratordemo.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "prompt_table")
data class Prompt(
    @PrimaryKey val prompt: String,
    val timeStamp: String,
    @ColumnInfo(name = "imageList") val images: List<String>
) : Parcelable