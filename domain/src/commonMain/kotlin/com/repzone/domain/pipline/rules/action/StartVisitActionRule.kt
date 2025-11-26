package com.repzone.domain.pipline.rules.action

import com.repzone.core.model.UiText
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.model.VisitReasonInformation
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.pipline.model.pipline.PipelineContext
import com.repzone.domain.pipline.model.pipline.Rule
import com.repzone.domain.pipline.model.pipline.RuleResult
import com.repzone.domain.pipline.model.pipline.RuleType
import com.repzone.domain.pipline.rules.util.RuleId
import com.repzone.domain.repository.IVisitRepository

class StartVisitActionRule(
    override val id: RuleId = RuleId.START_VISIT,
    override val title: UiText = UiText.dynamic("Ziyaret Başlat"),
    val iVisitRepository: IVisitRepository,
    val customerItemModel: CustomerItemModel,
    val visitInfo: VisitReasonInformation? = null) :  Rule {
    //region Field
    override val type: RuleType = RuleType.ACTION
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun canExecute(context: PipelineContext): Boolean {
        val hasActiveVisitSameCustomer = context.getData<Boolean>("has_active_visit_same_customer") ?: false
        return !hasActiveVisitSameCustomer
    }

    override suspend fun execute(context: PipelineContext): RuleResult {
        try {
            val activeLocation = context.getData<GpsLocation>("active_gps_location")
            iVisitRepository.startVisit(customerItemModel, visitInfo, activeLocation!!)
            context.putData("has_visit_started", true)
            return RuleResult.Success(this)
        }catch (ex: Exception){
            return RuleResult.Failed(this, ex.message ?: "Unknown error")
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}