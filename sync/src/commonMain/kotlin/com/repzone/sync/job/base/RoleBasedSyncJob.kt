package com.repzone.sync.job.base

import com.repzone.sync.model.UserRole

abstract class RoleBasedSyncJob: BaseSyncJob() {
    //region Field
    protected abstract val allowedRoles: Set<UserRole>

    companion object {
        val SALES_ROLES = setOf(UserRole.SALES_REP, UserRole.MANAGER, UserRole.ADMIN)
        val MERGE_ROLES = setOf(UserRole.MERGE_STAFF, UserRole.MANAGER, UserRole.ADMIN)
        val ALL_ROLES = UserRole.entries.toSet()
    }
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun isApplicableForRole(userRole: UserRole): Boolean {
        return userRole in allowedRoles
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}