package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncAppListEntity
import com.repzone.domain.model.SyncAppListModel

class SyncAppListEntityDbMapper : Mapper<SyncAppListEntity, SyncAppListModel> {
    //region Public Method
    override fun toDomain(from: SyncAppListEntity): SyncAppListModel {
        return SyncAppListModel(
            id = from.Id,
            applicationId = from.ApplicationId,
            clientKey = from.ClientKey,
            clientSecretKey = from.ClientSecretKey,
            code = from.Code,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            organizationId = from.OrganizationId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE,
            tenantId = from.TenantId,
            url = from.Url
        )
    }

    override fun fromDomain(domain: SyncAppListModel): SyncAppListEntity {
        return SyncAppListEntity(
            Id = domain.id,
            ApplicationId = domain.applicationId,
            ClientKey = domain.clientKey,
            ClientSecretKey = domain.clientSecretKey,
            Code = domain.code,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            OrganizationId = domain.organizationId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state.enumToLong(),
            TenantId = domain.tenantId,
            Url = domain.url
        )
    }
    //endregion

}
