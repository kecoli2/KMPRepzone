package com.repzone.data.mapper

import com.repzone.core.util.extensions.jsonToModel
import com.repzone.domain.model.forms.FormAttribute
import com.repzone.domain.model.forms.FormAttributeObject
import com.repzone.domain.model.forms.FormBase
import com.repzone.domain.model.forms.FormEmbeddedProperty
import com.repzone.domain.model.forms.FormFilters
import com.repzone.domain.model.forms.FormRow
import com.repzone.domain.model.forms.FormRowColumn
import com.repzone.network.dto.form.FormBaseDto
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class FormBaseEntityDbMapper {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    fun toJsonFromDomain(jsonData: String): FormBase? {
        val formBaseDto = jsonData.jsonToModel<FormBaseDto>()

        if(formBaseDto != null){
            return FormBase(
                id = formBaseDto.id,
                state = formBaseDto.state,
                modificationDateUtc = formBaseDto.modificationDateUtc,
                recordDateUtc = formBaseDto.recordDateUtc,
                schemaName = formBaseDto.schemaName,
                name = formBaseDto.name,
                version = formBaseDto.version,
                formDocumentType = formBaseDto.formDocumentType,
                tenantId = formBaseDto.tenantId,
                organizationId = formBaseDto.organizationId,
                organizationIds = formBaseDto.organizationIds,
                longitude = formBaseDto.longitude,
                latitude = formBaseDto.latitude,
                tags = formBaseDto.tags,
                representativeTags = formBaseDto.representativeTags,
                customerTags = formBaseDto.customerTags,
                targetType = formBaseDto.targetType,
                targetCount = formBaseDto.targetCount,
                embeddedProperties = formBaseDto.embeddedProperties.map {
                    FormEmbeddedProperty(
                        id = it.id,
                        state = it.state,
                        modificationDateUtc = it.modificationDateUtc,
                        recordDateUtc = it.recordDateUtc,
                        formId = it.formId,
                        controlName = it.controlName,
                        controlType = it.controlType,
                        controlText = it.controlText,
                        caption = it.caption,
                        returnValue = it.returnValue,
                        maxMbSize = it.maxMbSize,
                        maxFileCount = it.maxFileCount,
                    )
                },
                formAttributes = formBaseDto.formAttributes.map {
                    FormAttribute(
                        id = it.id,
                        key = it.key,
                        value = it.value,
                        alias = it.alias,
                        description = it.description,
                        valueType = it.valueType,
                        state = it.state
                    )
                },
                formRows = formBaseDto.formRows.map {
                    FormRow(
                        id = it.id,
                        state = it.state,
                        modificationDateUtc = it.modificationDateUtc,
                        recordDateUtc = it.recordDateUtc,
                        formId = it.formId,
                        controlName = it.controlName,
                        viewType = it.viewType,
                        order = it.order,
                        caption = it.caption,
                        dataSourceType = it.dataSourceType,
                        controlData = it.controlData,
                        dataId = it.dataId,
                        dataValue = it.dataValue,
                        rowColumns = it.rowColumns.map { it ->
                            FormRowColumn(
                                id = it.id,
                                state = it.state,
                                modificationDateUtc = it.modificationDateUtc,
                                recordDateUtc = it.recordDateUtc,
                                formRowId = it.formRowId,
                                controlName = it.controlName,
                                controlType = it.controlType,
                                mandatory = it.mandatory,
                                order = it.order,
                                controlText = it.controlText,
                                caption = it.caption,
                                defaultValue = it.defaultValue,
                                controlValueType = it.controlValueType,
                                dataRangeBegin = it.dataRangeBegin,
                                dataRangeEnd = it.dataRangeEnd,
                                controlData = it.controlData,
                                dataSourceType = it.dataSourceType,
                                dataId = it.dataId,
                                dataValue = it.dataValue,
                                parentControlName = it.parentControlName,
                                parentDataId = it.parentDataId,
                                parentMappedId = it.parentMappedId,
                                returnValue = it.returnValue,
                                min = it.min,
                                max = it.max,
                                maxLimit = it.maxLimit,
                                maxLimitType = it.maxLimitType,
                                useOnlyCamera = it.useOnlyCamera,
                                saveToAlbum = it.saveToAlbum,
                                imageQuality = it.imageQuality,
                                videoQuality = it.videoQuality,
                                videoDuration = it.videoDuration,
                                smsValidation = it.smsValidation,
                                phoneCountryCode = it.phoneCountryCode,
                                fileUrl = it.fileUrl,
                                showInSummary = it.showInSummary,
                                showEntityImages = it.showEntityImages,
                                decimalPlaces = it.decimalPlaces,
                                docUrl = it.docUrl,
                                digits = it.digits,
                                uniqueControl = it.uniqueControl,
                                uniqueControlInterval = it.uniqueControlInterval,
                                formRow = it.formRow?.let { it ->
                                    FormRow(
                                        id = it.id,
                                        state = it.state,
                                        modificationDateUtc = it.modificationDateUtc,
                                        recordDateUtc = it.recordDateUtc,
                                        formId = it.formId,
                                        controlName = it.controlName,
                                        viewType = it.viewType,
                                        order = it.order,
                                        caption = it.caption,
                                        dataSourceType = it.dataSourceType,
                                        controlData = it.controlData,
                                        dataId = it.dataId,
                                        dataValue = it.dataValue,
                                        rowColumns = it.rowColumns.map { it ->
                                            FormRowColumn(
                                                id = it.id,
                                                state = it.state,
                                                modificationDateUtc = it.modificationDateUtc,
                                                recordDateUtc = it.recordDateUtc,
                                                formRowId = it.formRowId,
                                                controlName = it.controlName,
                                                controlType = it.controlType,
                                                mandatory = it.mandatory,
                                                order = it.order,
                                                controlText = it.controlText,
                                                caption = it.caption,
                                                defaultValue = it.defaultValue,
                                                controlValueType = it.controlValueType,
                                                dataRangeBegin = it.dataRangeBegin,
                                                dataRangeEnd = it.dataRangeEnd,
                                                controlData = it.controlData,
                                                dataSourceType = it.dataSourceType,
                                                dataId = it.dataId,
                                                dataValue = it.dataValue,
                                                parentControlName = it.parentControlName,
                                                parentDataId = it.parentDataId,
                                                parentMappedId = it.parentMappedId,
                                                returnValue = it.returnValue,
                                                min = it.min,
                                                max = it.max,
                                                maxLimit = it.maxLimit,
                                                maxLimitType = it.maxLimitType,
                                                useOnlyCamera = it.useOnlyCamera,
                                                saveToAlbum = it.saveToAlbum,
                                                imageQuality = it.imageQuality,
                                                videoQuality = it.videoQuality,
                                                videoDuration = it.videoDuration,
                                                smsValidation = it.smsValidation,
                                                phoneCountryCode = it.phoneCountryCode,
                                                fileUrl = it.fileUrl,
                                                showInSummary = it.showInSummary,
                                                showEntityImages = it.showEntityImages,
                                                decimalPlaces = it.decimalPlaces,
                                                docUrl = it.docUrl,
                                                digits = it.digits,
                                                uniqueControl = it.uniqueControl,
                                                uniqueControlInterval = it.uniqueControlInterval,
                                                formRow = null,
                                                view = it.view
                                            )
                                        },
                                        filters = it.filters.map { it ->
                                            FormFilters(
                                                id = it.id,
                                                state = it.state,
                                                modificationDateUtc = it.modificationDateUtc,
                                                recordDateUtc = it.recordDateUtc,
                                                attribute = it.attribute?.let { it->
                                                    FormAttributeObject(
                                                        key = it.key,
                                                        value = it.value,
                                                        alias = it.alias,
                                                        description = it.description,
                                                        valueType = it.valueType
                                                    )
                                                },
                                                filterType = it.filterType,
                                                source = it.source,
                                                formRowId = it.formRowId,
                                                operator = it.operator,
                                                formRow = null
                                            )
                                        },
                                        form = it.form?.let { it ->
                                            FormBase(
                                                id = it.id,
                                                state = it.state,
                                                modificationDateUtc = it.modificationDateUtc,
                                                recordDateUtc = it.recordDateUtc,
                                                schemaName = it.schemaName,
                                                name = it.name,
                                                version = it.version,
                                                formDocumentType = it.formDocumentType,
                                                tenantId = it.tenantId,
                                                organizationId = it.organizationId,
                                                organizationIds = it.organizationIds,
                                                longitude = it.longitude,
                                                latitude = it.latitude,
                                                tags = it.tags,
                                                representativeTags = it.representativeTags,
                                                customerTags = it.customerTags,
                                                targetType = it.targetType,
                                                targetCount = it.targetCount,
                                                embeddedProperties = it.embeddedProperties.map { it ->
                                                    FormEmbeddedProperty(
                                                        id = it.id,
                                                        state = it.state,
                                                        modificationDateUtc = it.modificationDateUtc,
                                                        recordDateUtc = it.recordDateUtc,
                                                        formId = it.formId,
                                                        controlName = it.controlName,
                                                        controlType = it.controlType,
                                                        controlText = it.controlText,
                                                        caption = it.caption,
                                                        returnValue = it.returnValue,
                                                        maxMbSize = it.maxMbSize,
                                                        maxFileCount = it.maxFileCount
                                                    )
                                                },
                                                formAttributes = it.formAttributes.map { it ->
                                                    FormAttribute(
                                                        id = it.id,
                                                        key = it.key,
                                                        value = it.value,
                                                        alias = it.alias,
                                                        description = it.description,
                                                    )
                                                },
                                                formRows = it.formRows.map {
                                                    FormRow(
                                                        id = it.id,
                                                        state = it.state,
                                                        modificationDateUtc = it.modificationDateUtc,
                                                        recordDateUtc = it.recordDateUtc,
                                                        formId = it.formId,
                                                        controlName = it.controlName,
                                                        viewType = it.viewType,
                                                        order = it.order,
                                                        caption = it.caption,
                                                        dataSourceType = it.dataSourceType,
                                                        controlData = it.controlData,
                                                        dataId = it.dataId,
                                                        dataValue = it.dataValue,
                                                        rowColumns = emptyList(),
                                                        filters = emptyList(),
                                                        form = null
                                                    )
                                                },
                                                iconIndex = it.iconIndex,
                                                beginDate = it.beginDate,
                                                endDate = it.endDate,
                                                isMandatory = it.isMandatory,
                                                interval = it.interval,
                                                createdByTask = it.createdByTask,
                                                displayOrder = it.displayOrder,
                                                visibleOption = it.visibleOption,
                                                operationType = it.operationType,
                                                customerCategories = it.customerCategories,
                                                customerChannels = it.customerChannels,
                                                customerClasses = it.customerClasses,
                                                customerSegments = it.customerSegments,
                                                customerGroups = it.customerGroups
                                            )
                                        }
                                    )
                                },
                                view = it.view
                            )
                        },
                        filters = it.filters.map { it ->
                            FormFilters(
                                id = it.id,
                                state = it.state,
                                modificationDateUtc = it.modificationDateUtc,
                                recordDateUtc = it.recordDateUtc,
                                attribute = it.attribute?.let { it ->
                                    FormAttributeObject(
                                        key = it.key,
                                        value = it.value,
                                        alias = it.alias,
                                        description = it.description,
                                        valueType = it.valueType
                                    )
                                },
                                filterType = it.filterType,
                                source = it.source,
                                formRowId = it.formRowId,
                                operator = it.operator,
                                formRow = null
                            )
                        },
                        form = null
                    )
                },
                iconIndex = formBaseDto.iconIndex,
                beginDate = formBaseDto.beginDate,
                endDate = formBaseDto.endDate,
                isMandatory = formBaseDto.isMandatory,
                interval = formBaseDto.interval,
                createdByTask = formBaseDto.createdByTask,
                displayOrder = formBaseDto.displayOrder,
                visibleOption = formBaseDto.visibleOption,
                operationType = formBaseDto.operationType,
                customerCategories = formBaseDto.customerCategories,
                customerChannels = formBaseDto.customerChannels ,
                customerClasses = formBaseDto.customerClasses,
                customerSegments = formBaseDto.customerSegments,
                customerGroups = formBaseDto.customerGroups
            )
        }
        return null
    }

    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}