package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerSegmentClassificationEntity
import com.repzone.domain.model.SyncCustomerSegmentClassificationModel

class SyncCustomerSegmentClassificationEntityDbMapper : Mapper<SyncCustomerSegmentClassificationEntity, SyncCustomerSegmentClassificationModel> {
    //region Public Method
    override fun toDomain(from: SyncCustomerSegmentClassificationEntity): SyncCustomerSegmentClassificationModel {
        return SyncCustomerSegmentClassificationModel(
            id = from.Id,
            classificationId = from.ClassificationId,
            classificationName = from.ClassificationName,
            customerId = from.CustomerId,
            modificationDateUtc = from.ModificationDateUtc,
            organizationId = from.OrganizationId,
            recordDateUtc = from.RecordDateUtc,
            shareStatus = from.ShareStatus,
            state = from.State
        )
    }

    override fun fromDomain(domain: SyncCustomerSegmentClassificationModel): SyncCustomerSegmentClassificationEntity {
        return SyncCustomerSegmentClassificationEntity(
            Id = domain.id,
            ClassificationId = domain.classificationId,
            ClassificationName = domain.classificationName,
            CustomerId = domain.customerId,
            ModificationDateUtc = domain.modificationDateUtc,
            OrganizationId = domain.organizationId,
            RecordDateUtc = domain.recordDateUtc,
            ShareStatus = domain.shareStatus,
            State = domain.state
        )
    }
    //endregion

}
