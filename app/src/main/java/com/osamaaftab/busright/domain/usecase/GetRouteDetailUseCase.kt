package com.osamaaftab.busright.domain.usecase

import com.osamaaftab.busright.data.ApiErrorHandle
import com.osamaaftab.busright.domain.model.Resource
import com.osamaaftab.busright.domain.model.RouteModel
import com.osamaaftab.busright.domain.repository.RouteRepository
import com.osamaaftab.busright.domain.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow

class GetRouteDetailUseCase constructor(
    private val routeRepository: RouteRepository,
    apiErrorHandle: ApiErrorHandle?
) : UseCase<RouteModel, String>(apiErrorHandle) {
    override suspend fun run(params: String?): Flow<Resource<RouteModel>> {
        if (params == null) {
            throw IllegalArgumentException("Param must not be null")
        }
        return routeRepository.getRouteDetail(params)
    }
}