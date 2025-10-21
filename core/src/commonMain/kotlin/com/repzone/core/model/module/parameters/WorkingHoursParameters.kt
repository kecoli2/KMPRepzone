package com.repzone.core.model.module.parameters

import com.repzone.core.model.module.base.IModuleParametersBase

data class WorkingHoursParameters(
    override val isActive: Boolean,
    val howManyHoursAfterTheFirstVisit: Int = 0,
    val emailList: List<String> = emptyList()
): IModuleParametersBase
