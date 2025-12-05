package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncCampaignCustomerImplementationsEntity
import com.repzone.domain.model.SyncCampaignCustomerImplementationsModel

class SyncCampaignCustomerImplementationsEntityDbMapper : Mapper<SyncCampaignCustomerImplementationsEntity, SyncCampaignCustomerImplementationsModel> {
    //region Public Method
    override fun toDomain(from: SyncCampaignCustomerImplementationsEntity): SyncCampaignCustomerImplementationsModel {
        return SyncCampaignCustomerImplementationsModel(
            id = from.Id,
            campaignMasterId = from.CampaignMasterId,
            count = from.Count,
            customerId = from.CustomerId,
            modificationDateUtc = from.ModificationDateUtc,
            recordDateUtc = from.RecordDateUtc,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE
        )
    }

    override fun fromDomain(domain: SyncCampaignCustomerImplementationsModel): SyncCampaignCustomerImplementationsEntity {
        return SyncCampaignCustomerImplementationsEntity(
            Id = domain.id,
            CampaignMasterId = domain.campaignMasterId,
            Count = domain.count,
            CustomerId = domain.customerId,
            ModificationDateUtc = domain.modificationDateUtc,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state.enumToLong()
        )
    }
    //endregion

}
