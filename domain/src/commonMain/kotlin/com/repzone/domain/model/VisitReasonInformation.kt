package com.repzone.domain.model

import com.repzone.core.enums.VisitType

data class VisitReasonInformation(val reason: String? = null, val reasonId: Int, val selectedVisitType: VisitType)
