package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.FormLogInformationEntity
import com.repzone.domain.model.FormLogInformationModel

class FormLogInformationEntityDbMapper : Mapper<FormLogInformationEntity, FormLogInformationModel> {
    //region Public Method
    override fun toDomain(from: FormLogInformationEntity): FormLogInformationModel {
        return FormLogInformationModel(
            id = from.Id,
            customerId = from.CustomerId,
            customerName = from.CustomerName,
            formId = from.FormId,
            isListView = from.IsListView,
            lastOpenedPage = from.LastOpenedPage,
            recordDate = from.RecordDate,
            restServiceTaskDoneDate = from.RestServiceTaskDoneDate,
            restServiceTaskId = from.RestServiceTaskId,
            restServiceTaskObject = from.RestServiceTaskObject,
            routeAppointmentId = from.RouteAppointmentId,
            status = from.Status,
            version = from.Version,
            visitUniqueId = from.visitUniqueId
        )
    }

    override fun fromDomain(domain: FormLogInformationModel): FormLogInformationEntity {
        return FormLogInformationEntity(
            Id = domain.id,
            CustomerId = domain.customerId,
            CustomerName = domain.customerName,
            FormId = domain.formId,
            IsListView = domain.isListView,
            LastOpenedPage = domain.lastOpenedPage,
            RecordDate = domain.recordDate,
            RestServiceTaskDoneDate = domain.restServiceTaskDoneDate,
            RestServiceTaskId = domain.restServiceTaskId,
            RestServiceTaskObject = domain.restServiceTaskObject,
            RouteAppointmentId = domain.routeAppointmentId,
            Status = domain.status,
            Version = domain.version,
            visitUniqueId = domain.visitUniqueId
        )
    }
    //endregion

}
