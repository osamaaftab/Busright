package com.osamaaftab.busright.data.source.base

import com.osamaaftab.busright.domain.model.ResponseModel
import com.osamaaftab.busright.domain.model.RouteModel
import kotlinx.coroutines.Deferred

interface RouteDataSource {
    suspend fun getRoutesAsync(): Deferred<ResponseModel>
    suspend fun getRouteDetailAsync(routeId: String): Deferred<RouteModel>
}