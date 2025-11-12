package com.repzone.domain.pipline.usecase

import com.repzone.core.enums.DocumentActionType
import com.repzone.core.platform.randomUUID
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.pipline.executer.PipelineExecutor
import com.repzone.domain.pipline.model.pipline.PipelineContext
import com.repzone.domain.repository.IPipelineRepository
import kotlin.uuid.ExperimentalUuidApi

class ExecuteActionUseCase(
    private val pipelineRepository: IPipelineRepository,
    private val pipelineExecutor: PipelineExecutor
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(actionType: DocumentActionType, customerItemModel: CustomerItemModel) {
        val pipeline = pipelineRepository.getStartVisit(customerItemModel)

        val context = PipelineContext(
            actionType = actionType,
            sessionId = randomUUID()
        )

        pipelineExecutor.execute(pipeline, context)
    }
}