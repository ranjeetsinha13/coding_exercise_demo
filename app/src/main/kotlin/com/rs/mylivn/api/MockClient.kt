package com.rs.mylivn.api

import android.os.Environment
import android.util.Log
import com.rs.mylivn.FILE_NAME
import com.rs.mylivn.entity.ImageItem
import com.rs.mylivn.entity.ImagesData
import com.rs.mylivn.toFile
import com.rs.mylivn.toJson
import io.reactivex.Single
import java.io.File
import java.util.*
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class MockClient : RemoteImageApi {


    val TAG: String = MockClient::class.java.name

    companion object {
        val FILE = File(Environment.getExternalStorageDirectory().path + "/" +
                Environment.DIRECTORY_DOWNLOADS + "/" + FILE_NAME)
    }

    override fun getImages(): Single<List<ImageItem>> {
        //read json file and get data
        Log.d(TAG, "calling ---get images-------")

        return Single.create { emitter ->
            emitter.onSuccess(FILE.toJson().images)
        }
    }

    override fun addImage(imageUrl: String): Single<ImageItem> {
        val uuid = UUID.randomUUID().toString()
        val imageItem = ImageItem(uuid, imageUrl)
        val images = FILE.toJson().images
        images.add(imageItem)

        // write to file
        ImagesData(images).toFile(FILE)
        return Single.create { emitter -> emitter.onSuccess(imageItem) }
    }

    override fun deleteImage(uuid: String) {
        val images = ArrayList<ImageItem>(FILE.toJson().images.filter { it.uuid != uuid })
        ImagesData(images).toFile(FILE)

    }

    override fun getImage(uuid: String): Single<ImageItem> {
        val images = ArrayList<ImageItem>(FILE.toJson().images.filter { it.uuid == uuid })
        return Single.create { images[0] }
    }

    override fun swapImages(fromPosition: Int, toPosition: Int) {
        val images = FILE.toJson().images as MutableList<ImageItem>
        Collections.swap(images, fromPosition, toPosition)
        //Write back to File
        ImagesData(images as ArrayList<ImageItem>).toFile(FILE)

    }

}