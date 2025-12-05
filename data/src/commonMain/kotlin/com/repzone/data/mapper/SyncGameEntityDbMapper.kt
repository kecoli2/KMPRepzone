package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncGameEntity
import com.repzone.domain.model.SyncGameModel

class SyncGameEntityDbMapper : Mapper<SyncGameEntity, SyncGameModel> {
    //region Public Method
    override fun toDomain(from: SyncGameEntity): SyncGameModel {
        return SyncGameModel(
            id = from.Id,
            description = from.Description,
            icon = from.Icon,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            point = from.Point,
            priority = from.Priority,
            rankDescription = from.RankDescription,
            rankId = from.RankId,
            rankName = from.RankName,
            recordDateUtc = from.RecordDateUtc,
            stageIcon = from.StageIcon,
            stageId = from.StageId,
            stageName = from.StageName,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE,
            unSuccessIcon = from.UnSuccessIcon
        )
    }

    override fun fromDomain(domain: SyncGameModel): SyncGameEntity {
        return SyncGameEntity(
            Id = domain.id,
            Description = domain.description,
            Icon = domain.icon,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            Point = domain.point,
            Priority = domain.priority,
            RankDescription = domain.rankDescription,
            RankId = domain.rankId,
            RankName = domain.rankName,
            RecordDateUtc = domain.recordDateUtc,
            StageIcon = domain.stageIcon,
            StageId = domain.stageId,
            StageName = domain.stageName,
            State = domain.state.enumToLong(),
            UnSuccessIcon = domain.unSuccessIcon
        )
    }
    //endregion

}
