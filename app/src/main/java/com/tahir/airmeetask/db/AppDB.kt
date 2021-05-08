package com.tahir.airmeetask.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tahir.airmeetask.models.Appartments

// Creating database abstract class holding the data access object
@Database(entities = [Appartments::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)

abstract class AppDB : RoomDatabase() {

    abstract fun appartmentDao(): appartmentsDao
}
