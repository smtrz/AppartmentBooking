package com.tahir.airmeetask.modules

import dagger.Module
import dagger.Provides
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Module
class DateModule {

    @Provides
    @Singleton
    fun getDate(): Date {

        return Date()
    }

    @Provides
    @Singleton
    fun getDateFormat(): SimpleDateFormat {

        return SimpleDateFormat("dd/MM/yyy HH:mm:ss", Locale.US)
    }


}

