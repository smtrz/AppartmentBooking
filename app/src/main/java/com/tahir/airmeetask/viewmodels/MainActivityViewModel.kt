package com.tahir.airmeetask.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tahir.airmeetask.app.App
import com.tahir.airmeetask.db.DbRepository
import com.tahir.airmeetask.models.Appartments
import com.tahir.airmeetask.viewstate.DataState
import com.tahir.airmeetask.viewstate.SubmitStatus
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel : ViewModel() {

    @Inject
    lateinit var dbrepo: DbRepository
    private val noteInsertionObserver: MutableLiveData<Long> = MutableLiveData()
    private val noteEditObserver: MutableLiveData<Int> = MutableLiveData()
    private val _dataState: MutableLiveData<DataState<List<Appartments>>> = MutableLiveData()
    val dataState: MutableLiveData<DataState<List<Appartments>>>
        get() = _dataState

    init {
        //dagger initialization
        App.app.appLevelComponent.inject(this)

    }

    /**
     * Method for setting up state
     * @param mainStateEvent - instance of sealed  class - MainActSM.kt file.
     */
    fun setStateEvent(mainStateEvent: SubmitStatus, Selectedappartment: Appartments?) {

        viewModelScope.launch {
            when (mainStateEvent) {
                is SubmitStatus.getData -> {
                    //  getting appartment's data
                    dbrepo.getAppartmentsData()
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }

                SubmitStatus.None -> {
                    // For now nothing...
                }
                SubmitStatus.Book -> {
                    //appartment booking.
                    dbrepo.bookAppartment(
                        Selectedappartment?.fromDate!!,
                        Selectedappartment?.toDate!!,
                        Selectedappartment?.id!!
                    )
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
                SubmitStatus.Search -> {
                    //appartment search.
                    dbrepo.searchAppartment(
                        Selectedappartment?.fromDate!!,
                        Selectedappartment?.toDate!!,
                        Selectedappartment?.bedrooms!!
                    )
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }
}