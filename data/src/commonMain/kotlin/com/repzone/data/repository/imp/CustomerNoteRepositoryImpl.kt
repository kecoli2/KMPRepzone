package com.repzone.data.repository.imp

import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.domain.repository.ICustomerNoteRepository

class CustomerNoteRepositoryImpl(private val iDatabaseManager: IDatabaseManager): ICustomerNoteRepository {

    //region Public Method
    override suspend fun getCustomerNoteCount(selectedCustomerId: Int): Int {


        var query = "SELECT " +
                "CN.Id AS CustomerNoteId, " +
                "CN.UserName AS NoteTaker, " +
                "CN.BeginDate, " +
                "CN.EndDate, " +
                "CN.Status, " +
                "CN.Note AS NoteDescription, " +
                "C.Code AS CustomerCode, " +
                "C.Name AS CustomerName, " +
                "ER.Name AS NoteType, " +
                "CN.CustomerId CustomerId"
                "FROM CustomerNoteSpModel CN " +
                "INNER JOIN MobileSyncCustomerModel C ON CN.CustomerId = C.Id " +
                "INNER JOIN MobileSyncEventReasonModel ER ON CN.NoteTypeId = ER.Id "
        return 1
    }
    //endregion

}