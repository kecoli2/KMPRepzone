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
            body = from.Body,
            footer = from.Footer,
            header = from.Header,
            image = from.Image
        )
    }

    override fun fromDomain(domain: NotificationLogInformationModel): NotificationLogInformationEntity {
        return NotificationLogInformationEntity(
            Id = domain.id,
            EventSource = domain.eventSource,
            EventType = domain.eventType,
            LogDate = domain.logDate,
            Body = domain.body,
            Footer = domain.footer,
            Header = domain.header,
            Image = domain.image
        )
    }
    //endregion

}
