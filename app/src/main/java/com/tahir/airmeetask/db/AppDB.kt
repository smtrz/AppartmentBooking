package com.tahir.airmeetask.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.tahir.airmeetask.models.Appartments

// Creating database abstract class holding the data access object
@Database(entities = [Appartments::class], version = 1, exportSchema = false)
abstract class AppDB : RoomDatabase() {

    abstract fun appartmentDao(): appartmentsDao
}
