package com.repzone.domain.repository

import com.repzone.domain.model.MobileParameterModel
import com.repzone.domain.model.SyncPackageCustomFieldProductModel

interface IMobileModuleParameterRepository {
    fun getMobileModuleParameter(): MobileParameterModel

    fun getMobileModulePrameters(productId: Int): List<SyncPackageCustomFieldProductModel>
}