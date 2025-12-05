package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerCategoryClassificationEntity
import com.repzone.domain.model.SyncCustomerCategoryClassificationModel

class SyncCustomerCategoryClassificationEntityDbMapper : Mapper<SyncCustomerCategoryClassificationEntity, SyncCustomerCategoryClassificationModel> {
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
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE
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
            State = domain.state.enumToLong()
        )
    }
    //endregion

}
