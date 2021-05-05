package com.tahir.airmeetask.interfaces


import com.tahir.airmeetask.models.Appartments
import retrofit2.Call
import retrofit2.http.GET

interface EndpointsInterface {

    @GET("product.versioning.com/apartments.json")
    fun getAppartments(

    ): Call<List<Appartments>>

}



