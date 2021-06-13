package com.jmlb0003.itcv

import android.app.Application
import com.jmlb0003.itcv.di.ApplicationComponent
import com.jmlb0003.itcv.di.DaggerApplicationComponent

class CustomApplication : Application() {

    val appComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent
            .factory().create(applicationContext)
    }

}
