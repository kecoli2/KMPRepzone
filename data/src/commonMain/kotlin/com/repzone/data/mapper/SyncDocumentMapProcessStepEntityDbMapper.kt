package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncDocumentMapProcessStepEntity
import com.repzone.domain.model.SyncDocumentMapProcessStepModel

class SyncDocumentMapProcessStepEntityDbMapper : Mapper<SyncDocumentMapProcessStepEntity, SyncDocumentMapProcessStepModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncDocumentMapProcessStepEntity): SyncDocumentMapProcessStepModel {
        return SyncDocumentMapProcessStepModel(
            id = from.Id,
            name = from.Name,
            objectName = from.ObjectName,
            processId = from.ProcessId,
            state = from.State,
            stepOrder = from.StepOrder
        )
    }

    override fun fromDomain(domain: SyncDocumentMapProcessStepModel): SyncDocumentMapProcessStepEntity {
        return SyncDocumentMapProcessStepEntity(
            Id = domain.id,
            Name = domain.name,
            ObjectName = domain.objectName,
            ProcessId = domain.processId,
            State = domain.state,
            StepOrder = domain.stepOrder
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
