package com.repzone.data.di

import com.repzone.core.config.BuildConfig
import com.repzone.domain.events.base.EventBus
import com.repzone.domain.events.base.EventLoggerLogCat
import com.repzone.domain.events.base.IEventBus
import com.repzone.domain.events.base.IEventLogger
import com.repzone.domain.events.base.NoOpEventLogger
import org.koin.dsl.module

var EventBusModule = module {
    //region EVENTBUS
    single<IEventLogger> {
        if(BuildConfig.IS_DEBUG){
            EventLoggerLogCat()
        }else{
            NoOpEventLogger()
        }
    }

    single<IEventBus> {
        EventBus(logger = get())
    }
    //region EVENTBUS
}