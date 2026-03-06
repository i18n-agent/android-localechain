package com.i18nagent.localechain

import android.content.Context
import android.content.res.Resources
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

internal class ChainResources(
    private val base: Resources,
    private val resolver: FallbackResolver,
    private val context: Context
) : Resources(base.assets, base.displayMetrics, base.configuration) {

    private val currentLocaleTag: String by lazy {
        FallbackResolver.currentLocaleTag(base)
    }

    @Throws(NotFoundException::class)
    override fun getString(@StringRes id: Int): String {
        val result = base.getString(id)
        val fallback = resolver.resolve(id, context, currentLocaleTag)
        return fallback ?: result
    }

    @Throws(NotFoundException::class)
    override fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
        val template = resolver.resolve(id, context, currentLocaleTag)
        return if (template != null) {
            String.format(template, *formatArgs)
        } else {
            base.getString(id, *formatArgs)
        }
    }

    @Throws(NotFoundException::class)
    override fun getText(@StringRes id: Int): CharSequence {
        val result = base.getText(id)
        val fallback = resolver.resolveText(id, context, currentLocaleTag)
        return fallback ?: result
    }

    override fun getText(@StringRes id: Int, def: CharSequence): CharSequence {
        return try {
            getText(id)
        } catch (_: NotFoundException) {
            def
        }
    }

    @Throws(NotFoundException::class)
    override fun getQuantityString(@PluralsRes id: Int, quantity: Int): String {
        val result = base.getQuantityString(id, quantity)
        val fallback = resolver.resolveQuantity(id, quantity, context, currentLocaleTag)
        return fallback ?: result
    }

    @Throws(NotFoundException::class)
    override fun getQuantityString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any): String {
        val template = resolver.resolveQuantity(id, quantity, context, currentLocaleTag)
        return if (template != null) {
            String.format(template, *formatArgs)
        } else {
            base.getQuantityString(id, quantity, *formatArgs)
        }
    }
}
