package com.repzone.data.repository.imp

import com.repzone.core.enums.DocumentActionType
import com.repzone.domain.document.base.IDocumentSession
import com.repzone.domain.events.base.IEventBus
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.pipline.model.pipline.Pipeline
import com.repzone.domain.pipline.model.pipline.Stage
import com.repzone.domain.pipline.rules.action.EndVisitActionRule
import com.repzone.domain.pipline.rules.action.PrepareDocumentActionRule
import com.repzone.domain.pipline.rules.action.StartVisitActionRule
import com.repzone.domain.pipline.rules.check.ActiveVisitCheckRule
import com.repzone.domain.pipline.rules.decision.CustomerBlockedDecisionRule
import com.repzone.domain.pipline.rules.decision.EndVisitDecisionRule
import com.repzone.domain.pipline.rules.decision.StartVisitGpsDistanceDecisionRule
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.repository.IPipelineRepository
import com.repzone.domain.repository.IRouteAppointmentRepository
import com.repzone.domain.repository.IVisitRepository
import com.repzone.domain.service.ILocationService
import com.repzone.domain.util.models.VisitActionItem

class PipelineRepositoryImpl(private val eventBus: IEventBus,
                             private val iVisitRepository: IVisitRepository,
                             private val iRouteAppointmentRepository: IRouteAppointmentRepository,
                             private val iModuleParameterRepository: IMobileModuleParameterRepository,
                             private val iLocationService: ILocationService,
                             private val iDocumentSession: IDocumentSession): IPipelineRepository {
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

    override fun getFinishVisit(customerItemModel: CustomerItemModel): Pipeline {
        return Pipeline(
            id = "finish_visit",
            actionType = DocumentActionType.END_VISIT,
            stages = baseFinishVisitStage(customerItemModel)
        )
    }

    override fun getOrders(customerItemModel: CustomerItemModel, visitActionItem: VisitActionItem): Pipeline {
        val pipeline = Pipeline(
            id = "orders",
            actionType = DocumentActionType.ORDER,
            stages = baseStartVisitStage(customerItemModel) + listOf(
                Stage(
                    id = "orders_stage",
                    name = "Siparişler",
                    rules = listOf(PrepareDocumentActionRule(
                        iDocumentSession = iDocumentSession,
                        visitActionItem = visitActionItem,
                        customerItem = customerItemModel,
                        eventBus = eventBus
                    )),
                    isConditional = true,
                    condition = {
                        val hasActiveVisitSameCustomer = it.getData<Boolean>("has_active_visit_same_customer") ?: false
                        val hasVisitStarted = it.getData<Boolean>("has_visit_started") ?: false
                        hasActiveVisitSameCustomer && hasVisitStarted
                    }
            ))
        )
            return pipeline
    }
    //endregion

    //region Private Method
    private fun baseStartVisitStage(customerItemModel: CustomerItemModel): List<Stage> {
        return listOf(
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
                    /*EndVisitGpsDistanceDecisionRule(eventBus = eventBus,
                        iModuleParameterRepository = iModuleParameterRepository,
                        iLocationService = iLocationService, customerItem = customerItemModel),*/
                    EndVisitDecisionRule(eventBus = eventBus),
                    EndVisitActionRule(iVisitRepository = iVisitRepository),
                ),
                isConditional = true,
                condition = {
                    it.getData<Boolean>("has_active_visit") ?: false
                }
            ),
            Stage(
                id = "start_visit_stage",
                name = "Ziyaret Başlat",
                rules = listOf(
                    StartVisitGpsDistanceDecisionRule(eventBus = eventBus,
                        iModuleParameterRepository = iModuleParameterRepository,
                        iLocationService = iLocationService, customerItem = customerItemModel),
                    StartVisitActionRule(customerItemModel = customerItemModel, visitInfo = null, iVisitRepository = iVisitRepository)
                ),
                isConditional = true,
                condition = {
                    val hasActiveVisit = it.getData<Boolean>("has_active_visit") ?: false
                    val visitEnded = it.getData<Boolean>("visit_ended") ?: false
                    val hasActiveVisitSameCustomer = it.getData<Boolean>("has_active_visit_same_customer") ?: false

                    (!hasActiveVisit || visitEnded) && !hasActiveVisitSameCustomer
                }
            )
        )
    }

    private fun baseFinishVisitStage(customerItemModel: CustomerItemModel): List<Stage> {
        return listOf(
            Stage(
                id = "end_visit_stage",
                name = "Ziyareti Bitir",
                rules = listOf(
                    EndVisitActionRule(iVisitRepository = iVisitRepository)
                ),
                isConditional = false
            )
        )
    }
    private fun startVisitPipeline(customerItemModel: CustomerItemModel) = Pipeline(
        id = "start_visit",
        actionType = DocumentActionType.START_VISIT,
        stages = baseStartVisitStage(customerItemModel)
    )

    //endregion
}