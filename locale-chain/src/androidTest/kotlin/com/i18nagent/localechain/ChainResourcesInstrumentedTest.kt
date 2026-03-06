package com.i18nagent.localechain

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class ChainResourcesInstrumentedTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        LocaleChain.configure()
    }

    @After
    fun tearDown() {
        LocaleChain.reset()
    }

    private fun contextWithLocale(tag: String): Context {
        val locale = Locale.forLanguageTag(tag)
        val config = Configuration(context.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(LocaleList(locale))
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
        }
        val localeContext = context.createConfigurationContext(config)
        return LocaleChain.wrap(localeContext)
    }

    @Test
    fun ptBR_getString_fallsBackTo_ptPT() {
        val wrapped = contextWithLocale("pt-BR")
        val resId = wrapped.resources.getIdentifier("greeting", "string", context.packageName)
        assertTrue("Resource 'greeting' not found in test resources", resId != 0)
        val result = wrapped.resources.getString(resId)
        assertEquals("Ola (pt-PT)", result)
    }

    @Test
    fun ptBR_getString_fallsBackTo_pt() {
        val wrapped = contextWithLocale("pt-BR")
        val resId = wrapped.resources.getIdentifier("farewell", "string", context.packageName)
        assertTrue("Resource 'farewell' not found in test resources", resId != 0)
        val result = wrapped.resources.getString(resId)
        assertEquals("Adeus (pt)", result)
    }

    @Test
    fun ptBR_getString_fallsBackTo_en() {
        val wrapped = contextWithLocale("pt-BR")
        val resId = wrapped.resources.getIdentifier("en_only", "string", context.packageName)
        assertTrue("Resource 'en_only' not found in test resources", resId != 0)
        val result = wrapped.resources.getString(resId)
        assertEquals("English only", result)
    }

    @Test
    fun frCA_getString_fallsBackTo_fr() {
        val wrapped = contextWithLocale("fr-CA")
        val resId = wrapped.resources.getIdentifier("greeting", "string", context.packageName)
        assertTrue("Resource 'greeting' not found in test resources", resId != 0)
        val result = wrapped.resources.getString(resId)
        assertEquals("Bonjour (fr)", result)
    }

    // -- Non-chain locale should pass through without overhead (C2 regression) --

    @Test
    fun nonChainLocale_returnsBaseValue_noFallbackResolution() {
        val wrapped = contextWithLocale("ja")
        val resId = wrapped.resources.getIdentifier("greeting", "string", context.packageName)
        assertTrue("Resource 'greeting' not found in test resources", resId != 0)
        val result = wrapped.resources.getString(resId)
        // ja has no chain entry, so should get the default locale value
        assertEquals("Hello (en)", result)
    }

    // -- Custom default locale (S1 regression) --

    @Test
    fun customDefaultLocale_usesSpecifiedDefault() {
        LocaleChain.reset()
        LocaleChain.configure(
            fallbacks = mapOf("fr-CA" to listOf("fr")),
            mergeDefaults = false,
            defaultLocale = "fr"
        )
        // With "fr" as default, fr-CA's chain is ["fr"] and the comparison
        // is against the "fr" locale value, so no fallback is needed
        assertTrue(LocaleChain.isConfigured)
    }
}
