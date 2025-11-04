package com.repzone.domain.usecase.visit

import com.repzone.core.enums.OnOf
import com.repzone.domain.common.DomainException
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.common.Result
import com.repzone.domain.util.enums.ActionButtonType
import com.repzone.domain.util.models.ActionButtonListItem
import com.repzone.domain.util.models.ActionMenuListItem

class GetVisitMenuListUseCase(
    private val iModuleParameters: IMobileModuleParameterRepository

) {
    //region Field
    private var actionMenuList: List<ActionMenuListItem> = emptyList()
    private var actionButtonList: ArrayList<ActionButtonListItem> = arrayListOf()
    //endregion

    //region Properties
    //endregion

    //region Constructor
    suspend operator fun invoke(customer: CustomerItemModel): Result<Pair<List<ActionMenuListItem>, List<ActionButtonListItem>>> {
        return try{
            prepareActionButtonList()
            prepareActionMenuItemList()
            Result.Success(Pair(actionMenuList, actionButtonList))
        }catch (ex: Exception){
            Result.Error(DomainException.UnknownException())
        }
    }
    //endregion

    //region Public Method
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private fun prepareActionButtonList(){
        actionButtonList.clear()

        if(iModuleParameters.getModule().drive){
            actionButtonList.add(
                ActionButtonListItem(
                    actionType = ActionButtonType.DRIVE,
                )
            )
        }
        if(iModuleParameters.getModule().qlikReport){
            actionButtonList.add(
                ActionButtonListItem(
                    actionType = ActionButtonType.REPORT,
                )
            )
        }

        if((iModuleParameters.getOrdersParameters()?.isActive == true && iModuleParameters.getOrdersParameters()?.allowDraft == true) &&
            (iModuleParameters.getCustomMobileFormsParameters()?.isActive == true && iModuleParameters.getCustomMobileFormsParameters()?.allowDraft == true)){
            actionButtonList.add(
                ActionButtonListItem(
                    actionType = ActionButtonType.ORDER_LOG,
                )
            )
        }

        iModuleParameters.getGeofenceRouteTrackingParameters()?.let {
           // MAP
            if (it.isActive && it.navigation == OnOf.ON) {
                actionButtonList.add(
                    ActionButtonListItem(
                        actionType = ActionButtonType.MAP,
                    )
                )
            }

            // CUSTOMER NOTE
            if(it.isActive && (it.customerNoteEntry == OnOf.ON || it.visitNoteEntry == OnOf.ON)){
                actionButtonList.add(
                    ActionButtonListItem(
                        actionType = ActionButtonType.NOTES,
                    )
                )
            }

        }
    }

    private fun prepareActionMenuItemList() {

    }
    //endregion
}