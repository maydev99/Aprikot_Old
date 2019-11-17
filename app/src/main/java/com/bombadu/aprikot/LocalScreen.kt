package com.bombadu.aprikot

import android.Manifest
import android.content.pm.ActivityInfo
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.local_screen.*
import java.util.*


class LocalScreen : AppCompatActivity() {
    private lateinit var  fusedLocationClient : FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.local_screen)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        //runWithPermissions()
        //currentLocation()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.local_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            if (item.itemId == R.id.get_location) {
                currentLocation()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun currentLocation() /*= runWithPermissions(Manifest.permission.ACCESS_COARSE_LOCATION) */{
        //runWithPermissions()
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    latitude = location.latitude
                }
                if (location != null) {
                    longitude = location.longitude
                }
            }


            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> = geocoder!!.getFromLocation(latitude, longitude ,1)

            if(addresses.isNotEmpty()) {
                var myAddress = addresses[0].getAddressLine(0)
                addressTextView.text = myAddress

            }
    }

    override fun onStart() {
        super.onStart()
        runWithPermissions()
        currentLocation()

    }

    private fun runWithPermissions() = runWithPermissions(Manifest.permission.ACCESS_COARSE_LOCATION) {
        currentLocation()

    }

}