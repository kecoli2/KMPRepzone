package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncAddressEntity
import com.repzone.domain.model.SyncAddressModel

class SyncAddressEntityDbMapper : Mapper<SyncAddressEntity, SyncAddressModel> {
    //region Public Method
    override fun toDomain(from: SyncAddressEntity): SyncAddressModel {
        return SyncAddressModel(
            id = from.Id,
            addressName = from.AddressName,
            addressType = from.AddressType,
            city = from.City,
            contact = from.Contact,
            country = from.Country,
            customerId = from.CustomerId,
            district = from.District,
            faxNumber = from.FaxNumber,
            latitude = from.Latitude,
            longitude = from.Longitude,
            phoneNumber = from.PhoneNumber,
            state = from.State,
            street = from.Street,
            street2 = from.Street2
        )
    }

    override fun fromDomain(domain: SyncAddressModel): SyncAddressEntity {
        return SyncAddressEntity(
            Id = domain.id,
            AddressName = domain.addressName,
            AddressType = domain.addressType,
            City = domain.city,
            Contact = domain.contact,
            Country = domain.country,
            CustomerId = domain.customerId,
            District = domain.district,
            FaxNumber = domain.faxNumber,
            Latitude = domain.latitude,
            Longitude = domain.longitude,
            PhoneNumber = domain.phoneNumber,
            State = domain.state,
            Street = domain.street,
            Street2 = domain.street2
        )
    }
    //endregion

}
