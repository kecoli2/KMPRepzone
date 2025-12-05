package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncTaskEntity
import com.repzone.domain.model.SyncTaskModel

class SyncTaskEntityDbMapper : Mapper<SyncTaskEntity, SyncTaskModel> {
    //region Public Method
    override fun toDomain(from: SyncTaskEntity): SyncTaskModel {
        return SyncTaskModel(
            id = from.Id,
            beginDate = from.BeginDate,
            customers = from.Customers,
            endDate = from.EndDate,
            modificationDateUtc = from.ModificationDateUtc,
            note = from.Note,
            organizationId = from.OrganizationId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE,
            status = from.Status,
            target = from.Target,
            targetGroupId = from.TargetGroupId,
            targetTags = from.TargetTags,
            targetType = from.TargetType,
            taskSource = from.TaskSource,
            title = from.Title
        )
    }

    override fun fromDomain(domain: SyncTaskModel): SyncTaskEntity {
        return SyncTaskEntity(
            Id = domain.id,
            BeginDate = domain.beginDate,
            Customers = domain.customers,
            EndDate = domain.endDate,
            ModificationDateUtc = domain.modificationDateUtc,
            Note = domain.note,
            OrganizationId = domain.organizationId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state.enumToLong(),
            Status = domain.status,
            Target = domain.target,
            TargetGroupId = domain.targetGroupId,
            TargetTags = domain.targetTags,
            TargetType = domain.targetType,
            TaskSource = domain.taskSource,
            Title = domain.title
        )
    }
    //endregion

}
