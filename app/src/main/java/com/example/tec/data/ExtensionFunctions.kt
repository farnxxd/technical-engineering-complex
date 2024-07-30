package com.example.tec.data

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.tec.data.member.Member

/**
 * Returns member's full name.
 */
fun Member.fullName(): String {
    return "$firstName $lastName"
}

/**
 * Profile pictures are store as byteArray in database. In order to show profile picture as
 * Bitmap, we use this function.
 */
fun convertByteArrayToBitmap(byteArray: ByteArray?): Bitmap? =
    byteArray?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }

/**
 * This function opens Email app with receiver field ready.
 */
fun sendEmail(context: Context, address: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$address")
    }

    val chooser = Intent.createChooser(intent, "Choose an Email app")
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(chooser)
    }
}

/**
 * This function opens Phone app with contact number ready to dial.
 */
fun callUnit(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

/**
 * This function opens Google Map with university marker on it.
 *      Attention: only needed if osmdroid is implemented.
 *          not necessary in google map version.
 */
fun redirectToGoogleMap(context: Context) {
    val uri = Uri.parse("geo:0,0?q=37.2435,49.5776")
    val intent = Intent(Intent.ACTION_VIEW, uri)

    context.startActivity(intent)
}