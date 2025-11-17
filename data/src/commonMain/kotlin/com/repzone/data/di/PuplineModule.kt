package com.repzone.data.di

import com.repzone.data.repository.imp.PipelineRepositoryImpl
import com.repzone.domain.pipline.executer.PipelineExecutor
import com.repzone.domain.pipline.usecase.ExecuteActionUseCase
import com.repzone.domain.repository.IPipelineRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

var PublineModule = module {
    singleOf(::PipelineRepositoryImpl) { bind<IPipelineRepository>() }
    factoryOf(::PipelineExecutor)
    factoryOf(::ExecuteActionUseCase)
}