package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerCategoryClassificationEntity
import com.repzone.domain.model.SyncCustomerCategoryClassificationModel

class SyncCustomerCategoryClassificationEntityDbMapper : Mapper<SyncCustomerCategoryClassificationEntity, SyncCustomerCategoryClassificationModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncCustomerCategoryClassificationEntity): SyncCustomerCategoryClassificationModel {
        return SyncCustomerCategoryClassificationModel(
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

    override fun fromDomain(domain: SyncCustomerCategoryClassificationModel): SyncCustomerCategoryClassificationEntity {
        return SyncCustomerCategoryClassificationEntity(
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

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
