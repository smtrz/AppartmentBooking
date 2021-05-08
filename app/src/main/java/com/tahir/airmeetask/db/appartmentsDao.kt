package com.tahir.airmeetask.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tahir.airmeetask.models.Appartments
import java.util.*

@Dao
interface appartmentsDao {
    // inserting all the records and ignoring the one which are already there for the sake of simplicity.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertappartmentData(appartment: List<Appartments>): List<Long>

    //getting all the inserted appartments ordered by distance in ascending  order
    @Query("SELECT * from appartments where isBooked=0  ORDER BY distance  ASC")
    suspend fun getAllAppartments(): List<Appartments>

    // getting appartment data from ID
    @Query("SELECT * from appartments WHERE id=:id")
    fun getAppartmentFromId(id: Int): LiveData<Appartments>

    // update appartment from id
    @Query("UPDATE appartments SET isBooked=1,fromDate=:fromDate,toDate=toDate  WHERE id = :id")
    suspend fun updateAppartment(
        fromDate: Date,
        id: String
    ): Int

    // udating the appartment data if it is booked.
    @Query("UPDATE appartments SET isBooked=1,fromDate=:fromDate,toDate=:toDate  WHERE id = :id")
    suspend fun bookAppartment(
        fromDate: Date,
        toDate: Date,
        id: String
    ): Int

    // Query for searching the appartment.
    @Query("SELECT * from appartments where  (:fromDate>=toDate OR fromDate IS NULL)  AND bedrooms=:bedrooms   ORDER BY distance  ASC")
    suspend fun searchAppartments(
        fromDate: Date,
        bedrooms: Int
    ): List<Appartments>
}