package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.RouteInformationViewEntity
import com.repzone.domain.model.RouteInformationViewModel

class RouteInformationViewEntityDbMapper : Mapper<RouteInformationViewEntity, RouteInformationViewModel> {
    //region Public Method
    override fun toDomain(from: RouteInformationViewEntity): RouteInformationViewModel {
        return RouteInformationViewModel(
            appointmentId = from.AppointmentId,
            start = from.Start,
            end = from.End,
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

    override fun fromDomain(domain: RouteInformationViewModel): RouteInformationViewEntity {
        return RouteInformationViewEntity(
            AppointmentId = domain.appointmentId,
            Start = domain.start,
            End = domain.end,
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
