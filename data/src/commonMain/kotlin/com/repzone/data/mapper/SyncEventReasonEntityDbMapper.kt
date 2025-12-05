package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncEventReasonEntity
import com.repzone.domain.model.SyncEventReasonModel
import com.repzone.network.dto.EventReasonDto
import kotlin.time.ExperimentalTime

class SyncEventReasonEntityDbMapper : MapperDto<SyncEventReasonEntity, SyncEventReasonModel, EventReasonDto> {
    //region Public Method
    override fun toDomain(from: SyncEventReasonEntity): SyncEventReasonModel {
        return SyncEventReasonModel(
            id = from.Id,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            reasonType = from.ReasonType,
            recordDateUtc = from.RecordDateUtc,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE,
            tags = from.Tags
        )
    }

    override fun fromDomain(domain: SyncEventReasonModel): SyncEventReasonEntity {
        return SyncEventReasonEntity(
            Id = domain.id,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            ReasonType = domain.reasonType,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state.enumToLong(),
            Tags = domain.tags
        )
    }

    @OptIn(ExperimentalTime::class)
    override fun fromDto(dto: EventReasonDto): SyncEventReasonEntity {
        return SyncEventReasonEntity(
            Id = dto.id.toLong(),
            ModificationDateUtc = dto.modificationDateUtc?.toEpochMilliseconds(),
            Name = dto.name,
            ReasonType = dto.reasonType?.toLong(),
            RecordDateUtc = dto.recordDateUtc?.toEpochMilliseconds(),
            State = dto.state.toLong(),
            Tags = dto.tags
        )
    }
    //endregion

}
