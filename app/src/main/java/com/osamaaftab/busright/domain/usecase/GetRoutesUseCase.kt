package com.osamaaftab.busright.domain.usecase

import com.osamaaftab.busright.data.ApiErrorHandle
import com.osamaaftab.busright.domain.model.Resource
import com.osamaaftab.busright.domain.model.ResponseModel
import com.osamaaftab.busright.domain.repository.RouteRepository
import com.osamaaftab.busright.domain.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow

class GetRoutesUseCase constructor(
    private val routeRepository: RouteRepository,
    apiErrorHandle: ApiErrorHandle?
) : UseCase<ResponseModel, Int>(apiErrorHandle) {
    override suspend fun run(params: Int?): Flow<Resource<ResponseModel>> {
        return routeRepository.getRoutes()
    }
}
