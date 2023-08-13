package com.osamaaftab.busright.di.module

import com.osamaaftab.busright.data.ApiErrorHandle
import com.osamaaftab.busright.data.repository.DirectionRepositoryImp
import com.osamaaftab.busright.data.repository.RouteRepositoryImp
import com.osamaaftab.busright.data.source.remote.DirectionRemoteDataSource
import com.osamaaftab.busright.data.source.remote.RouteRemoteDataSource
import com.osamaaftab.busright.domain.repository.DirectionRepository
import com.osamaaftab.busright.domain.repository.RouteRepository
import org.koin.dsl.module

val RepositoryModule = module {
    single { provideRouteRepository(get(), get(), get()) }
    single { provideDirectionRepository(get(), get()) }
}

fun provideRouteRepository(
    routeRemoteDataSource: RouteRemoteDataSource,
    directionRepository: DirectionRepository,
    errorHandle: ApiErrorHandle
): RouteRepository {
    return RouteRepositoryImp(routeRemoteDataSource, directionRepository, errorHandle)
}

fun provideDirectionRepository(
    directionRemoteDataSource: DirectionRemoteDataSource,
    errorHandle: ApiErrorHandle
): DirectionRepository {
    return DirectionRepositoryImp(directionRemoteDataSource, errorHandle)
}