package com.repzone.data.mapper

import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.Mapper
import com.repzone.database.SyncPaymentPlanEntity
import com.repzone.domain.model.SyncPaymentPlanModel
import com.repzone.network.dto.SyncPaymentPlanDto
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SyncPaymentPlanEntityDbMapper : Mapper<SyncPaymentPlanEntity, SyncPaymentPlanModel> {
    //region Public Method
    override fun toDomain(from: SyncPaymentPlanEntity): SyncPaymentPlanModel {
        return SyncPaymentPlanModel(
            id = from.Id,
            code = from.Code,
            ediCode = from.EdiCode,
            ids = from.Ids,
            isDefault = from.IsDefault,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            recordDateUtc = from.RecordDateUtc,
            state = from.State,
            tenantId = from.TenantId
        )
    }

    override fun fromDomain(domain: SyncPaymentPlanModel): SyncPaymentPlanEntity {
        return SyncPaymentPlanEntity(
            Id = domain.id,
            Code = domain.code,
            EdiCode = domain.ediCode,
            Ids = domain.ids,
            IsDefault = domain.isDefault,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state,
            TenantId = domain.tenantId
        )
    }

    fun fromDto(dto: SyncPaymentPlanDto): SyncPaymentPlanEntity {
        return SyncPaymentPlanEntity(
            Id = dto.id.toLong(),
            Code = dto.code,
            EdiCode = null,
            Ids = dto.ids,
            IsDefault = dto.isDefault.toLong(),
            ModificationDateUtc = dto.modificationDateUtc?.toEpochMilliseconds(),
            Name = dto.name,
            RecordDateUtc = dto.recordDateUtc?.toEpochMilliseconds(),
            State = dto.state.toLong(),
            TenantId = dto.tenantId.toLong()
        )
    }
    //endregion

}
