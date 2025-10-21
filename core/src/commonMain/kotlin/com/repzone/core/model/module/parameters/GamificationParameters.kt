package com.repzone.core.model.module.parameters

import com.repzone.core.enums.OnOf
import com.repzone.core.model.module.base.IModuleParametersBase

data class GamificationParameters(
    override val isActive: Boolean,
    val slashScreen: OnOf,
    val infoScreen: OnOf
): IModuleParametersBase
