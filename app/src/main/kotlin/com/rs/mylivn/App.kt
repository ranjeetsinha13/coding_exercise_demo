package com.rs.mylivn

import android.app.Application
import com.rs.mylivn.api.MockClient
import com.rs.mylivn.di.AppComponent
import com.rs.mylivn.di.AppModule
import com.rs.mylivn.di.DaggerAppComponent
import com.rs.mylivn.di.NetworkModule


class App : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initializeDagger()
    }

    private fun initializeDagger() {
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this))
                .networkModule(NetworkModule(MockClient())).build()


    }
}