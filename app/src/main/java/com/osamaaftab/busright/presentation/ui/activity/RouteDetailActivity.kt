package com.osamaaftab.busright.presentation.ui.activity

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.osamaaftab.busright.R
import com.osamaaftab.busright.databinding.ActivityRouteDetailBinding
import com.osamaaftab.busright.domain.model.RouteModel
import com.osamaaftab.busright.domain.model.StopModel
import com.osamaaftab.busright.presentation.viewmodel.RouteDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class RouteDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private val routeDetailViewModel: RouteDetailViewModel by viewModel()
    private lateinit var activityRouteDetailBinding: ActivityRouteDetailBinding
    private val routeId: String by lazy { intent?.getStringExtra("routeId") ?: "" }
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRouteDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_route_detail)
        initObserver()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.route_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        routeDetailViewModel.getRouteDetails(routeId)
    }

    private fun initObserver() {
        observeRouteListLiveData()
        observerProgressLiveData()
        observeShowErrorLiveData()
    }

    private fun observeShowErrorLiveData() {
        routeDetailViewModel.getShowErrorLiveData().observe(this) {
            if (it == true) {
                activityRouteDetailBinding.statusImg.visibility = View.VISIBLE
            } else activityRouteDetailBinding.statusImg.visibility = View.GONE
        }
    }

    private fun observerProgressLiveData() {
        routeDetailViewModel.getShowProgressLiveData().observe(this) {
            if (it == true) {
                activityRouteDetailBinding.indeterminateBar.visibility = View.VISIBLE
            } else activityRouteDetailBinding.indeterminateBar.visibility = View.GONE
        }
    }

    private fun observeRouteListLiveData() {
        routeDetailViewModel.getRouteDetailsLiveData().observe(this) {
            activityRouteDetailBinding.routeContentView.visibility = View.VISIBLE
            activityRouteDetailBinding.route.text = it.name
            activityRouteDetailBinding.totalStop.text =
                "Number of stops in this route are: " + it.stops.size
            activityRouteDetailBinding.totalStudents.text =
                "Number of students in this route based on stops are: " + it.stops.map { stop -> stop.students.size }
            drawRoutePath(it)
        }
    }

    private fun drawRoutePath(routeModel: RouteModel) {
        val polylineOptions = PolylineOptions()
        polylineOptions.addAll(PolyUtil.decode(routeModel.overviewPolyline?.points))
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(routeModel.stops.first().coordinates.lat, routeModel.stops.first().coordinates.lng),
                16f
            )
        )
        addMarkers(routeModel.stops)
        polylineOptions.color(Color.MAGENTA)
        polylineOptions.width(10f)
        map.addPolyline(polylineOptions)
    }

    private fun addMarkers(points: List<StopModel>) {
        for (latLng in points) {
            map.addMarker(
                MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMarker(R.drawable.ic_marker)))
                    .position(LatLng(latLng.coordinates.lat, latLng.coordinates.lng))
            )
        }
    }

    private fun resizeMarker(drawable: Int): Bitmap {
        val bitmapDrawable =
            ResourcesCompat.getDrawable(resources, drawable, null) as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap
        val expectedWidth = 80
        return Bitmap.createScaledBitmap(
            bitmap,
            expectedWidth,
            bitmap.height * expectedWidth / bitmap.width,
            false
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_in_night))
    }
}