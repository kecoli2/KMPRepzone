package com.repzone.domain.pipline.rules

import com.repzone.domain.events.base.IEventBus
import com.repzone.domain.events.base.events.ScreenEvents
import com.repzone.domain.pipline.model.Screen
import com.repzone.domain.pipline.model.pipline.PipelineContext
import com.repzone.domain.pipline.model.pipline.Rule
import com.repzone.domain.pipline.model.pipline.RuleResult
import com.repzone.domain.pipline.model.pipline.RuleType

class ScreenRule(
    override val id: String,
    override val title: String,
    val screen: Screen,
    val eventBus: IEventBus
) : Rule {

    override val type = RuleType.SCREEN

    override suspend fun execute(context: PipelineContext): RuleResult {

        val existingData = context.getData<Map<String, Any>>("screen_${screen.id}")
        if (existingData != null) {
            return RuleResult.Success(this, existingData)
        }

        eventBus.publish(
            ScreenEvents.ScreenRequired(
                ruleId = id,
                screen = screen,
                sessionId = context.sessionId
            )
        )

        return RuleResult.AwaitingScreen(
            rule = this,
            screen = screen
        )
    }
}