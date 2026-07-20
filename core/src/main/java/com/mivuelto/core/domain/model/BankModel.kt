package com.mivuelto.core.domain.model

import androidx.annotation.DrawableRes


data class BankModel(
    @DrawableRes val logo: Int? = null,
    val code: String = "",
    val name: String = ""
)


