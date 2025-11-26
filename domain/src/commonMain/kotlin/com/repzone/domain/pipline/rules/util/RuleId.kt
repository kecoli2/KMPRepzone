package com.repzone.domain.pipline.rules.util

enum class RuleId(val id: String) {
    CUSTOMER_BLOCKED_CHECK("customer_blocked_check"),
    ACTIVE_VISIT_CHECK("active_visit_check"),
    END_VISIT("end_visit"),
    END_VISIT_DECISION("end_visit_decision"),
    START_VISIT("start_visit"),
    START_VISIT_GPS_DISTANCE_CHECK("start_visit_gps_distance_check"),
    END_VISIT_GPS_DISTANCE_CHECK("end_visit_gps_distance_check"),
    PREPARE_DOCUMENT("prepare_document"),

}