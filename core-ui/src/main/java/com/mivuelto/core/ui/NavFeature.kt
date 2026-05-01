package com.mivuelto.core.ui.navigation

enum class NavFeature(val route: String) {
    WELCOME("welcome"),
    PASSWORD("password"),
    HOME("home"),
    PURCHASE("purchase"),
    TRANSACTIONS("transactions"),
    REP_SUMMARY("op_summary"),
    INVOICES("invoices"),
    OTHER_OPERATIONS("other_operations"),
    VOID("void"),
    SETTINGS("settings"),
    SETTLEMENT("settlement"),
    KEYS_DOWNLOADING("keys_downloading"),
    APK_DOWNLOADING("apk_downloading"),
    EMPTY_ROUTE("")
}

enum class NavFeatOtherOp(val route: String){
    ECHO_TEST("echo_test"),
    CONNECTION_TYPE("connection_type"),
    BANK_CHANGE("bank_change"),
    TECH_MENU("tech_menu"),
    EXTRAFINANCING("extrafinancing"),
//    OPENED_KEYBOARD("opened_keyboard"),
    ACCESS("access"),
}

enum class InvoiceType(val route: String) {
    SUCCESSFUL("invoice_successful"),
    FAILED("invoice_failed"),
    EXTRA_QUERY("extra_query"),
}

interface Invoices {
    fun invoice(type: InvoiceType): String = type.route
}


