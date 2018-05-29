package com.rs.mylivn.di

import android.content.Context
import com.rs.mylivn.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Provides
    @Singleton
    fun provideContext(): Context = app
}