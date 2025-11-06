package com.repzone.domain.model.forms

import com.repzone.core.enums.FormDocumentType
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toInstant
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Form modellerinin temel soyut sınıfı.
 * Tüm form türleri için ortak özellikleri ve yapıyı tanımlar.
 */
@OptIn(ExperimentalTime::class)
interface BaseFormModel{
    /**
     * Dokümanın benzersiz kimliği.
     */
    var documentUniqueId: String?

    /**
     * Form ID'si.
     */
    var formId: Int

    /**
     * Formun şema adı (kodu).
     */
    var schemaName: String?

    /**
     * Formun görünen adı.
     */
    var name: String?

    /**
     * Form doküman tipi (Form, Survey vb.).
     */
    var formDocumentType: FormDocumentType?

    /**
     * Formun versiyon bilgisi.
     */
    var version: String?

    /**
     * İlişkili ziyaret ID'si.
     */
    var visitId: Int

    /**
     * Ziyaretin benzersiz kimliği.
     */
    var visitUniqueId: String?

    /**
     * Tenant (kiracı) ID'si.
     */
    var tenantId: Int

    /**
     * Organizasyon ID'si.
     */
    var organizationId: Int

    /**
     * Müşteri ID'si.
     */
    var customerId: Int

    /**
     * Temsilci ID'si.
     */
    var representativeId: Int

    /**
     * Kaydın oluşturulma tarihi (UTC).
     */
    var recordDateUtc: Instant

    /**
     * Formun benzersiz kimliği.
     */
    var formUniqueId: String?

    /**
     * Konum bilgisi - Boylam.
     */
    var longitude: Double

    /**
     * Konum bilgisi - Enlem.
     */
    var latitude: Double

    /**
     * Satır kontrol listesi.
     */
    var rowControls: List<RowControl>

    /**
     * Gömülü özellik listesi.
     */
    var embeddedProperties: List<ColumnControl>

    /**
     * Harici nesne listesi.
     */
    var externalObjects: List<ColumnControl>
}
