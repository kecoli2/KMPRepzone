package com.repzone.data.repository.imp

import com.repzone.core.enums.RepresentativeEventReasonType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.database.AppDatabase
import com.repzone.domain.repository.IEventReasonRepository
import com.repzone.domain.util.models.EventReasonCode

class EventReasonRepositoryImpl(private var database: AppDatabase): IEventReasonRepository {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getEventReasonList(type: RepresentativeEventReasonType): List<EventReasonCode> {
        return database.syncEventReasonEntityQueries.selectBySyncEventReasonEntityStateNotEqualAndReasonTypeEqual(4, type.enumToLong()).executeAsList().map {
            EventReasonCode(
                id = it.Id.toInt(),
                name = it.Name,
                tags = it.Tags?.split(",")?.toList()
            )
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}