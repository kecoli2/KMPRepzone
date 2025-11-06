package com.repzone.domain.model.dataset

import com.repzone.core.enums.EntityModelType
import com.repzone.core.enums.ImageQuality
import com.repzone.core.enums.UniqueControlIntervalEnum
import com.repzone.core.enums.VideoDuration
import com.repzone.core.enums.VideoQuality
import com.repzone.domain.model.forms.FormFilters
import com.repzone.domain.model.forms.FormRowColumn

data class DataColumn(
    var formRowColumnId: Int = 0,
    var source: FormRowColumn? = null,
    /*var dataRule: DataRule? = null,*/
    var index: Int = 0,
    var comboPilot: List<ControlDataItem> = emptyList(),
    var dataType: DataType = DataType.Unidentified,
    var entryType: EntryType = EntryType.Unidentified,
    var caption: String? = null,
    var rules: List<IDataRule> = emptyList(),
    var dataSourceType: EntityModelType? = null,
/*    var dataProvider: IListDataProvider? = null,
    var visualDataProvider: IVisualListDataProvider? = null,*/
    var galeryLimit: GaleryLimit? = null,
    var imageQuality: ImageQuality? = null,
    var videoQuality: VideoQuality? = null,
    var videoDuration: VideoDuration? = null,
    var smsValidation: Boolean = false,
    var phoneCountryCode: String? = null,
    var fileUrl: String? = null,
    var max: Double? = null,
    var min: Double? = null,
    var showInSummary: Boolean = false,
    var showEntityImages: Boolean = false,
    var docUrl: String? = null,
    var digits: Int? = null,
    var uniqueControl: Boolean = false,
    var uniqueControlInterval: UniqueControlIntervalEnum = UniqueControlIntervalEnum.None,
    var defaultValue: String? = null,
    var filters: List<FormFilters> = emptyList()
) {
    fun getReturnValue(value: String?): String? {
        if (value.isNullOrEmpty()) {
            return value
        }
        if (comboPilot.isEmpty()) {
            return value
        }

        val index = value.toIntOrNull() ?: return value
        return comboPilot.getOrNull(index)?.value
    }
}

enum class EntryType {
    Unidentified,
    Entry,
    TextArea,
    Time,
    Date,
    Swich,
    ComboBox,
    CheckBoxList,
    Photo,
    Video,
    BrandGroupProduct,
    GroupBrandProduct,
    BrandProduct,
    GroupProduct,
    Signature,
    PageBreak,
    Label,
    Number,
    Money,
    OpenUrl,
    OpenApp,
    OpenFile,
    PhoneNumber,
    Gallery,
    Stock,
    Price,
    ShelfShare,
    Facing,
    EmbeddedImage,
    Barcode
}

enum class DataType {
    Unidentified,
    Number,
    Text
}