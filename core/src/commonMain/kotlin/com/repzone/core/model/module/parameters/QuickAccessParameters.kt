package com.repzone.core.model.module.parameters

import com.repzone.core.model.module.base.IModuleParametersBase

data class QuickAccessParameters(
    override val isActive: Boolean,
    val quickAccessForm: Int? = null
): IModuleParametersBase
