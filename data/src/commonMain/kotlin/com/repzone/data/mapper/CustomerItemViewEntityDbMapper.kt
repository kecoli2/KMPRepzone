package com.repzone.data.mapper

import com.repzone.core.util.extensions.toDateOnly
import com.repzone.core.util.extensions.toInstant
import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.Mapper
import com.repzone.database.CustomerItemViewEntity
import com.repzone.domain.model.CustomerItemModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CustomerItemViewEntityDbMapper: Mapper<CustomerItemViewEntity, CustomerItemModel> {

    //region Public Method

    override fun toDomain(from: CustomerItemViewEntity): CustomerItemModel {
        return CustomerItemModel(
            customerId = from.CustomerId,
            visitId = from.VisitId,
            iconIndex = from.IconIndex,
            finishDate = from.FinishDate?.toInstant(),
            appointmentId = from.AppointmentId,
            date = from.Date?.toInstant(),
            tagRaw = from.TagRaw?.split(","),
            name = from.Name,
            customerCode = from.CustomerCode,
            customerGroupName = from.CustomerGroupName,
            address = from.Address,
            latitude = from.Latitude,
            longitude = from.Longitude,
            addressType = from.AddressType,
            imageUri = from.ImageUri,
            parentCustomerId = from.ParentCustomerId,
            endDate = from.EndDate?.toInstant(),
            customerBlocked = from.CustomerBlocked,
            sprintId = from.SprintId
        )
    }

    override fun fromDomain(domain: CustomerItemModel): CustomerItemViewEntity {
        return CustomerItemViewEntity(
            CustomerId = domain.customerId,
            VisitId = domain.visitId,
            IconIndex = domain.iconIndex,
            FinishDate = domain.finishDate?.toEpochMilliseconds(),
            AppointmentId = domain.appointmentId,
            Date = domain.date?.toEpochMilliseconds(),
            TagRaw = domain.tagRaw?.joinToString(","),
            Name = domain.name,
            CustomerCode = domain.customerCode,
            CustomerGroupName = domain.customerGroupName,
            Address = domain.address,
            Latitude = domain.latitude,
            Longitude = domain.longitude,
            AddressType = domain.addressType,
            ImageUri = domain.imageUri,
            ParentCustomerId = domain.parentCustomerId,
            EndDate = domain.endDate?.toEpochMilliseconds(),
            CustomerBlocked = domain.customerBlocked,
            SprintId = domain.sprintId,
            ShowDisplayClock = domain.showDisplayClock.toLong(),
        )

    }
    //endregion

}