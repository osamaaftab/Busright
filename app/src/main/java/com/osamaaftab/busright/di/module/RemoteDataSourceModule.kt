package com.osamaaftab.busright.di.module

import com.osamaaftab.busright.data.service.DirectionServices
import com.osamaaftab.busright.data.service.RouteServices
import com.osamaaftab.busright.data.source.remote.DirectionRemoteDataSource
import com.osamaaftab.busright.data.source.remote.RouteRemoteDataSource
import org.koin.dsl.module

val RemoteDataSourceModule = module {
    single { provideRemoteRouteDataSource(get()) }
    single { provideRemoteDirectionDataSource(get()) }

}

fun provideRemoteRouteDataSource(
    routeServices: RouteServices
): RouteRemoteDataSource {
    return RouteRemoteDataSource(routeServices)
}

fun provideRemoteDirectionDataSource(
    directionServices: DirectionServices
): DirectionRemoteDataSource {
    return DirectionRemoteDataSource(directionServices)
}