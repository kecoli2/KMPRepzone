package com.repzone.data.repository.imp

import com.repzone.core.enums.ModuleProductIdsEnum
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncPackageCustomFieldProductEntity
import com.repzone.database.SyncPackageCustomFieldProductEntityQueries
import com.repzone.domain.model.MobileParameterModel
import com.repzone.domain.model.SyncPackageCustomFieldProductModel
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.network.dto.PackageCustomFieldProductDto

class MobileModuleParameterRepositoryImpl(
        private val syncPackageCustomFieldProductQueries: SyncPackageCustomFieldProductEntityQueries,
    private val mapperCustomFieldProduct: MapperDto<SyncPackageCustomFieldProductEntity, SyncPackageCustomFieldProductModel, PackageCustomFieldProductDto> ): IMobileModuleParameterRepository {

    //region Public Method
    override fun getMobileModuleParameter(): MobileParameterModel {
        val activePack = syncPackageCustomFieldProductQueries.selectAllSyncPackageCustomFieldProductEntity().executeAsList()

        var retModel = MobileParameterModel(
            order = true,
            invoice = true,
            qlikReport = true,
            drive = true,
            task = true,
            eagleEye = true,
            chat = true,
            geoFence = true,
            form = true,
            timeTracker = true,
            report = true,
            dispatch = true,
            payment = true,
            gamification = true,
            crmOperations = true
        )

        activePack.forEach { pkg ->
            val parameters = getMobileModulePrameters(pkg.Id.toInt())
            var isActive = false

            parameters.forEach { param ->
                if (param.fieldName == "IsActive") {
                    isActive = param.value.toBoolean()
                }
            }

            when (pkg.Id.toEnum<ModuleProductIdsEnum>()) {
                ModuleProductIdsEnum.ORDERS -> { // Orders - 19
                    retModel = retModel.copy(order = isActive)
                }
                ModuleProductIdsEnum.INVOICESANDEINVOICES -> { // Invoices & EInvoices - 26
                    retModel = retModel.copy(invoice = isActive)
                }
                ModuleProductIdsEnum.BUSINESSINTELLIGENCE -> { // Business Intelligence - 22
                    retModel = retModel.copy(qlikReport = isActive)
                }
                ModuleProductIdsEnum.DIGITALCONTENTSHARING -> { // Digital Content Sharing - 11
                    retModel = retModel.copy(drive = isActive)
                }
                ModuleProductIdsEnum.TASKMANAGEMENT -> { // Task Management - 23
                    retModel = retModel.copy(task = isActive)
                }
                ModuleProductIdsEnum.EAGLEEYEANDLOCATIONTRACKING -> { // EagleEye / LocationTracking - 13
                    retModel = retModel.copy(eagleEye = isActive)
                }
                ModuleProductIdsEnum.MESSAGINGANDCHAT -> { // Messaging / Chat - 12
                    retModel = retModel.copy(chat = isActive)
                }
                ModuleProductIdsEnum.GEOFENCINGANDROUTETRACKING -> { // GeoFence / RouteTracking - 32
                    retModel = retModel.copy(geoFence = isActive)
                }
                ModuleProductIdsEnum.CUSTOMMOBILEFORMS -> { // Custom Mobile Forms - 8
                    retModel = retModel.copy(form = isActive)
                }
                ModuleProductIdsEnum.ATTENDANCETRACKING -> { // Attendance Tracking - 24
                    retModel = retModel.copy(timeTracker = isActive)
                }
                ModuleProductIdsEnum.REPORTS -> { // Reports - 15
                    retModel = retModel.copy(report = isActive)
                }
                ModuleProductIdsEnum.PAYMENTCOLLECTION -> { // Payments - 29
                    retModel = retModel.copy(payment = isActive)
                }
                ModuleProductIdsEnum.DISPATCHES -> { // Dispatches - 36
                    retModel = retModel.copy(dispatch = isActive)
                }
                ModuleProductIdsEnum.GAMIFICATION -> { // 38
                    retModel = retModel.copy(gamification = isActive)
                }
                ModuleProductIdsEnum.CRMOPERATIONS -> { // 39
                    retModel = retModel.copy(crmOperations = isActive)
                }

                ModuleProductIdsEnum.DIGITALPRODUCTCATALOG -> {

                }
                ModuleProductIdsEnum.QUICKACCESS -> {

                }
                ModuleProductIdsEnum.PRODUCTALLOCATION -> {

                }
                ModuleProductIdsEnum.VISIT -> {

                }
                null -> {


                }
            }
        }

        return retModel
    }

    override fun getMobileModulePrameters(productId: Int): List<SyncPackageCustomFieldProductModel> {
        return syncPackageCustomFieldProductQueries.selectBySyncPackageCustomFieldProductEntityProductId(productId.toLong()).executeAsList().map { mapperCustomFieldProduct.toDomain(it) }
    }
    //endregion
}