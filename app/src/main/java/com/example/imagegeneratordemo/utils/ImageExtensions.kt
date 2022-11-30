package com.example.imagegeneratordemo.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

object ImageExtensions {

    fun String.base64ToBitmap(): Bitmap {
        val bytes: ByteArray = Base64.decode(this, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}