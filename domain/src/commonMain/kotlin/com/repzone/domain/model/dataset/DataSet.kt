package com.repzone.domain.model.dataset

import com.repzone.domain.model.forms.FormBase

data class DataSet(
    var tables: List<DataTable> = emptyList(),
    var isListView: Boolean = false,
    var isWebView: Boolean = false,
    var isCustomForm: Boolean = false,
    var isAppLink: Boolean = false,
    var source: FormBase? = null,
    var table: DataTable? = null
)