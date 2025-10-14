package com.repzone.mobile

import com.repzone.mobile.di.FirebaseIosModule
import org.koin.core.context.startKoin
import com.repzone.mobile.di.IosDIModule


fun initKoin() {
    startKoin {
        modules(
            IosDIModule,FirebaseIosModule
        )
    }
}
