package com.rs.mylivn.di

import com.rs.mylivn.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, ImagesRepositoryModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)

}