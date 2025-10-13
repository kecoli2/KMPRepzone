package com.repzone.sync.job.impl

import com.repzone.core.constant.ICommonApiControllerConstant
import com.repzone.core.enums.UIModule
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.PackageCustomFieldDto
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobType
import com.repzone.sync.model.UserRole

class PakageCustomFieldSyncJob(apiService: ISyncApiService<List<PackageCustomFieldDto>>,
                               bulkInsertService: IBulkInsertService<List<PackageCustomFieldDto>>, syncModuleRepository: ISyncModuleRepository,
): BasePaginatedSyncJob<List<PackageCustomFieldDto>>(apiService, bulkInsertService, syncModuleRepository) {
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP, UserRole.MERGE_STAFF, UserRole.MANAGER, UserRole.ADMIN)
    override val jobType = SyncJobType.COMMON_MODULES
    override val defaultRequestEndPoint = ICommonApiControllerConstant.COMMON_APP_MODULES_ENDPOINT
    override val moduleType = UIModule.NEW
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getFetchedMessage(count: Int): String {
        return "Fetched $count custom fields..."
    }

    override fun getCompletedMessage(count: Int): String {
        return "$count product custom fields saved..."
    }

    override fun extractLastId(dtoData: List<PackageCustomFieldDto>): Long {
        return dtoData.lastOrNull()?.id?.toLong() ?: 0
    }

    override fun getDataSize(dtoData: List<PackageCustomFieldDto>): Int {
        return dtoData.size
    }

    override fun onPreExecuteFilterModel(value: FilterModelRequest): FilterModelRequest {
        value.take = 5000
        return value
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}