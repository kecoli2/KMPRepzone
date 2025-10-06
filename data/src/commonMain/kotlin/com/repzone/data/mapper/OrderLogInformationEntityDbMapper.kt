package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.OrderLogInformationEntity
import com.repzone.domain.model.OrderLogInformationModel

class OrderLogInformationEntityDbMapper : Mapper<OrderLogInformationEntity, OrderLogInformationModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: OrderLogInformationEntity): OrderLogInformationModel {
        return OrderLogInformationModel(
            id = from.Id,
            closedStatus = from.ClosedStatus,
            customerId = from.CustomerId,
            customerName = from.CustomerName,
            customerOrganizationId = from.CustomerOrganizationId,
            documentMapName = from.DocumentMapName,
            documentTypeId = from.DocumentTypeId,
            documentUniqueId = from.DocumentUniqueId,
            isApproved = from.IsApproved,
            orderDate = from.OrderDate,
            repzoneCurrentState = from.RepzoneCurrentState,
            repzoneDocNumber = from.RepzoneDocNumber,
            repzoneOrderId = from.RepzoneOrderId,
            repzoneOrderStatus = from.RepzoneOrderStatus,
            restServiceTaskDoneDate = from.RestServiceTaskDoneDate,
            restServiceTaskId = from.RestServiceTaskId,
            routeAppointmentId = from.RouteAppointmentId,
            sessionId = from.SessionId,
            state = from.State,
            status = from.Status,
            totalCost = from.TotalCost,
            transferStatus = from.TransferStatus,
            waitingOnRepzone = from.WaitingOnRepzone
        )
    }

    override fun fromDomain(domain: OrderLogInformationModel): OrderLogInformationEntity {
        return OrderLogInformationEntity(
            Id = domain.id,
            ClosedStatus = domain.closedStatus,
            CustomerId = domain.customerId,
            CustomerName = domain.customerName,
            CustomerOrganizationId = domain.customerOrganizationId,
            DocumentMapName = domain.documentMapName,
            DocumentTypeId = domain.documentTypeId,
            DocumentUniqueId = domain.documentUniqueId,
            IsApproved = domain.isApproved,
            OrderDate = domain.orderDate,
            RepzoneCurrentState = domain.repzoneCurrentState,
            RepzoneDocNumber = domain.repzoneDocNumber,
            RepzoneOrderId = domain.repzoneOrderId,
            RepzoneOrderStatus = domain.repzoneOrderStatus,
            RestServiceTaskDoneDate = domain.restServiceTaskDoneDate,
            RestServiceTaskId = domain.restServiceTaskId,
            RouteAppointmentId = domain.routeAppointmentId,
            SessionId = domain.sessionId,
            State = domain.state,
            Status = domain.status,
            TotalCost = domain.totalCost,
            TransferStatus = domain.transferStatus,
            WaitingOnRepzone = domain.waitingOnRepzone
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
