package com.repzone.network.dto.form

import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class FormEmbeddedPropertyDto (
    val id: Int,
    val state: Int,
    @Serializable(with = InstantSerializer::class)
    val modificationDateUtc: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val recordDateUtc: Instant? = null,

    val formId: Int? = null,

    /**
     * Propertinin adı otomatik verilcek
     */
    val controlName: String? = null,

    /**
     * Kontrol tipi buton
     */
    val controlType: String? = null,

    /**
     * Kontrol ekranda gösterilirken kullanılacak nesnein texti (evet/hatır" 2 >3"
     */
    val controlText: String? = null,

    /**
     * Ekrana eklenecek kontrolün etiketi ... Arabanız varmı?
     */
    val caption: String? = null,

    /**
     * Dönecek değer. Form/anket doldurulduktan sonra mobil cihaz tarafında bu alan doldurularak
     * bu şablonun bilgileri şablonun json yada binary değeri gönderilecek
     */
    val returnValue: String? = null,

    /**
     * İzin verilen boyut KB cinsinden
     */
    val maxMbSize: Int? = null,

    /**
     * İzin verilen dosya sayısı (resim vs)
     */
    val maxFileCount: Int? = null,
)