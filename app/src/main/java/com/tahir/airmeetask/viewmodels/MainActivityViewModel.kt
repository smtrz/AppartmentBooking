package com.tahir.airmeetask.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tahir.airmeetask.app.App
import com.tahir.airmeetask.db.DbRepository
import com.tahir.airmeetask.models.Appartments
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MainActivityViewModel : ViewModel() {
    @Inject
    lateinit var now: Date

    @Inject
    lateinit var dateTimeFormat: SimpleDateFormat

    @Inject
    lateinit var dbrepo: DbRepository
    var notes: LiveData<List<Appartments>> = MutableLiveData()
    private val noteInsertionObserver: MutableLiveData<Long> = MutableLiveData()
    private val noteEditObserver: MutableLiveData<Int> = MutableLiveData()

    init {
        //dagger initialization
        App.app.appLevelComponent.inject(this)
        // getting the notes as soon as instance of MainActivityView Model is created.
        // getMyNotes()
    }


}