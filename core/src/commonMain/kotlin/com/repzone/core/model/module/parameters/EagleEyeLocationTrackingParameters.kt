package com.repzone.core.model.module.parameters

import com.repzone.core.enums.YesOrNo
import com.repzone.core.model.module.base.IModuleParametersBase
import kotlinx.datetime.DayOfWeek
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class EagleEyeLocationTrackingParameters(
    override val isActive: Boolean,
    val trackStatus: Boolean,
    val trackInterval: Int,
    val trackStartTime: Instant? = null,
    val trackEndTime: Instant? = null,
    val trackDays: List<DayOfWeek> = emptyList(),
    val backgroundTracking: Boolean,
    val showRepresentativePhoneDetails: Boolean? = null

): IModuleParametersBase
