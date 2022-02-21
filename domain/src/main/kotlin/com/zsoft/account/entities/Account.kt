package com.zsoft.account.entities

import com.zsoft.account.values.AccountProperties
import com.zsoft.account.exceptions.InvalidDepositException
import com.zsoft.account.exceptions.InvalidWithdrawalException
import java.math.BigDecimal
import java.time.LocalDateTime

data class Account(
    val iban: String,
    val currency: Currency,
    val accountProperties: AccountProperties,
    var amount: BigDecimal,
    var operations: Collection<Operation>,
) {
    fun deposit(amount: BigDecimal) {
        this.validateDeposit(amount)
        this.amount += amount
    }

    fun withdrawal(amount: BigDecimal) {
        this.validateWithdrawal(amount)
        this.amount -= amount
    }

    private fun validateDeposit(amount: BigDecimal) {
        if (amount <= BigDecimal.ZERO) {
            this.operations += Operation(OperationType.DEPOSIT, LocalDateTime.now(), amount, false)
            throw InvalidDepositException("Invalid deposit amount : $amount")
        }
        this.operations += Operation(OperationType.DEPOSIT, LocalDateTime.now(), amount, true)
    }

    private fun validateWithdrawal(amount: BigDecimal) {
        if (amount <= BigDecimal.ZERO) {
            this.operations += Operation(OperationType.WITHDRAWAL, LocalDateTime.now(), amount, false)
            throw InvalidWithdrawalException("Invalid withdrawal amount: $amount")
        }
        if (!this.accountProperties.canWithdrawal(amount, fromAmount = this.amount)) {
            this.operations += Operation(OperationType.WITHDRAWAL, LocalDateTime.now(), amount, false)
            throw InvalidWithdrawalException("Can not withdrawal this amount: $amount")
        }
        this.operations += Operation(OperationType.WITHDRAWAL, LocalDateTime.now(), amount, true)
    }
}

enum class Currency {
    EUR
}
