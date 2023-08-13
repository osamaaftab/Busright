package com.osamaaftab.busright.data.source.remote

import com.osamaaftab.busright.data.service.DirectionServices
import com.osamaaftab.busright.data.source.base.DirectionDataSource
import com.osamaaftab.busright.domain.model.DirectionModel
import com.osamaaftab.busright.domain.model.StopModel
import kotlinx.coroutines.Deferred
import com.osamaaftab.busright.BuildConfig

class DirectionRemoteDataSource(private val directionServices: DirectionServices) :
    DirectionDataSource {
    override suspend fun getDirectionAsync(stops: List<StopModel>): Deferred<DirectionModel> {
        val origin = stops.first().coordinates.lat.toString() + "," + stops.first().coordinates.lng.toString()
        val destination =
            stops.last().coordinates.lat.toString() + "," + stops.last().coordinates.lng.toString()

        val latitudes: List<Double> = stops.map { stop -> stop.coordinates.lat }
        val longitudes: List<Double> = stops.map { stop -> stop.coordinates.lng }

        val coordinates: List<Pair<Double, Double>> = latitudes.zip(longitudes)
        val waypoints = formatWaypoints(coordinates)

        return directionServices.getDirectionAsync(
            origin,
            destination,
            waypoints,
            BuildConfig.API_KEY
        )
    }

    private fun formatWaypoints(coordinates: List<Pair<Double, Double>>): String {
        val waypoints = StringBuilder()
        waypoints.append("optimize:true|")
        for ((lat, lng) in coordinates) {
            waypoints.append("$lat,$lng|")
        }

        return waypoints.toString().removeSuffix("|")
    }
}