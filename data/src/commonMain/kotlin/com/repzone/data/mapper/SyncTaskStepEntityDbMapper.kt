package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncTaskStepEntity
import com.repzone.domain.model.SyncTaskStepModel

class SyncTaskStepEntityDbMapper : Mapper<SyncTaskStepEntity, SyncTaskStepModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncTaskStepEntity): SyncTaskStepModel {
        return SyncTaskStepModel(
            id = from.Id,
            controlType = from.ControlType,
            controlTypeDescription = from.ControlTypeDescription,
            modificationDateUtc = from.ModificationDateUtc,
            needComplteThisToGoToNextStep = from.NeedComplteThisToGoToNextStep,
            order = from.Order,
            recordDateUtc = from.RecordDateUtc,
            state = from.State,
            taskId = from.TaskId,
            title = from.Title
        )
    }

    override fun fromDomain(domain: SyncTaskStepModel): SyncTaskStepEntity {
        return SyncTaskStepEntity(
            Id = domain.id,
            ControlType = domain.controlType,
            ControlTypeDescription = domain.controlTypeDescription,
            ModificationDateUtc = domain.modificationDateUtc,
            NeedComplteThisToGoToNextStep = domain.needComplteThisToGoToNextStep,
            Order = domain.order,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state,
            TaskId = domain.taskId,
            Title = domain.title
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
