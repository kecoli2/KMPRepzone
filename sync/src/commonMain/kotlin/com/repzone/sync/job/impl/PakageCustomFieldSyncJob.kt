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
import com.repzone.core.enums.UserRole
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.model.ResourceUI
import com.repzone.core.util.extensions.toDateString
import com.repzone.network.dto.EventReasonDto
import com.repzone.sync.model.SyncJobGroup
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_fetched
import repzonemobile.core.generated.resources.job_complate_saved
import repzonemobile.core.generated.resources.job_module

class PakageCustomFieldSyncJob(apiService: ISyncApiService<List<PackageCustomFieldDto>>,
                               bulkInsertService: IBulkInsertService<List<PackageCustomFieldDto>>, syncModuleRepository: ISyncModuleRepository,
    private val iUserSession: IUserSession
): BasePaginatedSyncJob<List<PackageCustomFieldDto>>(apiService, bulkInsertService, syncModuleRepository) {
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP)
    override val jobType = SyncJobType.COMMON_MODULES
    override val defaultRequestEndPoint = ICommonApiControllerConstant.COMMON_APP_MODULES_ENDPOINT
    override val moduleType = UIModule.NEW
    override val jobGroup: SyncJobGroup = SyncJobGroup.NONE
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getFetchedMessage(count: Int): ResourceUI {
        return ResourceUI(
            res = Res.string.job_complate_fetched,
            args = listOf(count, Res.string.job_module)
        )
    }

    override fun getCompletedMessage(count: Int): ResourceUI {
        iUserSession.setReloadParameters()
        return ResourceUI(
            res = Res.string.job_complate_saved,
            args = listOf(count, Res.string.job_module)
        )
    }

    override fun extractLastIdAndLastDate(dtoData: List<PackageCustomFieldDto>, requestModel: FilterModelRequest?){
        dtoData.lastOrNull()?.let {
            requestModel?.lastId = it.id
        }
    }

    override fun getDataSize(dtoData: List<PackageCustomFieldDto>): Int {
        return dtoData.size
    }

    override fun onPreExecuteFilterModel(value: FilterModelRequest): FilterModelRequest {
        value.take = 5000
        value.lastId = 0
        value.lastModDate = ""
        return value
    }
    //endregion

}