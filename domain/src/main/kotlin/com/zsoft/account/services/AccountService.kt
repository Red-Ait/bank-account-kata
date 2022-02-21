package com.zsoft.account.services

import com.zsoft.account.entities.Account

interface AccountService {

    fun findAccount(account: Account): Account?

    fun updateAccount(account: Account): Account

    fun findAccountByIban(iban: String): Account?
}
