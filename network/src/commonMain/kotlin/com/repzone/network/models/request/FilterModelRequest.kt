package com.repzone.network.models.request

import com.repzone.network.models.others.FilterModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilterModelRequest(
    @SerialName("Take")
    var take: Int = 0,
    @SerialName("LastModDate")
    var lastModDate: String? = null,
    @SerialName("LastId")
    var lastId: Int = 0,
    @SerialName("FetchOnlyActive")
    var fetchOnlyActive: Boolean = true,
    @SerialName("Keyword")
    var keyword: String? = null,
    @SerialName("FilterModels")
    var filterModels: ArrayList<FilterModel> = arrayListOf()
)
