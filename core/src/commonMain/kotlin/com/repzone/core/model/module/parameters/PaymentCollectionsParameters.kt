package com.repzone.core.model.module.parameters

import com.repzone.core.model.module.base.IModuleParametersBase

data class PaymentCollectionsParameters(
    override val isActive: Boolean,
    val printingAllowed: Boolean): IModuleParametersBase
