package com.osamaaftab.busright.domain.model

import com.squareup.moshi.Json

data class RouteModel(
    val id: String,
    val name: String,
    val stops: List<StopModel>,
    @field:Json(name = "overview_polyline")
    var overviewPolyline: PolyLineModel?,
)