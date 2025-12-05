package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncRiskDueDayEntity
import com.repzone.domain.model.SyncRiskDueDayModel

class SyncRiskDueDayEntityDbMapper : Mapper<SyncRiskDueDayEntity, SyncRiskDueDayModel> {
    //region Public Method
    override fun toDomain(from: SyncRiskDueDayEntity): SyncRiskDueDayModel {
        return SyncRiskDueDayModel(
            id = from.Id,
            customerId = from.CustomerId,
            lastInvoiceDate = from.LastInvoiceDate,
            modificationDateUtc = from.ModificationDateUtc,
            organizationId = from.OrganizationId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE
        )
    }

    override fun fromDomain(domain: SyncRiskDueDayModel): SyncRiskDueDayEntity {
        return SyncRiskDueDayEntity(
            Id = domain.id,
            CustomerId = domain.customerId,
            LastInvoiceDate = domain.lastInvoiceDate,
            ModificationDateUtc = domain.modificationDateUtc,
            OrganizationId = domain.organizationId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state.enumToLong()
        )
    }
    //endregion

}
