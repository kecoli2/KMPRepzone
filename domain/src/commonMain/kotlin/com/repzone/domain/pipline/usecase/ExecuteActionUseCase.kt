package com.repzone.domain.pipline.usecase

import com.repzone.core.enums.DocumentActionType
import com.repzone.core.platform.randomUUID
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.pipline.executer.PipelineExecutor
import com.repzone.domain.pipline.model.pipline.PipelineContext
import com.repzone.domain.repository.IPipelineRepository
import com.repzone.domain.util.models.VisitActionItem
import kotlin.uuid.ExperimentalUuidApi

class ExecuteActionUseCase(
    private val pipelineRepository: IPipelineRepository,
    private val pipelineExecutor: PipelineExecutor
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(actionType: DocumentActionType, customerItemModel: CustomerItemModel) {
        val pipeline = when(actionType){
            DocumentActionType.START_VISIT -> {
                pipelineRepository.getStartVisit(customerItemModel)
            }
            DocumentActionType.END_VISIT -> {
                pipelineRepository.getFinishVisit(customerItemModel)
            }
            else -> {
                null
            }
        }

        if (pipeline == null)
            return

        val context = PipelineContext(
            actionType = actionType,
            sessionId = randomUUID()
        )

        pipelineExecutor.execute(pipeline, context)
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(visitActionItem: VisitActionItem, customerItemModel: CustomerItemModel){
        val pipeline = when(visitActionItem.documentType){
            DocumentActionType.ORDER -> {
                pipelineRepository.getOrders(visitActionItem = visitActionItem, customerItemModel = customerItemModel)
            }
            else -> {
                null
            }
        }

        if (pipeline == null)
            return

        val context = PipelineContext(
            actionType = visitActionItem.documentType,
            sessionId = randomUUID()
        )

        pipelineExecutor.execute(pipeline, context)
    }
}