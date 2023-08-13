package com.osamaaftab.busright.presentation.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osamaaftab.busright.domain.model.ErrorModel
import com.osamaaftab.busright.domain.model.ResponseModel
import com.osamaaftab.busright.domain.model.RouteModel
import com.osamaaftab.busright.domain.usecase.GetRoutesUseCase

class RoutesViewModel(private val getRoutesUseCase: GetRoutesUseCase) : ViewModel() {

    private val onProgressShowLiveData = MutableLiveData<Boolean>()

    private val onErrorShowLiveData = MutableLiveData<Boolean>()

    private val routeListLiveData = MutableLiveData<List<RouteModel>>()

    private val routeOnClickLiveData = MutableLiveData<String>()

    init {
        loadRouteList()
    }

    fun getRoutesSuccess(responseModel: ResponseModel) {
        Log.i(ContentValues.TAG, "result : ${responseModel.data}")
        onProgressShowLiveData.postValue(false)
        onErrorShowLiveData.postValue(false)
        routeListLiveData.postValue(responseModel.data)
    }

    fun getRoutesFails(errorModel: ErrorModel?) {
        Log.i(ContentValues.TAG, "error status: ${errorModel?.errorStatus}")
        Log.i(ContentValues.TAG, "error message: ${errorModel?.message}")
        onProgressShowLiveData.postValue(false)
        onErrorShowLiveData.postValue(true)
    }


    private fun loadRouteList() {
        onProgressShowLiveData.postValue(true)
        getRoutesUseCase()
    }

    fun refreshRouteList() {
        getRoutesUseCase()
    }

    private fun getRoutesUseCase() {
        getRoutesUseCase.invoke(viewModelScope, null,
            onSuccess = {
                getRoutesSuccess(it)
            }, onError = {
                getRoutesFails(it)
            })
    }

    fun getShowProgressLiveData(): LiveData<Boolean> {
        return onProgressShowLiveData
    }

    fun getShowErrorLiveData(): LiveData<Boolean> {
        return onErrorShowLiveData
    }

    fun getRouteListLiveData(): LiveData<List<RouteModel>> {
        return routeListLiveData
    }

    fun getOnRouteOnClick(): LiveData<String> {
        return routeOnClickLiveData
    }

    fun onRouteClick(routeId: String) {
        routeOnClickLiveData.postValue(routeId)
    }
}