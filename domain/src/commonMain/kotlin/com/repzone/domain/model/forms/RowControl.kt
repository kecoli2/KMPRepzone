package com.repzone.domain.model.forms

data class RowControl(
    var id: Int = 0,
    var name: String? = null,
    var viewType: String? = null,
    var caption: String? = null,
    var dataId: String? = null,
    var dataValue: String? = null,
    var columnControls: List<ColumnControl> = emptyList()
)
