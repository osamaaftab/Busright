package com.osamaaftab.busright.di.module

import com.osamaaftab.busright.data.ApiErrorHandle
import org.koin.dsl.module

val AppModule = module {
    single { provideApiError() }
}

fun provideApiError(): ApiErrorHandle {
    return ApiErrorHandle()
}