package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.CustomerNoteSpEntity
import com.repzone.domain.model.CustomerNoteSpModel

class CustomerNoteSpEntityDbMapper : Mapper<CustomerNoteSpEntity, CustomerNoteSpModel> {
    //region Public Method
    override fun toDomain(from: CustomerNoteSpEntity): CustomerNoteSpModel {
        return CustomerNoteSpModel(
            id = from.Id,
            beginDate = from.BeginDate,
            customerId = from.CustomerId,
            endDate = from.EndDate,
            isPrivate = from.IsPrivate,
            modificationDateUtc = from.ModificationDateUtc,
            note = from.Note,
            noteTypeId = from.NoteTypeId,
            organizationId = from.OrganizationId,
            packageId = from.PackageId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State?.toEnum<StateType>(),
            status = from.Status,
            userId = from.UserId,
            userName = from.UserName
        )
    }

    override fun fromDomain(domain: CustomerNoteSpModel): CustomerNoteSpEntity {
        return CustomerNoteSpEntity(
            Id = domain.id,
            BeginDate = domain.beginDate,
            CustomerId = domain.customerId,
            EndDate = domain.endDate,
            IsPrivate = domain.isPrivate,
            ModificationDateUtc = domain.modificationDateUtc,
            Note = domain.note,
            NoteTypeId = domain.noteTypeId,
            OrganizationId = domain.organizationId,
            PackageId = domain.packageId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state?.enumToLong(),
            Status = domain.status,
            UserId = domain.userId,
            UserName = domain.userName
        )
    }
    //endregion

}
