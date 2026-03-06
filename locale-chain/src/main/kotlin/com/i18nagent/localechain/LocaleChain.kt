package com.i18nagent.localechain

import android.content.Context
import androidx.annotation.AnyThread
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

object LocaleChain {

    const val VERSION = "0.1.0"

    private val lock = ReentrantReadWriteLock()
    private var resolver: FallbackResolver? = null

    val isConfigured: Boolean
        @JvmStatic get() = lock.read { resolver != null }

    @JvmStatic
    @AnyThread
    fun configure() {
        lock.write {
            resolver = FallbackResolver(
                fallbacks = FallbackMap.defaultFallbacks,
                defaultLocale = "en"
            )
        }
    }

    @JvmStatic
    @AnyThread
    fun configure(overrides: Map<String, List<String>>) {
        lock.write {
            resolver = FallbackResolver(
                fallbacks = FallbackMap.merge(FallbackMap.defaultFallbacks, overrides),
                defaultLocale = "en"
            )
        }
    }

    @JvmStatic
    @AnyThread
    fun configure(fallbacks: Map<String, List<String>>, mergeDefaults: Boolean) {
        lock.write {
            val effective = if (mergeDefaults) {
                FallbackMap.merge(FallbackMap.defaultFallbacks, fallbacks)
            } else {
                fallbacks
            }
            resolver = FallbackResolver(
                fallbacks = effective,
                defaultLocale = "en"
            )
        }
    }

    @JvmStatic
    @AnyThread
    fun reset() {
        lock.write {
            resolver?.clearCache()
            resolver = null
        }
    }

    @JvmStatic
    fun wrap(base: Context): Context {
        val currentResolver = lock.read { resolver } ?: return base
        val chainResources = ChainResources(base.resources, currentResolver, base)
        return ChainContextWrapper(base, chainResources)
    }
}
