package com.suffixit.smartadmin.core.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Base64
import java.io.ByteArrayOutputStream

object ImageUtils {
    fun compressAndRotateImage(filePath: String,image: Bitmap): Bitmap {
        val originalWidth = image.width
        val originalHeight = image.height
        val targetWidth = originalWidth / 5
        val targetHeight = originalHeight / 5
        val resizedImage = Bitmap.createScaledBitmap(image, targetWidth, targetHeight, false)
        val outputStream = ByteArrayOutputStream()
        resizedImage.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        val imageBytes = outputStream.toByteArray()
        val compressedBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        val rotatedBitmap = rotateBitmapIfNeeded(compressedBitmap, filePath)
        return rotatedBitmap
    }

    private fun rotateBitmapIfNeeded(bitmap: Bitmap, filePath: String): Bitmap {
        val exif = ExifInterface(filePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val matrix = Matrix()

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun bitmapToBase64(bitmap: Bitmap, quality: Int = 100): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream) // Compress the bitmap
        val byteArray = outputStream.toByteArray() // Convert the output stream to byte array
        return Base64.encodeToString(byteArray, Base64.DEFAULT) // Encode the byte array to Base64 string
    }

    fun base64StringToBitmap(image:String): Bitmap {
        val decodedString: ByteArray = Base64.decode(image, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        return decodedByte
    }

}