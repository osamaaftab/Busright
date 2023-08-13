package com.osamaaftab.busright.data.service

import com.osamaaftab.busright.domain.model.DirectionModel
import com.osamaaftab.busright.domain.model.ResponseModel
import com.osamaaftab.busright.domain.model.RouteModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DirectionServices {
    @GET("maps/api/directions/json")
    fun getDirectionAsync(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("waypoints") waypoints : String,
        @Query("key") key: String
    ): Deferred<DirectionModel>
}