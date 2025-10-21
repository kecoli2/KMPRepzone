package com.repzone.core.model.module.parameters

import com.repzone.core.enums.CustomerGPSUpdateType
import com.repzone.core.enums.OnOf
import com.repzone.core.enums.PreviousDayVisitsType
import com.repzone.core.enums.ShowNearbyCustomersType
import com.repzone.core.enums.ViolationActionType
import com.repzone.core.enums.VisitPlanSchedulesType
import com.repzone.core.model.module.base.IModuleParametersBase

data class GeofenceRouteTrackingParameters(
    override val isActive: Boolean,
    val distance: Int,
    val violationAction : ViolationActionType,
    val visitPlanSchedules: VisitPlanSchedulesType,
    val navigation: OnOf,
    val previousDayVisits: PreviousDayVisitsType,
    val groupByParentCustomer: OnOf,
    val customerGPSUpdate: CustomerGPSUpdateType,
    val visitNoteEntry: OnOf,
    val customerNoteEntry: OnOf,
    val askVisitTypeEntryDuringVisitStart: Boolean,
    val controlGPSWrtVisitType: Boolean,
    val formMandatoryCheckWrtVisitType: Boolean,
    val showNearbyCustomers: ShowNearbyCustomersType
): IModuleParametersBase
