package com.zsoft.account.usercase

import com.zsoft.account.entities.Account
import com.zsoft.account.exceptions.NotFoundAccountException
import com.zsoft.account.services.AccountService
import java.math.BigDecimal

class AccountUseCase(private val accountService: AccountService) {

    fun makeDeposit(account: Account, amount: BigDecimal): Account {
        return this.accountService.findAccount(account)
            ?.let {
                account.deposit(amount)
                return this.accountService.updateAccount(account)
            } ?: throw NotFoundAccountException("Account not found : $account")
    }

    fun makeWithdrawal(account: Account, amount: BigDecimal): Account {
        return this.accountService.findAccount(account)
            ?.let {
                account.withdrawal(amount)
                return this.accountService.updateAccount(account)
            } ?: throw NotFoundAccountException("Account not found : $account")
    }

    fun getAccountStatement(iban: String): Account {
        return this.accountService.findAccountByIban(iban)
            ?: throw NotFoundAccountException("No account with iban : $iban was found")
    }
}
