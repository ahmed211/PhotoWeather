package com.robusta.photoweatherapp.utilities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cabi.driver.utilities.AppUtils.showSnackBar


const val PERMISSION_ALL = 1
const val MY_PERMISSIONS_REQUEST_READ_STORAGE = 2
const val READ_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
const val WRITE_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
const val CAMERA_PERMISSION = Manifest.permission.CAMERA
fun Context.requestPermissions(permissions: Array<String>): Boolean {

    var cont = 0
    for (permission in permissions) {

        if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this as Activity, permission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_ALL)
            } else {
                // No explanation needed, we can request the permission.
                showSnackBar("please allow permissions from settings")
                ActivityCompat.requestPermissions(this,
                    arrayOf(permission),
                    MY_PERMISSIONS_REQUEST_READ_STORAGE)


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            cont++
//            return false
            // Permission has already been granted
        }

    }
    return cont != 4
}

