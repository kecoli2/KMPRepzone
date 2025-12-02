package com.repzone.domain.transactioncoordinator

import com.repzone.core.model.ResourceUI

data class CompositeOperation(
    var id: Long = 0,
    val operations: List<TableOperation>,
    val description: ResourceUI? = null
)