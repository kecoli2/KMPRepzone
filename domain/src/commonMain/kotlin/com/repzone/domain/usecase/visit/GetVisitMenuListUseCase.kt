package com.repzone.domain.usecase.visit

import com.repzone.core.enums.DocumentTypeGroup
import com.repzone.core.enums.OnOf
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toInstant
import com.repzone.domain.common.DomainException
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.common.Result
import com.repzone.domain.model.RouteInformationModel
import com.repzone.domain.model.VisitInformation
import com.repzone.domain.repository.IDocumentMapRepository
import com.repzone.domain.repository.IDynamicFormRepository
import com.repzone.domain.repository.IVisitRepository
import com.repzone.domain.util.enums.ActionButtonType
import com.repzone.domain.util.models.VisitButtonItem
import com.repzone.domain.util.models.VisitActionItem
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class GetVisitMenuListUseCase(private val iModuleParameters: IMobileModuleParameterRepository,
                              private val iUserSession: IUserSession,
                              private val iDocumentMapreRepository: IDocumentMapRepository,
                              private var iDynamicFormRepository: IDynamicFormRepository,
                              private val iVisitRepository: IVisitRepository) {
    //region Field
    private var actionMenuList: ArrayList<VisitActionItem> = arrayListOf()
    private var actionButtonList: ArrayList<VisitButtonItem> = arrayListOf()
    //endregion

    //region Properties
    //endregion

    //region Constructor
    suspend operator fun invoke(customer: CustomerItemModel, visitInformation: VisitInformation?, routeInformation: RouteInformationModel): Result<Pair<List<VisitActionItem>, List<VisitButtonItem>>> {
        return try{
            prepareActionButtonList(visitInformation, customer)
            prepareActionMenuItemList(customer, visitInformation, routeInformation)
            Result.Success(Pair(actionMenuList, actionButtonList))
        }catch (ex: Exception){
            Result.Error(DomainException.UnknownException(cause = ex))
        }
    }
    //endregion

    //region Public Method
    //endregion

    //region Private Method
    private fun prepareActionButtonList(visitInformation: VisitInformation?, customer: CustomerItemModel) {
        actionButtonList.clear()
        if(visitInformation == null){
            actionButtonList.add(
                VisitButtonItem(
                    actionType = ActionButtonType.VISITING_START,
                )
            )
        }else{
            if (visitInformation.appointmentId == customer.appointmentId){
                VisitButtonItem(
                    actionType = ActionButtonType.VISITING_END,
                )
            }else{
                VisitButtonItem(
                    actionType = ActionButtonType.VISITING_START,
                )
            }
        }

        if(iModuleParameters.getModule().drive){
            actionButtonList.add(
                VisitButtonItem(
                    actionType = ActionButtonType.DRIVE,
                )
            )
        }
        if(iModuleParameters.getModule().qlikReport){
            actionButtonList.add(
                VisitButtonItem(
                    actionType = ActionButtonType.REPORT,
                )
            )
        }

        if((iModuleParameters.getOrdersParameters()?.isActive == true && iModuleParameters.getOrdersParameters()?.allowDraft == true) &&
            (iModuleParameters.getCustomMobileFormsParameters()?.isActive == true && iModuleParameters.getCustomMobileFormsParameters()?.allowDraft == true)){
            actionButtonList.add(
                VisitButtonItem(
                    actionType = ActionButtonType.ORDER_LOG,
                )
            )
        }

        iModuleParameters.getGeofenceRouteTrackingParameters()?.let {
           //TODO() MAP off olur ise listeden kaldır
            if (it.isActive && it.navigation == OnOf.ON) {
                actionButtonList.add(
                    VisitButtonItem(
                        actionType = ActionButtonType.MAP,
                    )
                )
            }

            // CUSTOMER NOTE
            if(it.isActive && (it.customerNoteEntry == OnOf.ON || it.visitNoteEntry == OnOf.ON)){
                actionButtonList.add(
                    VisitButtonItem(
                        actionType = ActionButtonType.NOTES,
                    )
                )
            }

        }
    }

    @OptIn(ExperimentalTime::class)
    private suspend fun prepareActionMenuItemList(customer: CustomerItemModel, activeVisit: VisitInformation?, routeInformation: RouteInformationModel) {
        actionMenuList.clear()

        //TODO BURASINA BAKILACAK ACTIVITE VISIT
       /* var activityFormTags = new List<string>();
        if (ActiveVisitActivityId > 0)
        {
            var activeVisitActivity = visitService.GetActivityModel(ActiveVisitActivityId);

            if (string.IsNullOrEmpty(activeVisitActivity.FormTags) == false)
            {
                activityFormTags = activeVisitActivity.FormTags.Split(',').ToList();
            }
        }*/

        val realCustomerOrganizationId = routeInformation.customerOrganizationId?.toInt()!!
        val usedCustomerOrganizationId = iUserSession.decideWhichOrgIdToBeUsed(routeInformation.customerOrganizationId.toInt())
        val documents = iDocumentMapreRepository.getAll(usedCustomerOrganizationId)
        val fulfillmentOrderDocuments = documents.filter {
            it.isFulfillment && it.documentTypeGroup == DocumentTypeGroup.ORDER && it.isElectronicDocument == (iUserSession.getActiveSession()!!.identity!!.doNotCheckEDocumentWhileSave != 1 && routeInformation.isECustomer)
        }

        val orderDocuments = documents.filter {
            !it.isFulfillment && it.documentTypeGroup == DocumentTypeGroup.ORDER && it.isElectronicDocument == (iUserSession.getActiveSession()!!.identity!!.doNotCheckEDocumentWhileSave != 1 && routeInformation.isECustomer)
        }

        val invoiceDocuments = documents.filter {
            !it.isFulfillment && it.documentTypeGroup == DocumentTypeGroup.INVOICE && it.isElectronicDocument == (iUserSession.getActiveSession()!!.identity!!.doNotCheckEDocumentWhileSave != 1 && routeInformation.isECustomer)
        }

        val dispatchDocuments = documents.filter {
            !it.isFulfillment && it.documentTypeGroup == DocumentTypeGroup.DISPATCH && it.isElectronicDocument == (iUserSession.getActiveSession()!!.identity!!.doNotCheckEDocumentWhileSave != 1 && routeInformation.isECustomer)
        }

        val paymentDocuments = documents.filter {
            !it.isFulfillment && it.documentTypeGroup == DocumentTypeGroup.COLLECTION
        }

        if(fulfillmentOrderDocuments.isNotEmpty() && iModuleParameters.getModule().order){

        }

        if(orderDocuments.isNotEmpty() && iModuleParameters.getModule().order){
            orderDocuments.forEach { it ->
                actionMenuList.add(
                    VisitActionItem(
                        name = it.name,
                        description = it.description,
                        documentType = DocumentTypeGroup.ORDER,
                        isFulfillment = it.isFulfillment
                    )
                )
            }
        }

        if(invoiceDocuments.isNotEmpty() && iModuleParameters.getModule().invoice){
            invoiceDocuments.forEach { it ->
                actionMenuList.add(
                    VisitActionItem(
                        name = it.name,
                        description = it.description,
                        documentType = DocumentTypeGroup.INVOICE,
                        isFulfillment = it.isFulfillment
                    )
                )
            }
        }

        if(dispatchDocuments.isNotEmpty() && iModuleParameters.getModule().dispatch){
            dispatchDocuments.forEach { it ->
                actionMenuList.add(
                    VisitActionItem(
                        name = it.name,
                        description = it.description,
                        documentType = DocumentTypeGroup.DISPATCH,
                        isFulfillment = it.isFulfillment
                    )
                )
            }
        }

        if(paymentDocuments.isNotEmpty() && iModuleParameters.getModule().payment){
            dispatchDocuments.forEach { it ->
                actionMenuList.add(
                    VisitActionItem(
                        name = it.name,
                        description = it.description,
                        documentType = DocumentTypeGroup.COLLECTION,
                        isFulfillment = it.isFulfillment
                    )
                )
            }
        }

        if(iModuleParameters.getModule().form){
            val nowDateTime = now().toInstant()
            val customerOrganizationList = ArrayList<String>();
            customerOrganizationList.add(usedCustomerOrganizationId.toString())

            val list = iDynamicFormRepository.getForms().map { form ->
                val formMaster = iDynamicFormRepository.getFormBase(form.data)!!
                VisitActionItem(
                    smallIcon = formMaster.iconIndex.toString(),
                    name = form.formName,
                    description = form.description,
                    serialized = form.data,
                    displayOrder = formMaster.displayOrder,
                    isMandatory = formMaster.isMandatory,
                    interval = formMaster.interval,
                    beginDate = formMaster.beginDate ?: Instant.DISTANT_PAST,
                    customerTags = formMaster.customerTags?.split(",")?.toMutableList() as? ArrayList<String> ?: arrayListOf(),
                    representTags = formMaster.representativeTags?.split(",")?.toMutableList() as? ArrayList<String> ?: arrayListOf(),
                    organizationIds = formMaster.organizationIds?.split(",")?.toMutableList() as? ArrayList<String> ?: arrayListOf(),
                    formTags = formMaster.tags?.split(",")?.toMutableList() as? ArrayList<String> ?: arrayListOf(),
                    customerCategories = formMaster.customerCategories.toMutableList() as? ArrayList<Int> ?: arrayListOf(),
                    customerClasses = formMaster.customerClasses.toMutableList() as? ArrayList<Int> ?: arrayListOf(),
                    customerChannels = formMaster.customerChannels.toMutableList() as? ArrayList<Int> ?: arrayListOf(),
                    customerSegments = formMaster.customerSegments.toMutableList() as? ArrayList<Int> ?: arrayListOf(),
                    customerGroups = formMaster.customerGroups.toMutableList() as? ArrayList<Int> ?: arrayListOf(),
                    documentType = DocumentTypeGroup.FORM,
                )

            }

            val filteredForms = list
                .filter { form ->
                    form.hasValidRepresentTags(iUserSession.getActiveSession()!!.identity!!.getTags()) &&
                            form.hasValidCustomerTags(customer.tagRaw) &&
                            form.hasValidFormTags() &&
                            form.hasValidOrganizationIds(customerOrganizationList) &&
                            form.hasValidCustomerGroups(routeInformation) &&
                            form.hasValidCustomerSegments(customer, usedCustomerOrganizationId, realCustomerOrganizationId) &&
                            form.hasValidCustomerClasses(customer, usedCustomerOrganizationId, realCustomerOrganizationId) &&
                            form.hasValidCustomerChannels(customer, usedCustomerOrganizationId, realCustomerOrganizationId) &&
                            form.hasValidCustomerCategories(customer, usedCustomerOrganizationId, realCustomerOrganizationId)
                }
                .onEach { form ->
                    form.copy(
                      hasDone = activeVisit?.let {
                        iVisitRepository.hasAnyForm(it.guid ?: "", routeInformation.appointmentId, form.name ?: "")
                    } ?: false)
                }

            filteredForms.toMutableList().sortByDescending { it.displayOrder }
            actionMenuList.addAll(filteredForms)
        }
    }
    //endregion

    //region Private Extensions
    private suspend fun VisitActionItem.hasValidRepresentTags(repTags: List<String>): Boolean {
        if (representTags.isEmpty()) return true
        return repTags.any { tag ->
            representTags.any { it.equals(tag.trim(), ignoreCase = true) }
        }
    }

    private suspend fun VisitActionItem.hasValidCustomerTags(customerTags: List<String>): Boolean {
        if (this.customerTags.isEmpty()) return true
        return customerTags.any { tag ->
            this.customerTags.any { it.equals(tag.trim(), ignoreCase = true) }
        }
    }

    private suspend fun VisitActionItem.hasValidFormTags(): Boolean {
        if (this.formTags.isEmpty()) return true
        if (this.formTags.isEmpty()) return true
        return this.formTags.any { tag ->
            formTags.any { it.equals(tag.trim(), ignoreCase = true) }
        }
    }

    private suspend fun VisitActionItem.hasValidOrganizationIds(customerOrganizationList: List<String>): Boolean {
        if (organizationIds.isEmpty()) return true

        val identityOrgId = iUserSession.getActiveSession()!!.identity?.organizationId.toString()
        if (organizationIds.any { it.equals(identityOrgId, ignoreCase = true) }) {
            return true
        }

        return customerOrganizationList.any { orgId ->
            organizationIds.any { it.equals(orgId.trim(), ignoreCase = true) }
        }
    }

    private suspend fun VisitActionItem.hasValidCustomerGroups(customer: RouteInformationModel): Boolean {
        if (customerGroups.isEmpty()) return true
        return customerGroups.any { it == customer.customerGroupId?.toInt() }
    }

    private suspend fun VisitActionItem.hasValidCustomerSegments(customer: CustomerItemModel, usedCustomerOrganizationId: Int, realCustomerOrganizationId: Int): Boolean {
        if (customerSegments.isEmpty()) return true
        val segments = iDynamicFormRepository.getCustomerSegments(customer.customerId.toInt(), usedCustomerOrganizationId, realCustomerOrganizationId)
        return segments.any { customerSegments.contains(it) }
    }

    private suspend fun VisitActionItem.hasValidCustomerClasses(customer: CustomerItemModel, usedCustomerOrganizationId: Int, realCustomerOrganizationId: Int): Boolean {
        if (customerClasses.isEmpty()) return true
        val classes = iDynamicFormRepository.getCustomerClasses(customer.customerId.toInt(), usedCustomerOrganizationId, realCustomerOrganizationId)
        return classes.any { customerClasses.contains(it) }
    }

    private suspend fun VisitActionItem.hasValidCustomerChannels(customer: CustomerItemModel, usedCustomerOrganizationId: Int, realCustomerOrganizationId: Int): Boolean {
        if (customerChannels.isEmpty()) return true
        val channels = iDynamicFormRepository.getCustomerChannels(customer.customerId.toInt(), usedCustomerOrganizationId, realCustomerOrganizationId)
        return channels.any { customerChannels.contains(it) }
    }

    private suspend fun VisitActionItem.hasValidCustomerCategories(customer: CustomerItemModel,  usedCustomerOrganizationId: Int,realCustomerOrganizationId: Int): Boolean {
        if (customerCategories.isEmpty()) return true
        val categories = iDynamicFormRepository.getCustomerCategories(customer.customerId.toInt(), usedCustomerOrganizationId, realCustomerOrganizationId)
        return categories.any { customerCategories.contains(it) }
    }

    //endregion Private Extensions
}







































