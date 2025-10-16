package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncFeedbackListEntity
import com.repzone.domain.model.SyncFeedbackListModel

class SyncFeedbackListEntityDbMapper : Mapper<SyncFeedbackListEntity, SyncFeedbackListModel> {
    //region Public Method
    override fun toDomain(from: SyncFeedbackListEntity): SyncFeedbackListModel {
        return SyncFeedbackListModel(
            id = from.Id,
            categoryId = from.CategoryId,
            categoryName = from.CategoryName,
            lastFeedbackDate = from.LastFeedbackDate,
            message = from.Message,
            modificationDateUtc = from.ModificationDateUtc,
            organizationId = from.OrganizationId,
            organizationName = from.OrganizationName,
            parentId = from.ParentId,
            recordDateUtc = from.RecordDateUtc,
            rootId = from.RootId,
            state = from.State,
            userId = from.UserId,
            userName = from.UserName
        )
    }

    override fun fromDomain(domain: SyncFeedbackListModel): SyncFeedbackListEntity {
        return SyncFeedbackListEntity(
            Id = domain.id,
            CategoryId = domain.categoryId,
            CategoryName = domain.categoryName,
            LastFeedbackDate = domain.lastFeedbackDate,
            Message = domain.message,
            ModificationDateUtc = domain.modificationDateUtc,
            OrganizationId = domain.organizationId,
            OrganizationName = domain.organizationName,
            ParentId = domain.parentId,
            RecordDateUtc = domain.recordDateUtc,
            RootId = domain.rootId,
            State = domain.state,
            UserId = domain.userId,
            UserName = domain.userName
        )
    }
    //endregion

}
