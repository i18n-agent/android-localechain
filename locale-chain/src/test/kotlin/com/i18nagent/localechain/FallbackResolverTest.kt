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
