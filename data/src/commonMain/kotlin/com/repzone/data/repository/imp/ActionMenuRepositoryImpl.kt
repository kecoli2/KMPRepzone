package com.repzone.data.repository.imp

import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.repository.IActionMenuRepository
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.util.models.ActionMenuListItem

class ActionMenuRepositoryImpl(private val moduleParameters: IMobileModuleParameterRepository
): IActionMenuRepository {
    //region Field
    private var activeCustomer : CustomerItemModel? = null
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun getActionMenuList(customer: CustomerItemModel): List<ActionMenuListItem> {
        activeCustomer = customer
        TODO("Not yet implemented")
    }
    //endregion

    //region Private Method
    private fun prepareActionMenuItem(): List<ActionMenuListItem>{
        TODO("Not yet implemented")
    }
    //endregion
}