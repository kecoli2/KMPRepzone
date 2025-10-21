package com.repzone.core.enums

interface IStringValueEnum {
    val value: String
}

inline fun <reified T> findEnumByValue(value: String): T? where T : Enum<T>, T : IStringValueEnum {
    return enumValues<T>().find { it.value.equals(value, ignoreCase = true) }
}

enum class YesOrNo(override val value: String): IStringValueEnum {
    YES("Yes"),
    NO("No");

    companion object {
        fun fromValue(value: String): YesOrNo = findEnumByValue(value) ?: NO
    }
}

enum class OnOf(override val value: String) : IStringValueEnum{
    ON("On"),
    OFF("Off");

    companion object {
        fun fromValue(value: String): OnOf = findEnumByValue(value) ?: ON
    }
}

enum class OrganizationType(override val value: String): IStringValueEnum {
    CUSTOMERORGANIZATION("CustomerOrganization"),
    REPRESENTATIVEORGANIZATION("RepresentativeOrganization");

    companion object {
        fun fromValue(value: String): OrganizationType = findEnumByValue(value) ?: CUSTOMERORGANIZATION
    }
}

enum class StockEntryForm(override val value: String): IStringValueEnum {
    PRODUCTLIST("0");

    companion object {
        fun fromValue(value: String): StockEntryForm = findEnumByValue(value) ?: PRODUCTLIST
    }
}

enum class AutoFilterByStock(override val value: String): IStringValueEnum {
    NO("No"),
    YES_LIST_PRODUCTS_IF_WAREHOUSE_STOCK_POSITIVE("Yes : list products if warehouse stock positive"),
    YES_LIST_PRODUCTS_IF_WAREHOUSE_OR_TRANSIT_STOCK_POSITIVE("Yes : list products if warehouse or transit stock positive");

    companion object {
        fun fromValue(value: String): AutoFilterByStock = findEnumByValue(value) ?: NO
    }
}

enum class LeaveDurationLimitType(override val value: String) : IStringValueEnum {
    DAY_1("1 Day"),
    DAYS_2("2 Days"),
    DAYS_3("3 Days"),
    DAYS_4("4 Days"),
    DAYS_5("5 Days"),
    DAYS_6("6 Days"),
    WEEK_1("1 Week"),
    WEEK_2("2 Weeks"),
    WEEK_3("3 Weeks"),
    MONTH_1("1 Month"),
    QUARTER_1("1 Quarter"),
    YEAR_1("1 Year");

    companion object {
        fun fromValue(value: String): LeaveDurationLimitType = findEnumByValue(value) ?: DAY_1
    }
}

enum class ShowDocumentLabelsType(override val value: String) : IStringValueEnum {
    YES_ONLY_ONE_LABEL_CAN_BE_SELECTED("Yes : only one label can be selected"),
    YES_MULTIPLE_LABELS_CAN_BE_SELECTED("Yes : multiple labels can be selected"),
    No("No");

    companion object {
        fun fromValue(value: String): ShowDocumentLabelsType = findEnumByValue(value) ?: No
    }
}

enum class ShowDocumentLabelsOrderType(override val value: String) : IStringValueEnum{
    ONLY_ONE_LABEL_CAN_BE_SELECTED("Only one label can be selected (optional)"),
    MULTIPLE_LABELS_CAN_BE_SELECTED_OPTIONAL("Multiple labels can be selected (optional)"),
    No("No"),
    ONLY_ONE_LABEL_CAN_BE_SELECTED_MANDATORY("Only one label can be selected (mandatory)"),
    MULTIPLE_LABELS_CAN_BE_SELECTED_MANDATORY("Multiple labels can be selected (mandatory)");

    companion object {
        fun fromValue(value: String): ShowDocumentLabelsOrderType = findEnumByValue(value) ?: No
    }
}
enum class ShowPaytermSelectionType(override val value: String): IStringValueEnum {
    No("No"),
    YES_ALLOW_USER_TO_SELECT("Yes : allow user to select"),
    YES_DONT_ALLOW_USER_TO_SELECT("Yes : dont allow user to select");

    companion object {
        fun fromValue(value: String): ShowPaytermSelectionType = findEnumByValue(value) ?: No
    }
}

enum class UserSelectionType(override val value: String): IStringValueEnum{
    No("No"),
    YES_ALLOW_USER_TO_SELECT("Yes : allow user to select"),
    YES_DONT_ALLOW_USER_TO_SELECT("Yes : dont allow user to select");

    companion object {
        fun fromValue(value: String): UserSelectionType = findEnumByValue(value) ?: No
    }
}

enum class ViolationActionType(override val value: String): IStringValueEnum {
    CONTINUE("Continue"),
    WARN_USER_AND_CONTINUE("Warn user and continue"),
    WARN_USER_AND_DO_NOT_ALLOW("Warn user and do not allow");

    companion object {
        fun fromValue(value: String): ViolationActionType = findEnumByValue(value) ?: CONTINUE
    }
}

enum class VisitPlanSchedulesType(override val value: String) : IStringValueEnum {
    FIXED_DATES("Fixed Dates"),
    FLEXIBLE_DATES("Flexible Dates"),
    FIXED_DATES_SHOW_VISIT_ORDER_INSTEAD_OF_VIST_TIME_INTERVAL("Fixed Dates - Show visit order instead of vist time interval"),
    FIXED_DATES_SHOW_VISIT_ORDER_INSTEAD_OF_VIST_TIME_INTERVAL_WITHOUT_DURATION("Fixed Dates - Show visit order instead of vist time interval without duration");

    companion object {
        fun fromValue(value: String): VisitPlanSchedulesType = findEnumByValue(value) ?: FIXED_DATES
    }

}

enum class PreviousDayVisitsType(override val value: String) : IStringValueEnum {
    CONTINUE("Continue"),
    WARN_USER_AND_CONTINUE("Warn User And Continue"),
    WARN_USER_AND_FINISH_PREVIOUS_DAY_VISIT("Warn User And Finish Previous Day Visit");

    companion object {
        fun fromValue(value: String): PreviousDayVisitsType = findEnumByValue(value) ?: CONTINUE
    }
}

enum class CustomerGPSUpdateType(override val value: String): IStringValueEnum {
    DONT_ALLOW("Don't allow"),
    ALLOW_ONLY_IF_CUSTOMER_HAS_NO_GPS("Allow only if customer has no GPS"),
    ALWAYS_ALLOW("Always allow");

    companion object {
        fun fromValue(value: String): CustomerGPSUpdateType = findEnumByValue(value) ?: DONT_ALLOW
    }
}

enum class StoreDurationType(override val value: String): IStringValueEnum {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly");

    companion object {
        fun fromValue(value: String): StoreDurationType = findEnumByValue(value) ?: DAILY
    }
}

enum class ShowNearbyCustomersType(override val value: String): IStringValueEnum {
    NO("No"),
    YES_AND_SHOW_CUSTOMERS_ON_ROUTE("Yes and show customers on route"),
    YES_AND_SHOW_ALL_CUSTOMERS("Yes and show all customers");

    companion object {
        fun fromValue(value: String): ShowNearbyCustomersType = findEnumByValue(value) ?: NO
    }
}























