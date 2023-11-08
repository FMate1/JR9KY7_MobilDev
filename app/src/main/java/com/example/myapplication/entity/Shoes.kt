package com.example.myapplication.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "shoes")
class Shoes (

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "shoesName")
    var shoesName: String,

    @ColumnInfo(name = "price")
    var price: Int,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "pictureUrl")
    var picture: String

) : Serializable