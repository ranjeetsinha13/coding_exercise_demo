package com.rs.mylivn.viewmodels

import android.arch.lifecycle.ViewModel
import com.rs.mylivn.repository.ImagesRepository
import javax.inject.Inject

class ImagesViewModel @Inject constructor(val imagesRepository: ImagesRepository) : ViewModel() {
    fun getImages() = imagesRepository.getAllImages()
    fun getImage(uuid: String) = imagesRepository.getImage(uuid)
    fun addImage(imageUrl: String) = imagesRepository.addImage(imageUrl)
    fun deleteImage(uuid: String) = imagesRepository.deleteImage(uuid)
    fun swapImages(fromPosition: Int, toPosition: Int) = imagesRepository.swapImage(fromPosition, toPosition)
}

