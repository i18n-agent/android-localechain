package com.i18nagent.localechain

object FallbackMap {

    @JvmField
    val defaultFallbacks: Map<String, List<String>> = mapOf(
        // Portuguese
        "pt-BR" to listOf("pt-PT", "pt"),
        "pt-PT" to listOf("pt"),

        // Spanish (Latin America uses es-419 as intermediate)
        "es-419" to listOf("es"),
        "es-MX" to listOf("es-419", "es"),
        "es-AR" to listOf("es-419", "es"),
        "es-CO" to listOf("es-419", "es"),
        "es-CL" to listOf("es-419", "es"),
        "es-PE" to listOf("es-419", "es"),
        "es-VE" to listOf("es-419", "es"),
        "es-EC" to listOf("es-419", "es"),
        "es-GT" to listOf("es-419", "es"),
        "es-CU" to listOf("es-419", "es"),
        "es-BO" to listOf("es-419", "es"),
        "es-DO" to listOf("es-419", "es"),
        "es-HN" to listOf("es-419", "es"),
        "es-PY" to listOf("es-419", "es"),
        "es-SV" to listOf("es-419", "es"),
        "es-NI" to listOf("es-419", "es"),
        "es-CR" to listOf("es-419", "es"),
        "es-PA" to listOf("es-419", "es"),
        "es-UY" to listOf("es-419", "es"),
        "es-PR" to listOf("es-419", "es"),

        // French
        "fr-CA" to listOf("fr"),
        "fr-BE" to listOf("fr"),
        "fr-CH" to listOf("fr"),
        "fr-LU" to listOf("fr"),
        "fr-MC" to listOf("fr"),
        "fr-SN" to listOf("fr"),
        "fr-CI" to listOf("fr"),
        "fr-ML" to listOf("fr"),
        "fr-CM" to listOf("fr"),
        "fr-MG" to listOf("fr"),
        "fr-CD" to listOf("fr"),

        // German
        "de-AT" to listOf("de"),
        "de-CH" to listOf("de"),
        "de-LU" to listOf("de"),
        "de-LI" to listOf("de"),

        // Italian
        "it-CH" to listOf("it"),

        // Dutch
        "nl-BE" to listOf("nl"),

        // Norwegian
        "nb" to listOf("no"),
        "nn" to listOf("nb", "no"),

        // Malay
        "ms-MY" to listOf("ms"),
        "ms-SG" to listOf("ms"),
        "ms-BN" to listOf("ms")
    )

    @JvmStatic
    fun merge(
        defaults: Map<String, List<String>>,
        overrides: Map<String, List<String>>
    ): Map<String, List<String>> {
        return defaults + overrides
    }
}
