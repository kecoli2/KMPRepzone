package com.repzone.domain.model

data class DistributionControllerModel(
    var customerDistributionListId: Int = -1,
    var representativeDistributionListId: Int = -1,
    var customerReturnDistributionListId: Int = -1,
    var representativeReturnDistributionListId: Int = -1,
    var mustStockListId: Int = -1,
    var customerPriceListId: Int = -1,
    var customerGroupPriceListId: Int = -1,
    var customerReturnPriceListId: Int = -1,
    var customerGroupReturnPriceListId: Int = -1,
    var customerDamagedPriceListId: Int = -1,
    var customerGroupDamagedPriceListId: Int = -1)