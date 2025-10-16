package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncTenantDiscountEntity
import com.repzone.domain.model.SyncTenantDiscountModel

class SyncTenantDiscountEntityDbMapper : Mapper<SyncTenantDiscountEntity, SyncTenantDiscountModel> {
    //region Public Method
    override fun toDomain(from: SyncTenantDiscountEntity): SyncTenantDiscountModel {
        return SyncTenantDiscountModel(
            id = from.Id,
            discountType = from.DiscountType,
            isDeletable = from.IsDeletable,
            lineEffectScopeType = from.LineEffectScopeType,
            maxPercent = from.MaxPercent,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            order = from.Order,
            recordDateUtc = from.RecordDateUtc,
            reserved = from.Reserved,
            scope = from.Scope,
            state = from.State,
            tenantId = from.TenantId
        )
    }

    override fun fromDomain(domain: SyncTenantDiscountModel): SyncTenantDiscountEntity {
        return SyncTenantDiscountEntity(
            Id = domain.id,
            DiscountType = domain.discountType,
            IsDeletable = domain.isDeletable,
            LineEffectScopeType = domain.lineEffectScopeType,
            MaxPercent = domain.maxPercent,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            Order = domain.order,
            RecordDateUtc = domain.recordDateUtc,
            Reserved = domain.reserved,
            Scope = domain.scope,
            State = domain.state,
            TenantId = domain.tenantId
        )
    }
    //endregion

}
