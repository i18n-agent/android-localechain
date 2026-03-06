package com.i18nagent.localechain

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FallbackResolverTest {

    private lateinit var resolver: FallbackResolver

    @Before
    fun setUp() {
        resolver = FallbackResolver(
            fallbacks = FallbackMap.defaultFallbacks,
            defaultLocale = "en"
        )
    }

    // -- Chain building --

    @Test
    fun fullChain_includesDefaultLocale() {
        val chain = resolver.fullChain("pt-BR")
        assertEquals(listOf("pt-PT", "pt", "en"), chain)
    }

    @Test
    fun fullChain_unknownLocale_onlyHasDefaultLocale() {
        val chain = resolver.fullChain("xx-YY")
        assertEquals(listOf("en"), chain)
    }

    @Test
    fun fullChain_doesNotDuplicateDefaultLocale() {
        val custom = FallbackResolver(
            fallbacks = mapOf("test" to listOf("en")),
            defaultLocale = "en"
        )
        val chain = custom.fullChain("test")
        assertEquals(listOf("en"), chain)
    }

    @Test
    fun fullChain_spanishMexico() {
        val chain = resolver.fullChain("es-MX")
        assertEquals(listOf("es-419", "es", "en"), chain)
    }

    @Test
    fun fullChain_norwegianNynorsk() {
        val chain = resolver.fullChain("nn")
        assertEquals(listOf("nb", "no", "en"), chain)
    }

    // -- Early return for non-chain locales (C2 regression) --

    @Test
    fun fullChain_nonChainLocale_returnsOnlyDefault() {
        val chain = resolver.fullChain("ja")
        assertEquals(listOf("en"), chain)
    }

    @Test
    fun fullChain_defaultLocale_returnsOnlyDefault() {
        val chain = resolver.fullChain("en")
        assertEquals(listOf("en"), chain)
    }

    // -- Custom default locale (S1 regression) --

    @Test
    fun fullChain_customDefaultLocale_appendsCustomDefault() {
        val custom = FallbackResolver(
            fallbacks = mapOf("de-AT" to listOf("de")),
            defaultLocale = "de"
        )
        val chain = custom.fullChain("de-AT")
        assertEquals(listOf("de"), chain)
    }

    @Test
    fun fullChain_customDefaultLocale_unknownLocale() {
        val custom = FallbackResolver(
            fallbacks = mapOf("de-AT" to listOf("de")),
            defaultLocale = "de"
        )
        val chain = custom.fullChain("fr")
        assertEquals(listOf("de"), chain)
    }

    @Test
    fun fullChain_customDefaultLocale_appendsWhenNotInChain() {
        val custom = FallbackResolver(
            fallbacks = mapOf("pt-BR" to listOf("pt-PT", "pt")),
            defaultLocale = "de"
        )
        val chain = custom.fullChain("pt-BR")
        assertEquals(listOf("pt-PT", "pt", "de"), chain)
    }

    // -- Thread safety --

    @Test
    fun concurrentAccess_doesNotCrash() {
        val threads = (1..100).map {
            Thread {
                resolver.fullChain("pt-BR")
                resolver.fullChain("es-MX")
                resolver.fullChain("fr-CA")
            }
        }
        threads.forEach { it.start() }
        threads.forEach { it.join(10_000) }
    }
}
