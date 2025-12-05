package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncTaskModelAddressEntity
import com.repzone.domain.model.SyncTaskModelAddressModel

class SyncTaskModelAddressEntityDbMapper : Mapper<SyncTaskModelAddressEntity, SyncTaskModelAddressModel> {
    //region Public Method
    override fun toDomain(from: SyncTaskModelAddressEntity): SyncTaskModelAddressModel {
        return SyncTaskModelAddressModel(
            id = from.Id,
            city = from.City,
            country = from.Country,
            district = from.District,
            latitude = from.Latitude,
            longitude = from.Longitude,
            phoneNumber = from.PhoneNumber,
            postalCode = from.PostalCode,
            relatedPerson = from.RelatedPerson,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE,
            street1 = from.Street1,
            street2 = from.Street2,
            taskStepId = from.TaskStepId
        )
    }

    override fun fromDomain(domain: SyncTaskModelAddressModel): SyncTaskModelAddressEntity {
        return SyncTaskModelAddressEntity(
            Id = domain.id,
            City = domain.city,
            Country = domain.country,
            District = domain.district,
            Latitude = domain.latitude,
            Longitude = domain.longitude,
            PhoneNumber = domain.phoneNumber,
            PostalCode = domain.postalCode,
            RelatedPerson = domain.relatedPerson,
            State = domain.state.enumToLong(),
            Street1 = domain.street1,
            Street2 = domain.street2,
            TaskStepId = domain.taskStepId
        )
    }
    //endregion

}
