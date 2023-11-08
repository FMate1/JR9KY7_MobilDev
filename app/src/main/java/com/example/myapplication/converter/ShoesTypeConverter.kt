package com.example.myapplication.converter

import androidx.room.TypeConverter
import com.example.myapplication.entity.Shoes
import com.google.gson.Gson

class ShoesTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromShoes(shoes: Shoes): String {
        return gson.toJson(shoes)
    }

    @TypeConverter
    fun toShoes(json: String): Shoes {
        return gson.fromJson(json, Shoes::class.java)
    }
}
