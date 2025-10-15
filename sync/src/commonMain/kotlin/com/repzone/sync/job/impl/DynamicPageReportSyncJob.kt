package com.repzone.sync.job.impl

import com.repzone.core.constant.ICommonApiControllerConstant
import com.repzone.core.enums.UIModule
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.DynamicPageReportDto
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobType
import com.repzone.core.enums.UserRole

class DynamicPageReportSyncJob(apiService: ISyncApiService<List<DynamicPageReportDto>>,
                               bulkInsertService: IBulkInsertService<List<DynamicPageReportDto>>,
                               syncModuleRepository: ISyncModuleRepository,
): BasePaginatedSyncJob<List<DynamicPageReportDto>>(apiService, bulkInsertService, syncModuleRepository) {
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP)
    override val jobType = SyncJobType.COMMON_DYNAMIC_PAGES
    override val defaultRequestEndPoint= ICommonApiControllerConstant.COMMON_APP_DYNAMIC_PAGE
    override val moduleType = UIModule.NEW
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getFetchedMessage(count: Int): String {
        return "Fetched $count dynamic page reports..."
    }

    override fun getCompletedMessage(count: Int): String {
        return "$count dynamic page reports saved..."
    }

    override fun extractLastId(dtoData: List<DynamicPageReportDto>): Long {
        return dtoData.lastOrNull()?.id?.toLong() ?: 0
    }

    override fun getDataSize(dtoData: List<DynamicPageReportDto>): Int {
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