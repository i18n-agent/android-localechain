package com.i18nagent.localechain

import org.junit.After
import org.junit.Assert.*
import org.junit.Test

class LocaleChainTest {

    @After
    fun tearDown() {
        LocaleChain.reset()
    }

    @Test
    fun configure_setsActiveState() {
        LocaleChain.configure()
        assertTrue(LocaleChain.isConfigured)
    }

    @Test
    fun configureWithOverrides_setsActiveState() {
        LocaleChain.configure(overrides = mapOf("xx-YY" to listOf("xx")))
        assertTrue(LocaleChain.isConfigured)
    }

    @Test
    fun configureWithCustomFallbacks_noMerge() {
        LocaleChain.configure(
            fallbacks = mapOf("pt-BR" to listOf("pt")),
            mergeDefaults = false
        )
        assertTrue(LocaleChain.isConfigured)
    }

    @Test
    fun reset_deactivates() {
        LocaleChain.configure()
        LocaleChain.reset()
        assertFalse(LocaleChain.isConfigured)
    }

    @Test
    fun configureTwice_isIdempotent() {
        LocaleChain.configure()
        LocaleChain.configure()
        assertTrue(LocaleChain.isConfigured)
    }

    @Test
    fun version_isSet() {
        assertTrue(LocaleChain.VERSION.isNotEmpty())
    }
}
