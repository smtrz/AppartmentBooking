package com.tahir.airmeetask.app

import android.app.Application
import com.tahir.airmeetask.components.AppLevelComponent
import com.tahir.airmeetask.components.DaggerAppLevelComponent
import com.tahir.airmeetask.modules.*

class App : Application() {
    lateinit var appLevelComponent: AppLevelComponent


    override fun onCreate() {
        super.onCreate()
        app = this
        // setting up modules.

        appLevelComponent = DaggerAppLevelComponent.builder()
            .contextModule(ContextModule(this))
            .dateModule(DateModule())
            .netModule(NetModule("https://s3-eu-west-1.amazonaws.com/"))
            .dbRepoModule(DbRepoModule())
            .build()


    }

    companion object {
        lateinit var app: App
    }


}
