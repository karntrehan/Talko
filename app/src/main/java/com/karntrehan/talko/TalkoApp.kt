package com.karntrehan.talko

import android.app.Application
import com.karntrehan.talko.dependencies.AppModule
import com.karntrehan.talko.dependencies.CoreComponent
import com.karntrehan.talko.dependencies.DaggerCoreComponent

class TalkoApp : Application() {

    val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}