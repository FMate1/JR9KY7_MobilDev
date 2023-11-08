package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.myapplication.entity.Shoes

@Dao
interface ShoesDao {

    @Query("SELECT * FROM shoes ORDER BY id DESC")
    fun getAllShoes(): List<Shoes>

}