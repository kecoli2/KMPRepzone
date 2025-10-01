package com.repzone.mobile

import org.koin.core.context.startKoin
import com.repzone.mobile.di.IosDIModule
import com.repzone.firebase.di.FirebaseIosModule

fun initKoin() {
    startKoin {
        modules(
            IosDIModule,FirebaseIosModule
        )
    }
}
