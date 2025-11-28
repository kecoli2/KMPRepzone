package com.repzone.domain.util

// ProductQueryBuilder.kt
class ProductQueryBuilder {
    //region Public Method
    fun buildAllProductsQuery(params: ProductQueryParams): String {
        val displayOrder = buildDisplayOrderColumn(params)
        val color = buildColorColumn(params)
        val price = buildPriceColumn(params)
        val vat = buildVatColumn(params)
        val minOrderQuantity = buildMinOrderQuantityColumn()
        val maxOrderQuantity = buildMaxOrderQuantityColumn()
        val isCloseCondition = buildCloseCondition(params.salesOperationType)

        return buildString {
            // SELECT
            append("""
                SELECT 
                    P.Id AS ProductId,
                    PU.Barcode,
                    P.Tags,
                    P.BrandId,
                    P.BrandName,
                    P.GroupId,
                    P.GroupName,
                    P.SKU AS Sku,
                    P.Name AS ProductName,
                    P.PhotoPath,
                    $vat,
                    PU.UnitId,
                    U.Name AS UnitName,
                    PU.Multiplier,
                    PU.Weight,
                    COALESCE(St.Stock, 0.0) AS Stock,
                    COALESCE(St.OrderStock, 0.0) AS OrderStock,
                    COALESCE(TSt.Stock, 0.0) AS TransitStock,
                    COALESCE(RSt.Stock, 0.0) AS ReservedStock,
                    COALESCE(Wrh.Stock, 0.0) AS VanStock,
                    COALESCE(PRM.PendingAmount, 0.0) AS PendingStock,
                    P.BrandPhotoPath,
                    P.GroupPhotoPath,
                    PU.OrderQuantityFactor,
                    P.Description,
                    P.ManufacturerId,
                    $displayOrder,
                    $color,
                    PU.DisplayOrder AS UnitDisplayOrder,
                    ${if (params.showTransitStock) 1 else 0} AS ShowTransitStock,
                    ${if (params.showAvailableStock) 1 else 0} AS ShowAvailableStock,
                    $price,
                    $minOrderQuantity,
                    $maxOrderQuantity
            """.trimIndent())

            // FROM
            append("\nFROM MobileSyncProductModel P")

            // Dynamic JOINs
            appendJoins(params)

            // WHERE
            append("""
                
                WHERE P.IsVisible = 1 
                AND P.State = 1 
                AND U.State = 1 
                AND PU.State = 1 
                AND PM.State = 1
                AND ((MPM.AllowedUnitIds IS NULL AND U.IsVisible = 1) 
                     OR (MPM.AllowedUnitIds = '' AND U.IsVisible = 1) 
                     OR MPM.AllowedUnitIds LIKE '%,' || PU.UnitId || ',%')
                $isCloseCondition
            """.trimIndent())

            // Manufacturer filters
            if (params.mfrId > 0) {
                append("\nAND P.ManufacturerId = ${params.mfrId}")
            }
            if (params.notAllowedMfrs.isNotEmpty()) {
                append("\nAND P.ManufacturerId NOT IN (${params.notAllowedMfrs.joinToString(",")})")
            }

            append("\nORDER BY DisplayOrder")
        }
    }
    //endregion Public Method

    //region Private Method
    private fun StringBuilder.appendJoins(params: ProductQueryParams) {
        val p = params

        // Distribution joins based on sales operation type
        if (p.salesOperationType == 2 || p.salesOperationType == 8) {
            // Return operations
            if (p.repReturnDistId > 0) {
                append("""
                    INNER JOIN MobileSyncProductDistributionLineModel PDLR 
                        ON P.Id = PDLR.ProductId 
                        AND PDLR.DistributionId = ${p.repReturnDistId} 
                        AND PDLR.State = 1
                """.trimIndent())
            }
            if (p.custReturnDistId > 0) {
                append("""
                    INNER JOIN MobileSyncProductDistributionLineModel PDL_R 
                        ON P.Id = PDL_R.ProductId 
                        AND PDL_R.DistributionId = ${p.custReturnDistId} 
                        AND PDL_R.State = 1
                """.trimIndent())
            }
        } else {
            // Sales operations
            if (p.repDistId > 0) {
                append("""
                    
                    INNER JOIN MobileSyncProductDistributionLineModel PDL 
                        ON P.Id = PDL.ProductId 
                        AND PDL.DistributionId = ${p.repDistId} 
                        AND PDL.State = 1
                """.trimIndent())
            }
            if (p.custDistId > 0) {
                append("""
                    
                    INNER JOIN MobileSyncProductDistributionLineModel PDL_ 
                        ON P.Id = PDL_.ProductId 
                        AND PDL_.DistributionId = ${p.custDistId} 
                        AND PDL_.State = 1
                """.trimIndent())
            }
        }

        // Must stock list (always LEFT JOIN)
        if (p.custGrpMustStockListId > 0) {
            append("""
                
                LEFT JOIN MobileSyncProductDistributionLineModel PDL__ 
                    ON P.Id = PDL__.ProductId 
                    AND PDL__.DistributionId = ${p.custGrpMustStockListId} 
                    AND PDL__.State = 1
            """.trimIndent())
        }

        // Product Parameter (required)
        append("""
            
            INNER JOIN MobileProductParameterv4 PM 
                ON PM.ProductId = P.Id 
                AND ((P.OrganizationId = ${p.organizationId} 
                      AND P.OrganizationId = PM.OrganizationId 
                      AND PM.IsVisible = 1) 
                     OR (P.Shared = 1 
                         AND PM.OrganizationId = ${p.organizationId} 
                         AND PM.IsVisible = 1))
        """.trimIndent())

        // Product Unit (required)
        append("\nINNER JOIN MobileSyncProductUnitModel PU ON P.Id = PU.ProductId")

        // Price list joins
        appendPriceListJoins(p)

        // Unit (required)
        append("\nINNER JOIN MobileSyncUnitModel U ON PU.UnitId = U.Id")

        // Product Unit Parameters
        append("""
            
            LEFT JOIN MobileSyncProductUnitParameter PUP_customer 
                ON PUP_customer.ProductId = P.Id 
                AND PUP_customer.ProductUnitId = PU.Id 
                AND PUP_customer.State = 1 
                AND PUP_customer.EntityType = 0 
                AND PUP_customer.EntityId = ${p.customerId}
            LEFT JOIN MobileSyncProductUnitParameter PUP_customer_group 
                ON PUP_customer_group.ProductId = P.Id 
                AND PUP_customer_group.ProductUnitId = PU.Id 
                AND PUP_customer_group.State = 1 
                AND PUP_customer_group.EntityType = 1 
                AND PUP_customer_group.EntityId = ${p.customerGroupId}
            LEFT JOIN MobileSyncProductUnitParameter PUP_represent 
                ON PUP_represent.ProductId = P.Id 
                AND PUP_represent.ProductUnitId = PU.Id 
                AND PUP_represent.State = 1 
                AND PUP_represent.EntityType = 2 
                AND PUP_represent.EntityId = ${p.representId}
        """.trimIndent())

        // Stock joins
        val stockOrgId = if (p.prefOrgId > 0) p.prefOrgId else p.organizationId
        append("""
            
            LEFT JOIN MobileSyncStockModel St 
                ON St.ProductId = P.Id AND St.OrganizationId = $stockOrgId
            LEFT JOIN MobileSyncTransitStockModel TSt 
                ON TSt.ProductId = P.Id AND TSt.OrganizationId = ${p.organizationId}
            LEFT JOIN MobileSyncReservedStockModel RSt 
                ON RSt.ProductId = P.Id AND RSt.OrganizationId = ${p.organizationId}
            LEFT JOIN SyncMobileWarehouseTotalModel Wrh 
                ON Wrh.ProductId = P.Id
            LEFT JOIN MobilePendingReportModel PRM 
                ON PRM.ProductId = P.Id
            LEFT JOIN MobileSyncManufacturerParameterModel MPM 
                ON MPM.ManufacturerId = P.ManufacturerId 
                AND MPM.State = 1 
                AND MPM.OrganizationId = ${p.organizationId}
        """.trimIndent())
    }
    private fun StringBuilder.appendPriceListJoins(p: ProductQueryParams) {
        // Ruled price lists
        if (p.ruledPriceListIds.isNotEmpty()) {
            append("""
                
                LEFT JOIN MobileSyncProductPriceLines PPL_ruled 
                    ON P.Id = PPL_ruled.ProductId 
                    AND PPL_ruled.PriceListId IN (${p.ruledPriceListIds.joinToString(",")}) 
                    AND PU.Id = PPL_ruled.ProductUnitId 
                    AND PPL_ruled.State = 1
            """.trimIndent())
        }

        // Customer price list
        if (p.custPriceListId > 0) {
            append("""
                
                LEFT JOIN MobileSyncProductPriceLines PPL 
                    ON P.Id = PPL.ProductId 
                    AND PPL.PriceListId = ${p.custPriceListId} 
                    AND PU.Id = PPL.ProductUnitId 
                    AND PPL.State = 1
            """.trimIndent())
        }

        // Customer group price list
        if (p.custGrpPriceListId > 0) {
            append("""
                
                LEFT JOIN MobileSyncProductPriceLines PPL_ 
                    ON P.Id = PPL_.ProductId 
                    AND PPL_.PriceListId = ${p.custGrpPriceListId} 
                    AND PU.Id = PPL_.ProductUnitId 
                    AND PPL_.State = 1
            """.trimIndent())
        }

        // Return price lists
        if (p.custReturnPriceListId > 0) {
            append("""
                
                LEFT JOIN MobileSyncProductPriceLines PPLR 
                    ON P.Id = PPLR.ProductId 
                    AND PPLR.PriceListId = ${p.custReturnPriceListId} 
                    AND PU.Id = PPLR.ProductUnitId 
                    AND PPLR.State = 1
            """.trimIndent())
        }

        if (p.custGrpReturnPriceListId > 0) {
            append("""
                
                LEFT JOIN MobileSyncProductPriceLines PPL_R 
                    ON P.Id = PPL_R.ProductId 
                    AND PPL_R.PriceListId = ${p.custGrpReturnPriceListId} 
                    AND PU.Id = PPL_R.ProductUnitId 
                    AND PPL_R.State = 1
            """.trimIndent())
        }

        // Damaged return price lists
        if (p.custDmgPriceListId > 0) {
            append("""
                
                LEFT JOIN MobileSyncProductPriceLines PPLD 
                    ON P.Id = PPLD.ProductId 
                    AND PPLD.PriceListId = ${p.custDmgPriceListId} 
                    AND PU.Id = PPLD.ProductUnitId 
                    AND PPLD.State = 1
            """.trimIndent())
        }

        if (p.custGrpDmgPriceListId > 0) {
            append("""
                
                LEFT JOIN MobileSyncProductPriceLines PPL_D 
                    ON P.Id = PPL_D.ProductId 
                    AND PPL_D.PriceListId = ${p.custGrpDmgPriceListId} 
                    AND PU.Id = PPL_D.ProductUnitId 
                    AND PPL_D.State = 1
            """.trimIndent())
        }
    }
    private fun buildDisplayOrderColumn(params: ProductQueryParams): String {
        val p = params

        if (p.salesOperationType == 2 || p.salesOperationType == 8) {
            return "COALESCE(Pm.[Order], P.DisplayOrder) AS DisplayOrder"
        }

        return when {
            p.custDistId > 0 && p.repDistId > 0 && p.custGrpMustStockListId > 0 ->
                "COALESCE(PDL__.DisplayOrder, PDL_.DisplayOrder, PDL.DisplayOrder, Pm.[Order], P.DisplayOrder) AS DisplayOrder"
            p.custDistId > 0 && p.repDistId > 0 ->
                "COALESCE(PDL_.DisplayOrder, PDL.DisplayOrder, Pm.[Order], P.DisplayOrder) AS DisplayOrder"
            p.custDistId > 0 && p.custGrpMustStockListId > 0 ->
                "COALESCE(PDL__.DisplayOrder, PDL_.DisplayOrder, Pm.[Order], P.DisplayOrder) AS DisplayOrder"
            p.custDistId > 0 ->
                "COALESCE(PDL_.DisplayOrder, Pm.[Order], P.DisplayOrder) AS DisplayOrder"
            p.repDistId > 0 && p.custGrpMustStockListId > 0 ->
                "COALESCE(PDL__.DisplayOrder, PDL.DisplayOrder, Pm.[Order], P.DisplayOrder) AS DisplayOrder"
            p.repDistId > 0 ->
                "COALESCE(PDL.DisplayOrder, Pm.[Order], P.DisplayOrder) AS DisplayOrder"
            p.custGrpMustStockListId > 0 ->
                "COALESCE(PDL__.DisplayOrder, Pm.[Order], P.DisplayOrder) AS DisplayOrder"
            else ->
                "COALESCE(Pm.[Order], P.DisplayOrder) AS DisplayOrder"
        }
    }
    private fun buildColorColumn(params: ProductQueryParams): String {
        val p = params

        if (p.salesOperationType == 2 || p.salesOperationType == 8) {
            return "COALESCE(Pm.Color, P.Color) AS Color"
        }

        return when {
            p.custDistId > 0 && p.repDistId > 0 && p.custGrpMustStockListId > 0 ->
                "COALESCE(PDL__.Color, PDL_.Color, PDL.Color, Pm.Color, P.Color) AS Color"
            p.custDistId > 0 && p.repDistId > 0 ->
                "COALESCE(PDL_.Color, PDL.Color, Pm.Color, P.Color) AS Color"
            p.custDistId > 0 && p.custGrpMustStockListId > 0 ->
                "COALESCE(PDL__.Color, PDL_.Color, Pm.Color, P.Color) AS Color"
            p.custDistId > 0 ->
                "COALESCE(PDL_.Color, Pm.Color, P.Color) AS Color"
            p.repDistId > 0 && p.custGrpMustStockListId > 0 ->
                "COALESCE(PDL__.Color, PDL.Color, Pm.Color, P.Color) AS Color"
            p.repDistId > 0 ->
                "COALESCE(PDL.Color, Pm.Color, P.Color) AS Color"
            p.custGrpMustStockListId > 0 ->
                "COALESCE(PDL__.Color, Pm.Color, P.Color) AS Color"
            else ->
                "COALESCE(Pm.Color, P.Color) AS Color"
        }
    }
    private fun buildPriceColumn(params: ProductQueryParams): String {
        val p = params

        return when (p.salesOperationType) {
            2 -> buildReturnPriceColumn(p) // SalesReturn
            8 -> buildDamagedReturnPriceColumn(p) // SalesDamagedReturn
            else -> buildSalesPriceColumn(p)
        }
    }
    private fun buildSalesPriceColumn(p: ProductQueryParams): String {
        val hasRuledPrices = p.ruledPriceListIds.isNotEmpty()
        val ruledPrefix = if (hasRuledPrices) "PPL_ruled.Price, " else ""

        return when {
            p.custPriceListId > 0 && p.custGrpPriceListId > 0 ->
                "COALESCE(${ruledPrefix}PPL.Price, PPL_.Price, PU.Price) AS Price"
            p.custPriceListId > 0 ->
                "COALESCE(${ruledPrefix}PPL.Price, PU.Price) AS Price"
            p.custGrpPriceListId > 0 ->
                "COALESCE(${ruledPrefix}PPL_.Price, PU.Price) AS Price"
            hasRuledPrices ->
                "COALESCE(PPL_ruled.Price, PU.Price) AS Price"
            else ->
                "PU.Price AS Price"
        }
    }
    private fun buildReturnPriceColumn(p: ProductQueryParams): String {
        val fallbackPrice = when (p.returnPriceType) {
            1 -> "PU.Price" // sales
            3 -> "PU.SalesDamagedReturnPrice, PU.Price" // dmg return
            else -> "PU.SalesReturnPrice, PU.Price" // default return
        }

        return when {
            p.custReturnPriceListId > 0 && p.custGrpReturnPriceListId > 0 ->
                "COALESCE(PPLR.Price, PPL_R.Price, $fallbackPrice) AS Price"
            p.custReturnPriceListId > 0 ->
                "COALESCE(PPLR.Price, $fallbackPrice) AS Price"
            p.custGrpReturnPriceListId > 0 ->
                "COALESCE(PPL_R.Price, $fallbackPrice) AS Price"
            else ->
                "COALESCE($fallbackPrice) AS Price"
        }
    }
    private fun buildDamagedReturnPriceColumn(p: ProductQueryParams): String {
        val fallbackPrice = when (p.dmgReturnPriceType) {
            1 -> "PU.Price" // sales
            2 -> "PU.SalesReturnPrice, PU.Price" // return
            else -> "PU.SalesDamagedReturnPrice, PU.Price" // default dmg return
        }

        return when {
            p.custDmgPriceListId > 0 && p.custGrpDmgPriceListId > 0 ->
                "COALESCE(PPLD.Price, PPL_D.Price, $fallbackPrice) AS Price"
            p.custDmgPriceListId > 0 ->
                "COALESCE(PPLD.Price, $fallbackPrice) AS Price"
            p.custGrpDmgPriceListId > 0 ->
                "COALESCE(PPL_D.Price, $fallbackPrice) AS Price"
            else ->
                "COALESCE($fallbackPrice) AS Price"
        }
    }
    private fun buildVatColumn(params: ProductQueryParams): String {
        if (params.isCustomerVatExempt) {
            return "0 AS Vat"
        }

        val p = params

        return when (p.salesOperationType) {
            2 -> { // SalesReturn
                when {
                    p.custReturnPriceListId > 0 && p.custGrpReturnPriceListId > 0 ->
                        "COALESCE(PPLR.Vat, PPL_R.Vat, P.Vat) AS Vat"
                    p.custReturnPriceListId > 0 ->
                        "COALESCE(PPLR.Vat, P.Vat) AS Vat"
                    p.custGrpReturnPriceListId > 0 ->
                        "COALESCE(PPL_R.Vat, P.Vat) AS Vat"
                    else -> "P.Vat AS Vat"
                }
            }
            8 -> { // SalesDamagedReturn
                when {
                    p.custDmgPriceListId > 0 && p.custGrpDmgPriceListId > 0 ->
                        "COALESCE(PPLD.Vat, PPL_D.Vat, P.Vat) AS Vat"
                    p.custDmgPriceListId > 0 ->
                        "COALESCE(PPLD.Vat, P.Vat) AS Vat"
                    p.custGrpDmgPriceListId > 0 ->
                        "COALESCE(PPL_D.Vat, P.Vat) AS Vat"
                    else -> "P.Vat AS Vat"
                }
            }
            else -> { // Sales
                when {
                    p.custPriceListId > 0 && p.custGrpPriceListId > 0 ->
                        "COALESCE(PPL.Vat, PPL_.Vat, P.Vat) AS Vat"
                    p.custPriceListId > 0 ->
                        "COALESCE(PPL.Vat, P.Vat) AS Vat"
                    p.custGrpPriceListId > 0 ->
                        "COALESCE(PPL_.Vat, P.Vat) AS Vat"
                    else -> "P.Vat AS Vat"
                }
            }
        }
    }
    private fun buildMinOrderQuantityColumn(): String =
        "COALESCE(PUP_customer.MinOrderQuantity, PUP_customer_group.MinOrderQuantity, PUP_represent.MinOrderQuantity, PU.MinimumOrderQuantity) AS MinimumOrderQuantity"
    private fun buildMaxOrderQuantityColumn(): String =
        "COALESCE(PUP_customer.MaxOrderQuantity, PUP_customer_group.MaxOrderQuantity, PUP_represent.MaxOrderQuantity, PU.MaxOrderQuantity) AS MaxOrderQuantity"
    private fun buildCloseCondition(salesOperationType: Int): String {
        return when (salesOperationType) {
            2, 8 -> "AND (P.CloseToReturns IS NULL OR P.CloseToReturns = 0)"
            else -> "AND (P.CloseToSales IS NULL OR P.CloseToSales = 0)"
        }
    }
    //endregion Private Method
}