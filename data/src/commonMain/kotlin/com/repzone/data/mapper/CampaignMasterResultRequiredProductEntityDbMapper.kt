package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.CampaignMasterResultRequiredProductEntity
import com.repzone.domain.model.CampaignMasterResultRequiredProductModel

class CampaignMasterResultRequiredProductEntityDbMapper : Mapper<CampaignMasterResultRequiredProductEntity, CampaignMasterResultRequiredProductModel> {
    //region Public Method
    override fun toDomain(from: CampaignMasterResultRequiredProductEntity): CampaignMasterResultRequiredProductModel {
        return CampaignMasterResultRequiredProductModel(
            id = from.Id,
            campaignMasterResultId = from.CampaignMasterResultId,
            modificationDateUtc = from.ModificationDateUtc,
            productId = from.ProductId,
            quantity = from.Quantity,
            recordDateUtc = from.RecordDateUtc,
            state = from.State?.toEnum<StateType>()
        )
    }

    override fun fromDomain(domain: CampaignMasterResultRequiredProductModel): CampaignMasterResultRequiredProductEntity {
        return CampaignMasterResultRequiredProductEntity(
            Id = domain.id,
            CampaignMasterResultId = domain.campaignMasterResultId,
            ModificationDateUtc = domain.modificationDateUtc,
            ProductId = domain.productId,
            Quantity = domain.quantity,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state?.enumToLong()
        )
    }
    //endregion

}
