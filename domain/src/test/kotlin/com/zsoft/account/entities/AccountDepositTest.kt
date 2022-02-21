package com.zsoft.account.entities

import com.zsoft.account.exceptions.InvalidDepositException
import com.zsoft.account.values.AccountProperties
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class AccountDepositTest {
    private val accountProperties = mockk<AccountProperties>(relaxed = true)

    @Test
    fun `should add amount to account`() {
        val account = Account("123123", Currency.EUR, accountProperties, BigDecimal.ZERO, emptyList())
        account.deposit(BigDecimal.ONE)
        assertEquals(BigDecimal.ONE, account.amount)
    }

    @Test
    fun `should add 'deposit operation' to the operations list`() {
        val account = Account("123123", Currency.EUR, accountProperties, BigDecimal.ZERO, emptyList())
        account.deposit(BigDecimal.ONE)
        assertEquals(OperationType.DEPOSIT, account.operations.elementAt(0).type)
    }

    @Test
    fun `should assign current date the deposit operation`() {
        val account = Account("123123", Currency.EUR, accountProperties, BigDecimal.ZERO, emptyList())
        val beforeDepositDate = LocalDateTime.now()
        account.deposit(BigDecimal.ONE)
        val afterDepositDate = LocalDateTime.now()
        val operationDate = account.operations.elementAt(0).date
        assertTrue(beforeDepositDate <= operationDate && afterDepositDate >= operationDate)
    }

    @Test
    fun `should add a valid operation to the operations list`() {
        val account = Account("123123", Currency.EUR, accountProperties, BigDecimal.ZERO, emptyList())
        account.deposit(BigDecimal.ONE)
        assertTrue(account.operations.elementAt(0).validated)
    }

    @Test(expected = InvalidDepositException::class)
    fun `should throw InvalidDepositException on deposit amount '0'`() {
        val account = Account("123123", Currency.EUR, accountProperties, BigDecimal.ZERO, emptyList())
        account.deposit(BigDecimal.ZERO)
    }

    @Test(expected = InvalidDepositException::class)
    fun `should throw InvalidDepositException on deposit amount '-1'`() {
        val account = Account("123123", Currency.EUR, accountProperties, BigDecimal.ZERO, emptyList())
        account.deposit(BigDecimal("-1"))
    }

    @Test
    fun `should add invalid operation on deposit amount '0'`() {
        val account = Account("123123", Currency.EUR, accountProperties, BigDecimal.ZERO, emptyList())
        try {
            account.deposit(BigDecimal.ZERO)
        } catch (ignored: Exception) {
        }
        assertFalse(account.operations.elementAt(0).validated)
    }
}
