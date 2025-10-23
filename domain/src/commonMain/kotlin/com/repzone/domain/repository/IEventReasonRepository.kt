package com.repzone.domain.repository

import com.repzone.core.enums.RepresentativeEventReasonType
import com.repzone.domain.util.models.EventReasonCode

interface IEventReasonRepository {
    fun getEventReasonList(type: RepresentativeEventReasonType): List<EventReasonCode>
}