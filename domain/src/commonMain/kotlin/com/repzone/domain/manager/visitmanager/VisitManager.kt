package com.repzone.domain.manager.visitmanager

import com.repzone.domain.common.DomainException
import com.repzone.domain.common.Result
import com.repzone.domain.common.fold
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.model.RouteInformationModel
import com.repzone.domain.model.VisitInformation
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.repository.IVisitRepository
import com.repzone.domain.usecase.visit.GetRouteInformationUseCase
import com.repzone.domain.usecase.visit.GetVisitMenuListUseCase
import com.repzone.domain.util.models.VisitButtonItem
import com.repzone.domain.util.models.VisitActionItem

class VisitManager(private val iVisitRepository: IVisitRepository,
                   private val iModuleParameters: IMobileModuleParameterRepository,
    private val getVisitMenuUseCase: GetVisitMenuListUseCase,
    private val getRouteInformationUseCase: GetRouteInformationUseCase
): IVisitManager {
    //region Field
    private var customerItemModel: CustomerItemModel? = null
    private var routeInformation : RouteInformationModel? = null
    private var visitInformation: VisitInformation? = null
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun prepareVisitMenu(): Result<Pair<List<VisitActionItem>, List<VisitButtonItem>>> {
        getVisitMenuUseCase.invoke(customerItemModel!!, visitInformation, routeInformation!!).fold(
            onSuccess = {
                return Result.Success(it)
            },
            onError = {
                return Result.Error(it)
            }
        )
    }

    override suspend fun initiliaze(customer: CustomerItemModel): Result<Unit> {
        return try {
            customerItemModel = customer
            getRouteInformationUseCase.invoke(customer).fold(
                onSuccess = {
                    routeInformation = it
                    customer.customerBalance = it?.customerBalance ?: 0.0
                    customer.customerRiskBalance = it?.customerRisk ?: 0.0
                },
                onError = {
                    return Result.Error(it)
                }
            )
            visitInformation = iVisitRepository.getActiveVisit()
            Result.Success(Unit)
        }catch (ex: DomainException){
            Result.Error(ex)
        }catch (ex: Exception){
            Result.Error(DomainException.UnknownException(cause = ex))
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}