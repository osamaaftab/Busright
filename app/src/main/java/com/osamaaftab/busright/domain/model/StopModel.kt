package com.osamaaftab.busright.domain.model

import com.squareup.moshi.Json

data class StopModel(
    val id: String,
    @field:Json(name = "coord")
    val coordinates: CoordinatesModel,
    val students: List<StudentModel>
)