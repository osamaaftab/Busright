package com.osamaaftab.busright.presentation.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osamaaftab.busright.domain.model.ErrorModel
import com.osamaaftab.busright.domain.model.RouteModel
import com.osamaaftab.busright.domain.usecase.GetRouteDetailUseCase

class RouteDetailViewModel(private val getRouteDetailUseCase: GetRouteDetailUseCase) : ViewModel() {

    private val onProgressShowLiveData = MutableLiveData<Boolean>()

    private val onErrorShowLiveData = MutableLiveData<Boolean>()

    private val routeDetailsLiveData = MutableLiveData<RouteModel>()

    fun getRouteDetailSuccess(routeModel: RouteModel) {
        Log.i(ContentValues.TAG, "result : $routeModel")
        onProgressShowLiveData.postValue(false)
        onErrorShowLiveData.postValue(false)
        routeDetailsLiveData.postValue(routeModel)
    }

    fun getRouteDetailFails(errorModel: ErrorModel?) {
        Log.i(ContentValues.TAG, "error status: ${errorModel?.errorStatus}")
        Log.i(ContentValues.TAG, "error message: ${errorModel?.message}")
        onProgressShowLiveData.postValue(false)
        onErrorShowLiveData.postValue(true)
    }


    fun getRouteDetails(routeId: String) {
        onProgressShowLiveData.postValue(true)
        getRouteDetailUseCase.invoke(viewModelScope, routeId,
            onSuccess = {
                getRouteDetailSuccess(it)
            }, onError = {
                getRouteDetailFails(it)
            })
    }

    fun getShowProgressLiveData(): LiveData<Boolean> {
        return onProgressShowLiveData
    }

    fun getShowErrorLiveData(): LiveData<Boolean> {
        return onErrorShowLiveData
    }

    fun getRouteDetailsLiveData(): LiveData<RouteModel> {
        return routeDetailsLiveData
    }
}