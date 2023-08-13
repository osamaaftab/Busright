package com.osamaaftab.busright.domain.repository

import com.osamaaftab.busright.domain.model.DirectionModel
import com.osamaaftab.busright.domain.model.Resource
import com.osamaaftab.busright.domain.model.StopModel

interface DirectionRepository{
    suspend fun getDirections(stops: List<StopModel>): Resource<DirectionModel>
}