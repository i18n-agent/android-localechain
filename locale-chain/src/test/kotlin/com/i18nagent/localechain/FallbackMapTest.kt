package com.i18nagent.localechain

import org.junit.Assert.*
import org.junit.Test

class FallbackMapTest {

    // -- Portuguese --

    @Test
    fun ptBR_fallsBackTo_ptPT_then_pt() {
        assertEquals(listOf("pt-PT", "pt"), FallbackMap.defaultFallbacks["pt-BR"])
    }

    @Test
    fun ptPT_fallsBackTo_pt() {
        assertEquals(listOf("pt"), FallbackMap.defaultFallbacks["pt-PT"])
    }

    // -- Spanish --

    @Test
    fun es419_fallsBackTo_es() {
        assertEquals(listOf("es"), FallbackMap.defaultFallbacks["es-419"])
    }

    @Test
    fun esMX_fallsBackTo_es419_then_es() {
        assertEquals(listOf("es-419", "es"), FallbackMap.defaultFallbacks["es-MX"])
    }

    @Test
    fun esAR_fallsBackTo_es419_then_es() {
        assertEquals(listOf("es-419", "es"), FallbackMap.defaultFallbacks["es-AR"])
    }

    @Test
    fun esCO_fallsBackTo_es419_then_es() {
        assertEquals(listOf("es-419", "es"), FallbackMap.defaultFallbacks["es-CO"])
    }

    @Test
    fun esCL_fallsBackTo_es419_then_es() {
        assertEquals(listOf("es-419", "es"), FallbackMap.defaultFallbacks["es-CL"])
    }

    // -- French --

    @Test
    fun frCA_fallsBackTo_fr() {
        assertEquals(listOf("fr"), FallbackMap.defaultFallbacks["fr-CA"])
    }

    @Test
    fun frBE_fallsBackTo_fr() {
        assertEquals(listOf("fr"), FallbackMap.defaultFallbacks["fr-BE"])
    }

    @Test
    fun frCH_fallsBackTo_fr() {
        assertEquals(listOf("fr"), FallbackMap.defaultFallbacks["fr-CH"])
    }

    // -- German --

    @Test
    fun deAT_fallsBackTo_de() {
        assertEquals(listOf("de"), FallbackMap.defaultFallbacks["de-AT"])
    }

    @Test
    fun deCH_fallsBackTo_de() {
        assertEquals(listOf("de"), FallbackMap.defaultFallbacks["de-CH"])
    }

    // -- Other --

    @Test
    fun itCH_fallsBackTo_it() {
        assertEquals(listOf("it"), FallbackMap.defaultFallbacks["it-CH"])
    }

    @Test
    fun nlBE_fallsBackTo_nl() {
        assertEquals(listOf("nl"), FallbackMap.defaultFallbacks["nl-BE"])
    }

    @Test
    fun nb_fallsBackTo_no() {
        assertEquals(listOf("no"), FallbackMap.defaultFallbacks["nb"])
    }

    @Test
    fun nn_fallsBackTo_nb_then_no() {
        assertEquals(listOf("nb", "no"), FallbackMap.defaultFallbacks["nn"])
    }

    // -- Merge behavior --

    @Test
    fun merge_overridesReplaceDefaults() {
        val overrides = mapOf("pt-BR" to listOf("pt"))
        val merged = FallbackMap.merge(FallbackMap.defaultFallbacks, overrides)
        assertEquals(listOf("pt"), merged["pt-BR"])
        assertEquals(listOf("fr"), merged["fr-CA"])
    }

    @Test
    fun merge_addsNewLocales() {
        val overrides = mapOf("custom-XX" to listOf("custom", "en"))
        val merged = FallbackMap.merge(FallbackMap.defaultFallbacks, overrides)
        assertEquals(listOf("custom", "en"), merged["custom-XX"])
        assertNotNull(merged["pt-BR"])
    }

    // -- Completeness --

    @Test
    fun allChainsAreNonEmpty() {
        for ((locale, chain) in FallbackMap.defaultFallbacks) {
            assertTrue("Fallback chain for $locale should not be empty", chain.isNotEmpty())
        }
    }

    @Test
    fun noCyclicFallbacks() {
        for ((locale, chain) in FallbackMap.defaultFallbacks) {
            assertFalse("Fallback chain for $locale must not contain itself", chain.contains(locale))
        }
    }
}
