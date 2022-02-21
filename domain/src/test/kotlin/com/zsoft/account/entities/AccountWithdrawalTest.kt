package com.zsoft.account.entities

import com.zsoft.account.values.AccountProperties
import com.zsoft.account.exceptions.InvalidWithdrawalException
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import java.lang.Exception
import java.math.BigDecimal
import java.time.LocalDateTime

class AccountWithdrawalTest {
    private val accountProperties = mockk<AccountProperties>(relaxed = true)

    @Test
    fun `should subtract amount to account`() {
        val toWithdrawal = BigDecimal.ONE
        val currentAmount = BigDecimal.ONE
        every { accountProperties.canWithdrawal(toWithdrawal, currentAmount) } returns true
        val account = Account("123123", Currency.EUR, accountProperties, currentAmount, emptyList())
        account.withdrawal(toWithdrawal)
        assertEquals(BigDecimal.ZERO, account.amount)
    }

    @Test
    fun `should add 'withdrawal operation' to the operations list`() {
        val toWithdrawal = BigDecimal.ONE
        val currentAmount = BigDecimal.ONE
        every { accountProperties.canWithdrawal(toWithdrawal, currentAmount) } returns true
        val account = Account("123123", Currency.EUR, accountProperties, currentAmount, emptyList())
        account.withdrawal(toWithdrawal)
        assertEquals(OperationType.WITHDRAWAL, account.operations.elementAt(0).type)
    }

    @Test
    fun `should assign current date the withdrawal operation`() {
        val toWithdrawal = BigDecimal.ONE
        val currentAmount = BigDecimal.ONE
        every { accountProperties.canWithdrawal(toWithdrawal, currentAmount) } returns true
        val account = Account("123123", Currency.EUR, accountProperties, currentAmount, emptyList())
        val beforeWithdrawalDate = LocalDateTime.now()
        account.withdrawal(toWithdrawal)
        val afterWithdrawalDate = LocalDateTime.now()
        val operationDate = account.operations.elementAt(0).date
        assertTrue(beforeWithdrawalDate <= operationDate && afterWithdrawalDate >= operationDate)
    }

    @Test
    fun `should add a valid operation to the operations list`() {
        val toWithdrawal = BigDecimal.ONE
        val currentAmount = BigDecimal.ONE
        every { accountProperties.canWithdrawal(toWithdrawal, currentAmount) } returns true
        val account = Account("123123", Currency.EUR, accountProperties, currentAmount, emptyList())
        account.withdrawal(toWithdrawal)
        assertTrue(account.operations.elementAt(0).validated)
    }

    @Test(expected = InvalidWithdrawalException::class)
    fun `should throw InvalidWithdrawalException when account properties are not valid `() {
        val toWithdrawal = BigDecimal.ONE
        val currentAmount = BigDecimal.TEN
        every { accountProperties.canWithdrawal(toWithdrawal, currentAmount) } returns false
        val account = Account("123123", Currency.EUR, accountProperties, currentAmount, emptyList())
        account.withdrawal(toWithdrawal)
    }

    @Test(expected = InvalidWithdrawalException::class)
    fun `should throw InvalidWithdrawalException on withdrawal amount '0'`() {
        val toWithdrawal = BigDecimal.ZERO
        val currentAmount = BigDecimal.ONE
        every { accountProperties.canWithdrawal(toWithdrawal, currentAmount) } returns true
        val account = Account("123123", Currency.EUR, accountProperties, currentAmount, emptyList())
        account.withdrawal(toWithdrawal)
    }

    @Test(expected = InvalidWithdrawalException::class)
    fun `should throw InvalidWithdrawalException on withdrawal amount '-1'`() {
        val toWithdrawal = BigDecimal.valueOf(-1L)
        val currentAmount = BigDecimal.ONE
        every { accountProperties.canWithdrawal(toWithdrawal, currentAmount) } returns true
        val account = Account("123123", Currency.EUR, accountProperties, currentAmount, emptyList())
        account.withdrawal(toWithdrawal)
    }

    @Test
    fun `should add invalid operation on withdrawal amount '0'`() {
        val toWithdrawal = BigDecimal.ZERO
        val currentAmount = BigDecimal.ONE
        every { accountProperties.canWithdrawal(toWithdrawal, currentAmount) } returns true
        val account = Account("123123", Currency.EUR, accountProperties, currentAmount, emptyList())
        try {
            account.withdrawal(toWithdrawal)
        } catch (ignored: Exception) {}
        assertFalse(account.operations.elementAt(0).validated)
    }
}
