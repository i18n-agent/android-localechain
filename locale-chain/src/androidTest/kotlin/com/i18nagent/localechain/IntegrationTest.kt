package com.i18nagent.localechain

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IntegrationTest {

    @After
    fun tearDown() {
        LocaleChain.reset()
    }

    @Test
    fun fullFlow_configure_wrap_resolve() {
        LocaleChain.configure()
        assertTrue(LocaleChain.isConfigured)

        val context: Context = ApplicationProvider.getApplicationContext()
        val wrapped = LocaleChain.wrap(context)
        assertNotNull(wrapped)
        assertTrue(wrapped is ChainContextWrapper)
    }

    @Test
    fun wrap_withoutConfigure_returnsOriginalContext() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val result = LocaleChain.wrap(context)
        assertSame(context, result)
    }

    @Test
    fun configureWithOverrides_usesOverrides() {
        LocaleChain.configure(overrides = mapOf("pt-BR" to listOf("pt")))
        assertTrue(LocaleChain.isConfigured)
    }

    @Test
    fun configureWithCustom_noMerge() {
        LocaleChain.configure(
            fallbacks = mapOf("pt-BR" to listOf("pt")),
            mergeDefaults = false
        )
        assertTrue(LocaleChain.isConfigured)
    }

    @Test
    fun reset_clearsState() {
        LocaleChain.configure()
        LocaleChain.reset()
        assertFalse(LocaleChain.isConfigured)

        val context: Context = ApplicationProvider.getApplicationContext()
        val result = LocaleChain.wrap(context)
        assertSame(context, result)
    }

    // -- Custom default locale (S1 regression) --

    @Test
    fun configureWithCustomDefaultLocale_wrapsContext() {
        LocaleChain.configure(defaultLocale = "de")
        assertTrue(LocaleChain.isConfigured)

        val context: Context = ApplicationProvider.getApplicationContext()
        val wrapped = LocaleChain.wrap(context)
        assertTrue(wrapped is ChainContextWrapper)
    }

    @Test
    fun configureOverridesWithCustomDefaultLocale_wrapsContext() {
        LocaleChain.configure(
            overrides = mapOf("de-AT" to listOf("de")),
            defaultLocale = "de"
        )
        assertTrue(LocaleChain.isConfigured)

        val context: Context = ApplicationProvider.getApplicationContext()
        val wrapped = LocaleChain.wrap(context)
        assertTrue(wrapped is ChainContextWrapper)
    }
}
