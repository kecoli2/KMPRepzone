package com.repzone.core.constant

object PreferencesConstant {
    const val USER_SESSIONS = "USER_SESSIONS"
    const val TOKEN = "Token_Pref"
    const val TOKEN_EXPIRES_AT = "TOKEN_EXPIRES_AT"
    const val REFRESH_TOKEN = "REFRESH_TOKEN"
    const val ACTIVE_USER_CODE = "ACTIVE_USER_CODE"
}

object CdnConfig {
    const val CDN_IMAGE_CONFIG = "https://cdn.repzone.com/images/"
}


object ITokenApiControllerConstant {
    const val TOKEN_ENDPOINT = "/api/Auth/Login"
    const val TOKEN_INFO = "api/Auth/Info"
}

object IProductApiControllerConstant {
    const val PRODUCT_LIST_ENDPOINT = "/api/v1/Product/List"
    const val PRODUCT_GROUP_LIST_ENDPOINT = "/api/v1/Product/Groups"
    const val PRODUCT_UNIT_LIST_ENDPOINT = "/api/v1/Product/Units"
}

object IRouteApiControllerConstant {
    const val ROUTE_LIST_ENDPOINT = "/api/v1/Route/List"
}

object  ICustomerApiControllerConstant {
    const val CUSTOMER_LIST_ENDPOINT = "/api/v1/Customer/List"
    const val CUSTOMER_GROUPS_ENDPOINT = "/api/v1/Customer/Groups"
    const val CUSTOMER_EMAIL_ENDPOINT = "/api/v1/Customer/Emails"
    const val CUSTOMER_PRICES_PARAMETERS_ENDPOINT = "/api/v1/Customer/PriceListParameters"
    const val CUSTOMER_GROUP_PRICES_PARAMETERS_ENDPOINT = "/api/v1/Customer/GroupPriceListParameters"
}

object  IStockApiControllerConstant {
    const val STOCK_MAIN_LIST_ENDPOINT = "/api/v1/Stock/List"
}

object  IMiscApiControllerConstant {
    const val PAYMENT_PLAN_LIST_ENDPOINT = "/api/v1/Misc/PaymentPlans"
    const val WAREHOUSE_LIST_ENDPOINT = "/api/v1/Misc/Warehouses"
}

object ICommonApiControllerConstant {
    const val COMMON_APP_MODULES_ENDPOINT = "/api/App/Modules"
    const val COMMON_APP_MODULES_REASONS_ENDPOINT = "/api/v1/Misc/EventReasons"
    const val COMMON_APP_DOCUMENT_MAPS = "/api/Document/Maps"
    const val COMMON_APP_DYNAMIC_PAGE = "/api/v1/Misc/DynamicPages"
}

object IDocumentApiControllerConstant {
    const val DOCUMENTORGANIZATION_ENDPOINT = "/api/Document/Organizations"
}

object IMobileApiControllerConstant {
    const val LAST_DOC_NUMBERS = "/api/Document/LastDocNumbers"
    const val DELETE_DOCUMENT = "/api/v1/Mobile/DeleteDocument"
}

object IFormApiControllerConstant {
    const val FORM_DEFINATION_ENDPOINT = "/api/v1/Form/List"
    const val FORM_MANDATORY_ENDPOINT = "/api/v1/Form/MandatoryForms"
}

object IDistributionApiControllerConstant {
    const val CUSTOMER_PRODUCT_DISTRIBUTION_ENDPOINT = "/api/Distribution/CustomerProductDistributions"
    const val CUSTOMER_PRODUCT_GROUP_DISTRIBUTION_ENDPOINT = "/api/Distribution/CustomerProductGroupDistributions"
    const val PRODUCT_DISTRIBUTION_PRODUCT_ENDPOINT = "/api/Distribution/ProductDistributions"
    const val PRODUCT_DISTRIBUTION_LINES_ENDPOINT = "/api/Distribution/ProductDistributionLines"
    const val REPRESENATIVE_DISTRIBUTION_ENDPOINT = "/api/Distribution/RepresentativeDistributions"
}

object IPriceListApiControllerConstant {
    const val PRICE_LIST_ENDPOINT = "/api/PriceList/List"
    const val PRICE_LINE_ENDPOINT = "/api/PriceList/Lines"
}

object VersionModuleConstant {
    const val LEGACY_VERSION = "LegacyVersion"
    const val NEW_VERSION = "NewVersion"
}
