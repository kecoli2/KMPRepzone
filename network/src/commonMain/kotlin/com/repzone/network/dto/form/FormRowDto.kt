package com.repzone.network.dto.form

import com.repzone.core.enums.EntityModelType
import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class FormRowDto(
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
     * ViewType SingleView, ListView
     */
    val viewType: String? = null,

    /**
     * Ekranda görüntüleme sırası
     */
    val order: Int? = null,

    /**
     * Ekrana eklenecek kontrolün etiketi ... Arabanız varmı?
     */
    val caption: String? = null,

    /**
     * Bir data kaynağı kullanılıyorsa bunun hangi entity olduğu
     */
    @Serializable(with = EntityModelType.Companion.Serializer::class)
    val dataSourceType: EntityModelType? = null,

    /**
     * Database den çekilmeyecek sadece o ankete/forma özel çoklu seçim listeleri oluşturulabilir
     * Form tasarım ekranında bu yapıya izin verilecek. oluşan liste array olarak şablona eklenecek
     */
    val controlData: String? = null,

    /**
     * Databaseden doldurulacaksa {Tabloadı}.{PK}
     */
    val dataId: String? = null,

    /**
     * Databaseden doldurulacaksa {Tabloadı}.{Name}
     */
    val dataValue: String? = null,

    val rowColumns: List<FormRowColumnDto> = emptyList(),
    val filters: List<FormFiltersDto> = emptyList(),

    @kotlinx.serialization.Transient
    val form: FormBaseDto? = null,
)
