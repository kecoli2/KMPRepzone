package com.repzone.domain.pipline.rules.action

import com.repzone.core.enums.DocumentActionType
import com.repzone.core.model.UiText
import com.repzone.domain.common.DomainException
import com.repzone.domain.document.base.IDocumentSession
import com.repzone.domain.document.model.DocumentType
import com.repzone.domain.events.base.IEventBus
import com.repzone.domain.events.base.events.DomainEvent
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.pipline.model.pipline.PipelineContext
import com.repzone.domain.pipline.model.pipline.Rule
import com.repzone.domain.pipline.model.pipline.RuleResult
import com.repzone.domain.pipline.model.pipline.RuleType
import com.repzone.domain.pipline.rules.util.RuleId
import com.repzone.domain.util.models.VisitActionItem

class PrepareDocumentActionRule(
    override val id: RuleId = RuleId.PREPARE_DOCUMENT,
    override val title: UiText = UiText.dynamic("Start Document"),
    override val type: RuleType = RuleType.ACTION,
    private val iDocumentSession: IDocumentSession,
    private val visitActionItem: VisitActionItem,
    private val customerItem: CustomerItemModel,
    private val eventBus: IEventBus
) : Rule {
    //region Field
    //endregion

    //region Public Method
    override suspend fun execute(context: PipelineContext): RuleResult {
        try {

            iDocumentSession.start(when(visitActionItem.documentType){
                DocumentActionType.ORDER -> DocumentType.ORDER
                DocumentActionType.INVOICE -> DocumentType.INVOICE
                DocumentActionType.WAYBILL -> DocumentType.WAYBILL
                else -> DocumentType.ORDER

            }, customerItem = customerItem, documentName = visitActionItem.name ?: "")

            context.putData("prepare_document", true)

            eventBus.publish(DomainEvent.OpenDocumentEvent(visitActionItem))

            return RuleResult.Success(this)

        }catch (e: Exception){
            return RuleResult.Failed(this, domainException = DomainException.UnknownException(cause = e))
        }
    }
    //endregion

    //region Protected Method
    //endregion
}