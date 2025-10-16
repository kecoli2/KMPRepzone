package com.repzone.data.mapper

import com.repzone.core.util.extensions.enumToLong
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncDocumentMapProcessEntity
import com.repzone.domain.model.SyncDocumentMapProcessModel
import com.repzone.network.dto.DocumentMapProcessDto

class SyncDocumentMapProcessEntityDbMapper : MapperDto<SyncDocumentMapProcessEntity, SyncDocumentMapProcessModel, DocumentMapProcessDto> {
    //region Public Method
    override fun toDomain(from: SyncDocumentMapProcessEntity): SyncDocumentMapProcessModel {
        return SyncDocumentMapProcessModel(
            id = from.Id,
            docProcessType = from.DocProcessType,
            documentMapName = from.DocumentMapName,
            name = from.Name
        )
    }

    override fun fromDomain(domain: SyncDocumentMapProcessModel): SyncDocumentMapProcessEntity {
        return SyncDocumentMapProcessEntity(
            Id = domain.id,
            DocProcessType = domain.docProcessType,
            DocumentMapName = domain.documentMapName,
            Name = domain.name
        )
    }

    override fun fromDto(dto: DocumentMapProcessDto): SyncDocumentMapProcessEntity {
        return SyncDocumentMapProcessEntity(
            Id = dto.id.toLong(),
            DocProcessType = dto.docProcessType?.enumToLong(),
            DocumentMapName = null,
            Name = dto.name
        )
    }

    fun fromDto(dto: DocumentMapProcessDto,documentMapName: String?): SyncDocumentMapProcessEntity {
        return SyncDocumentMapProcessEntity(
            Id = dto.id.toLong(),
            DocProcessType = dto.docProcessType?.enumToLong(),
            DocumentMapName = documentMapName,
            Name = dto.name
        )
    }
    //endregion

}
