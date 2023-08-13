package com.osamaaftab.busright.data.repository

import com.osamaaftab.busright.data.ApiErrorHandle
import com.osamaaftab.busright.data.repository.base.BaseRepositoryImp
import com.osamaaftab.busright.data.source.remote.DirectionRemoteDataSource
import com.osamaaftab.busright.domain.model.DirectionModel
import com.osamaaftab.busright.domain.model.Resource
import com.osamaaftab.busright.domain.model.StopModel
import com.osamaaftab.busright.domain.repository.DirectionRepository

class DirectionRepositoryImp(
    private val directionRemoteDataSource: DirectionRemoteDataSource,
    errorHandle: ApiErrorHandle
) : DirectionRepository, BaseRepositoryImp(errorHandle) {

    override suspend fun getDirections(stops: List<StopModel>): Resource<DirectionModel> {
        return directionRemoteDataSource.getDirectionAsync(stops).awaitAndCatch()
    }
}