package com.repzone.domain.pipline.rules.check

import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.pipline.model.pipline.PipelineContext
import com.repzone.domain.pipline.model.pipline.Rule
import com.repzone.domain.pipline.model.pipline.RuleResult
import com.repzone.domain.pipline.model.pipline.RuleType
import com.repzone.domain.repository.IRouteAppointmentRepository
import com.repzone.domain.repository.IVisitRepository

class ActiveVisitCheckRule(
    override val id: String = "active_visit_check",
    override val title: String = "Active Visit Check",
    private val customerItemModel: CustomerItemModel,
    private val iVisitRepository: IVisitRepository,
    private val iRouteAppointmentRepository: IRouteAppointmentRepository
) : Rule {
    //region Field
    override val type = RuleType.CHECK
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun execute(context: PipelineContext): RuleResult {
        val activeVisit = iVisitRepository.getActiveVisit()
        if(activeVisit == null){
            return RuleResult.Success(this)
        }
        val activeAppointment = iRouteAppointmentRepository.getRouteInformation(activeVisit.appointmentId)
        context.putData("has_active_visit", true)
        context.putData("active_visit_id", activeVisit.visitId)
        context.putData("active_customer_name", activeAppointment?.customerName ?: "")

        return RuleResult.Success(this)
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}