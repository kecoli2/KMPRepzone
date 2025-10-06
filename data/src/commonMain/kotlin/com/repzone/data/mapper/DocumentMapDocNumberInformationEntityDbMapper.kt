package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.DocumentMapDocNumberInformationEntity
import com.repzone.domain.model.DocumentMapDocNumberInformationModel

class DocumentMapDocNumberInformationEntityDbMapper : Mapper<DocumentMapDocNumberInformationEntity, DocumentMapDocNumberInformationModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: DocumentMapDocNumberInformationEntity): DocumentMapDocNumberInformationModel {
        return DocumentMapDocNumberInformationModel(
            id = from.Id,
            documentMapId = from.DocumentMapId,
            documentNumberBody = from.DocumentNumberBody,
            documentNumberPostfix = from.DocumentNumberPostfix,
            documentNumberPrefix = from.DocumentNumberPrefix,
            documentType = from.DocumentType,
            state = from.State
        )
    }

    override fun fromDomain(domain: DocumentMapDocNumberInformationModel): DocumentMapDocNumberInformationEntity {
        return DocumentMapDocNumberInformationEntity(
            Id = domain.id,
            DocumentMapId = domain.documentMapId,
            DocumentNumberBody = domain.documentNumberBody,
            DocumentNumberPostfix = domain.documentNumberPostfix,
            DocumentNumberPrefix = domain.documentNumberPrefix,
            DocumentType = domain.documentType,
            State = domain.state
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
