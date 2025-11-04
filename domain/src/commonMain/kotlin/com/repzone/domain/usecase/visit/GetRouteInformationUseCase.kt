package com.repzone.domain.usecase.visit

import com.repzone.domain.common.DomainException
import com.repzone.domain.common.Result
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.model.RouteInformationModel
import com.repzone.domain.repository.IRouteAppointmentRepository

class GetRouteInformationUseCase(private val iRouteAppointmentRepository: IRouteAppointmentRepository) {
    //region Public Method

    suspend operator fun invoke(customerItemModel: CustomerItemModel): Result<RouteInformationModel?> {
        return try {
            val result = iRouteAppointmentRepository.getRouteInformation(customerItemModel.appointmentId)
            Result.Success(result)
        }catch (ex: DomainException){
            Result.Error(ex)
        }catch (ex: Exception){
            Result.Error(DomainException.UnknownException(cause = ex))
        }
    }
    //endregion

}