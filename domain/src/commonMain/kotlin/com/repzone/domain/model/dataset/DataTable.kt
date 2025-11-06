package com.repzone.domain.model.dataset

import com.repzone.core.enums.EntityModelType
import com.repzone.domain.model.forms.FormRow

data class DataTable(
    var rows: MutableList<DataRow> = mutableListOf(),
    var columns: List<DataColumn> = emptyList(),
    var controlType: String? = null,
    var dataSourceType: EntityModelType? = null,
    var brands: List<String> = emptyList(),
    var groups: List<String> = emptyList(),
    var tags: List<String> = emptyList()
) {
    fun newRow(): DataRow {
        val row = DataRow(
            data = MutableList(columns.size) { "" }
        )
        return row
    }

    fun loadData(formRow: FormRow, orgId: Int) {
/*        val provider = ListFormDataProvider()
        val hasFilter = formRow.filters.any {
            it.filterType == "Query" && it.state == 1
        }

        var data = provider.getData(dataSourceType, hasFilter, orgId, false, null)

        if (hasFilter) {
            data = provider.filter(data, formRow.filters)
        }

        data.forEach { datum ->
            val row = newRow()
            row.primaryData = datum
            row.brand = datum.brandName
            row.group = datum.groupName
            row.tags = datum.tags
            rows.add(row)
        }

        if (dataSourceType != EntityModelType.Product) {
            return
        }

        brands = data.map { it.brandName }.distinct()
        groups = data.map { it.groupName }.distinct()
        tags = data.flatMap { it.tags }.distinct().map { it.lowercase() }*/
    }
}