package com.repzone.sync.job.impl

import com.repzone.core.constant.ICommonApiControllerConstant
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.EventReasonDto
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobType
import com.repzone.sync.model.UserRole

class EventReasonsSyncJob(apiService: ISyncApiService<List<EventReasonDto>>, bulkinsertService: IBulkInsertService<List<EventReasonDto>>,
                          syncModuleRepository: ISyncModuleRepository)
    : BasePaginatedSyncJob<List<EventReasonDto>>(apiService, bulkinsertService, syncModuleRepository){
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP, UserRole.MERGE_STAFF, UserRole.MANAGER, UserRole.ADMIN)
    override val jobType = SyncJobType.COMMON_MODULES_REASONS
    override val defaultRequestEndPoint = ICommonApiControllerConstant.COMMON_APP_MODULES_REASONS_ENDPOINT
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun extractLastId(dtoData: List<EventReasonDto>): Long {
        return dtoData.lastOrNull()?.id?.toLong() ?: 0
    }

    override fun getDataSize(dtoData: List<EventReasonDto>): Int {
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