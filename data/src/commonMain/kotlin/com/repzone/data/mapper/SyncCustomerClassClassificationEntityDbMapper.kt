package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerClassClassificationEntity
import com.repzone.domain.model.SyncCustomerClassClassificationModel

class SyncCustomerClassClassificationEntityDbMapper : Mapper<SyncCustomerClassClassificationEntity, SyncCustomerClassClassificationModel> {
    //region Public Method
    override fun toDomain(from: SyncCustomerClassClassificationEntity): SyncCustomerClassClassificationModel {
        return SyncCustomerClassClassificationModel(
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

    override fun fromDomain(domain: SyncCustomerClassClassificationModel): SyncCustomerClassClassificationEntity {
        return SyncCustomerClassClassificationEntity(
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
