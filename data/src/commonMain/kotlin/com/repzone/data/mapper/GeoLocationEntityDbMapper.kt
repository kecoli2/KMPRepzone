package com.repzone.data.mapper

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
            time = from.Time
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
            Time = domain.time
        )
    }

    fun fromGeoLocationFromDomain(model: GpsLocation): GeoLocationEntity{
        return GeoLocationEntity(
            Id = 0,
            Accuracy = model.accuracy.toDouble(),
            Altitude = model.altitude,
            AltitudeAccuracy = model.altitudeAccuracy?.toDouble(),
            BatteryLevel = model.batteryLevel?.toLong(),
            DailyOperationType = 0,
            Description = model.provider,
            Heading = model.bearing?.toDouble(),
            Latitude = model.latitude,
            Longitude = model.longitude,
            RepresentativeId = model.representativeId,
            ReverseGeocoded = model.reverseGeocoded,
            Speed = model.speed?.toDouble() ,
            Time = model.timestamp,
        )
    }
    //endregion

}
