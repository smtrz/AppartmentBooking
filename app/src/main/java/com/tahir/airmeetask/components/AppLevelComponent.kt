package com.tahir.airmeetask.components

import com.tahir.airmeetask.db.DbRepository
import com.tahir.airmeetask.modules.ContextModule
import com.tahir.airmeetask.modules.DateModule
import com.tahir.airmeetask.modules.DbRepoModule
import com.tahir.airmeetask.modules.NetModule
import com.tahir.airmeetask.vm.MainActivityViewModel

import dagger.Component
import javax.inject.Singleton

@Component(modules = [ContextModule::class, DbRepoModule::class, DateModule::class, NetModule::class])
@Singleton
interface AppLevelComponent {
    fun inject(dr: DbRepository)
    fun inject(mv: MainActivityViewModel)
}
