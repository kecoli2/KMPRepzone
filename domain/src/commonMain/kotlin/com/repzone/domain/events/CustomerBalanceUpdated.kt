package com.repzone.domain.events

import com.repzone.core.util.extensions.now


/**
 * Müşteri bakiyesi güncellendiğinde emit edilir
 *
 * @param customerId Güncellenen müşterinin ID'si
 * @param balance Yeni bakiye değeri
 * @param timestamp Event zamanı (default: şu anki zaman)
 */
data class CustomerBalanceUpdated(val customerId: Long,
                                  val balance: Double,
                                  override val timestamp: Long = now()
): DomainEvent
