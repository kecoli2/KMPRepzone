package com.repzone.data.mapper

import com.repzone.core.util.extensions.toBoolean
import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.Mapper
import com.repzone.database.ProductParameterEntity
import com.repzone.database.SyncProductEntity
import com.repzone.database.SyncProductUnitEntity
import com.repzone.network.dto.ProductDto
import com.repzone.network.dto.ProductParameterDto
import com.repzone.network.dto.ProductUnitDto
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class ProductEntityDtoDbMapper: Mapper<SyncProductEntity, ProductDto> {
    //region Public Method

    override fun toDomain(from: SyncProductEntity): ProductDto {
        return ProductDto(
            id = from.Id.toInt(),
            state = from.State?.toInt() ?: 0,
            sku = from.Sku,
            name = from.Name,
            description = from.Description,
            tags = from.Tags,
            brandId = from.BrandId?.toInt(),
            brandName = from.BrandName,
            displayOrder = from.DisplayOrder?.toInt(),
            orderQuantityFactor = from.OrderQuantityFactor?.toInt(),
            minimumOrderQuantity = from.MinimumOrderQuantity?.toInt(),
            maximumOrderQuantity = from.MaximumOrderQuantity?.toInt(),
            groupId = from.GroupId?.toInt(),
            groupName = from.GroupName,
            photoPath = from.PhotoPath,
            vat = from.Vat,
            organizationId = from.OrganizationId?.toInt(),
            organizationIds = from.OrganizationIds,
            units = emptyList(),
            color = from.Color,
            brandPhotoPath = from.BrandPhotoPath,
            groupPhotoPath = from.GroupPhotoPath,
            parameters = emptyList(),
            modificationDateUtc = from.ModificationDateUtc?.let {  Instant.fromEpochMilliseconds(it) },
            closeToSales = from.CloseToSales?.toBoolean(true),
            closeToReturns = from.CloseToReturns.toBoolean(true),
        )
    }

    override fun fromDomain(domain: ProductDto): SyncProductEntity {
        return SyncProductEntity(
            Id = domain.id.toLong(),
            BrandId = domain.brandId?.toLong(),
            BrandName = domain.brandName,
            BrandPhotoPath = domain.brandPhotoPath,
            CloseToReturns = domain.closeToReturns?.toLong(),
            CloseToSales = domain.closeToSales?.toLong(),
            Color = domain.color,
            Description = domain.description,
            DisplayOrder = domain.displayOrder?.toLong(),
            GroupId = domain.groupId?.toLong(),
            GroupName = domain.groupName,
            GroupPhotoPath = domain.groupPhotoPath,
            IsVisible = true.toLong(),
            ManufacturerId = null,
            MaximumOrderQuantity = domain.maximumOrderQuantity?.toLong(),
            MinimumOrderQuantity = domain.minimumOrderQuantity?.toLong(),
            ModificationDateUtc = domain.modificationDateUtc?.toEpochMilliseconds(),
            Name = domain.name,
            OrderQuantityFactor = domain.orderQuantityFactor?.toLong(),
            OrganizationId = domain.organizationId?.toLong(),
            OrganizationIds = domain.organizationIds,
            PhotoPath = domain.photoPath,
            RecordDateUtc = null,
            Shared = null,
            Sku = domain.sku,
            State = domain.state.toLong(),
            Tags = domain.tags,
            TenantId = 0,
            Vat = domain.vat
        )
    }

    fun toUnitEntities(productId : Long?, list: List<ProductUnitDto>): List<SyncProductUnitEntity>  {
        return list.map { domain ->
            SyncProductUnitEntity(
                Id = domain.id.toLong(),
                Barcode = domain.barcode,
                DisplayOrder = domain.displayOrder.toLong(),
                IsVisible = true.toLong(),
                MaxOrderQuantity = domain.maxOrderQuantity.toLong(),
                MinimumOrderQuantity = domain.minimumOrderQuantity.toLong(),
                ModificationDateUtc = null,
                Multiplier = domain.multiplier.toLong(),
                OrderQuantityFactor = domain.orderQuantityFactor.toLong(),
                Price = domain.price,
                PriceId = domain.priceId.toLong(),
                ProductId = productId,
                PurchaseDamagedReturnPrice = domain.purchaseDamagedReturnPrice,
                PurchasePrice = domain.purchasePrice,
                PurchaseReturnPrice = domain.purchaseReturnPrice,
                RecordDateUtc = null,
                SalesDamagedReturnPrice = domain.salesDamagedReturnPrice,
                SalesReturnPrice = domain.salesReturnPrice,
                Selected = domain.selected.let{ if (it) 1L else 0L },
                State = domain.state.toLong(),
                TenantId = 0,
                UnitId = domain.unitId.toLong(),
                Weight = domain.weight
            )
        }
    }

    fun toParametersEnties(productId : Long?, list: List<ProductParameterDto>): List<ProductParameterEntity> {
        return list.map { domain ->
            ProductParameterEntity(
                Id = domain.id.toLong(),
                ProductId = productId,
                Order = domain.order?.toLong(),
                Color = domain.color,
                IsVisible = true.toLong(),
                OrganizationId = domain.id.toLong()
            )
        }
    }
    //endregion

}