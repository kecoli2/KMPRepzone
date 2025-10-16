package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncCampaignMasterRuleEntity
import com.repzone.domain.model.SyncCampaignMasterRuleModel

class SyncCampaignMasterRuleEntityDbMapper : Mapper<SyncCampaignMasterRuleEntity, SyncCampaignMasterRuleModel> {
    //region Public Method
    override fun toDomain(from: SyncCampaignMasterRuleEntity): SyncCampaignMasterRuleModel {
        return SyncCampaignMasterRuleModel(
            id = from.Id,
            campaignMasterId = from.CampaignMasterId,
            discover = from.Discover,
            isOr = from.IsOr,
            modificationDateUtc = from.ModificationDateUtc,
            operator = from.Operator,
            property = from.Property,
            recordDateUtc = from.RecordDateUtc,
            state = from.State
        )
    }

    override fun fromDomain(domain: SyncCampaignMasterRuleModel): SyncCampaignMasterRuleEntity {
        return SyncCampaignMasterRuleEntity(
            Id = domain.id,
            CampaignMasterId = domain.campaignMasterId,
            Discover = domain.discover,
            IsOr = domain.isOr,
            ModificationDateUtc = domain.modificationDateUtc,
            Operator = domain.operator,
            Property = domain.property,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state
        )
    }
    //endregion

}
