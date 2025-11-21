package com.repzone.data.repository.imp

import com.repzone.core.enums.DocumentActionType
import com.repzone.domain.events.base.IEventBus
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.pipline.model.pipline.Pipeline
import com.repzone.domain.pipline.model.pipline.Stage
import com.repzone.domain.pipline.rules.action.EndVisitActionRule
import com.repzone.domain.pipline.rules.action.StartVisitActionRule
import com.repzone.domain.pipline.rules.check.ActiveVisitCheckRule
import com.repzone.domain.pipline.rules.decision.CustomerBlockedDecisionRule
import com.repzone.domain.pipline.rules.decision.EndVisitDecisionRule
import com.repzone.domain.repository.IPipelineRepository
import com.repzone.domain.repository.IRouteAppointmentRepository
import com.repzone.domain.repository.IVisitRepository

class PipelineRepositoryImpl(private val eventBus: IEventBus,
                             private val iVisitRepository: IVisitRepository,
                             private val iRouteAppointmentRepository: IRouteAppointmentRepository): IPipelineRepository {
    //region Field
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getPipelineForAction(actionType: DocumentActionType): Pipeline {
        TODO("Not yet implemented")
    }

    override fun getStartVisit(customerItemModel: CustomerItemModel): Pipeline {
        return startVisitPipeline(customerItemModel)
    }
    //endregion

    //region Private Method
    private fun startVisitPipeline(customerItemModel: CustomerItemModel) = Pipeline(
        id = "start_visit",
        actionType = DocumentActionType.START_VISIT,
        stages = listOf(
            Stage(
                id = "check_stage",
                name = "Kontroller",
                rules = listOf(
                    CustomerBlockedDecisionRule(
                        customerItemModel = customerItemModel,
                        eventBus = eventBus
                    ),
                    ActiveVisitCheckRule(customerItemModel = customerItemModel,
                        iVisitRepository = iVisitRepository,
                        iRouteAppointmentRepository = iRouteAppointmentRepository)
                ),
            ),
            Stage(
                id = "end_visit_stage",
                name = "Ziyaret Sonlandırma",
                rules = listOf(
                    EndVisitDecisionRule(eventBus = eventBus),
                    EndVisitActionRule(iVisitRepository = iVisitRepository)
                ),
                isConditional = true,
                condition = {
                  val hasActiveVisit =  it.getData<Boolean>("has_active_visit") ?: false


                  hasActiveVisit

                }
            ),

            Stage(
                id = "start_visit_stage",
                name = "Ziyaret Başlat",
                rules = listOf(
                    StartVisitActionRule(customerItemModel = customerItemModel, visitInfo = null, iVisitRepository = iVisitRepository)
                ),
                isConditional = true,
                condition = {
                    val hasActiveVisit = it.getData<Boolean>("has_active_visit") ?: false
                    val visitEnded = it.getData<Boolean>("visit_ended") ?: false

                    !hasActiveVisit || visitEnded
                }
            )
        )
    )

    //endregion
}