package com.repzone.data.repository.di

import com.repzone.data.mapper.OrderDbMapper
import com.repzone.data.outbox.OrderOutboxService
import com.repzone.data.repository.imp.OrderRepositoryImpl
import com.repzone.data.util.Mapper
import com.repzone.database.Orders
import com.repzone.domain.model.OrderEntity
import com.repzone.domain.repository.IOrderRepository
import org.koin.dsl.module

val RepositoryModule = module {
    single<Mapper<Orders, OrderEntity>> { OrderDbMapper() }
    single<IOrderRepository> { OrderRepositoryImpl(get(), get()) } // get() -> OrderDao, Mapper
    single { OrderOutboxService(get(), get(), get()) }
}