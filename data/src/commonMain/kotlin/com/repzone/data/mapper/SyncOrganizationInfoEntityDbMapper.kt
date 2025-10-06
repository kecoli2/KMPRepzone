package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncOrganizationInfoEntity
import com.repzone.domain.model.SyncOrganizationInfoModel

class SyncOrganizationInfoEntityDbMapper : Mapper<SyncOrganizationInfoEntity, SyncOrganizationInfoModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncOrganizationInfoEntity): SyncOrganizationInfoModel {
        return SyncOrganizationInfoModel(
            id = from.Id,
            city = from.City,
            country = from.Country,
            district = from.District,
            faxNumber = from.FaxNumber,
            isEOrganization = from.IsEOrganization,
            logoPath = from.LogoPath,
            name = from.Name,
            phoneNumber = from.PhoneNumber,
            postalCode = from.PostalCode,
            street1 = from.Street1,
            street2 = from.Street2,
            title = from.Title
        )
    }

    override fun fromDomain(domain: SyncOrganizationInfoModel): SyncOrganizationInfoEntity {
        return SyncOrganizationInfoEntity(
            Id = domain.id,
            City = domain.city,
            Country = domain.country,
            District = domain.district,
            FaxNumber = domain.faxNumber,
            IsEOrganization = domain.isEOrganization,
            LogoPath = domain.logoPath,
            Name = domain.name,
            PhoneNumber = domain.phoneNumber,
            PostalCode = domain.postalCode,
            Street1 = domain.street1,
            Street2 = domain.street2,
            Title = domain.title
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
