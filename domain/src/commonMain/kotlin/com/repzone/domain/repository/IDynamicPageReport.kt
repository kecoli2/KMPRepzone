package com.repzone.domain.repository

import com.repzone.domain.model.SyncDynamicPageReportModel

interface IDynamicPageReport {
    suspend fun getAll(): List<SyncDynamicPageReportModel>
    suspend fun get(reportName : String): SyncDynamicPageReportModel?
}