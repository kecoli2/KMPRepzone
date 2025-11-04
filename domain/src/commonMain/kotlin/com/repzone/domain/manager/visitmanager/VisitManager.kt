package com.repzone.domain.manager.visitmanager

import com.repzone.domain.common.DomainException
import com.repzone.domain.common.Result
import com.repzone.domain.common.fold
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.repository.IVisitRepository
import com.repzone.domain.usecase.visit.GetVisitMenuListUseCase
import com.repzone.domain.util.models.ActionButtonListItem
import com.repzone.domain.util.models.ActionMenuListItem

class VisitManager(private val iVisitRepository: IVisitRepository,
                   private val iModuleParameters: IMobileModuleParameterRepository,
    private val getVisitMenuUseCase: GetVisitMenuListUseCase
): IVisitManager {
    //region Field
    private var customerItemModel: CustomerItemModel? = null
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun prepareVisitMenu(): Result<Pair<List<ActionMenuListItem>, List<ActionButtonListItem>>> {
        getVisitMenuUseCase.invoke(customerItemModel!!).fold(
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