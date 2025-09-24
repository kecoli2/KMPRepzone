package com.repzone.sync.job
/*

import com.repzone.core.util.json
import com.repzone.network.api.IOrderApi
import com.repzone.network.dto.OrderDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class OrderSyncJob(
    private val outbox: OrderOutboxService,
    private val api: IOrderApi,
    private val scope: CoroutineScope
) {
    private var running: Job? = null

    fun start(periodMs: Long = 15_000, batchSize: Int = 20) {
        if (running?.isActive == true) return
        running = scope.launch {
            while (isActive) {
                try { processBatch(batchSize) } catch (_: Throwable) {}
                delay(periodMs)
            }
        }
    }

    suspend fun processBatch(batchSize: Int) {
        val items: List<OrderOutBoxEntity> = outbox.takeBatch(batchSize)
        items.forEach { row: OrderOutBoxEntity ->
            val dto = runCatching { json.decodeFromString<OrderDto>(row.payload) }.getOrNull()
            if (dto == null) {
                outbox.failWithBackoff(row.id, row.attemptCount.toInt() + 1, "Invalid payload")
                return@forEach
            }
            val r = api.push(dto)
            if (r.isSuccess){
                outbox.success(row.id)
            }else{
                outbox.failWithBackoff(row.id, row.attemptCount.toInt() + 1, r.exceptionOrNull()?.message)
            }
        }
    }

    fun stop() { running?.cancel(); running = null }
}*/
