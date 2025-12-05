package com.repzone.domain.model.forms

import com.repzone.core.enums.StateType
import com.repzone.core.model.base.IBaseModel
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class FormEmbeddedProperty(
    override val id: Int,
    @Serializable(with = StateType.Companion.Serializer::class)
    override val state: StateType,
    override val modificationDateUtc: Instant? = null,
    override val recordDateUtc: Instant? = null,

    var formId: Int = 0,
    // Propertinin adı otomatik verilcek
    var controlName: String? = null,
    // Kontrol tipi buton
    var controlType: String? = null,
    // Kontrol ekranda gösterilirken kullanılacak nesnenin texti (evet/hatır" 2 >3")
    var controlText: String? = null,
    // Ekrana eklenecek kontrolün etiketi ... Arabanız varmı?
    var caption: String? = null,
    // Dönecek değer. Form/anket doldurulduktan sonra mobil cihaz tarafında bu alan
    // doldurularak bu şablonun bilgileri şablonun json yada binary değeri gönderilecek
    var returnValue: String? = null,
    // İzin verilen boyut KB cinsinden
    var maxMbSize: Int = 0,
    // İzin verilen dosya sayısı (resim vs)
    var maxFileCount: Int = 0,

) : IBaseModel {
    override fun getUpdateTime(): Instant? {
        return modificationDateUtc ?: recordDateUtc
    }
}
