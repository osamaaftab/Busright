package com.osamaaftab.busright.domain.repository

import com.osamaaftab.busright.domain.model.Resource
import com.osamaaftab.busright.domain.model.ResponseModel
import com.osamaaftab.busright.domain.model.RouteModel
import kotlinx.coroutines.flow.Flow

interface RouteRepository {
    suspend fun getRoutes(): Flow<Resource<ResponseModel>>
    suspend fun getRouteDetail(routeId: String): Flow<Resource<RouteModel>>
}