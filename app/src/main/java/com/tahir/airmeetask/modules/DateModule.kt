package com.tahir.airmeetask.modules

import android.app.Activity
import com.tahir.airmeetask.activities.MainActivity

import com.tahir.airmeetask.helpers.DatePickerHelper
import dagger.Module
import dagger.Provides
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
    fun getdatePickerHelper(activity: MainActivity): DatePickerHelper {

        return DatePickerHelper(activity)
    }


}

