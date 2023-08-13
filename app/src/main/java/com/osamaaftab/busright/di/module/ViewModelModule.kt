package com.osamaaftab.busright.di.module

import com.osamaaftab.busright.presentation.viewmodel.RouteDetailViewModel
import com.osamaaftab.busright.presentation.viewmodel.RoutesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ActivityModule = module {
    viewModel { RoutesViewModel(get()) }
    viewModel { RouteDetailViewModel(get()) }
}