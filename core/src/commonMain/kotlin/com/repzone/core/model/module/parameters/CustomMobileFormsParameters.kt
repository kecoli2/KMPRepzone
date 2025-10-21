package com.repzone.core.model.module.parameters

import com.repzone.core.enums.OnOf
import com.repzone.core.enums.OrganizationType
import com.repzone.core.model.module.base.IModuleParametersBase

data class CustomMobileFormsParameters(
    override val isActive: Boolean,
    val allowDraft: Boolean,
    val askReasonCode: OnOf,
    val allowCloudDownload: Boolean,
    val keepDraftAfterProcess: Boolean,
    val orderOrganizationSelectionOptions: OrganizationType
): IModuleParametersBase