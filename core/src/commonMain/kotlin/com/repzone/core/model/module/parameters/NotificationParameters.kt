package com.repzone.core.model.module.parameters

import com.repzone.core.enums.StoreDurationType
import com.repzone.core.model.module.base.IModuleParametersBase

data class NotificationParameters(
    override val isActive: Boolean,
    val storeDuration: StoreDurationType): IModuleParametersBase
