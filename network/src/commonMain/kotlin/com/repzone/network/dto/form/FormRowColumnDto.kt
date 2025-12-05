package com.repzone.network.dto.form

import com.repzone.core.enums.EntityModelType
import com.repzone.core.enums.ImageQuality
import com.repzone.core.enums.ImageQualitySerializer
import com.repzone.core.enums.StateType
import com.repzone.core.enums.UniqueControlIntervalEnum
import com.repzone.core.enums.UniqueControlIntervalEnumSerializer
import com.repzone.core.enums.VideoDuration
import com.repzone.core.enums.VideoDurationSerializer
import com.repzone.core.enums.VideoQuality
import com.repzone.core.model.base.IBaseModel
import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class FormRowColumnDto(
    override val id: Int,
    @Serializable(with = StateType.Companion.Serializer::class)
    override val state: StateType,
    @Serializable(with = InstantSerializer::class)
    override val modificationDateUtc: Instant?,
    @Serializable(with = InstantSerializer::class)
    override val recordDateUtc: Instant?,
    var formRowId: Int = 0,
    var controlName: String? = null,
    var controlType: String? = null,
    var mandatory: Boolean = false,
    var order: Int = 0,
    var controlText: String? = null,
    var caption: String? = null,
    var defaultValue: String? = null,
    var controlValueType: String? = null,
    var dataRangeBegin: String? = null,
    var dataRangeEnd: String? = null,
    var controlData: String? = null,
    @Serializable(with = EntityModelType.Companion.Serializer::class)
    var dataSourceType: EntityModelType? = null,
    var dataId: String? = null,
    var dataValue: String? = null,
    var parentControlName: String? = null,
    var parentDataId: String? = null,
    var parentMappedId: String? = null,
    var returnValue: String? = null,
    var min: Double? = null,
    var max: Double? = null,
    var maxLimit: Double? = null,
    var maxLimitType: String? = null,
    var useOnlyCamera: Boolean = false,
    var saveToAlbum: Boolean = false,
    @Serializable(with = ImageQualitySerializer::class)
    var imageQuality: ImageQuality? = null,
    @Serializable(with = VideoQuality.Companion.Serializer::class)
    var videoQuality: VideoQuality? = null,
    @Serializable(with = VideoDurationSerializer::class)
    var videoDuration: VideoDuration? = null,
    var smsValidation: Boolean = false,
    var phoneCountryCode: String? = null,
    var fileUrl: String? = null,
    var showInSummary: Boolean = false,
    var showEntityImages: Boolean = false,
    var decimalPlaces: Int = 0,
    var docUrl: String? = null,
    var digits: Int? = null,
    var uniqueControl: Boolean = false,
    @Serializable(with = UniqueControlIntervalEnumSerializer::class)
    var uniqueControlInterval: UniqueControlIntervalEnum = UniqueControlIntervalEnum.None,
    @Transient
    var formRow: FormRowDto? = null,
    @Transient
    var view: Any? = null,
): IBaseModel {
    override fun getUpdateTime(): Instant? {
        return modificationDateUtc ?: recordDateUtc
    }
}
