package com.repzone.core.constant

object PreferencesConstant {
    const val TOKEN = "Token_Pref"
    const val TOKEN_EXPIRES_AT = "TOKEN_EXPIRES_AT"
    const val REFRESH_TOKEN = "REFRESH_TOKEN"
}


object ITokenApiControllerConstant {
    const val TOKEN_ENDPOINT = "/api/ExternalAccount/MobileLogin"
    const val TOKEN_REFRESH_ENDPOINT= "/api/TokenEndPointRefresh"
}

object IProductApiControllerConstant {
    const val PRODUCT_LIST_ENDPOINT = "/api/v1/Product/List"
    const val PRODUCT_GROUP_LIST_ENDPOINT = "/api/v1/Product/Groups"

}