package com.tahir.airmeetask.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.tahir.airmeetask.models.Appartments

@Dao
interface appartmentsDao {


    //getting all the inserted appartments ordered by distance in ascending  order
    @Query("SELECT * from appartments WHERE isBooked=0  ORDER BY distance  ASC")
    fun getAllAppartments(): LiveData<List<Appartments>>

    @Query("SELECT * from appartments WHERE id=:id")
    fun getAppartmentFromId(id: Int): LiveData<Appartments>

    // update appartment from id
    @Query("UPDATE appartments SET isBooked=1,fromDate=:fromDate,toDate=toDate  WHERE id = :id")
    suspend fun updateAppartment(
        fromDate: String,
        id: String
    ): Int


}