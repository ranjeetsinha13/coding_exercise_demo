package com.rs.mylivn.di

import com.rs.mylivn.repository.ImageRepositoryImpl
import com.rs.mylivn.repository.ImagesRepository
import dagger.Binds
import dagger.Module

@Module
abstract class ImagesRepositoryModule {
    @Binds
    abstract fun bindsImagesRepository(imagesRepositoryImpl: ImageRepositoryImpl): ImagesRepository
}