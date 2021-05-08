package com.tahir.airmeetask.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "appartments")
data class Appartments(
    @PrimaryKey val id: String,

    var bedrooms: Int?,
    var name: String?,
    var latitude: Double?,
    var longitude: Double?,
    @ColumnInfo(name = "isBooked", defaultValue = "0") var isBooked: Boolean,
    var fromDate: Date? = null,
    var toDate: Date? = null,
    @ColumnInfo(name = "distance", defaultValue = "0.0") var distance: Double

)


