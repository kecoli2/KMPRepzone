package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toBoolean
import com.repzone.core.util.extensions.toEnum
import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.Mapper
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncDynamicPageReportEntity
import com.repzone.domain.model.SyncDynamicPageReportModel
import com.repzone.network.dto.DynamicPageReportDto
import kotlin.time.ExperimentalTime

class SyncDynamicPageReportEntityDbMapper :
    MapperDto<SyncDynamicPageReportEntity, SyncDynamicPageReportModel, DynamicPageReportDto> {
    //region Public Method
    override fun toDomain(from: SyncDynamicPageReportEntity): SyncDynamicPageReportModel {
        return SyncDynamicPageReportModel(
            id = from.Id,
            category = from.Category,
            code = from.Code,
            description = from.Description,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            quickAccessOrder = from.QuickAccessOrder?.toInt(),
            quickAccessShow = from.QuickAccessShow?.toBoolean() ?: false,
            recordDateUtc = from.RecordDateUtc,
            requested = from.Requested,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE
        )
    }

    override fun fromDomain(domain: SyncDynamicPageReportModel): SyncDynamicPageReportEntity {
        return SyncDynamicPageReportEntity(
            Id = domain.id,
            Category = domain.category,
            Code = domain.code,
            Description = domain.description,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            QuickAccessOrder = domain.quickAccessOrder?.toLong(),
            QuickAccessShow = domain.quickAccessShow.toLong(),
            RecordDateUtc = domain.recordDateUtc,
            Requested = domain.requested,
            State = domain.state.enumToLong()
        )
    }

    @OptIn(ExperimentalTime::class)
    override fun fromDto(dto: DynamicPageReportDto): SyncDynamicPageReportEntity {
        return SyncDynamicPageReportEntity(
            Id = dto.id.toLong(),
            Category = dto.category,
            Code = dto.code,
            Description = dto.description,
            ModificationDateUtc = dto.modificationDateUtc?.toEpochMilliseconds(),
            Name = dto.name,
            QuickAccessOrder = dto.quickAccessOrder?.toLong(),
            QuickAccessShow = dto.quickAccessShow.toLong(),
            RecordDateUtc = dto.recordDateUtc?.toEpochMilliseconds(),
            Requested = dto.requested.toLong(),
            State = dto.state.toLong()
        )
    }
    //endregion

}
