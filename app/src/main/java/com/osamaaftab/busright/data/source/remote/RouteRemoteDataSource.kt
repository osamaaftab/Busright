package com.osamaaftab.busright.data.source.remote

import com.osamaaftab.busright.data.service.RouteServices
import com.osamaaftab.busright.data.source.base.RouteDataSource
import com.osamaaftab.busright.domain.model.ResponseModel
import com.osamaaftab.busright.domain.model.RouteModel
import kotlinx.coroutines.Deferred

class RouteRemoteDataSource(private val routeServices: RouteServices) : RouteDataSource {

    override suspend fun getRoutesAsync(): Deferred<ResponseModel> {
        return routeServices.getRoutesAsync()
    }

    override suspend fun getRouteDetailAsync(routeId: String): Deferred<RouteModel> {
        return routeServices.getRoutesDetailAsync(routeId)
    }
}