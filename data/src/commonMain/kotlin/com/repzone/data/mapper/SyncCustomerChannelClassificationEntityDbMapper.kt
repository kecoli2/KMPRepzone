package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerChannelClassificationEntity
import com.repzone.domain.model.SyncCustomerChannelClassificationModel

class SyncCustomerChannelClassificationEntityDbMapper : Mapper<SyncCustomerChannelClassificationEntity, SyncCustomerChannelClassificationModel> {
    //region Public Method
    override fun toDomain(from: SyncCustomerChannelClassificationEntity): SyncCustomerChannelClassificationModel {
        return SyncCustomerChannelClassificationModel(
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

    override fun fromDomain(domain: SyncCustomerChannelClassificationModel): SyncCustomerChannelClassificationEntity {
        return SyncCustomerChannelClassificationEntity(
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
