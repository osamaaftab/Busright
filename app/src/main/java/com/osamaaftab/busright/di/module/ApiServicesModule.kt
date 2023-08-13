package com.osamaaftab.busright.di.module

import com.osamaaftab.busright.data.service.DirectionServices
import com.osamaaftab.busright.data.service.RouteServices
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val ApiServicesModule = module {
    single { provideRouteService(get(named("bus-rightRetrofit"))) }
    single { provideDirectionService(get(named("googleRetrofit"))) }
}

private fun provideRouteService(retrofit: Retrofit): RouteServices {
    return retrofit.create(RouteServices::class.java)
}

private fun provideDirectionService(retrofit: Retrofit): DirectionServices {
    return retrofit.create(DirectionServices::class.java)
}