package com.repzone.domain.pipline.executer

import com.repzone.domain.events.base.IEventBus
import com.repzone.domain.events.base.events.DecisionEvents
import com.repzone.domain.events.base.events.PipelineEvents
import com.repzone.domain.events.base.events.ScreenEvents
import com.repzone.domain.pipline.model.pipline.DecisionOptionTypeEnum
import com.repzone.domain.pipline.model.pipline.Pipeline
import com.repzone.domain.pipline.model.pipline.PipelineContext
import com.repzone.domain.pipline.model.pipline.Rule
import com.repzone.domain.pipline.model.pipline.RuleResult
import com.repzone.domain.pipline.model.pipline.Stage
import com.repzone.domain.pipline.rules.util.RuleId
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.cancellation.CancellationException

class PipelineExecutor(private val eventBus: IEventBus) {
    //region Field
    private var activeSessionId: String? = null
    private val waitingRules = mutableMapOf<RuleId, WaitingState>()
    private var eventListenerJob: Job? = null
    //endregion

    //region Properties
    //endregion

    //region Public Method
    suspend fun execute(pipeline: Pipeline, context: PipelineContext) = coroutineScope {
        try {
            activeSessionId = context.sessionId

            startEventListener(this)

            for (stage in pipeline.stages) {

                if (!stage.shouldExecute(context)) continue

                for (rule in stage.rules) {

                    if (!rule.canExecute(context)) {
                        continue
                    }

                    var shouldRetry = true

                    while (shouldRetry){
                        context.remove("retry_${rule.id}")
                        val result = rule.execute(context)

                        when (result) {
                            is RuleResult.Success -> {
                                result.data?.forEach { (k, v) ->
                                    context.putData(k, v)
                                }
                                shouldRetry = false
                            }

                            is RuleResult.AwaitingScreen -> {
                                waitingRules[rule.id] = WaitingState(
                                    rule = rule,
                                    stage = stage,
                                    context = context
                                )

                                suspendCancellableCoroutine { continuation ->
                                    waitingRules[rule.id]?.continuation = continuation
                                }
                                shouldRetry = context.getData<Boolean>("retry_${rule.id}") ?: false
                            }

                            is RuleResult.AwaitingDecision -> {
                                waitingRules[rule.id] = WaitingState(
                                    rule = rule,
                                    stage = stage,
                                    context = context
                                )

                                suspendCancellableCoroutine<Unit> { continuation ->
                                    waitingRules[rule.id]?.continuation = continuation
                                }
                                shouldRetry = context.getData<Boolean>("retry_${rule.id}") ?: false
                            }

                            is RuleResult.Failed -> {
                                throw Exception(result.message)
                            }
                        }
                    }
                }
            }

            eventBus.publish(
                PipelineEvents.PipelineCompleted(
                    actionType = context.actionType,
                    context = context,
                    sessionId = context.sessionId
                )
            )

        } catch (e: CancellationException) {
            throw e

        } catch (e: Exception) {
            eventBus.publish(
                PipelineEvents.PipelineFailed(
                    actionType = context.actionType,
                    reason = e.message ?: "Unknown error",
                    sessionId = context.sessionId
                )
            )

        } finally {
            stopEventListener()
            activeSessionId = null
            waitingRules.clear()
        }
    }
    //endregion

    //region Private Method
    private fun startEventListener(scope: CoroutineScope) {
        eventListenerJob = scope.launch {
            eventBus.events
                .filter { isEventForThisSession(it) }
                .collect { event ->
                    when (event) {
                        is ScreenEvents.ScreenCompleted -> handleScreenCompleted(event)
                        is ScreenEvents.ScreenCancelled -> handleScreenCancelled(event)
                        is DecisionEvents.DecisionMade -> handleDecisionMade(event)
                        else -> {}
                    }
                }
        }
    }

    private fun stopEventListener() {
        eventListenerJob?.cancel()
        eventListenerJob = null
    }

    private fun isEventForThisSession(event: Any): Boolean {
        val sessionId = when (event) {
            is ScreenEvents.ScreenCompleted -> event.sessionId
            is ScreenEvents.ScreenCancelled -> event.sessionId
            is DecisionEvents.DecisionMade -> event.sessionId
            else -> null
        }
        return sessionId == activeSessionId
    }

    private fun handleScreenCompleted(event: ScreenEvents.ScreenCompleted) {
        val state = waitingRules[event.ruleId] ?: return

        state.context.putData("screen_${event.screenId}", event.screenData)

        state.continuation?.resume(Unit){
            cause, _, _ -> null
        }
        waitingRules.remove(event.ruleId)
    }

    private fun handleScreenCancelled(event: ScreenEvents.ScreenCancelled) {
        val state = waitingRules[event.ruleId] ?: return

        state.continuation?.cancel(CancellationException("Screen cancelled"))
        waitingRules.remove(event.ruleId)
    }

    private fun handleDecisionMade(event: DecisionEvents.DecisionMade) {
        val state = waitingRules[event.ruleId] ?: return

        when (event.selectedOption.id) {
            DecisionOptionTypeEnum.CANCEL -> {
                state.continuation?.cancel(
                    CancellationException("User cancelled: ${event.selectedOption.label}")
                )
                waitingRules.remove(event.ruleId)
                return
            }

            DecisionOptionTypeEnum.RETRY -> {
                state.context.putData("retry_${event.ruleId}", true)

                state.continuation?.resume(Unit) { cause, _, _ -> null }
                waitingRules.remove(event.ruleId)
                return
            }

            else -> {
                state.context.putData("decision_${event.ruleId}", event.selectedOption.id)
                state.continuation?.resume(Unit){ cause, _, _ -> null }
                waitingRules.remove(event.ruleId)
            }
        }
    }
    //endregion
}

private data class WaitingState(
    val rule: Rule,
    val stage: Stage,
    val context: PipelineContext,
    var continuation: CancellableContinuation<Unit>? = null
)