package com.repzone.domain.repository

import com.repzone.core.enums.FormVisibleOption
import com.repzone.domain.model.SyncFormBaseModel
import com.repzone.domain.model.forms.FormBase
import com.repzone.domain.model.forms.FormModel

interface IDynamicFormRepository {
    suspend fun updateDefinitions(forms: List<FormBase>)
    suspend fun getForms(): List<SyncFormBaseModel>
    suspend fun pushToRestService(model: FormModel)
    suspend fun validFormVersion(formId: Int, version: String): Boolean
    suspend fun getDataSetByFormID(formId: Int)
    suspend fun getFormsByVisibleOption(visibleOption: FormVisibleOption): List<SyncFormBaseModel>
    suspend fun getCustomerCategories(customerId: Int, orgId: Int, customerOrgId: Int): List<Int>
    suspend fun getCustomerChannels(customerId: Int, orgId: Int, customerOrgId: Int): List<Int>
    suspend fun getCustomerClasses(customerId: Int, orgId: Int, customerOrgId: Int): List<Int>
    suspend fun getCustomerSegments(customerId: Int, orgId: Int, customerOrgId: Int): List<Int>

    suspend fun getFormBase(json: String?): FormBase?
}