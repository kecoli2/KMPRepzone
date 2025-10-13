package com.repzone.core.di

import com.repzone.core.interfaces.IUserSession
import com.repzone.core.model.UserSessionImp
import org.koin.dsl.module

val CoreModule = module {
    single<IUserSession> { UserSessionImp(get()) }
}