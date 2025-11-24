package com.repzone.domain.pipline.rules.decision

import com.repzone.core.enums.ViolationActionType
import com.repzone.core.model.StringResource
import com.repzone.core.model.UiText
import com.repzone.domain.common.DomainException
import com.repzone.domain.events.base.IEventBus
import com.repzone.domain.events.base.events.DecisionEvents
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.model.RouteInformationModel
import com.repzone.domain.pipline.model.pipline.DecisionOption
import com.repzone.domain.pipline.model.pipline.DecisionOptionTypeEnum
import com.repzone.domain.pipline.model.pipline.PipelineContext
import com.repzone.domain.pipline.model.pipline.Rule
import com.repzone.domain.pipline.model.pipline.RuleResult
import com.repzone.domain.pipline.model.pipline.RuleType
import com.repzone.domain.pipline.rules.util.RuleId
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.service.ILocationService
import com.repzone.domain.util.distanceTo

@Suppress("UNCHECKED_CAST")
class StartVisitGpsDistanceDecisionRule(
    private val eventBus: IEventBus,
    private val iModuleParameterRepository: IMobileModuleParameterRepository,
    private val iLocationService: ILocationService,
    private val customerItem: CustomerItemModel
) :Rule {
    //region Field
    override val type: RuleType = RuleType.DECISION
    override val id: RuleId = RuleId.START_VISIT_GPS_DISTANCE_CHECK
    override val title: UiText = UiText.resource(StringResource.WARNING)
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method

    override suspend fun execute(context: PipelineContext): RuleResult {
        var shouldControlGeofence = true

        if(iModuleParameterRepository.getGeofenceRouteTrackingParameters()!!.isActive && iModuleParameterRepository.getGeofenceRouteTrackingParameters()!!.controlGPSWrtVisitType){
            //TODO ViewModel.reasonInformation.SelectedVisitType != VisitType.AtLocation BU KISMI DUSUNELIM
        }
        try {
            val distanceRule = iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.distance ?: 0
            val violationAction = iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.violationAction ?: ViolationActionType.CONTINUE
            val isActive = iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.isActive ?: false
            val activeAppointment = context.getData<RouteInformationModel>("active_appointment")
            var stringResource: UiText
            if(distanceRule > 0 && violationAction != ViolationActionType.CONTINUE && isActive){
                val currentLocation = iLocationService.getLastKnownLocation()
                if(currentLocation == null){
                    stringResource = UiText.resource(StringResource.WARNING_GPS_LOCATION_NOT_FOUND)
                    val options = listOf(
                        DecisionOption(DecisionOptionTypeEnum.RETRY, UiText.resource(StringResource.TRY_AGAIN)),
                        if(violationAction == ViolationActionType.WARN_USER_AND_CONTINUE){
                            DecisionOption(DecisionOptionTypeEnum.NO, UiText.resource(StringResource.BUTTONCANCEL))
                        } else {

                        }
                    )
                    return retryGetGpsLocation(options, context,stringResource )
                }else{
                    context.putData("active_gps_location", currentLocation)
                    var distance = currentLocation.distanceTo(activeAppointment?.latitude ?: 0.0,activeAppointment?.longitude?: 0.0).toInt()
                    var distanceUnit = ""
                    if(distance > distanceRule){
                        var distanceAsKm = 0.0
                        if (distance > 1000.0)
                        {
                            distanceAsKm = distance / 1000.0;
                        }

                        distanceUnit = when (distanceAsKm > 0){
                            true -> "Km"
                            false -> "Metre"
                        }
                        if (distanceAsKm > 0){
                            distance = distanceAsKm.toInt()
                        }
                    }

                    when(violationAction){
                        ViolationActionType.CONTINUE -> {
                            context.putData("end_visit_gps_distance_sucsess",true)
                            return RuleResult.Success(this)
                        }
                        ViolationActionType.WARN_USER_AND_CONTINUE -> {
                            val options = listOf(
                                DecisionOption(DecisionOptionTypeEnum.YES, UiText.resource(StringResource.DIALOGYES)),
                                DecisionOption(DecisionOptionTypeEnum.CANCEL, UiText.resource(StringResource.DIALOGNO))
                            )
                            stringResource = UiText.resource(StringResource.DISTANCEWARNINGQUESTION,distanceRule, distance, distanceUnit)
                            return retryGetGpsLocation(options, context,stringResource )

                        }
                        ViolationActionType.WARN_USER_AND_DO_NOT_ALLOW -> {
                            val options = listOf(
                                DecisionOption(DecisionOptionTypeEnum.CANCEL, UiText.resource(StringResource.BUTTONCANCEL)),
                            )
                            stringResource = UiText.resource(StringResource.DISTANCEWARNING,distance.toString(), distanceUnit)
                            return retryGetGpsLocation(options, context,stringResource )
                        }
                    }
                }
            }
            return RuleResult.Success(this)

        }catch (e:Exception){
            shouldControlGeofence = false
        }

        TODO("Not yet implemented")
    }
    //endregion
    //region Private Method
    private suspend fun retryGetGpsLocation(options: List<Any>, context: PipelineContext, message: UiText): RuleResult{

        eventBus.publish(DecisionEvents.DecisionRequired(
            ruleId = id,
            title = title,
            message = message,
            sessionId = context.sessionId,
            options = options as List<DecisionOption>,
        ))

        return RuleResult.AwaitingDecision(this, options)

    }
    //endregion
}