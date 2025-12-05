package com.repzone.data.repository.imp

import com.repzone.core.enums.StateType
import com.repzone.data.mapper.SyncDynamicPageReportEntityDbMapper
import com.repzone.database.SyncDynamicPageReportEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.select
import com.repzone.domain.model.SyncDynamicPageReportModel
import com.repzone.domain.repository.IDynamicPageReport

class DynamicPageReportImpl(private val iDatabaseManager: IDatabaseManager, private val mapper: SyncDynamicPageReportEntityDbMapper): IDynamicPageReport {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun getAll(): List<SyncDynamicPageReportModel> {
        return iDatabaseManager.getSqlDriver().select<SyncDynamicPageReportEntity> {
            where {
                criteria("State", notEqual = StateType.DELETED.ordinal)
            }
        }.toList().map {
            mapper.toDomain(it)
        }
    }

    override suspend fun get(reportName: String): SyncDynamicPageReportModel? {
        return iDatabaseManager.getSqlDriver().select<SyncDynamicPageReportEntity> {
            where {
                criteria("Name", reportName)
            }
        }.firstOrNull()?.let {
            mapper.toDomain(it)
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}