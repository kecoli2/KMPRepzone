package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.TaskVisitLogInformationEntity
import com.repzone.domain.model.TaskVisitLogInformationModel

class TaskVisitLogInformationEntityDbMapper : Mapper<TaskVisitLogInformationEntity, TaskVisitLogInformationModel> {
    //region Public Method
    override fun toDomain(from: TaskVisitLogInformationEntity): TaskVisitLogInformationModel {
        return TaskVisitLogInformationModel(
            id = from.Id,
            customerId = from.CustomerId,
            finishDate = from.FinishDate,
            startDate = from.StartDate,
            taskId = from.TaskId,
            visitUniqueId = from.VisitUniqueId
        )
    }

    override fun fromDomain(domain: TaskVisitLogInformationModel): TaskVisitLogInformationEntity {
        return TaskVisitLogInformationEntity(
            Id = domain.id,
            CustomerId = domain.customerId,
            FinishDate = domain.finishDate,
            StartDate = domain.startDate,
            TaskId = domain.taskId,
            VisitUniqueId = domain.visitUniqueId
        )
    }
    //endregion

}
