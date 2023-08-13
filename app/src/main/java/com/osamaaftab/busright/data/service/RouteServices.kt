package com.osamaaftab.busright.data.service

import com.osamaaftab.busright.domain.model.ResponseModel
import com.osamaaftab.busright.domain.model.RouteModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface RouteServices {
    @GET("/mobile/routes")
    fun getRoutesAsync(): Deferred<ResponseModel>

    @GET("/mobile/routes/{routeID}")
    fun getRoutesDetailAsync(@Path("routeID") routeId: String): Deferred<RouteModel>
}