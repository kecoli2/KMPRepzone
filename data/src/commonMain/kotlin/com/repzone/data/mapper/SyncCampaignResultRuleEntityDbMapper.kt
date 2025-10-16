package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncCampaignResultRuleEntity
import com.repzone.domain.model.SyncCampaignResultRuleModel

class SyncCampaignResultRuleEntityDbMapper : Mapper<SyncCampaignResultRuleEntity, SyncCampaignResultRuleModel> {
    //region Public Method
    override fun toDomain(from: SyncCampaignResultRuleEntity): SyncCampaignResultRuleModel {
        return SyncCampaignResultRuleModel(
            id = from.Id,
            andDiscover = from.AndDiscover,
            andDiscoverLimit = from.AndDiscoverLimit,
            andDiscoverUnitId = from.AndDiscoverUnitId,
            andIsEqual = from.AndIsEqual,
            andIsQuantity = from.AndIsQuantity,
            calcOperator = from.CalcOperator,
            calcSource = from.CalcSource,
            campaignMasterResultId = from.CampaignMasterResultId,
            discover = from.Discover,
            isOr = from.IsOr,
            isSingleRule = from.IsSingleRule,
            modificationDateUtc = from.ModificationDateUtc,
            operator = from.Operator,
            property = from.Property,
            recordDateUtc = from.RecordDateUtc,
            state = from.State
        )
    }

    override fun fromDomain(domain: SyncCampaignResultRuleModel): SyncCampaignResultRuleEntity {
        return SyncCampaignResultRuleEntity(
            Id = domain.id,
            AndDiscover = domain.andDiscover,
            AndDiscoverLimit = domain.andDiscoverLimit,
            AndDiscoverUnitId = domain.andDiscoverUnitId,
            AndIsEqual = domain.andIsEqual,
            AndIsQuantity = domain.andIsQuantity,
            CalcOperator = domain.calcOperator,
            CalcSource = domain.calcSource,
            CampaignMasterResultId = domain.campaignMasterResultId,
            Discover = domain.discover,
            IsOr = domain.isOr,
            IsSingleRule = domain.isSingleRule,
            ModificationDateUtc = domain.modificationDateUtc,
            Operator = domain.operator,
            Property = domain.property,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state
        )
    }
    //endregion

}
