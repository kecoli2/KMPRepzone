package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.CustomerCampaignImplementationLogInformationEntity
import com.repzone.domain.model.CustomerCampaignImplementationLogInformationModel

class CustomerCampaignImplementationLogInformationEntityDbMapper : Mapper<CustomerCampaignImplementationLogInformationEntity, CustomerCampaignImplementationLogInformationModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: CustomerCampaignImplementationLogInformationEntity): CustomerCampaignImplementationLogInformationModel {
        return CustomerCampaignImplementationLogInformationModel(
            id = from.Id,
            campaignMasterId = from.CampaignMasterId,
            count = from.Count,
            customerId = from.CustomerId
        )
    }

    override fun fromDomain(domain: CustomerCampaignImplementationLogInformationModel): CustomerCampaignImplementationLogInformationEntity {
        return CustomerCampaignImplementationLogInformationEntity(
            Id = domain.id,
            CampaignMasterId = domain.campaignMasterId,
            Count = domain.count,
            CustomerId = domain.customerId
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
