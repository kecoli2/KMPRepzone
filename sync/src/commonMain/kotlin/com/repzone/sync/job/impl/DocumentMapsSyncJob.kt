package com.repzone.sync.job.impl

import com.repzone.core.constant.ICommonApiControllerConstant
import com.repzone.core.enums.UIModule
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.DocumentMapModelDto
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobType
import com.repzone.core.enums.UserRole

class DocumentMapsSyncJob(apiService: ISyncApiService<List<DocumentMapModelDto>>,
                          bulkInsertService: IBulkInsertService<List<DocumentMapModelDto>>,
                          syncModuleRepository: ISyncModuleRepository,
): BasePaginatedSyncJob<List<DocumentMapModelDto>>(apiService, bulkInsertService, syncModuleRepository){
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP)
    override val jobType = SyncJobType.COMMON_DOCUMENT_MAPS
    override val defaultRequestEndPoint = ICommonApiControllerConstant.COMMON_APP_DOCUMENT_MAPS
    override val moduleType = UIModule.NEW
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getFetchedMessage(count: Int): String {
        return "Fetched $count document maps..."
    }

    override fun getCompletedMessage(count: Int): String {
        return "$count document maps saved..."
    }

    override fun extractLastId(dtoData: List<DocumentMapModelDto>): Long {
        return dtoData.lastOrNull()?.id?.toLong() ?: 0
    }

    override fun getDataSize(dtoData: List<DocumentMapModelDto>): Int {
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