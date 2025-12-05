package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncCustomerGroupEntity
import com.repzone.domain.model.SyncCustomerGroupModel
import com.repzone.network.dto.CustomerGroupDto
import kotlin.time.ExperimentalTime

class SyncCustomerGroupEntityDbMapper : MapperDto<SyncCustomerGroupEntity, SyncCustomerGroupModel, CustomerGroupDto> {
    //region Public Method
    override fun toDomain(from: SyncCustomerGroupEntity): SyncCustomerGroupModel {
        return SyncCustomerGroupModel(
            id = from.Id,
            iconIndex = from.IconIndex,
            isDefault = from.IsDefault,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            organizationId = from.OrganizationId,
            parentId = from.ParentId,
            recordDateUtc = from.RecordDateUtc,
            shared = from.Shared,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE
        )
    }

    override fun fromDomain(domain: SyncCustomerGroupModel): SyncCustomerGroupEntity {
        return SyncCustomerGroupEntity(
            Id = domain.id,
            IconIndex = domain.iconIndex,
            IsDefault = domain.isDefault,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            OrganizationId = domain.organizationId,
            ParentId = domain.parentId,
            RecordDateUtc = domain.recordDateUtc,
            Shared = domain.shared,
            State = domain.state.enumToLong()
        )
    }

    @OptIn(ExperimentalTime::class)
    override fun fromDto(dto: CustomerGroupDto): SyncCustomerGroupEntity {
        return SyncCustomerGroupEntity(
            Id = dto.id.toLong(),
            IconIndex = dto.iconIndex?.toLong(),
            IsDefault = dto.isDefault.toLong(),
            ModificationDateUtc = dto.modificationDateUtc?.toEpochMilliseconds(),
            Name = dto.name,
            OrganizationId = dto.organizationId?.toLong(),
            ParentId = dto.parentId?.toLong(),
            RecordDateUtc = dto.recordDateUtc?.toEpochMilliseconds(),
            Shared = dto.shared.toLong(),
            State = dto.state.toLong()
        )
    }
    //endregion

}
