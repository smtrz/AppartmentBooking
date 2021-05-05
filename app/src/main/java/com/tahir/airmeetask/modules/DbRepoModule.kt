package com.tahir.airmeetask.modules

import android.content.Context
import androidx.room.Room
import com.tahir.airmeetask.db.AppDB
import com.tahir.airmeetask.db.DbRepository
import com.tahir.airmeetask.db.appartmentsDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DbRepoModule {


    @Provides
    @Singleton
    fun getRepository(): DbRepository {

        return DbRepository()

    }

    @Provides
    @Singleton
    fun getAppDb(c: Context): AppDB {
        return Room.databaseBuilder(c, AppDB::class.java, "appartments").build()


    }

    @Provides
    @Singleton
    fun getAppDao(appDB: AppDB): appartmentsDao {
        return appDB.appartmentDao()


    }

}