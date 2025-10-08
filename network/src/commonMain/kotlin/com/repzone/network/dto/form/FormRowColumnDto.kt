package com.repzone.network.dto.form

import com.repzone.core.enums.EntityModelType
import com.repzone.core.enums.ImageQuality
import com.repzone.core.enums.UniqueControlIntervalEnum
import com.repzone.core.enums.VideoDuration
import com.repzone.core.enums.VideoQuality
import kotlinx.serialization.Serializable

@Serializable
data class FormRowColumnDto(
    var formRowId: Int = 0,

    /**
     * Propertinin adı otomatik verilcek
     */
    val controlName: String? = null,

    /**
     * Kontrol tipi combobox, text
     */
    val controlType: String? = null,

    /**
     * Zorunlu mu ?
     */
    val mandatory: Boolean? = null,

    /**
     * Ekranda görüntüleme sırası
     */
    val order: Int? = null,

    /**
     * Kontrol ekranda gösterilirken kullanılacak nesnein texti (evet/hatır" 2 >3"
     */
    val controlText: String? = null,

    /**
     * Ekrana eklenecek kontrolün etiketi ... Arabanız varmı?
     */
    val caption: String? = null,

    /**
     * Combo / list vb çoklu seçim nesnelerinde varsayılan değer
     */
    val defaultValue: String? = null,

    /**
     * nesnenin value tipi : integer, string , decimal vb
     */
    val controlValueType: String? = null,

    /**
     * Aralık verilecekse başlangıç değeri
     */
    val dataRangeBegin: String? = null,

    /**
     * Aralık verilecekse başlangıç değeri
     * not: begin/end string olarak verilmiş mobil cihazda ControlValueType type göre bu değeri cast edebilir gerekirse
     */
    val dataRangeEnd: String? = null,

    /**
     * Database den çekilmeyecek sadece o ankete/forma özel çoklu seçim listeleri oluşturulabilir
     * Form tasarım ekranında bu yapıya izin verilecek. oluşan liste array olarak şablona eklenecek
     */
    val controlData: String? = null,

    /**
     * Bir data kaynağı kullanılıyorsa bunun hangi entity olduğu
     */
    @Serializable(with = EntityModelType.Companion.Serializer::class)
    val dataSourceType: EntityModelType? = null,

    /**
     * Databaseden doldurulacaksa {Tabloadı}.{PK}
     */
    val dataId: String? = null,

    /**
     * Databaseden doldurulacaksa {Tabloadı}.{Name}
     */
    val dataValue: String? = null,

    /**
     * Parent comboboxun unique Id si
     */
    val parentControlName: String? = null,

    /**
     * Cascade nesneler için bağlı olduğu parent nesnesinin Idi
     */
    val parentDataId: String? = null,

    /**
     * Parent Control deki source Id nin bu controldeki kolon adı
     * Parent = Brand
     */
    val parentMappedId: String? = null,

    /**
     * Dönecek değer. Form/anket doldurulduktan sonra mobil cihaz tarafında bu alan doldurularak
     * bu şablonun bilgileri şablonun json yada binary değeri gönderilecek
     */
    val returnValue: String? = null,

    /**
     * İzin verilen boyut KB cinsinden
     */
    val min: Double? = null,

    /**
     * İzin verilen dosya sayısı (resim vs)
     */
    val max: Double? = null,

    /**
     * nesneye ait boyut/ saniye max bilgileri
     */
    val maxLimit: Double? = null,

    val maxLimitType: String? = null,

    val useOnlyCamera: Boolean? = null,

    val saveToAlbum: Boolean? = null,

    // nesne türü fotoğref ise kalitesi ne olacak
    val imageQuality: ImageQuality? = null,

    // nesne türü video ise kalitesi ne olacak
    @Serializable(with = VideoQuality.Companion.Serializer::class)
    val videoQuality: VideoQuality? = null,

    // nesne türü video ise kaç saniyelik çekim yapılabilir
    val videoDuration: VideoDuration? = null,

    // PhoneNumber komponenti için SMS Onay
    val smsValidation: Boolean? = null,

    // PhoneNumber için default country code
    val phoneCountryCode: String? = null,

    // OpenX komponentleri için url
    val fileUrl: String? = null,

    // özet raporunda gözüksün mü
    val showInSummary: Boolean? = null,

    // ürün ürün grubu marka resimlerini goster?
    val showEntityImages: Boolean? = null,

    val decimalPlaces: Int? = null,

    val docUrl: String? = null,

    // Girilen telefon numarasi kac dijit olacak
    val digits: Int? = null,

    // Mobil numara kontrolu yapacak mi? Bu numara daha once eklenmis mi
    val uniqueControl: Boolean? = null,

    val uniqueControlInterval: UniqueControlIntervalEnum? = null
)
