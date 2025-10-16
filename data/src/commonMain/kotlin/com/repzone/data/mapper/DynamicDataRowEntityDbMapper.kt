package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.DynamicDataRowEntity
import com.repzone.domain.model.DynamicDataRowModel

class DynamicDataRowEntityDbMapper : Mapper<DynamicDataRowEntity, DynamicDataRowModel> {
    //region Public Method
    override fun toDomain(from: DynamicDataRowEntity): DynamicDataRowModel {
        return DynamicDataRowModel(
            rowId = from.RowId,
            rowValue = from.RowValue,
            utcLogDate = from.UtcLogDate
        )
    }

    override fun fromDomain(domain: DynamicDataRowModel): DynamicDataRowEntity {
        return DynamicDataRowEntity(
            RowId = domain.rowId,
            RowValue = domain.rowValue,
            UtcLogDate = domain.utcLogDate
        )
    }
    //endregion

}
