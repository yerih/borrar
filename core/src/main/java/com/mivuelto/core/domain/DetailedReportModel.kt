package com.mivuelto.core.domain

data class DetailedReportModel(
    val isSale: Boolean = true,
    val reference: String = "",
    val isApproved: Boolean = true,
    val pan: String = "",
    val brand: String = "",
    val date: String = "",
    val amount: String = "",

) {
}
