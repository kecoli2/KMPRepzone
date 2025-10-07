package com.repzone.data.mapper


import com.repzone.data.util.MapperDto
import com.repzone.database.SyncDocumentMapProcessStepEntity
import com.repzone.domain.model.SyncDocumentMapProcessStepModel
import com.repzone.network.dto.DocumentMapProcessStepDto

class SyncDocumentMapProcessStepEntityDbMapper :
    MapperDto<SyncDocumentMapProcessStepEntity, SyncDocumentMapProcessStepModel, DocumentMapProcessStepDto> {
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

    override fun fromDto(dto: DocumentMapProcessStepDto): SyncDocumentMapProcessStepEntity {
        return SyncDocumentMapProcessStepEntity(
            Id = dto.id.toLong(),
            Name = dto.name,
            ObjectName = dto.objectName,
            ProcessId = null,
            State = dto.state.toLong(),
            StepOrder = dto.stepOrder?.toLong()
        )
    }

    fun fromDto(dto: DocumentMapProcessStepDto, processId: Int?): SyncDocumentMapProcessStepEntity {
        return SyncDocumentMapProcessStepEntity(
            Id = dto.id.toLong(),
            Name = dto.name,
            ObjectName = dto.objectName,
            ProcessId = processId?.toLong(),
            State = dto.state.toLong(),
            StepOrder = dto.stepOrder?.toLong()
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
