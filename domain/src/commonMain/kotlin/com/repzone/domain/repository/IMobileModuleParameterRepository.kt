package com.repzone.domain.repository

import com.repzone.core.model.module.parameters.AttendanceTrackingParameters
import com.repzone.core.model.module.parameters.BusinessIntelligenceParameters
import com.repzone.core.model.module.parameters.CrmOperationsParameters
import com.repzone.core.model.module.parameters.CustomMobileFormsParameters
import com.repzone.core.model.module.parameters.DigitalContentSharingParameters
import com.repzone.core.model.module.parameters.DigitalProductCatalogParameters
import com.repzone.core.model.module.parameters.DispatchesParameters
import com.repzone.core.model.module.parameters.EagleEyeLocationTrackingParameters
import com.repzone.core.model.module.parameters.GamificationParameters
import com.repzone.core.model.module.parameters.GeofenceRouteTrackingParameters
import com.repzone.core.model.module.parameters.InvoiceEInvoiceParameters
import com.repzone.core.model.module.parameters.MessagingChatFeedbackParameters
import com.repzone.core.model.module.parameters.NotificationParameters
import com.repzone.core.model.module.parameters.OrderParameters
import com.repzone.core.model.module.parameters.PaymentCollectionsParameters
import com.repzone.core.model.module.parameters.ProductAllocationParameters
import com.repzone.core.model.module.parameters.QuickAccessParameters
import com.repzone.core.model.module.parameters.ReportsParameters
import com.repzone.core.model.module.parameters.TaskManagmentParameters
import com.repzone.core.model.module.parameters.VisitParameters
import com.repzone.core.model.module.parameters.WorkingHoursParameters
import com.repzone.domain.model.MobileParameterModel

interface IMobileModuleParameterRepository {

    fun getModule(): MobileParameterModel
    fun getOrdersParameters(): OrderParameters?
    fun getInvoiceEInvoceParameters(): InvoiceEInvoiceParameters?
    fun getBusinessIntelligenceParameters(): BusinessIntelligenceParameters?
    fun getDigitalContentSharingParameters(): DigitalContentSharingParameters?
    fun getTaskManagmentParameters(): TaskManagmentParameters?
    fun getEagleEyeLocationTrackingParameters(): EagleEyeLocationTrackingParameters?
    fun getMessagingChatFeedbackParameters(): MessagingChatFeedbackParameters?
    fun getGeofenceRouteTrackingParameters(): GeofenceRouteTrackingParameters?
    fun getCustomMobileFormsParameters(): CustomMobileFormsParameters?
    fun getAttendanceTrackingParameters(): AttendanceTrackingParameters?
    fun getReportsParameters(): ReportsParameters?
    fun getPaymentCollectionsParameters(): PaymentCollectionsParameters?
    fun getDispatchesParameters(): DispatchesParameters?
    fun getCrmOperationsParameters(): CrmOperationsParameters?
    fun getDigitalProductCatalogParameters(): DigitalProductCatalogParameters?
    fun getGamificationParameters(): GamificationParameters?
    fun getQuickAccessParameters(): QuickAccessParameters?
    fun getProductAllocationParameters(): ProductAllocationParameters?
    fun getVisitParameters(): VisitParameters?
    fun getNotificationParameters(): NotificationParameters?
    fun getWorkingHoursParameters(): WorkingHoursParameters?
}