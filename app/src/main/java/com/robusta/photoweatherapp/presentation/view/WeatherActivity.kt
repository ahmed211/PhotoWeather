package com.robusta.photoweatherapp.presentation.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cabi.driver.utilities.AppUtils.showSnackBar
import com.cabi.driver.utilities.InternetUtils.isInternetAvailable
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import com.robusta.photoweatherapp.databinding.ActivityWeatherBinding
import com.robusta.photoweatherapp.presentation.repository.datalayer.model.photo.Photo
import com.robusta.photoweatherapp.presentation.repository.datalayer.repository.ConnectionApi
import com.robusta.photoweatherapp.presentation.viewmodel.WeatherVM
import com.robusta.photoweatherapp.utilities.*
import com.robusta.photoweatherapp.utilities.PhotoUtils.CAMERA_REQUEST_CODE
import com.robusta.photoweatherapp.utilities.PhotoUtils.RotateBitmap
import com.robusta.photoweatherapp.utilities.PhotoUtils.currentPhotoPath
import com.robusta.photoweatherapp.utilities.PhotoUtils.dispatchTakePictureIntent
import com.robusta.photoweatherapp.utilities.PhotoUtils.drawTextToBitmap
import java.io.File
import javax.inject.Inject


class WeatherActivity : AppCompatActivity(), LocationListener, RecyclerClick {

    @Inject
    lateinit var connectionApi: ConnectionApi
    private lateinit var binding: ActivityWeatherBinding
    private lateinit var weatherVM: WeatherVM
    private lateinit var locationManager: LocationManager
    private var pickedPhotos = ArrayList<Photo>()
    private lateinit var selectedImage: Bitmap
    private lateinit var photosAdapter: PhotosAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (supportActionBar != null) {
            val actionBar = supportActionBar
            actionBar!!.hide()
        }

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeVM()
        initialization()
        listeners()
//        getWeather()
    }


    private fun listeners() {
        binding.ivPickPhoto.setOnClickListener {
            if (!requestPermissions(
                    arrayOf(
                        LOCATION_PERMISSION, CAMERA_PERMISSION,
                        WRITE_STORAGE_PERMISSION, READ_STORAGE_PERMISSION
                    )
                )
            ) {
                dispatchTakePictureIntent()
            }
        }

        binding.ivShare.setOnClickListener {
            val shareDialog = ShareDialog(this)
            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC)

        }
    }

    private fun initializeVM() {
        (applicationContext as App).netComponent!!.inject(this)
        weatherVM = ViewModelProvider(this).get(WeatherVM::class.java)
        weatherVM.init(connectionApi)
    }


    private fun initialization() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!requestPermissions(
                arrayOf(
                    LOCATION_PERMISSION, CAMERA_PERMISSION,
                    WRITE_STORAGE_PERMISSION, READ_STORAGE_PERMISSION
                )
            )
        ) {
            getCurrentLocation()
        }

        photosAdapter = PhotosAdapter(pickedPhotos, this)
        binding.recyclerPhotos.adapter = photosAdapter


    }


    var imageText = ""
    private fun getWeather(lat: String, lon: String) {
        if (isInternetAvailable()) {
            weatherVM.getWeather(lat, lon)
            weatherVM.response!!.observe(
                this,
                Observer { response ->
                    if (response.status == 200) {
                        val temp = response.data!!.main.temp - 273.15
                        imageText = "${response.data.name} \n $temp °C \n ${response.data.weather[0].description}"

                    } else {
                        showSnackBar("Something went wrong")
                    }
                })

        } else {
            showSnackBar("No internet connection")
        }

    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
    }


    override fun onLocationChanged(location: Location?) {
        Log.v("location", location.toString())
        getWeather(location!!.latitude.toString(), location.longitude.toString())
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {

            PERMISSION_ALL -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED
                            && grantResults[2] == PackageManager.PERMISSION_GRANTED
                            && grantResults[3] == PackageManager.PERMISSION_GRANTED)
                ) {
                    getCurrentLocation()
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    showSnackBar("you should accept this permission to pick photo")
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                Log.e("error", "error")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

                if (imageText.isNotEmpty()) {
                    val file = File(currentPhotoPath)
                    val bitmap = drawTextToBitmap(
                        RotateBitmap(
                            MediaStore.Images.Media.getBitmap(
                                contentResolver,
                                Uri.fromFile(file)
                            ), 90f
                        )!!, imageText
                    )
                    if (bitmap != null) {
                        binding.ivFullPhoto.setImageBitmap(bitmap)
                        pickedPhotos.add(Photo(bitmap))
                        photosAdapter.notifyDataSetChanged()
                        prepareImageForSharing(bitmap)

                    }
                    binding.apply {
                        ivShare.visibility = View.VISIBLE
                        ivFullPhoto.visibility = View.VISIBLE
                        tvNoPhotos.visibility = View.GONE
                    }
                }
                else{
                    showSnackBar("turn on your location")
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun prepareImageForSharing(bitmap: Bitmap) {
        val photo = SharePhoto.Builder()
            .setBitmap(bitmap)
            .build()
        content = SharePhotoContent.Builder()
            .addPhoto(photo)
            .build()
    }

    private lateinit var content: SharePhotoContent
    override fun onImageClick(bitmap: Bitmap?) {
        binding.ivFullPhoto.setImageBitmap(bitmap)
        prepareImageForSharing(bitmap!!)

    }


}