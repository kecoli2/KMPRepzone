package com.repzone.core.constant

object PreferencesConstant {
    const val USER_SESSIONS = "USER_SESSIONS"
    const val TOKEN = "Token_Pref"
    const val TOKEN_EXPIRES_AT = "TOKEN_EXPIRES_AT"
    const val REFRESH_TOKEN = "REFRESH_TOKEN"
    const val ACTIVE_USER_CODE = "ACTIVE_USER_CODE"
}


object ITokenApiControllerConstant {
    const val TOKEN_ENDPOINT = "/api/Auth/Login"
    const val TOKEN_INFO = "api/Auth/Info"
}

object IProductApiControllerConstant {
    const val PRODUCT_LIST_ENDPOINT = "/api/v1/Product/List"
    const val PRODUCT_GROUP_LIST_ENDPOINT = "/api/v1/Product/Groups"
}

object IRouteApiControllerConstant {
    const val ROUTE_LIST_ENDPOINT = "/api/v1/Route/List"
}

object  ICustomerApiControllerConstant {
    const val CUSTOMER_LIST_ENDPOINT = "/api/v1/Customer/List"
    const val CUSTOMER_GROUPS_ENDPOINT = "/api/v1/Customer/Groups"
    const val CUSTOMER_EMAIL_ENDPOINT = "/api/v1/Customer/Emails"
    const val CUSTOMER_PRICES_PARAMETERS_ENDPOINT = "/api/v1/Customer/PriceListParameters"
    const val CUSTOMER_GROUP_PRICES_PARAMETERS_ENDPOINT = "/api/v1/Customer/PriceListParameters"
}

object ICommonApiControllerConstant {
    const val COMMON_APP_MODULES_ENDPOINT = "/api/App/Modules"
    const val COMMON_APP_MODULES_REASONS_ENDPOINT = "/api/v1/Misc/EventReasons"
    const val COMMON_APP_DOCUMENT_MAPS = "/api/Document/Maps"
    const val COMMON_APP_DYNAMIC_PAGE = "/api/v1/Misc/DynamicPages"
}

object IFormApiControllerConstant {
    const val FORM_DEFINATION_ENDPOINT = "/api/v1/Form/List"
}

object VersionModuleConstant {
    const val LEGACY_VERSION = "LegacyVersion"
    const val NEW_VERSION = "NewVersion"
}
