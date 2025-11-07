package com.repzone.sync.job.impl

import com.repzone.core.constant.IFormApiControllerConstant
import com.repzone.core.enums.UIModule
import com.repzone.core.enums.UserRole
import com.repzone.core.model.ResourceUI
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.form.FormBaseDto
import com.repzone.network.models.request.FilterModelRequest
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.job.base.BasePaginatedSyncJob
import com.repzone.sync.model.SyncJobGroup
import com.repzone.sync.model.SyncJobType
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_fetched
import repzonemobile.core.generated.resources.job_complate_saved
import repzonemobile.core.generated.resources.job_form_definations

class FormDataFetchSyncJob(apiService: ISyncApiService<List<FormBaseDto>>,
                           bulkInsertService: IBulkInsertService<List<FormBaseDto>>, syncModuleRepository: ISyncModuleRepository):
    BasePaginatedSyncJob<List<FormBaseDto>>(apiService, bulkInsertService, syncModuleRepository) {
    //region Field
    override val allowedRoles = setOf(UserRole.SALES_REP, UserRole.DISTRIBUTION)
    override val jobType = SyncJobType.FORM
    override val defaultRequestEndPoint = IFormApiControllerConstant.FORM_DEFINATION_ENDPOINT
    override val moduleType = UIModule.NEW
    override val jobGroup: SyncJobGroup = SyncJobGroup.FORMS
    //endregion

    //region Public Method
    override fun getFetchedMessage(count: Int): ResourceUI {
        return ResourceUI(
            res = Res.string.job_complate_fetched,
            args = listOf(count, Res.string.job_form_definations)
        )
    }

    override fun getCompletedMessage(count: Int): ResourceUI {
        return ResourceUI(
            res = Res.string.job_complate_saved,
            args = listOf(count, Res.string.job_form_definations))
    }

    override fun extractLastIdAndLastDate(dtoData: List<FormBaseDto>, requestModel: FilterModelRequest?) {
        dtoData.lastOrNull()?.let {
            requestModel?.lastId = it.id
        }
    }

    override fun getDataSize(dtoData: List<FormBaseDto>): Int {
        return dtoData.size
    }

    override fun onPreExecuteFilterModel(value: FilterModelRequest): FilterModelRequest {
        value.take = 5000
        value.lastId = 0
        value.lastModDate = ""
        return value
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}