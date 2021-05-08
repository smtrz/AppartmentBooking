package com.tahir.airmeetask.interfaces


import com.tahir.airmeetask.models.Appartments
import retrofit2.Response
import retrofit2.http.GET

interface EndpointsInterface {

    @GET("product.versioning.com/apartments.json")
    suspend fun getAppartments(

    ): Response<List<Appartments>>

}



