@file:OptIn(ExperimentalTime::class)

package com.repzone.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.repzone.core.enums.DocumentActionType
import com.repzone.core.enums.TaskRepeatInterval
import com.repzone.core.ui.manager.theme.AppTheme
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.domain.util.models.VisitActionItem
import com.repzone.presentation.legacy.ui.visit.VisitActionList
import kotlin.time.ExperimentalTime

//region VİSİT SAMPLE PREVİEW

@Composable
fun ActivityVisit_Sample(themeManager: ThemeManager){
    val lists =  listOf(
        VisitActionItem(
            name = "Sipariş Oluştur",
            description = "Yeni sipariş kaydı oluşturun",
            documentType = DocumentActionType.ORDER,
            isMandatory = true,
            hasDone = false,
            displayOrder = 1,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Acil Sipariş",
            description = "Acil sipariş kaydı",
            documentType = DocumentActionType.ORDER,
            isMandatory = false,
            hasDone = true,
            displayOrder = 2,
            interval = TaskRepeatInterval.WEEK
        ),
        VisitActionItem(
            name = "Fatura Kes",
            description = "Müşteriye fatura düzenleyin",
            documentType = DocumentActionType.INVOICE,
            hasDone = false,
            displayOrder = 3,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Proforma Fatura",
            description = "Proforma fatura düzenleyin",
            documentType = DocumentActionType.INVOICE,
            hasDone = true,
            displayOrder = 4,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Sevkiyat Hazırla",
            description = "Sevkiyat belgesi hazırlayın",
            documentType = DocumentActionType.WAYBILL,
            hasDone = false,
            isFulfillment = true,
            displayOrder = 5,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Müşteri Formu",
            description = "Müşteri bilgi formunu doldurun",
            documentType = DocumentActionType.FORM,
            interval = TaskRepeatInterval.ONE_TIME,
            isMandatory = true,
            hasDone = false,
            displayOrder = 6
        ),
        VisitActionItem(
            name = "Memnuniyet Anketi",
            description = "Müşteri memnuniyet anketini doldurun",
            documentType = DocumentActionType.FORM,
            hasDone = false,
            displayOrder = 7,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Tahsilat Yap",
            description = "Müşteriden tahsilat yapın",
            documentType = DocumentActionType.COLLECTION,
            hasDone = false,
            displayOrder = 8,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Depo Fişi",
            description = "Depo giriş fişi oluşturun",
            documentType = DocumentActionType.WAREHOUSERECEIPT,
            hasDone = true,
            displayOrder = 9,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Diğer İşlem",
            description = "Diğer işlemleri gerçekleştirin",
            documentType = DocumentActionType.OTHER,
            hasDone = false,
            displayOrder = 10,
            interval = TaskRepeatInterval.ATVISITSTART
        )
    )
    AppTheme(themeManager) {
        VisitActionList(
            items = lists,
            onItemClick = {

            },
            themeManager = themeManager,
            modifier = Modifier
        )
    }
}

//endregion VİSİT SAMPLE PREVİEW