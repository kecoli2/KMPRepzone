package com.repzone.data.repository.imp

import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.repository.IActionMenuRepository
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.util.enums.ActionButtonType
import com.repzone.domain.util.enums.ActionMenuGroup
import com.repzone.domain.util.enums.ActionType
import com.repzone.domain.util.models.ActionButtonListItem
import com.repzone.domain.util.models.ActionMenuListItem

class ActionMenuRepositoryImpl(private val moduleParameters: IMobileModuleParameterRepository
): IActionMenuRepository {
    //region Field
    private var activeCustomer : CustomerItemModel? = null
    private var actionMenuList: List<ActionMenuListItem> = emptyList()
    private var actionButtonList: List<ActionButtonListItem> = emptyList()
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun getActionMenuList(customer: CustomerItemModel): List<ActionMenuListItem> {
        activeCustomer = customer
        prepareActionMenuItemAndButton()
        return actionMenuList
    }

    override suspend fun getActionButtonList(customer: CustomerItemModel): List<ActionButtonListItem> {
        activeCustomer = customer
        prepareActionButton()
        return actionButtonList
    }


    //endregion

    //region Private Method
    private fun prepareActionMenuItemAndButton(){
        actionMenuList = listOf(
            ActionMenuListItem(
                actionType = ActionType.SALES_ORDER,
                actionId = 21,
                title = "Satış Siparisi",
                subTitle = "5 tip",
                groupType = ActionMenuGroup.ORDER,
                order = 0
            ),

            ActionMenuListItem(
                actionType = ActionType.SALES_ORDER,
                title = "Fatura",
                actionId = 22,
                subTitle = null,
                groupType = ActionMenuGroup.ORDER,
                order = 0
            )
        )
    }

    private fun prepareActionButton(){
        actionButtonList = listOf(
            ActionButtonListItem(
                actionType = ActionButtonType.VISITING_START,
            ),
            ActionButtonListItem(
                actionType = ActionButtonType.LOCATION,
            ),
            ActionButtonListItem(
                actionType = ActionButtonType.LIST,
            ),
            ActionButtonListItem(
                actionType = ActionButtonType.FOLDER,
            ),
            ActionButtonListItem(
                actionType = ActionButtonType.REPORT,
            ),
            ActionButtonListItem(
                actionType = ActionButtonType.NOTES,
                badgeCount = 2
            ),
        )
    }
    //endregion
}