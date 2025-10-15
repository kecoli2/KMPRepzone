package com.repzone.sync.transaction

import com.repzone.core.model.ResourceUI

data class CompositeOperation(
    var id: Long = 0,
    val operations: List<TableOperation>,
    val description: ResourceUI? = null
)