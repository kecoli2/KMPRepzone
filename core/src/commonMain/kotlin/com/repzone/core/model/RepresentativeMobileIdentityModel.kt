package com.repzone.core.model

import com.repzone.core.enums.CustomerOrRepresentativeOrganizationSelection
import com.repzone.core.enums.UserRole
import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class RepresentativeMobileIdentityModel(
    val representativeId: Int,
    val userId: Int,
    val currency: String,
    val currencyDecimalDigits: Int? = null,
    val code: String? = null,
    val groupId: Int? = null,
    val groupName: String? = null,
    val tenantId: Int,
    val tenantName: String? = null,
    val organizationId: Int,
    val organizationCode: String? = null,
    val organizationName: String? = null,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val phone: String? = null,
    val photoPath: String? = null,
    val headerLogoPath: String? = null,
    val tagsRaw: String? = null,
    val notificationTagsRaw: String? = null,
    val tokenType: String? = null,
    val tokenCode: String? = null,
    val state: Int,
    val trackStatus: Boolean,
    val trackInterval: Int? = null,
    @Serializable(with = InstantSerializer::class)
    val trackStartTime: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val trackEndTime: Instant? = null,
    val trackDays: String? = null,
    val hasHotSellingAuthority: Boolean,
    val warehouseId: Int? = null,
    @Serializable(with = UserRole.Companion.Serializer::class)
    val role: UserRole,
    val activationKey: String? = null,
    val activationPassword: String? = null,
    val identityTenantId: Int,
    @Serializable(with = CustomerOrRepresentativeOrganizationSelection.Companion.Serializer::class)
    val organizationSelection: CustomerOrRepresentativeOrganizationSelection,
    val templateWillBeUsedWhenAddingCustomer: Boolean,
    val doNotCheckEDocumentWhileSave: Int,
    val utcTimeOffset: Int? = null,
    val userIdentityId: Int
)
