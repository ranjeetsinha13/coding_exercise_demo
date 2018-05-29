package com.rs.mylivn.repository

import com.rs.mylivn.api.RemoteImageApi
import com.rs.mylivn.entity.ImageItem
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepositoryImpl @Inject constructor(val imagesApi: RemoteImageApi) : ImagesRepository {
    override fun getAllImages(): Single<List<ImageItem>> {
        return imagesApi.getImages()
    }

    override fun getImage(uuid: String): Single<ImageItem> {
        return imagesApi.getImage(uuid)
    }

    override fun addImage(imageUrl: String): Single<ImageItem> {
        return imagesApi.addImage(imageUrl)
    }

    override fun deleteImage(uuid: String) {
        return imagesApi.deleteImage(uuid)
    }

    override fun swapImage(fromPosition: Int, toPosition: Int) {
        return imagesApi.swapImages(fromPosition, toPosition)

    }

}