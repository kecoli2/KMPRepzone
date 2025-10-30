package com.repzone.data.mapper

import com.repzone.database.SyncAddressEntity
import com.repzone.domain.model.SyncAddressModel

/**
 * Auto-generated mapper for SyncAddressEntity
 * Maps between Entity (database) and Model (domain)
 */
object SyncAddressEntityDbMapper {

    /**
     * Convert Entity to Model
     */
    fun SyncAddressEntity.toModel(): SyncAddressModel {
        return SyncAddressModel(
            Id = this.Id,
            AddressName = this.AddressName,
            AddressType = this.AddressType,
            City = this.City,
            Contact = this.Contact,
            Country = this.Country,
            CustomerId = this.CustomerId,
            District = this.District,
            FaxNumber = this.FaxNumber,
            Latitude = this.Latitude,
            Longitude = this.Longitude,
            PhoneNumber = this.PhoneNumber,
            State = this.State,
            Street = this.Street,
            Street2 = this.Street2
        )
    }

    /**
     * Convert Model to Entity
     */
    fun SyncAddressModel.toEntity(): SyncAddressEntity {
        return SyncAddressEntity(
            Id = this.Id,
            AddressName = this.AddressName,
            AddressType = this.AddressType,
            City = this.City,
            Contact = this.Contact,
            Country = this.Country,
            CustomerId = this.CustomerId,
            District = this.District,
            FaxNumber = this.FaxNumber,
            Latitude = this.Latitude,
            Longitude = this.Longitude,
            PhoneNumber = this.PhoneNumber,
            State = this.State,
            Street = this.Street,
            Street2 = this.Street2
        )
    }

    /**
     * Convert List of Entities to List of Models
     */
    fun List<SyncAddressEntity>.toModelList(): List<SyncAddressModel> {
        return map { it.toModel() }
    }

    /**
     * Convert List of Models to List of Entities
     */
    fun List<SyncAddressModel>.toEntityList(): List<SyncAddressEntity> {
        return map { it.toEntity() }
    }
}
