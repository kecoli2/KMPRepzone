package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncCampaignDynamicListOrganizationsEntity
import com.repzone.domain.model.SyncCampaignDynamicListOrganizationsModel

class SyncCampaignDynamicListOrganizationsEntityDbMapper : Mapper<SyncCampaignDynamicListOrganizationsEntity, SyncCampaignDynamicListOrganizationsModel> {
    //region Public Method
    override fun toDomain(from: SyncCampaignDynamicListOrganizationsEntity): SyncCampaignDynamicListOrganizationsModel {
        return SyncCampaignDynamicListOrganizationsModel(
            id = from.Id,
            campaignMasterId = from.CampaignMasterId,
            dynamicListId = from.DynamicListId,
            modificationDateUtc = from.ModificationDateUtc,
            organizationId = from.OrganizationId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State,
            tenantId = from.TenantId
        )
    }

    override fun fromDomain(domain: SyncCampaignDynamicListOrganizationsModel): SyncCampaignDynamicListOrganizationsEntity {
        return SyncCampaignDynamicListOrganizationsEntity(
            Id = domain.id,
            CampaignMasterId = domain.campaignMasterId,
            DynamicListId = domain.dynamicListId,
            ModificationDateUtc = domain.modificationDateUtc,
            OrganizationId = domain.organizationId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state,
            TenantId = domain.tenantId
        )
    }
    //endregion

}
