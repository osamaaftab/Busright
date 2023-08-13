package com.osamaaftab.busright.di.module

import com.osamaaftab.busright.data.ApiErrorHandle
import com.osamaaftab.busright.domain.repository.RouteRepository
import com.osamaaftab.busright.domain.usecase.GetRouteDetailUseCase
import com.osamaaftab.busright.domain.usecase.GetRoutesUseCase
import org.koin.dsl.module


val UseCaseModule = module {
    single { provideGetRoutesUseCase(get(), get()) }
    single { provideGetRouteDetailUseCase(get(), get()) }
}

private fun provideGetRoutesUseCase(
    routeRepository: RouteRepository,
    apiErrorHandle: ApiErrorHandle
): GetRoutesUseCase {
    return GetRoutesUseCase(routeRepository, apiErrorHandle)
}

private fun provideGetRouteDetailUseCase(
    routeRepository: RouteRepository,
    apiErrorHandle: ApiErrorHandle
): GetRouteDetailUseCase {
    return GetRouteDetailUseCase(routeRepository, apiErrorHandle)
}