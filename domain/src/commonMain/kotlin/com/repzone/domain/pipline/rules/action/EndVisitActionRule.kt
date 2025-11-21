package com.repzone.domain.pipline.rules.action

import com.repzone.core.model.UiText
import com.repzone.domain.pipline.model.pipline.DecisionOptionTypeEnum
import com.repzone.domain.pipline.model.pipline.PipelineContext
import com.repzone.domain.pipline.model.pipline.Rule
import com.repzone.domain.pipline.model.pipline.RuleResult
import com.repzone.domain.pipline.model.pipline.RuleType
import com.repzone.domain.pipline.rules.util.RuleId
import com.repzone.domain.repository.IVisitRepository

/**
 * Context E eklenen parametreler
 * visit_ended -> true
 * has_active_visit -> false
 */
class EndVisitActionRule(
    override val id: RuleId = RuleId.END_VISIT,
    override val title: UiText = UiText.dynamic("Ziyaret Sonlandır"),
    val iVisitRepository: IVisitRepository
) : Rule {
    //region Field
    override val type: RuleType = RuleType.ACTION
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun canExecute(context: PipelineContext): Boolean {
        val decision = context.getData<DecisionOptionTypeEnum>("decision_${RuleId.END_VISIT_DECISION}")
        return decision == DecisionOptionTypeEnum.YES
    }

    override suspend fun execute(context: PipelineContext): RuleResult {
        try {
            context.putData("visit_ended", true)
            context.putData("has_active_visit", false)
            iVisitRepository.complateVisit()
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