package com.tahir.airmeetask.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appartments")
data class Appartments(
    @PrimaryKey val id: String,

    val bedrooms: Int?,
    val name: String?,
    val latitude: Double?,
    val longitude: Double?,
    val isBooked: Boolean?,
    val fromDate: String?,
    val toDate: String?,
    val distance: Double?

)


