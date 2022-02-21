package com.zsoft.account.values

import java.math.BigDecimal

class AccountProperties(
    val overdraft: BigDecimal,
    val withdrawalLimit: BigDecimal? = null
) {
    fun canWithdrawal(amount: BigDecimal, fromAmount: BigDecimal): Boolean {
        if (this.withdrawalLimit != null && amount > this.withdrawalLimit) {
            return false
        }
        if (amount > fromAmount + this.overdraft) {
            return false
        }
        return true
    }
}
