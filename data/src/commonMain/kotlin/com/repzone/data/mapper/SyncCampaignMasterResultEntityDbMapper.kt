package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncCampaignMasterResultEntity
import com.repzone.domain.model.SyncCampaignMasterResultModel

class SyncCampaignMasterResultEntityDbMapper : Mapper<SyncCampaignMasterResultEntity, SyncCampaignMasterResultModel> {
    //region Public Method
    override fun toDomain(from: SyncCampaignMasterResultEntity): SyncCampaignMasterResultModel {
        return SyncCampaignMasterResultModel(
            id = from.Id,
            campaignMasterId = from.CampaignMasterId,
            discount = from.Discount,
            discountIndex = from.DiscountIndex,
            isOrGroup = from.IsOrGroup,
            isPercent = from.IsPercent,
            modificationDateUtc = from.ModificationDateUtc,
            productBrandId = from.ProductBrandId,
            productCustomFieldId = from.ProductCustomFieldId,
            productDynamicListId = from.ProductDynamicListId,
            productGroupId = from.ProductGroupId,
            productId = from.ProductId,
            productTag = from.ProductTag,
            promotionGroup = from.PromotionGroup,
            promotionGroupIndex = from.PromotionGroupIndex,
            promotionQuantityLimit = from.PromotionQuantityLimit,
            promotionType = from.PromotionType,
            quantity = from.Quantity,
            recordDateUtc = from.RecordDateUtc,
            resultType = from.ResultType,
            state = from.State,
            title = from.Title,
            unitId = from.UnitId
        )
    }

    override fun fromDomain(domain: SyncCampaignMasterResultModel): SyncCampaignMasterResultEntity {
        return SyncCampaignMasterResultEntity(
            Id = domain.id,
            CampaignMasterId = domain.campaignMasterId,
            Discount = domain.discount,
            DiscountIndex = domain.discountIndex,
            IsOrGroup = domain.isOrGroup,
            IsPercent = domain.isPercent,
            ModificationDateUtc = domain.modificationDateUtc,
            ProductBrandId = domain.productBrandId,
            ProductCustomFieldId = domain.productCustomFieldId,
            ProductDynamicListId = domain.productDynamicListId,
            ProductGroupId = domain.productGroupId,
            ProductId = domain.productId,
            ProductTag = domain.productTag,
            PromotionGroup = domain.promotionGroup,
            PromotionGroupIndex = domain.promotionGroupIndex,
            PromotionQuantityLimit = domain.promotionQuantityLimit,
            PromotionType = domain.promotionType,
            Quantity = domain.quantity,
            RecordDateUtc = domain.recordDateUtc,
            ResultType = domain.resultType,
            State = domain.state,
            Title = domain.title,
            UnitId = domain.unitId
        )
    }
    //endregion

}
