package com.repzone.domain.model.dataset

data class DataRow(
    var data: MutableList<String> = mutableListOf(),
    var primaryData: ListPrimaryData? = null,
    var tags: List<String> = emptyList(),
    var group: String? = null,
    var brand: String? = null
) {
    operator fun get(index: Int): String {
        return data[index]
    }

    operator fun set(index: Int, value: String) {
        data[index] = value
    }
}