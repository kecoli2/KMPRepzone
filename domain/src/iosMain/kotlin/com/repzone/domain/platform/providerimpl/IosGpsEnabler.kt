package com.repzone.domain.platform.providerimpl

import com.repzone.domain.manager.gps.IPlatformGpsEnabler

class IosGpsEnabler: IPlatformGpsEnabler {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun requestEnableGps(): Boolean {
        // iOS'ta programatik GPS açma yok
        return false
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}