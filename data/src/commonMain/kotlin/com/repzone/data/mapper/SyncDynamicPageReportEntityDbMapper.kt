package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncDynamicPageReportEntity
import com.repzone.domain.model.SyncDynamicPageReportModel

class SyncDynamicPageReportEntityDbMapper : Mapper<SyncDynamicPageReportEntity, SyncDynamicPageReportModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncDynamicPageReportEntity): SyncDynamicPageReportModel {
        return SyncDynamicPageReportModel(
            id = from.Id,
            category = from.Category,
            code = from.Code,
            description = from.Description,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            quickAccessOrder = from.QuickAccessOrder,
            quickAccessShow = from.QuickAccessShow,
            recordDateUtc = from.RecordDateUtc,
            requested = from.Requested,
            state = from.State
        )
    }

    override fun fromDomain(domain: SyncDynamicPageReportModel): SyncDynamicPageReportEntity {
        return SyncDynamicPageReportEntity(
            Id = domain.id,
            Category = domain.category,
            Code = domain.code,
            Description = domain.description,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            QuickAccessOrder = domain.quickAccessOrder,
            QuickAccessShow = domain.quickAccessShow,
            RecordDateUtc = domain.recordDateUtc,
            Requested = domain.requested,
            State = domain.state
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
