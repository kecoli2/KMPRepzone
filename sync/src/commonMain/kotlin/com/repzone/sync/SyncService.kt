package com.repzone.sync

import com.repzone.domain.repository.IOrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyncService(private val orderRepository: IOrderRepository) {
    private val scope = CoroutineScope(Dispatchers.Default)

    fun syncAll(){
        scope.launch {
         // outbox dan padding repolar okunur ve gönderilir sonuc ları buralara yazıla bilir
            val pedding = orderRepository.pending()
            // Network’e push et (network modülü ile)
            // Başarılı olursa DB’de güncelle
        }
    }
}