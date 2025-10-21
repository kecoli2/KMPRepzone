package com.repzone.core.model.module.parameters

import com.repzone.core.enums.LeaveDurationLimitType
import com.repzone.core.model.module.base.IModuleParametersBase

data class AttendanceTrackingParameters(
    override val isActive: Boolean,
    val multiLogin: Boolean,
    val leaveManagement: Boolean,
    val leaveDurationLimit: LeaveDurationLimitType,
    val returnDateLimit: LeaveDurationLimitType,
    val permissionDescription: Boolean,
    val permissionPhoto: Boolean,
    val askLeaveStatusAtStartup: Boolean,
    val showIconAtHeader: Boolean,
    val backwardLeaveDurationLimit: Int

): IModuleParametersBase
