package com.osamaaftab.busright.data.repository

import com.osamaaftab.busright.data.ApiErrorHandle
import com.osamaaftab.busright.data.repository.base.BaseRepositoryImp
import com.osamaaftab.busright.data.source.remote.RouteRemoteDataSource
import com.osamaaftab.busright.domain.model.DirectionModel
import com.osamaaftab.busright.domain.model.Resource
import com.osamaaftab.busright.domain.model.ResponseModel
import com.osamaaftab.busright.domain.model.RouteModel
import com.osamaaftab.busright.domain.repository.DirectionRepository
import com.osamaaftab.busright.domain.repository.RouteRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class RouteRepositoryImp(
    private val routeRemoteDataSource: RouteRemoteDataSource,
    private val directionRepository: DirectionRepository,
    errorHandle: ApiErrorHandle
) : RouteRepository, BaseRepositoryImp(errorHandle) {

    private lateinit var directionModel: Deferred<Resource<DirectionModel>>

    override suspend fun getRoutes(): Flow<Resource<ResponseModel>> = flow {
        emit(routeRemoteDataSource.getRoutesAsync().awaitAndCatch())
    }

    override suspend fun getRouteDetail(routeId: String): Flow<Resource<RouteModel>> = flow {
        routeRemoteDataSource.getRouteDetailAsync(routeId).awaitAndCatch().let {
            if (it.data != null) {
                getDirectionData(it.data)
            }
            emit(it)
        }
    }

    private suspend fun getDirectionData(routeModel: RouteModel) {
        withContext(Dispatchers.IO) {
            directionModel = async {
                directionRepository.getDirections(routeModel.stops)
            }
        }

        routeModel.apply {
            overviewPolyline = directionModel.await().data?.routes?.first()?.overviewPolyline
        }
    }
}