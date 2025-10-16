package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncCampaignOrganizationEntity
import com.repzone.domain.model.SyncCampaignOrganizationModel

class SyncCampaignOrganizationEntityDbMapper : Mapper<SyncCampaignOrganizationEntity, SyncCampaignOrganizationModel> {
    //region Public Method
    override fun toDomain(from: SyncCampaignOrganizationEntity): SyncCampaignOrganizationModel {
        return SyncCampaignOrganizationModel(
            id = from.Id,
            campaignMasterId = from.CampaignMasterId,
            modificationDateUtc = from.ModificationDateUtc,
            organizationId = from.OrganizationId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State,
            tenantId = from.TenantId
        )
    }

    override fun fromDomain(domain: SyncCampaignOrganizationModel): SyncCampaignOrganizationEntity {
        return SyncCampaignOrganizationEntity(
            Id = domain.id,
            CampaignMasterId = domain.campaignMasterId,
            ModificationDateUtc = domain.modificationDateUtc,
            OrganizationId = domain.organizationId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state,
            TenantId = domain.tenantId
        )
    }
    //endregion

}
