package com.repzone.domain.model.forms

import com.repzone.core.enums.EntityModelType
import com.repzone.core.enums.ImageQuality
import com.repzone.core.enums.StateType
import com.repzone.core.enums.UniqueControlIntervalEnum
import com.repzone.core.enums.VideoDuration
import com.repzone.core.enums.VideoQuality
import com.repzone.core.model.base.IBaseModel
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class FormRowColumn(
    override val id: Int,
    @Serializable(with = StateType.Companion.Serializer::class)
    override val state: StateType,
    override val modificationDateUtc: Instant?,
    override val recordDateUtc: Instant?,
    var formRowId: Int = 0,

    /**
     * Propertinin adı otomatik verilcek
      */

    var controlName: String? = null,

    /**
     * Kontrol tipi combobox, text
     */
    var controlType: String? = null,

    /**
     * Zorunlu mu ?
      */
    var mandatory: Boolean = false,

    /**
     * Ekranda görüntüleme sırası
      */
    var order: Int = 0,

    /**
     * Kontrol ekranda gösterilirken kullanılacak nesnenin texti (evet/hatır" 2 >3")
      */
    var controlText: String? = null,

    /**
     * Ekrana eklenecek kontrolün etiketi ... Arabanız varmı?
      */
    var caption: String? = null,

    /**
     * Combo / list vb çoklu seçim nesnelerinde varsayılan değer
      */
    var defaultValue: String? = null,

    /**
     * nesnenin value tipi : integer, string , decimal vb
      */
    var controlValueType: String? = null,

    /**
     * Aralık verilecekse başlangıç değeri
      */
    var dataRangeBegin: String? = null,

    /**
     * Aralık verilecekse başlangıç değeri
     * not: begin/end string olarak verilmiş mobil cihazda ControlValueType type göre bu değeri cast edebilir gerekirse
     */
    var dataRangeEnd: String? = null,

    /**
     * Database den çekilmeyecek sadece o ankete/forma özel çoklu seçim listeleri oluşturulabilir
     * Form tasarım ekranında bu yapıya izin verilecek. oluşan liste array olarak şablona eklenecek
      */
    var controlData: String? = null,

    /**
     * Bir data kaynağı kullanılıyorsa bunun hangi entity olduğu
      */
    var dataSourceType: EntityModelType? = null,

    /**
     * Databaseden doldurulacaksa {Tabloadı}.{PK}
      */
    var dataId: String? = null,

    /**
     * Databaseden doldurulacaksa {Tabloadı}.{Name}
      */
    var dataValue: String? = null,

    /**
     * Parent comboboxun unique Id si
     */
    var parentControlName: String? = null,

    /**
     * Cascade nesneler için bağlı olduğu parent nesnesinin Idi
     */
    var parentDataId: String? = null,

    /**
     * Parent Control deki source Id nin bu controldeki kolon adı
     * Parent = Brand
     */
    var parentMappedId: String? = null,

    /**
     * Dönecek değer. Form/anket doldurulduktan sonra mobil cihaz tarafında bu alan doldurularak
     * bu şablonun bilgileri şablonun json yada binary değeri gönderilecek
     */
    var returnValue: String? = null,

    /**
     * İzin verilen boyut KB cinsinden
      */
    var min: Double? = null,

    /**
     * İzin verilen dosya sayısı (resim vs)
      */
    var max: Double? = null,

    /**
     * nesneye ait boyut/ saniye max bilgileri
      */
    var maxLimit: Double? = null,

    var maxLimitType: String? = null,

    var useOnlyCamera: Boolean = false,

    var saveToAlbum: Boolean = false,

    /**
     * nesne türü fotoğref ise kalitesi ne olacak
      */
    var imageQuality: ImageQuality? = null,

    /**
     * nesne türü video ise kalitesi ne olacak
      */
    var videoQuality: VideoQuality? = null,

    /**
     * nesne türü video ise kaç saniyelik çekim yapılabilir
      */
    var videoDuration: VideoDuration? = null,

    /**
     * PhoneNumber komponenti için SMS Onay
      */
    var smsValidation: Boolean = false,

    /**
     * PhoneNumber için default country code
      */
    var phoneCountryCode: String? = null,

    /**
     * OpenX komponentleri için url
      */
    var fileUrl: String? = null,

    /**
     * özet raporunda gözüksün mü
      */
    var showInSummary: Boolean = false,

    /**
     * ürün ürün grubu marka resimlerini goster?
      */
    var showEntityImages: Boolean = false,

    var decimalPlaces: Int = 0,

    var docUrl: String? = null,

    /**
     * Girilen telefon numarasi kac dijit olacak
     */
    var digits: Int? = null,

    /**
     * Mobil numara kontrolu yapacak mi? Bu numara daha once eklenmis mi
      */
    var uniqueControl: Boolean = false,

    var uniqueControlInterval: UniqueControlIntervalEnum = UniqueControlIntervalEnum.None,

    var formRow: FormRow? = null,

    var view: Any? = null,

    ) : IBaseModel {

    //TODO BURASI STRINGLERLE DEGIL TYPE ROW LAR ILE YONETILECEK
    /*fun getEntryValue(): String {
        return when (controlType) {
            "TextBox", "Number", "Money", "Email" -> {
                // Entry view'den text al
                (view as? Entry)?.text ?: ""
            }
            "ComboBox" -> {
                val picker = view as? Picker
                if (picker != null && picker.selectedIndex >= 0) {
                    picker.items.getOrNull(picker.selectedIndex) ?: ""
                } else {
                    ""
                }
            }
            "CheckBoxList" -> {
                "Not Implemented.GetEntryValue()"
            }
            "YesNo" -> {
                val switch = view as? Switch
                if (switch?.isToggled == true) {
                    AppResources.Yes
                } else {
                    AppResources.No
                }
            }
            else -> {
                "Not Implemented.GetEntryValue()"
            }
        }
    }*/
    override fun getUpdateTime(): Instant? {
        return modificationDateUtc ?: recordDateUtc
    }
}
