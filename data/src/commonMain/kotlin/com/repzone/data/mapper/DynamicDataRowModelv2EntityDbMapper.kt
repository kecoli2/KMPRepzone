package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.DynamicDataRowModelv2Entity
import com.repzone.domain.model.DynamicDataRowModelv2Model

class DynamicDataRowModelv2EntityDbMapper : Mapper<DynamicDataRowModelv2Entity, DynamicDataRowModelv2Model> {
    //region Public Method
    override fun toDomain(from: DynamicDataRowModelv2Entity): DynamicDataRowModelv2Model {
        return DynamicDataRowModelv2Model(
            id = from.Id,
            rowId = from.RowId,
            rowValue = from.RowValue,
            utcLogDate = from.UtcLogDate
        )
    }

    override fun fromDomain(domain: DynamicDataRowModelv2Model): DynamicDataRowModelv2Entity {
        return DynamicDataRowModelv2Entity(
            Id = domain.id,
            RowId = domain.rowId,
            RowValue = domain.rowValue,
            UtcLogDate = domain.utcLogDate
        )
    }
    //endregion

}
