package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.TaskLogInformationEntity
import com.repzone.domain.model.TaskLogInformationModel

class TaskLogInformationEntityDbMapper : Mapper<TaskLogInformationEntity, TaskLogInformationModel> {
    //region Public Method
    override fun toDomain(from: TaskLogInformationEntity): TaskLogInformationModel {
        return TaskLogInformationModel(
            id = from.Id,
            customerId = from.CustomerId,
            date = from.Date,
            fileUrl = from.FileUrl,
            formDataUniqueId = from.FormDataUniqueId,
            formId = from.FormId,
            latitude = from.Latitude,
            longitude = from.Longitude,
            note = from.Note,
            status = from.Status,
            stepId = from.StepId,
            taskId = from.TaskId,
            uniqueId = from.UniqueId,
            visitId = from.VisitId,
            visitUniqueId = from.VisitUniqueId
        )
    }

    override fun fromDomain(domain: TaskLogInformationModel): TaskLogInformationEntity {
        return TaskLogInformationEntity(
            Id = domain.id,
            CustomerId = domain.customerId,
            Date = domain.date,
            FileUrl = domain.fileUrl,
            FormDataUniqueId = domain.formDataUniqueId,
            FormId = domain.formId,
            Latitude = domain.latitude,
            Longitude = domain.longitude,
            Note = domain.note,
            Status = domain.status,
            StepId = domain.stepId,
            TaskId = domain.taskId,
            UniqueId = domain.uniqueId,
            VisitId = domain.visitId,
            VisitUniqueId = domain.visitUniqueId
        )
    }
    //endregion

}
