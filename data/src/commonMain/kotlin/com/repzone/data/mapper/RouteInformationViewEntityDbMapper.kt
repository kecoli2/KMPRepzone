package com.repzone.data.mapper

import com.repzone.core.util.extensions.toInstant
import com.repzone.data.util.Mapper
import com.repzone.database.RouteInformationViewEntity
import com.repzone.domain.model.RouteInformationModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class RouteInformationViewEntityDbMapper : Mapper<RouteInformationViewEntity, RouteInformationModel> {
    //region Public Method
    override fun toDomain(from: RouteInformationViewEntity): RouteInformationModel {
        return RouteInformationModel(
            appointmentId = from.AppointmentId,
            start = from.Start?.toInstant(),
            end = from.End?.toInstant(),
            customerId = from.CustomerId,
            description = from.Description,
            customerName = from.CustomerName,
            photoUrl = from.PhotoUrl,
            code = from.Code,
            customerGroupName = from.CustomerGroupName,
            customerOrganizationId = from.CustomerOrganizationId,
            customerTagStr = from.CustomerTagStr,
            customerRisk = from.CustomerRisk,
            customerBalance = from.CustomerBalance,
            isECustomer = from.IsECustomer,
            blocked = from.Blocked,
            customerGroupId = from.CustomerGroupId,
            addressId = from.AddressId,
            latitude = from.Latitude,
            longitude = from.Longitude,
            street = from.Street,
            city = from.City,
            district = from.District
        )
    }

    override fun fromDomain(domain: RouteInformationModel): RouteInformationViewEntity {
        return RouteInformationViewEntity(
            AppointmentId = domain.appointmentId,
            Start = domain.start?.toEpochMilliseconds(),
            End = domain.end?.toEpochMilliseconds(),
            CustomerId = domain.customerId,
            Description = domain.description,
            CustomerName = domain.customerName,
            PhotoUrl = domain.photoUrl,
            Code = domain.code,
            CustomerGroupName = domain.customerGroupName,
            CustomerOrganizationId = domain.customerOrganizationId,
            CustomerTagStr = domain.customerTagStr,
            CustomerRisk = domain.customerRisk,
            CustomerBalance = domain.customerBalance,
            IsECustomer = domain.isECustomer,
            Blocked = domain.blocked,
            CustomerGroupId = domain.customerGroupId,
            AddressId = domain.addressId,
            Latitude = domain.latitude,
            Longitude = domain.longitude,
            Street = domain.street,
            City = domain.city,
            District = domain.district
        )
    }
    //endregion

}
