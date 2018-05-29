package com.rs.mylivn.repository

import com.rs.mylivn.entity.ImageItem
import io.reactivex.Single

interface ImagesRepository {
    fun getAllImages(): Single<List<ImageItem>>
    fun getImage(uuid: String): Single<ImageItem>
    fun addImage(imageUrl: String): Single<ImageItem>
    fun deleteImage(uuid: String)
    fun swapImage(fromPosition: Int, toPosition: Int)

}