package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.NotificationLogInformationEntity
import com.repzone.domain.model.NotificationLogInformationModel

class NotificationLogInformationEntityDbMapper : Mapper<NotificationLogInformationEntity, NotificationLogInformationModel> {
    //region Public Method
    override fun toDomain(from: NotificationLogInformationEntity): NotificationLogInformationModel {
        return NotificationLogInformationModel(
            id = from.Id,
            eventSource = from.EventSource,
            eventType = from.EventType,
            logDate = from.LogDate,
            body = from.body,
            footer = from.footer,
            header = from.header_,
            image = from.image
        )
    }

    override fun fromDomain(domain: NotificationLogInformationModel): NotificationLogInformationEntity {
        return NotificationLogInformationEntity(
            Id = domain.id,
            EventSource = domain.eventSource,
            EventType = domain.eventType,
            LogDate = domain.logDate,
            body = domain.body,
            footer = domain.footer,
            header_ = domain.header,
            image = domain.image
        )
    }
    //endregion

}
