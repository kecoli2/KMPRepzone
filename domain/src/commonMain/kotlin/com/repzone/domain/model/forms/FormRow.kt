package com.repzone.domain.model.forms

import com.repzone.core.enums.EntityModelType
import com.repzone.core.model.base.IBaseModel
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class FormRow(
    override val id: Int,
    override val state: Int,
    override val modificationDateUtc: Instant?,
    override val recordDateUtc: Instant?,
    var formId: Int = 0,

    /**
     * Propertinin adı otomatik verilcek
      */
    var controlName: String? = null,

    /**
     * ViewType SingleView, ListView
     **/
    var viewType: String? = null,

    /**
     * Ekranda görüntüleme sırası
     */
    var order: Int = 0,

    /**
     * Ekrana eklenecek kontrolün etiketi ... Arabanız varmı?
      */
    var caption: String? = null,

    /**
     * Bir data kaynağı kullanılıyorsa bunun hangi entity olduğu
      */
    var dataSourceType: EntityModelType? = null,

    /**
     * Database den çekilmeyecek sadece o ankete/forma özel çoklu seçim listeleri oluşturulabilir
     * Form tasarım ekranında bu yapıya izin verilecek. oluşan liste array olarak şablona eklenecek
      */
    var controlData: String? = null,

    /**
     * Databaseden doldurulacaksa {Tabloadı}.{PK}
      */
    var dataId: String? = null,

    /**
     * Databaseden doldurulacaksa {Tabloadı}.{Name}
      */
    var dataValue: String? = null,

    var rowColumns: List<FormRowColumn> = emptyList(),
    var filters: List<FormFilters> = emptyList(),

    var form: FormBase? = null,

) : IBaseModel {
    override fun getUpdateTime(): Instant? {
        return modificationDateUtc ?: recordDateUtc
    }
}
