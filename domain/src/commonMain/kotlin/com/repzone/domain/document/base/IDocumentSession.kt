package com.repzone.domain.document.base

import com.repzone.domain.common.Result
import com.repzone.domain.document.model.DocumentType
import com.repzone.domain.model.CustomerItemModel

interface IDocumentSession {
    /**
     * Yeni bir belge session'ı başlatır
     * Eğer aktif session varsa, mevcut olanı döner
     *
     * @param type Belge tipi (ORDER, INVOICE, WAYBILL)
     * @param customerId Müşteri ID'si
     * @param documentId Belge ID'si
     * @param organizationId Organizasyon ID'si
     * @return Oluşturulan veya mevcut DocumentManager instance'ı
     */
    suspend fun start(type: DocumentType, customerId: Long, documentId: Long): Result<IDocumentManager>

    /**
     * Mevcut aktif session'ı döner
     *
     * @return Aktif DocumentManager
     * @throws IllegalStateException session aktif değilse
     */
    fun current(): IDocumentManager

    /**
     * Session'ın aktif olup olmadığını kontrol eder
     *
     * @return true ise aktif session var
     */
    fun isActive(): Boolean

    /**
     * Aktif session'ı temizler
     * Belge tamamlandığında veya iptal edildiğinde çağrılmalı
     */
    fun clear()
}