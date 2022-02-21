package com.zsoft.account.entities

import java.math.BigDecimal
import java.time.LocalDateTime

data class Operation(
    val type: OperationType,
    val date: LocalDateTime,
    val amount: BigDecimal?,
    val validated: Boolean
)

enum class OperationType {
    DEPOSIT,
    WITHDRAWAL
}
