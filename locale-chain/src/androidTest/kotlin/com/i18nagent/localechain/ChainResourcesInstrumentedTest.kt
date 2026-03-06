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
        if (resId != 0) {
            val result = wrapped.resources.getString(resId)
            assertEquals("Ola (pt-PT)", result)
        }
    }

    @Test
    fun ptBR_getString_fallsBackTo_pt() {
        val wrapped = contextWithLocale("pt-BR")
        val resId = wrapped.resources.getIdentifier("farewell", "string", context.packageName)
        if (resId != 0) {
            val result = wrapped.resources.getString(resId)
            assertEquals("Adeus (pt)", result)
        }
    }

    @Test
    fun ptBR_getString_fallsBackTo_en() {
        val wrapped = contextWithLocale("pt-BR")
        val resId = wrapped.resources.getIdentifier("en_only", "string", context.packageName)
        if (resId != 0) {
            val result = wrapped.resources.getString(resId)
            assertEquals("English only", result)
        }
    }

    @Test
    fun frCA_getString_fallsBackTo_fr() {
        val wrapped = contextWithLocale("fr-CA")
        val resId = wrapped.resources.getIdentifier("greeting", "string", context.packageName)
        if (resId != 0) {
            val result = wrapped.resources.getString(resId)
            assertEquals("Bonjour (fr)", result)
        }
    }
}
