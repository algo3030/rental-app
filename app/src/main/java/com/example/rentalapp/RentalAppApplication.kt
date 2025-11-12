package com.example.rentalapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class RentalAppApplication: Application(){
    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(Timber.DebugTree()) // 仮
        }
    }
}