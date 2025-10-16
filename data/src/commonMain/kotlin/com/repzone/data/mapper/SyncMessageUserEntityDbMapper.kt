package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncMessageUserEntity
import com.repzone.domain.model.SyncMessageUserModel

class SyncMessageUserEntityDbMapper : Mapper<SyncMessageUserEntity, SyncMessageUserModel> {
    //region Public Method
    override fun toDomain(from: SyncMessageUserEntity): SyncMessageUserModel {
        return SyncMessageUserModel(
            id = from.Id,
            cellPhone = from.CellPhone,
            email = from.Email,
            firstName = from.FirstName,
            lastName = from.LastName,
            modificationDateUtc = from.ModificationDateUtc,
            organizationId = from.OrganizationId,
            organizationName = from.OrganizationName,
            photoPath = from.PhotoPath,
            recordDateUtc = from.RecordDateUtc,
            roleId = from.RoleId,
            roleName = from.RoleName,
            state = from.State
        )
    }

    override fun fromDomain(domain: SyncMessageUserModel): SyncMessageUserEntity {
        return SyncMessageUserEntity(
            Id = domain.id,
            CellPhone = domain.cellPhone,
            Email = domain.email,
            FirstName = domain.firstName,
            LastName = domain.lastName,
            ModificationDateUtc = domain.modificationDateUtc,
            OrganizationId = domain.organizationId,
            OrganizationName = domain.organizationName,
            PhotoPath = domain.photoPath,
            RecordDateUtc = domain.recordDateUtc,
            RoleId = domain.roleId,
            RoleName = domain.roleName,
            State = domain.state
        )
    }
    //endregion

}
