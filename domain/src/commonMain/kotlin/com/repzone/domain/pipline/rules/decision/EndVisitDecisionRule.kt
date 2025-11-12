package com.repzone.domain.pipline.rules.decision

import com.repzone.domain.events.base.IEventBus
import com.repzone.domain.events.base.events.DecisionEvents
import com.repzone.domain.pipline.model.pipline.DecisionOption
import com.repzone.domain.pipline.model.pipline.PipelineContext
import com.repzone.domain.pipline.model.pipline.Rule
import com.repzone.domain.pipline.model.pipline.RuleResult
import com.repzone.domain.pipline.model.pipline.RuleType

class EndVisitDecisionRule(
    override val id: String = "end_visit_decision",
    override val title: String = "Ziyaret Sonlandır",
    private val eventBus: IEventBus
) : Rule {
    //region Field
    override val type: RuleType = RuleType.DECISION
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun execute(context: PipelineContext): RuleResult {
        val hasActiveVisit = context.getData<Boolean>("has_active_visit") ?: false

        if (!hasActiveVisit) {
            return RuleResult.Success(this)
        }
        val activeVisitId = context.getData<Long>("active_visit_id") ?: 0L
        val customerName = context.getData<String>("active_customer_name")

        val options = listOf(
            DecisionOption("yes", "Evet, Sonlandır"),
            DecisionOption("no", "Hayır")
        )

        eventBus.publish(
            DecisionEvents.DecisionRequired(
                ruleId = id,
                title = title,
                message = "Aktif ziyaret var. Sonlandıralım mı?",
                options = options,
                sessionId = context.sessionId
            )
        )

        return RuleResult.AwaitingDecision(
            rule = this,
            options = options
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}