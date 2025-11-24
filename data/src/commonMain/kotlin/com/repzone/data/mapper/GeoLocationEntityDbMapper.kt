package com.repzone.data.mapper

import com.repzone.core.enums.DailyOperationType
import com.repzone.core.enums.SyncStatusType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.GeoLocationEntity
import com.repzone.domain.model.GeoLocationModel
import com.repzone.domain.model.gps.GpsLocation

class GeoLocationEntityDbMapper : Mapper<GeoLocationEntity, GeoLocationModel> {
    //region Public Method
    override fun toDomain(from: GeoLocationEntity): GeoLocationModel {
        return GeoLocationModel(
            id = from.Id,
            accuracy = from.Accuracy,
            altitude = from.Altitude,
            altitudeAccuracy = from.AltitudeAccuracy,
            batteryLevel = from.BatteryLevel,
            dailyOperationType = from.DailyOperationType,
            description = from.Description,
            heading = from.Heading,
            latitude = from.Latitude,
            longitude = from.Longitude,
            representativeId = from.RepresentativeId,
            reverseGeocoded = from.ReverseGeocoded,
            speed = from.Speed,
            time = from.Time,
            syncStatus = from.SyncStatus.toEnum<SyncStatusType>() ?: SyncStatusType.IDLE,
            gpsGuId = from.GpsGuId,
            organizationId = from.OrganizationId,
            tenantId = from.TenantId
        )
    }

    override fun fromDomain(domain: GeoLocationModel): GeoLocationEntity {
        return GeoLocationEntity(
            Id = domain.id,
            Accuracy = domain.accuracy,
            Altitude = domain.altitude,
            AltitudeAccuracy = domain.altitudeAccuracy,
            BatteryLevel = domain.batteryLevel,
            DailyOperationType = domain.dailyOperationType,
            Description = domain.description,
            Heading = domain.heading,
            Latitude = domain.latitude,
            Longitude = domain.longitude,
            RepresentativeId = domain.representativeId,
            ReverseGeocoded = domain.reverseGeocoded,
            Speed = domain.speed,
            Time = domain.time,
            SyncStatus = domain.syncStatus.enumToLong(),
            GpsGuId = domain.gpsGuId,
            OrganizationId = domain.organizationId,
            TenantId = domain.tenantId
        )
    }

    fun fromGeoLocationFromDomain(model: GpsLocation): GeoLocationEntity {
        return GeoLocationEntity(
            Id = 0,
            Accuracy = model.accuracy.toDouble(),
            Altitude = model.altitude,
            AltitudeAccuracy = model.altitudeAccuracy?.toDouble(),
            BatteryLevel = model.batteryLevel?.toLong(),
            DailyOperationType = model.dailyOperationType.enumToLong(),
            Description = model.provider,
            Heading = model.bearing?.toDouble(),
            Latitude = model.latitude,
            Longitude = model.longitude,
            RepresentativeId = model.representativeId,
            ReverseGeocoded = model.reverseGeocoded,
            Speed = model.speed?.toDouble(),
            Time = model.timestamp,
            SyncStatus = SyncStatusType.IDLE.enumToLong(),
            GpsGuId = model.id,
            OrganizationId = model.organizationId.toLong(),
            TenantId = model.tenantId.toLong()
        )
    }

        fun fromGeoLocationToDomain(model: GeoLocationEntity): GpsLocation {
            return GpsLocation(
                id = model.GpsGuId,
                latitude = model.Latitude ?: 0.0,
                longitude = model.Longitude ?: 0.0,
                accuracy = model.Accuracy?.toFloat() ?: 0f,
                timestamp = model.Time ?: now(),
                speed = model.Speed?.toFloat(),
                bearing = model.Heading?.toFloat(),
                altitude = model.Altitude,
                provider = "fused",
                isSynced = model.SyncStatus == SyncStatusType.SUCCESS.enumToLong(),
                altitudeAccuracy = model.AltitudeAccuracy?.toFloat(),
                batteryLevel = model.BatteryLevel?.toInt(),
                representativeId = model.RepresentativeId ?: 0,
                reverseGeocoded = model.ReverseGeocoded,
                organizationId = model.OrganizationId.toInt(),
                tenantId = model.TenantId.toInt(),
                dailyOperationType = model.DailyOperationType?.toEnum<DailyOperationType>() ?: DailyOperationType.ERROR,
                description = model.Description
            )
        }
    //endregion

}
