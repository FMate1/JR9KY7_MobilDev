package com.example.myapplication.database

import com.example.myapplication.converter.ShoesTypeConverter
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.converter.CartItemTypeConverter
import com.example.myapplication.converter.UserTypeConverter
import com.example.myapplication.dao.CartDao
import com.example.myapplication.dao.ShoesDao
import com.example.myapplication.dao.OrderDao
import com.example.myapplication.dao.UserDao
import com.example.myapplication.entity.Cart
import com.example.myapplication.entity.Shoes
import com.example.myapplication.entity.Order
import com.example.myapplication.entity.User

@Database(entities = [Shoes::class, User::class, Cart::class, Order::class], version = 7, exportSchema = false)
@TypeConverters(ShoesTypeConverter::class, CartItemTypeConverter::class, UserTypeConverter::class)
abstract class ShoesDB: RoomDatabase() {

    companion object{

        private var shoesDB:ShoesDB? = null

        @Synchronized
        fun getDatabase(context: Context): ShoesDB{
            if (shoesDB == null){
                shoesDB = Room.databaseBuilder(
                    context,
                    ShoesDB::class.java,
                    "shoes.db"
                ).fallbackToDestructiveMigration().build()
            }
            return shoesDB!!
        }
    }

    abstract fun getShoesDao():ShoesDao
    abstract fun getUserDao():UserDao
    abstract fun getCartDao(): CartDao
    abstract fun getOrderDao(): OrderDao

}