package com.example.imagegeneratordemo.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverters {

    @TypeConverter
    fun jsonToStringList(json: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(json, listType)
    }

    @TypeConverter
    fun stringListToJson(value: List<String>): String = Gson().toJson(value)
}