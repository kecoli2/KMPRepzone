package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncCustomerEmailEntity
import com.repzone.domain.model.SyncCustomerEmailModel
import com.repzone.network.dto.CustomerEmailDto
import com.repzone.network.dto.CustomerGroupDto
import kotlin.time.ExperimentalTime

class SyncCustomerEmailEntityDbMapper : MapperDto<SyncCustomerEmailEntity, SyncCustomerEmailModel, CustomerEmailDto> {
    //region Public Method
    override fun toDomain(from: SyncCustomerEmailEntity): SyncCustomerEmailModel {
        return SyncCustomerEmailModel(
            id = from.Id,
            companyName = from.CompanyName,
            customerId = from.CustomerId,
            email = from.Email,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            recordDateUtc = from.RecordDateUtc,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE,
            title = from.Title
        )
    }

    override fun fromDomain(domain: SyncCustomerEmailModel): SyncCustomerEmailEntity {
        return SyncCustomerEmailEntity(
            Id = domain.id,
            CompanyName = domain.companyName,
            CustomerId = domain.customerId,
            Email = domain.email,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state.enumToLong(),
            Title = domain.title
        )
    }

    @OptIn(ExperimentalTime::class)
    override fun fromDto(dto: CustomerEmailDto): SyncCustomerEmailEntity {
        return SyncCustomerEmailEntity(
            Id = dto.id.toLong(),
            CompanyName = dto.companyName,
            CustomerId = dto.customerId.toLong(),
            Email = dto.email,
            ModificationDateUtc = dto.modificationDateUtc?.toEpochMilliseconds(),
            Name = dto.name,
            RecordDateUtc = dto.recordDateUtc?.toEpochMilliseconds(),
            State = dto.state.toLong(),
            Title = dto.title
        )
    }
    //endregion

}
