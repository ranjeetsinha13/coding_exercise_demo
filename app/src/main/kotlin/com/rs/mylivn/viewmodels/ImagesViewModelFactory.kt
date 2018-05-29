package com.rs.mylivn.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.rs.mylivn.repository.ImagesRepository
import javax.inject.Inject

class ImagesViewModelFactory @Inject constructor(val imagesRepository: ImagesRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImagesViewModel::class.java)) {
            return ImagesViewModel(imagesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}