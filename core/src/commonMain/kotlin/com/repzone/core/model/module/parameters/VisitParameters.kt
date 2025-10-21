package com.repzone.core.model.module.parameters

import com.repzone.core.model.module.base.IModuleParametersBase

data class VisitParameters(
    override val isActive: Boolean,
    val synchWarehouseStockrAtVisitStart: Boolean,
    val synchReservedStockrAtVisitStart: Boolean,
    val synchTransitStockrAtVisitStart: Boolean,
    val orderFullfillment: String?, //TODO: SORULACAK
    val representativeCanSelectMultipleOrganizations: Boolean

): IModuleParametersBase
