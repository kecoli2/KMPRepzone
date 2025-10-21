package com.repzone.core.model.module.parameters

import com.repzone.core.model.module.base.IModuleParametersBase

data class CrmOperationsParameters(
    override val isActive: Boolean,
    val canAddNewCustomer: Boolean,
    val groupCanBeChanged: Boolean,
    val afterAddingTheCustomerCanVisit: Boolean,
    val defaultCustomerGroupId: Int? = null
): IModuleParametersBase
