package com.repzone.data.repository.imp

import com.repzone.core.enums.FormVisibleOption
import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.data.mapper.FormBaseEntityDbMapper
import com.repzone.data.mapper.SyncFormBaseEntityDbMapper
import com.repzone.database.SyncFormBaseEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.delete
import com.repzone.database.runtime.insertOrReplace
import com.repzone.database.runtime.select
import com.repzone.domain.model.SyncFormBaseModel
import com.repzone.domain.model.forms.FormBase
import com.repzone.domain.model.forms.FormModel
import com.repzone.domain.repository.IDynamicFormRepository
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class DynamicFormRepositoryImpl(private val iDatabaseManager: IDatabaseManager, private val mapper: SyncFormBaseEntityDbMapper): IDynamicFormRepository {
    //region Public Method
    override suspend fun updateDefinitions(forms: List<FormBase>) {
        forms.forEach { form ->
            form.formRows = form.formRows.sortedBy { it.order }
        }

        iDatabaseManager.getSqlDriver().delete<SyncFormBaseEntity> {

        }

        //TODO S json Serialize Yapilacak
        val list = forms.map { s ->
            SyncFormBaseEntity(
                Data = "",
                Description = s.name,
                FormName = s.schemaName,
                DocumentTypeId = s.formDocumentType?.enumToLong(),
                FormId = s.id.toLong(),
                ModificationDateUtc = s.modificationDateUtc?.toEpochMilliseconds(),
                RecordDateUtc = s.recordDateUtc?.toEpochMilliseconds(),
                State = s.state.enumToLong(),
                Id = s.id.toLong(),
                VisibleOption = s.visibleOption?.enumToLong()
            )
        }
        iDatabaseManager.getSqlDriver().insertOrReplace(list)
    }

    override suspend fun getForms(): List<SyncFormBaseModel> {
        val list =  iDatabaseManager.getSqlDriver().select<SyncFormBaseEntity> {
            where {
                criteria("State", notEqual = StateType.DELETED.ordinal)
            }
        }.toList().map {
            mapper.toDomain(it)
        }
        return list
    }

    override suspend fun pushToRestService(model: FormModel) {
        TODO("Not yet implemented")
    }

    override suspend fun validFormVersion(formId: Int, version: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getDataSetByFormID(formId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getFormsByVisibleOption(visibleOption: FormVisibleOption): List<SyncFormBaseModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomerCategories(customerId: Int, orgId: Int, customerOrgId: Int): List<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomerChannels(customerId: Int, orgId: Int, customerOrgId: Int): List<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomerClasses(customerId: Int, orgId: Int, customerOrgId: Int): List<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomerSegments(customerId: Int, orgId: Int, customerOrgId: Int): List<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getFormBase(json: String?): FormBase? {
        if(json == null){
            return null
        }
        val formBase = FormBaseEntityDbMapper().toJsonFromDomain(json)
        return formBase
    }
    //endregion

}