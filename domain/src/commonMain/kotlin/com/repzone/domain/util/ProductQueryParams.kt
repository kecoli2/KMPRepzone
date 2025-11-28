package com.repzone.domain.util

data class ProductQueryParams(
    val salesOperationType: Int,
    val organizationId: Int,
    val repDistId: Int,
    val custDistId: Int,
    val custGrpMustStockListId: Int,
    val custGrpPriceListId: Int,
    val custPriceListId: Int,
    val customerId: Int,
    val customerGroupId: Int,
    val representId: Int,
    val custReturnPriceListId: Int,
    val custGrpReturnPriceListId: Int,
    val custDmgPriceListId: Int,
    val custGrpDmgPriceListId: Int,
    val isCustomerVatExempt: Boolean,
    val repReturnDistId: Int,
    val custReturnDistId: Int,
    val returnPriceType: Int,
    val dmgReturnPriceType: Int,
    val showTransitStock: Boolean,
    val showAvailableStock: Boolean,
    val mfrId: Int,
    val notAllowedMfrs: List<Int>,
    val prefOrgId: Int,
    val ruledPriceListIds: List<Int>
)