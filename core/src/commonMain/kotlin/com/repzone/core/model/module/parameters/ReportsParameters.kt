package com.repzone.core.model.module.parameters

import com.repzone.core.enums.YesOrNo
import com.repzone.core.model.module.base.IModuleParametersBase

data class ReportsParameters(
    override val isActive: Boolean,
    val mobileDashBoard: Boolean,
    val mobileHub: Boolean
): IModuleParametersBase
