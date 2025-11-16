package com.example.lab14_maps


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Button // 匯入 Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.android.PolyUtil
import com.google.maps.model.TravelMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var currentPolyline: Polyline? = null
    private val taipei101 = LatLng(25.033611, 121.565000)
    private val taipeiMainStation = LatLng(25.047924, 121.517081)
    private val directionsApiKey = "AIzaSyA25NliRLxZxuKwAPZEMUQlIeiNyFYU5Pc"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        checkLocationPermission()

        mMap.addMarker(MarkerOptions().position(taipei101).title("台北101"))
        mMap.addMarker(MarkerOptions().position(taipeiMainStation).title("台北車站"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(25.04, 121.54), 13f))


        findViewById<Button>(R.id.btnDriving).setOnClickListener {
            drawRoute(TravelMode.DRIVING)
        }

        findViewById<Button>(R.id.btnWalking).setOnClickListener {
            drawRoute(TravelMode.WALKING)
        }

        findViewById<Button>(R.id.btnBicycling).setOnClickListener {
            drawRoute(TravelMode.BICYCLING)
        }


        drawRoute(TravelMode.WALKING)
    }

    private fun drawRoute(mode: TravelMode) {
        if (directionsApiKey.isEmpty() || directionsApiKey == "貼上你那把可以用的 Directions API 金鑰") {
            Toast.makeText(this, "Directions API Key 為空，無法規劃路線", Toast.LENGTH_SHORT).show()
            return
        }


        val geoApiContext = GeoApiContext.Builder()
            .apiKey(directionsApiKey)
            .build()


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val directionsResult = DirectionsApi.newRequest(geoApiContext)
                    .origin(com.google.maps.model.LatLng(taipei101.latitude, taipei101.longitude))
                    .destination(com.google.maps.model.LatLng(taipeiMainStation.latitude, taipeiMainStation.longitude))
                    .mode(mode)
                    .await()


                withContext(Dispatchers.Main) {
                    currentPolyline?.remove()
                    if (directionsResult.routes.isNotEmpty()) {
                        val route = directionsResult.routes[0]
                        val decodedPath = PolyUtil.decode(route.overviewPolyline.encodedPath)

                        val polylineOptions = PolylineOptions()
                            .addAll(decodedPath)
                            .color(Color.RED)
                            .width(15f)

                        currentPolyline = mMap.addPolyline(polylineOptions)
                    } else {
                        Toast.makeText(this@MainActivity, "找不到 ${mode.name} 路線", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "規劃路線時發生錯誤", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private val REQUEST_PERMISSIONS = 1

    private fun checkLocationPermission() {
        val fine = ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarse = ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fine && coarse) {
            mMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    mMap.isMyLocationEnabled = true
                }
            } else {
                Toast.makeText(this, "你需要同意定位權限才能使用此功能", Toast.LENGTH_SHORT).show()
            }
        }
    }
}