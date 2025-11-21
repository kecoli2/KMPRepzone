package com.repzone.domain.pipline.rules.decision

import com.repzone.core.model.StringResource
import com.repzone.core.model.UiText
import com.repzone.domain.common.DomainException
import com.repzone.domain.common.ErrorCode
import com.repzone.domain.events.base.IEventBus
import com.repzone.domain.events.base.events.DecisionEvents
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.pipline.model.pipline.DecisionOption
import com.repzone.domain.pipline.model.pipline.DecisionOptionTypeEnum
import com.repzone.domain.pipline.model.pipline.PipelineContext
import com.repzone.domain.pipline.model.pipline.Rule
import com.repzone.domain.pipline.model.pipline.RuleResult
import com.repzone.domain.pipline.model.pipline.RuleType

class CustomerBlockedDecisionRule(
    override val id: String = "customer_blocked_check",
    override val title: UiText = UiText.dynamic("CustomerBlockedKontrol"),
    private val customerItemModel: CustomerItemModel,
    private val eventBus: IEventBus
) : Rule {
    //region Field
    override val type: RuleType = RuleType.DECISION
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun execute(context: PipelineContext): RuleResult {
        if(customerItemModel.customerBlocked){

            val options = listOf(
                DecisionOption(DecisionOptionTypeEnum.CONTINUE, UiText.resource(StringResource.DIALOGCONTINUE)),
                DecisionOption(DecisionOptionTypeEnum.CANCEL, UiText.resource(StringResource.BUTTONCANCEL))
            )

            eventBus.publish(DecisionEvents.DecisionRequired(
                ruleId = id,
                title = title,
                message = UiText.resource(StringResource.CUSTOMERBLOCKEDMSG),
                sessionId = context.sessionId,
                options = options,
            ))

            return RuleResult.AwaitingDecision(this, options)
        }
        return RuleResult.Success(this)
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}