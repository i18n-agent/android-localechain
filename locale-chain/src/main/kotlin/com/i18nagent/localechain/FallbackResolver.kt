package com.i18nagent.localechain

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import androidx.annotation.AnyThread
import androidx.annotation.VisibleForTesting
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

internal data class ResolveResult(val value: String, val localeTag: String)

@AnyThread
internal class FallbackResolver(
    private val fallbacks: Map<String, List<String>>,
    private val defaultLocale: String
) {
    private val resourcesCache = ConcurrentHashMap<String, Resources>()

    @VisibleForTesting
    internal fun fullChain(locale: String): List<String> {
        val chain = (fallbacks[locale] ?: emptyList()).toMutableList()
        if (!chain.contains(defaultLocale)) {
            chain.add(defaultLocale)
        }
        return chain
    }

    fun resolve(
        resId: Int,
        context: Context,
        currentLocaleTag: String
    ): ResolveResult? {
        if (!fallbacks.containsKey(currentLocaleTag)) return null

        val chain = fullChain(currentLocaleTag)
        val defaultResources = getResourcesForLocale(context, defaultLocale)
        val defaultValue = try {
            defaultResources.getString(resId)
        } catch (_: Resources.NotFoundException) {
            return null
        }

        for (fallbackLocaleTag in chain) {
            if (fallbackLocaleTag == defaultLocale) continue
            val fallbackResources = getResourcesForLocale(context, fallbackLocaleTag)
            val fallbackValue = try {
                fallbackResources.getString(resId)
            } catch (_: Resources.NotFoundException) {
                continue
            }
            if (fallbackValue != defaultValue) {
                return ResolveResult(fallbackValue, fallbackLocaleTag)
            }
        }
        return null
    }

    fun resolveQuantity(
        resId: Int,
        quantity: Int,
        context: Context,
        currentLocaleTag: String
    ): ResolveResult? {
        if (!fallbacks.containsKey(currentLocaleTag)) return null

        val chain = fullChain(currentLocaleTag)
        val defaultResources = getResourcesForLocale(context, defaultLocale)
        val defaultValue = try {
            defaultResources.getQuantityString(resId, quantity)
        } catch (_: Resources.NotFoundException) {
            return null
        }

        for (fallbackLocaleTag in chain) {
            if (fallbackLocaleTag == defaultLocale) continue
            val fallbackResources = getResourcesForLocale(context, fallbackLocaleTag)
            val fallbackValue = try {
                fallbackResources.getQuantityString(resId, quantity)
            } catch (_: Resources.NotFoundException) {
                continue
            }
            if (fallbackValue != defaultValue) {
                return ResolveResult(fallbackValue, fallbackLocaleTag)
            }
        }
        return null
    }

    fun resolveText(
        resId: Int,
        context: Context,
        currentLocaleTag: String
    ): CharSequence? {
        if (!fallbacks.containsKey(currentLocaleTag)) return null

        val chain = fullChain(currentLocaleTag)
        val defaultResources = getResourcesForLocale(context, defaultLocale)
        val defaultValue = try {
            defaultResources.getText(resId)
        } catch (_: Resources.NotFoundException) {
            return null
        }

        for (fallbackLocaleTag in chain) {
            if (fallbackLocaleTag == defaultLocale) continue
            val fallbackResources = getResourcesForLocale(context, fallbackLocaleTag)
            val fallbackValue = try {
                fallbackResources.getText(resId)
            } catch (_: Resources.NotFoundException) {
                continue
            }
            if (fallbackValue.toString() != defaultValue.toString()) {
                return fallbackValue
            }
        }
        return null
    }

    fun resolveQuantityText(
        resId: Int,
        quantity: Int,
        context: Context,
        currentLocaleTag: String
    ): CharSequence? {
        if (!fallbacks.containsKey(currentLocaleTag)) return null

        val chain = fullChain(currentLocaleTag)
        val defaultResources = getResourcesForLocale(context, defaultLocale)
        val defaultValue = try {
            defaultResources.getQuantityText(resId, quantity)
        } catch (_: Resources.NotFoundException) {
            return null
        }

        for (fallbackLocaleTag in chain) {
            if (fallbackLocaleTag == defaultLocale) continue
            val fallbackResources = getResourcesForLocale(context, fallbackLocaleTag)
            val fallbackValue = try {
                fallbackResources.getQuantityText(resId, quantity)
            } catch (_: Resources.NotFoundException) {
                continue
            }
            if (fallbackValue.toString() != defaultValue.toString()) {
                return fallbackValue
            }
        }
        return null
    }

    fun resolveStringArray(
        resId: Int,
        context: Context,
        currentLocaleTag: String
    ): Array<String>? {
        if (!fallbacks.containsKey(currentLocaleTag)) return null

        val chain = fullChain(currentLocaleTag)
        val defaultResources = getResourcesForLocale(context, defaultLocale)
        val defaultValue = try {
            defaultResources.getStringArray(resId)
        } catch (_: Resources.NotFoundException) {
            return null
        }

        for (fallbackLocaleTag in chain) {
            if (fallbackLocaleTag == defaultLocale) continue
            val fallbackResources = getResourcesForLocale(context, fallbackLocaleTag)
            val fallbackValue = try {
                fallbackResources.getStringArray(resId)
            } catch (_: Resources.NotFoundException) {
                continue
            }
            if (!fallbackValue.contentEquals(defaultValue)) {
                return fallbackValue
            }
        }
        return null
    }

    fun resolveTextArray(
        resId: Int,
        context: Context,
        currentLocaleTag: String
    ): Array<CharSequence>? {
        if (!fallbacks.containsKey(currentLocaleTag)) return null

        val chain = fullChain(currentLocaleTag)
        val defaultResources = getResourcesForLocale(context, defaultLocale)
        val defaultValue = try {
            defaultResources.getTextArray(resId)
        } catch (_: Resources.NotFoundException) {
            return null
        }

        for (fallbackLocaleTag in chain) {
            if (fallbackLocaleTag == defaultLocale) continue
            val fallbackResources = getResourcesForLocale(context, fallbackLocaleTag)
            val fallbackValue = try {
                fallbackResources.getTextArray(resId)
            } catch (_: Resources.NotFoundException) {
                continue
            }
            if (!fallbackValue.contentEquals(defaultValue)) {
                return fallbackValue
            }
        }
        return null
    }

    @VisibleForTesting
    internal fun getResourcesForLocale(context: Context, localeTag: String): Resources {
        return resourcesCache.getOrPut(localeTag) {
            val locale = parseLocaleTag(localeTag)
            val config = Configuration(context.resources.configuration)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocales(LocaleList(locale))
            } else {
                @Suppress("DEPRECATION")
                config.locale = locale
            }
            context.createConfigurationContext(config).resources
        }
    }

    @VisibleForTesting
    internal fun clearCache() {
        resourcesCache.clear()
    }

    companion object {
        @VisibleForTesting
        internal fun parseLocaleTag(tag: String): Locale {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Locale.forLanguageTag(tag)
            } else {
                val parts = tag.split("-")
                when (parts.size) {
                    1 -> Locale(parts[0])
                    2 -> Locale(parts[0], parts[1])
                    else -> Locale(parts[0], parts[1], parts[2])
                }
            }
        }

        internal fun currentLocaleTag(resources: Resources): String {
            val config = resources.configuration
            val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.locales[0]
            } else {
                @Suppress("DEPRECATION")
                config.locale
            }
            return locale.toLanguageTag()
        }
    }
}
