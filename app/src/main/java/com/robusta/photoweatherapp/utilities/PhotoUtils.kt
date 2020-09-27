package com.robusta.photoweatherapp.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.ColorInt
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.robusta.photoweatherapp.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


/**
 * Created by Ahmed Mostafa on 9/24/2020.
 */
object PhotoUtils {

    val CAMERA_REQUEST_CODE = 1

    fun RotateBitmap(
        source: Bitmap,
        angle: Float
    ): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0,
            source.width, source.height, matrix, true)
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun Activity.createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


    fun Activity.dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.robusta.photoweatherapp.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
                }
            }
        }
    }

    fun drawTextToBitmap(
        bitmap: Bitmap,
        watermarkText: String,
        options: WatermarkOptions = WatermarkOptions()
    ): Bitmap {
        val result = bitmap.copy(bitmap.config, true)
        val canvas = Canvas(result)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        paint.textAlign = when (options.corner) {
            Corner.TOP_LEFT,
            Corner.BOTTOM_LEFT -> Paint.Align.LEFT
            Corner.TOP_RIGHT,
            Corner.BOTTOM_RIGHT -> Paint.Align.RIGHT
        }
        val textSize = result.width * options.textSizeToWidthRatio
        paint.textSize = textSize
        paint.color = options.textColor
        if (options.shadowColor != null) {
            paint.setShadowLayer(textSize / 2, 0f, 0f, options.shadowColor)
        }
        if (options.typeface != null) {
            paint.typeface = options.typeface
        }
        val padding = result.width * options.paddingToWidthRatio
        val coordinates =
            calculateCoordinates(watermarkText, paint, options, canvas.width, canvas.height, padding)
        canvas.drawText(watermarkText, coordinates.x, coordinates.y, paint)
        return result
    }

    private fun calculateCoordinates(
        watermarkText: String,
        paint: Paint,
        options: WatermarkOptions,
        width: Int,
        height: Int,
        padding: Float
    ): PointF {
        val x = when (options.corner) {
            Corner.TOP_LEFT,
            Corner.BOTTOM_LEFT -> {
                padding
            }
            Corner.TOP_RIGHT,
            Corner.BOTTOM_RIGHT -> {
                width - padding
            }
        }
        val y = when (options.corner) {
            Corner.BOTTOM_LEFT,
            Corner.BOTTOM_RIGHT -> {
                height - padding
            }
            Corner.TOP_LEFT,
            Corner.TOP_RIGHT -> {
                val bounds = Rect()
                paint.getTextBounds(watermarkText, 0, watermarkText.length, bounds)
                val textHeight = bounds.height()
                textHeight + padding

            }
        }
        return PointF(x, y)
    }

    enum class Corner {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
    }

    data class WatermarkOptions(
        val corner: Corner = Corner.BOTTOM_RIGHT,
        val textSizeToWidthRatio: Float = 0.04f,
        val paddingToWidthRatio: Float = 0.03f,
        @ColorInt val textColor: Int = Color.WHITE,
        @ColorInt val shadowColor: Int? = Color.BLACK,
        val typeface: Typeface? = null
    )

}