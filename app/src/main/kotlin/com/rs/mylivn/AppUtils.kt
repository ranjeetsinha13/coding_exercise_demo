package com.rs.mylivn

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.support.annotation.DrawableRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.rs.mylivn.entity.ImagesData
import org.jetbrains.anko.coroutines.experimental.bg
import java.io.*


val FILE_NAME = "data.json"
private val TAG = "AppUtils"

fun Activity.doInitialSetup() {
    // Check if file is present in External storage
    val filePath = Environment.getExternalStorageDirectory().path + "/" +
            Environment.DIRECTORY_DOWNLOADS + "/" + FILE_NAME
    val isFilePresent = checkFilePresent(filePath)
    Log.d(TAG, " File is present$isFilePresent")
    if (!isFilePresent) {
        bg { this.createFile(filePath) }

    }
}

fun checkFilePresent(filePath: String): Boolean {
    return File(filePath).exists()
}

fun Activity.createFile(filePath: String) {
    var inputStream: InputStream? = null
    try {
        inputStream = this.resources.openRawResource(R.raw.data)
        inputStream.toFile(filePath)
    } finally {
        inputStream?.close()
    }

}

fun InputStream.toFile(filePath: String) {
    try {
        File(filePath).outputStream().use { this.copyTo(it) }
    } catch (ex: FileNotFoundException) {
        ex.printStackTrace()

    } catch (ex: IOException) {
        ex.printStackTrace()
    } finally {
        this.close()

    }

}

fun File.toJson(): ImagesData {
    lateinit var imagesData: ImagesData
    try {
        imagesData = Gson().fromJson<ImagesData>(InputStreamReader(this.inputStream()),
                ImagesData::class.java)
        Log.d(TAG, imagesData.toString())

    } catch (ex: FileNotFoundException) {
        ex.printStackTrace()

    } catch (ex: IOException) {
        ex.printStackTrace()
    } catch (ex: JsonSyntaxException) {
        ex.printStackTrace()
    } catch (ex: JsonIOException) {
        ex.printStackTrace()
    } finally {
        this.inputStream().close()
    }
    return imagesData
}

fun ImagesData.toFile(file: File) {
    file.writeText(Gson().toJson(this))
}


fun View.getSnackBar(message: String, length: Int = Snackbar.LENGTH_LONG) = Snackbar.make(this, message, length)

fun Context.toast(message: String, length: Int = Toast.LENGTH_LONG) = Toast.makeText(this, message, length).show()

fun View.getDrawable(@DrawableRes imageResourceId: Int) = ContextCompat.getDrawable(context, imageResourceId)


