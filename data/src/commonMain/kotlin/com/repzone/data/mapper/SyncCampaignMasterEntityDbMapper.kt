package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncCampaignMasterEntity
import com.repzone.domain.model.SyncCampaignMasterModel

class SyncCampaignMasterEntityDbMapper : Mapper<SyncCampaignMasterEntity, SyncCampaignMasterModel> {
    //region Public Method
    override fun toDomain(from: SyncCampaignMasterEntity): SyncCampaignMasterModel {
        return SyncCampaignMasterModel(
            id = from.Id,
            documentTypes = from.DocumentTypes,
            end = from.End,
            groupKey = from.GroupKey,
            groupPriority = from.GroupPriority,
            implementationCalcType = from.ImplementationCalcType,
            implementationType = from.ImplementationType,
            isOptional = from.IsOptional,
            maxImplementationAmount = from.MaxImplementationAmount,
            maxImplementationCount = from.MaxImplementationCount,
            modificationDateUtc = from.ModificationDateUtc,
            organizationId = from.OrganizationId,
            recordDateUtc = from.RecordDateUtc,
            start = from.Start,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE,
            title = from.Title
        )
    }

    override fun fromDomain(domain: SyncCampaignMasterModel): SyncCampaignMasterEntity {
        return SyncCampaignMasterEntity(
            Id = domain.id,
            DocumentTypes = domain.documentTypes,
            End = domain.end,
            GroupKey = domain.groupKey,
            GroupPriority = domain.groupPriority,
            ImplementationCalcType = domain.implementationCalcType,
            ImplementationType = domain.implementationType,
            IsOptional = domain.isOptional,
            MaxImplementationAmount = domain.maxImplementationAmount,
            MaxImplementationCount = domain.maxImplementationCount,
            ModificationDateUtc = domain.modificationDateUtc,
            OrganizationId = domain.organizationId,
            RecordDateUtc = domain.recordDateUtc,
            Start = domain.start,
            State = domain.state.enumToLong(),
            Title = domain.title
        )
    }
    //endregion

}
