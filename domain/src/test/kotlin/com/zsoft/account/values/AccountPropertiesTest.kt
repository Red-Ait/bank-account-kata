package com.zsoft.account.values

import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal

class AccountPropertiesTest {
    @Test
    fun `return true if the it has the amount and no withdrawal limit` () {
        val accountProperties = AccountProperties(overdraft = BigDecimal.ZERO)
        assertTrue(accountProperties.canWithdrawal(BigDecimal.ONE, BigDecimal.ONE))
    }

    @Test
    fun `return true if the it has the amount and below the withdrawal limit` () {
        val accountProperties = AccountProperties(overdraft = BigDecimal.ZERO, withdrawalLimit = BigDecimal.TEN)
        assertTrue(accountProperties.canWithdrawal(BigDecimal.ONE, BigDecimal.ONE))
    }

    @Test
    fun `return true if the it has the amount and equals the withdrawal limit` () {
        val accountProperties = AccountProperties(overdraft = BigDecimal.ZERO, withdrawalLimit = BigDecimal.ONE)
        assertTrue(accountProperties.canWithdrawal(BigDecimal.ONE, BigDecimal.ONE))
    }

    @Test
    fun `return false if the it has the amount but exceed the withdrawal limit` () {
        val accountProperties = AccountProperties(overdraft = BigDecimal.ZERO, withdrawalLimit = BigDecimal.ONE)
        assertFalse(accountProperties.canWithdrawal(BigDecimal.TEN, BigDecimal.TEN))
    }

    @Test
    fun `return true if the it has not the amount but can over draft` () {
        val accountProperties = AccountProperties(overdraft = BigDecimal.TEN)
        assertTrue(accountProperties.canWithdrawal(BigDecimal.TEN, BigDecimal.ONE))
    }

    @Test
    fun `return false if the it has not the amount and can not over draft` () {
        val accountProperties = AccountProperties(overdraft = BigDecimal.ZERO)
        assertFalse(accountProperties.canWithdrawal(BigDecimal.TEN, BigDecimal.ONE))
    }
}
