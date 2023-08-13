package com.osamaaftab.busright.data.source.base

import com.osamaaftab.busright.domain.model.DirectionModel
import com.osamaaftab.busright.domain.model.ResponseModel
import com.osamaaftab.busright.domain.model.RouteModel
import com.osamaaftab.busright.domain.model.StopModel
import kotlinx.coroutines.Deferred

interface DirectionDataSource {
    suspend fun getDirectionAsync(stops: List<StopModel>): Deferred<DirectionModel>
}