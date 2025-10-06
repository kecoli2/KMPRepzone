package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncPackageCustomFieldProductEntity
import com.repzone.domain.model.SyncPackageCustomFieldProductModel

class SyncPackageCustomFieldProductEntityDbMapper : Mapper<SyncPackageCustomFieldProductEntity, SyncPackageCustomFieldProductModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncPackageCustomFieldProductEntity): SyncPackageCustomFieldProductModel {
        return SyncPackageCustomFieldProductModel(
            id = from.Id,
            dataType = from.DataType,
            dateMax = from.DateMax,
            dateMin = from.DateMin,
            decimalMax = from.DecimalMax,
            decimalMin = from.DecimalMin,
            defaultValue = from.DefaultValue,
            description = from.Description,
            fieldName = from.FieldName,
            integerMax = from.IntegerMax,
            integerMin = from.IntegerMin,
            itemList = from.ItemList,
            mandatory = from.Mandatory,
            order = from.Order,
            packageId = from.PackageId,
            packageName = from.PackageName,
            productId = from.ProductId,
            stringMax = from.StringMax,
            value = from.Value
        )
    }

    override fun fromDomain(domain: SyncPackageCustomFieldProductModel): SyncPackageCustomFieldProductEntity {
        return SyncPackageCustomFieldProductEntity(
            Id = domain.id,
            DataType = domain.dataType,
            DateMax = domain.dateMax,
            DateMin = domain.dateMin,
            DecimalMax = domain.decimalMax,
            DecimalMin = domain.decimalMin,
            DefaultValue = domain.defaultValue,
            Description = domain.description,
            FieldName = domain.fieldName,
            IntegerMax = domain.integerMax,
            IntegerMin = domain.integerMin,
            ItemList = domain.itemList,
            Mandatory = domain.mandatory,
            Order = domain.order,
            PackageId = domain.packageId,
            PackageName = domain.packageName,
            ProductId = domain.productId,
            StringMax = domain.stringMax,
            Value = domain.value
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
