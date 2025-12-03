package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncRepresentativeProductDistributionEntity
import com.repzone.domain.model.SyncRepresentativeProductDistributionModel
import com.repzone.network.dto.SyncRepresentativeProductDistributionDto
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SyncRepresentativeProductDistributionEntityDbMapper : Mapper<SyncRepresentativeProductDistributionEntity, SyncRepresentativeProductDistributionModel> {
    //region Public Method
    override fun toDomain(from: SyncRepresentativeProductDistributionEntity): SyncRepresentativeProductDistributionModel {
        return SyncRepresentativeProductDistributionModel(
            id = from.Id,
            groupId = from.GroupId,
            groupSalesDistributionListId = from.GroupSalesDistributionListId,
            groupSalesReturnDistributionListId = from.GroupSalesReturnDistributionListId,
            modificationDateUtc = from.ModificationDateUtc,
            recordDateUtc = from.RecordDateUtc,
            salesDistributionListId = from.SalesDistributionListId,
            salesReturnDistributionListId = from.SalesReturnDistributionListId,
            state = from.State
        )
    }

    override fun fromDomain(domain: SyncRepresentativeProductDistributionModel): SyncRepresentativeProductDistributionEntity {
        return SyncRepresentativeProductDistributionEntity(
            Id = domain.id,
            GroupId = domain.groupId,
            GroupSalesDistributionListId = domain.groupSalesDistributionListId,
            GroupSalesReturnDistributionListId = domain.groupSalesReturnDistributionListId,
            ModificationDateUtc = domain.modificationDateUtc,
            RecordDateUtc = domain.recordDateUtc,
            SalesDistributionListId = domain.salesDistributionListId,
            SalesReturnDistributionListId = domain.salesReturnDistributionListId,
            State = domain.state
        )
    }

    fun fromDto(dto:SyncRepresentativeProductDistributionDto):SyncRepresentativeProductDistributionEntity {
        return SyncRepresentativeProductDistributionEntity(
            Id = dto.id.toLong(),
            GroupId = dto.groupId.toLong(),
            GroupSalesDistributionListId = dto.groupSalesDistributionListId.toLong(),
            GroupSalesReturnDistributionListId = dto.groupSalesReturnDistributionListId.toLong(),
            ModificationDateUtc = dto.modificationDateUtc?.toEpochMilliseconds(),
            RecordDateUtc = dto.recordDateUtc?.toEpochMilliseconds(),
            SalesDistributionListId = dto.salesDistributionListId.toLong(),
            SalesReturnDistributionListId = dto.salesReturnDistributionListId.toLong(),
            State = dto.state.toLong()
        )

    }
    //endregion

}
