package com.repzone.domain.usecase.visit

import com.repzone.core.enums.DocumentTypeGroup
import com.repzone.core.enums.OnOf
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.util.extensions.jsonToModel
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toInstant
import com.repzone.domain.common.DomainException
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.common.Result
import com.repzone.domain.model.RouteInformationModel
import com.repzone.domain.model.VisitInformation
import com.repzone.domain.model.forms.FormBase
import com.repzone.domain.repository.IDocumentMapRepository
import com.repzone.domain.repository.IDynamicFormRepository
import com.repzone.domain.util.enums.ActionButtonType
import com.repzone.domain.util.models.VisitButtonItem
import com.repzone.domain.util.models.VisitActionItem
import kotlin.time.ExperimentalTime

class GetVisitMenuListUseCase(private val iModuleParameters: IMobileModuleParameterRepository,
                              private val iUserSession: IUserSession,
                              private val iDocumentMapreRepository: IDocumentMapRepository,
                              private var iDynamicFormRepository: IDynamicFormRepository) {
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

    //region Protected Method
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
            val list = iDynamicFormRepository.getForms().map {
                val formMaster = iDynamicFormRepository.getFormBase(it.data)
                if(1==1){

                }

            }

        }
    }
    //endregion
}







































