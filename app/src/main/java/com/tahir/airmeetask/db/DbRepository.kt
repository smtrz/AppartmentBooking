package com.tahir.airmeetask.db


import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tahir.airmeetask.app.App
import com.tahir.airmeetask.helpers.Utils
import com.tahir.airmeetask.interfaces.EndpointsInterface
import com.tahir.airmeetask.models.Appartments
import com.tahir.airmeetask.viewstate.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import java.util.*
import javax.inject.Inject


class DbRepository {
    @Inject
    lateinit var appartmentsDao: appartmentsDao

    @Inject
    lateinit var c: Context

    @Inject
    lateinit var retrofit: Retrofit
    lateinit var fusedLocationClient: FusedLocationProviderClient
    var updatedArray: MutableLiveData<ArrayList<Appartments>> = MutableLiveData()

    init {
        // injecting Dagger component
        App.app.appLevelComponent.inject(this@DbRepository)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(c)


    }

    @SuppressLint("MissingPermission")
    // we acquired location already
    fun getmyLocation(ILocation: locationSuccess) {

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
// callback for location
                    ILocation.getLocation(location)

                }

            }


    }


    suspend fun getAppartmentsData(): Flow<DataState<List<Appartments>>> =

        flow {
// getting current location of the user.
            var currentLocation: Location? = null
            getmyLocation(object : locationSuccess {
                override fun getLocation(loc: Location) {
                    currentLocation = loc
                }
            })

            var apiResponse: Int

            val endpoints = retrofit.create(EndpointsInterface::class.java)

            emit(DataState.Loading)

            try {

                apiResponse = endpoints.getAppartments().code()


                when (apiResponse) {
                    200 -> {

                        // first of getting the result into an array

                        var res_from_api =
                            endpoints.getAppartments().body() as ArrayList<Appartments>
                        // creating a copy
                        var updated_result: ArrayList<Appartments> = arrayListOf()
                        updated_result.addAll(res_from_api)

// iterating through the array and editing the copy all the way with distance.
                        var count = 0
                        for (appartment in res_from_api) {


                            appartment.distance = Utils.distance(
                                appartment.latitude!!,
                                appartment.longitude!!,
                                currentLocation!!.latitude,
                                currentLocation!!.longitude


                            )
                            updated_result.add(count, appartment)
                            count++
                        }

                        // then inserting the data into the db
                        appartmentsDao.insertappartmentData(
                            updated_result
                        )
                        // returning the inserted rows.

                        emit(DataState.Success(appartmentsDao.getAllAppartments()))
                    }
                    400 -> {
                        emit(DataState.Error("400 - Bad request"))

                    }
                    404 -> {
                        emit(DataState.Error("404 - Not found"))

                    }
                    500 -> {
                        emit(DataState.Error("500 - Internal server error"))

                    }
                    403 -> {
                        emit(DataState.Error("500 - Forbidden"))

                    }
                    401 -> {
                        emit(DataState.Error("401 -  Unauthorized"))

                    }
                    else -> {
                        emit(DataState.Error("Error occured with response code " + apiResponse))
                    }

                }


            } catch (e: Exception) {
                emit(DataState.Error(e.message.toString()))
            }

        }
// method for booking the apparment.

    suspend fun bookAppartment(
        fromDate: Date,
        toDate: Date,
        id: String
    ): Flow<DataState<List<Appartments>>> =

        flow {
            emit(DataState.Loading)
            try {
                // updating the db with the newly booked appartment.
                appartmentsDao.bookAppartment(fromDate, toDate, id)
                //emitting the data of all the appartments.
                emit(DataState.Success(appartmentsDao.getAllAppartments()))
            } catch (e: Exception) {
                emit(DataState.Error(e.message.toString()))
            }
        }
// searching the appartment.
    suspend fun searchAppartment(
        fromDate: Date,
        toDate: Date,
        bedroom: Int
    ): Flow<DataState<List<Appartments>>> =

        flow {
            emit(DataState.Loading)
            try {
                emit(
                    DataState.Success(
                        appartmentsDao.searchAppartments(fromDate, bedroom)
                    )
                )
            } catch (e: Exception) {
                emit(DataState.Error(e.message.toString()))
            }
        }

    interface locationSuccess {

        fun getLocation(loc: Location)

    }

}

