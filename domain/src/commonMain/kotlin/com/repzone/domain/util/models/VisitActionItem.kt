package com.repzone.domain.util.models

import com.repzone.core.enums.DocumentActionType
import com.repzone.core.enums.TaskRepeatInterval
import com.repzone.domain.util.enums.TransactionDocumentGroup
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class VisitActionItem(
    val documentName: String? = null,
    val name: String? = null,
    val representTags: ArrayList<String> = arrayListOf(),
    val customerTags: ArrayList<String> = arrayListOf(),
    val organizationIds: ArrayList<String> = arrayListOf(),
    val formTags: ArrayList<String> = arrayListOf(),
    val customerCategories: ArrayList<Int> = arrayListOf(),
    val customerChannels: ArrayList<Int> = arrayListOf(),
    val customerClasses: ArrayList<Int> = arrayListOf(),
    val customerSegments: ArrayList<Int> = arrayListOf(),
    val customerGroups: ArrayList<Int> = arrayListOf(),
    val serialized: String? = null,
    val description: String? = null,
    val hasDone: Boolean = false,
    val displayOrder:Int = 0,
    val beginDate: Instant? = null,
    val endDate: Instant? = null,
    val isMandatory: Boolean = false,
    val interval: TaskRepeatInterval = TaskRepeatInterval.NONE,
    val documentType: DocumentActionType = DocumentActionType.EMPTY,
    val transactionGroupType: TransactionDocumentGroup? = null,
    val isFulfillment: Boolean = false,
    val smallIcon: String? = null,
    val documentMapId: Long? = null
){
    val shouldBeSeen get() = interval != TaskRepeatInterval.ATVISITSTART
}