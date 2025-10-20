package com.repzone.data.mapper

import com.repzone.core.enums.CustomFieldDataType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toBoolean
import com.repzone.core.util.extensions.toEnum
import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncPackageCustomFieldProductEntity
import com.repzone.domain.model.SyncPackageCustomFieldProductModel
import com.repzone.network.dto.PackageCustomFieldProductDto
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class SyncPackageCustomFieldProductEntityDbMapper : MapperDto<SyncPackageCustomFieldProductEntity, SyncPackageCustomFieldProductModel, PackageCustomFieldProductDto> {
    //region Public Method
    @OptIn(ExperimentalTime::class)
    override fun toDomain(from: SyncPackageCustomFieldProductEntity): SyncPackageCustomFieldProductModel {
        return SyncPackageCustomFieldProductModel(
            id = from.Id,
            dataType = from.DataType?.toEnum<CustomFieldDataType>(),
            dateMax = if (from.DateMax == null) null else Instant.fromEpochMilliseconds(from.DateMax!!),
            dateMin = if (from.DateMin == null) null else Instant.fromEpochMilliseconds(from.DateMin!!),
            decimalMax = from.DecimalMax,
            decimalMin = from.DecimalMin,
            defaultValue = from.DefaultValue,
            description = from.Description,
            fieldName = from.FieldName,
            integerMax = from.IntegerMax,
            integerMin = from.IntegerMin,
            itemList = from.ItemList,
            mandatory = from.Mandatory?.toBoolean() ?: false,
            order = from.Order,
            packageId = from.PackageId,
            packageName = from.PackageName,
            productId = from.ProductId,
            stringMax = from.StringMax,
            value = from.Value
        )
    }

    @OptIn(ExperimentalTime::class)
    override fun fromDomain(domain: SyncPackageCustomFieldProductModel): SyncPackageCustomFieldProductEntity {
        return SyncPackageCustomFieldProductEntity(
            Id = domain.id,
            DataType = domain.dataType?.enumToLong(),
            DateMax = domain.dateMax?.toEpochMilliseconds(),
            DateMin = domain.dateMin?.toEpochMilliseconds(),
            DecimalMax = domain.decimalMax,
            DecimalMin = domain.decimalMin,
            DefaultValue = domain.defaultValue,
            Description = domain.description,
            FieldName = domain.fieldName,
            IntegerMax = domain.integerMax,
            IntegerMin = domain.integerMin,
            ItemList = domain.itemList,
            Mandatory = domain.mandatory.toLong(),
            Order = domain.order,
            PackageId = domain.packageId,
            PackageName = domain.packageName,
            ProductId = domain.productId,
            StringMax = domain.stringMax,
            Value = domain.value
        )
    }

    @OptIn(ExperimentalTime::class)
    override fun fromDto(dto: PackageCustomFieldProductDto): SyncPackageCustomFieldProductEntity {
        return SyncPackageCustomFieldProductEntity(
            Id = dto.id.toLong(),
            DataType = dto.dataType?.enumToLong(),
            DateMax = dto.dateMax?.toEpochMilliseconds(),
            DateMin = dto.dateMin?.toEpochMilliseconds(),
            DecimalMax = dto.decimalMax,
            DecimalMin = dto.decimalMin,
            DefaultValue = dto.defaultValue,
            Description = dto.description,
            FieldName = dto.fieldName,
            IntegerMax = dto.integerMax?.toLong(),
            IntegerMin = dto.integerMin?.toLong(),
            ItemList = dto.itemList,
            Mandatory = dto.mandatory.toLong(),
            Order = dto.order.toLong(),
            PackageId = dto.packageId?.toLong(),
            PackageName = dto.packageName,
            ProductId = dto.productId.toLong(),
            StringMax = dto.stringMax?.toLong(),
            Value = dto.value
        )
    }
    //endregion

}
