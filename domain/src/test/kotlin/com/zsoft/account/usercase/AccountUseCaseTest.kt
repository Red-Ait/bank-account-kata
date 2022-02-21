package com.zsoft.account.usercase

import com.zsoft.account.entities.Account
import com.zsoft.account.values.AccountProperties
import com.zsoft.account.entities.Currency
import com.zsoft.account.exceptions.NotFoundAccountException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import com.zsoft.account.services.AccountService
import java.math.BigDecimal

class AccountUseCaseTest {
    private val accountService = mockk<AccountService>(relaxed = true)
    private val accountUseCase = AccountUseCase(accountService)

    @Test
    fun `should deposit the amount if the account exists` (){
        val accountProperties = AccountProperties(BigDecimal.ZERO, BigDecimal.ZERO)
        val account = Account("123123", Currency.EUR, accountProperties, BigDecimal.ZERO, emptyList())
        every { accountService.findAccount(account) } returns account
        every { accountService.updateAccount(account) } returns account
        accountUseCase.makeDeposit(account, BigDecimal.ONE)
        verify { accountService.updateAccount(account) }
    }

    @Test(expected = NotFoundAccountException::class)
    fun `deposit - throw NotFoundAccountException when the account not exists` (){
        val accountProperties = AccountProperties(BigDecimal.ZERO, BigDecimal.ZERO)
        val account = Account("123123", Currency.EUR, accountProperties, BigDecimal.ZERO, emptyList())
        every { accountService.findAccount(account) } returns null
        accountUseCase.makeDeposit(account, BigDecimal.ONE)
    }

    @Test
    fun `should withdrawal the  amount if the account exists` (){
        val accountProperties = AccountProperties(BigDecimal.ZERO)
        val account = Account("123123", Currency.EUR, accountProperties, BigDecimal.ONE, emptyList())
        every { accountService.findAccount(account) } returns account
        every { accountService.updateAccount(account) } returns account
        accountUseCase.makeWithdrawal(account, BigDecimal.ONE)
        verify { accountService.updateAccount(account) }
    }

    @Test(expected = NotFoundAccountException::class)
    fun `withdrawal - throw NotFoundAccountException when the account not exists` (){
        val accountProperties = AccountProperties(BigDecimal.ZERO)
        val account = Account("123123", Currency.EUR, accountProperties, BigDecimal.ONE, emptyList())
        every { accountService.findAccount(account) } returns null
        accountUseCase.makeWithdrawal(account, BigDecimal.ONE)
    }

    @Test
    fun `should find account by its iban` (){
        val iban = "123213"
        accountUseCase.getAccountStatement(iban)
        verify { accountService.findAccountByIban(iban) }
    }

    @Test(expected = NotFoundAccountException::class)
    fun `find account by iban - throw NotFoundAccountException when the account not exists` (){
        val iban = "123213"
        every { accountService.findAccountByIban(iban) } returns null
        accountUseCase.getAccountStatement(iban)
    }
}
