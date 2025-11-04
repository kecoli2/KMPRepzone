package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SellerNoteViewEntity
import com.repzone.domain.model.SellerNoteModel

class SellerNoteViewEntityDbMapper : Mapper<SellerNoteViewEntity, SellerNoteModel> {
    //region Public Method
    override fun toDomain(from: SellerNoteViewEntity): SellerNoteModel {
        return SellerNoteModel(
            customerNoteId = from.CustomerNoteId,
            noteTaker = from.NoteTaker,
            beginDate = from.BeginDate,
            endDate = from.EndDate,
            status = from.Status,
            noteDescription = from.NoteDescription,
            customerCode = from.CustomerCode,
            customerName = from.CustomerName,
            noteType = from.NoteType,
            customerId = from.CustomerId
        )
    }

    override fun fromDomain(domain: SellerNoteModel): SellerNoteViewEntity {
        return SellerNoteViewEntity(
            CustomerNoteId = domain.customerNoteId,
            NoteTaker = domain.noteTaker,
            BeginDate = domain.beginDate,
            EndDate = domain.endDate,
            Status = domain.status,
            NoteDescription = domain.noteDescription,
            CustomerCode = domain.customerCode,
            CustomerName = domain.customerName,
            NoteType = domain.noteType,
            CustomerId = domain.customerId
        )
    }
    //endregion

}
