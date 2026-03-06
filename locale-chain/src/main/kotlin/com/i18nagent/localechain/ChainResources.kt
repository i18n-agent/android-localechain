package com.i18nagent.localechain

import android.content.Context
import android.content.res.Resources
import androidx.annotation.ArrayRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import java.util.Locale

internal class ChainResources(
    private val base: Resources,
    private val resolver: FallbackResolver,
    private val context: Context
) : Resources(base.assets, base.displayMetrics, base.configuration) {

    private val currentLocaleTag: String
        get() = FallbackResolver.currentLocaleTag(base)

    @Throws(NotFoundException::class)
    override fun getString(@StringRes id: Int): String {
        val result = base.getString(id)
        val resolved = resolver.resolve(id, context, currentLocaleTag)
        return resolved?.value ?: result
    }

    @Throws(NotFoundException::class)
    override fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
        val resolved = resolver.resolve(id, context, currentLocaleTag)
        return if (resolved != null) {
            String.format(Locale.forLanguageTag(resolved.localeTag), resolved.value, *formatArgs)
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
        val resolved = resolver.resolveQuantity(id, quantity, context, currentLocaleTag)
        return resolved?.value ?: result
    }

    @Throws(NotFoundException::class)
    override fun getQuantityString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any): String {
        val resolved = resolver.resolveQuantity(id, quantity, context, currentLocaleTag)
        return if (resolved != null) {
            String.format(Locale.forLanguageTag(resolved.localeTag), resolved.value, *formatArgs)
        } else {
            base.getQuantityString(id, quantity, *formatArgs)
        }
    }

    @Throws(NotFoundException::class)
    override fun getQuantityText(@PluralsRes id: Int, quantity: Int): CharSequence {
        val result = base.getQuantityText(id, quantity)
        val fallback = resolver.resolveQuantityText(id, quantity, context, currentLocaleTag)
        return fallback ?: result
    }

    @Throws(NotFoundException::class)
    override fun getStringArray(@ArrayRes id: Int): Array<String> {
        val result = base.getStringArray(id)
        val fallback = resolver.resolveStringArray(id, context, currentLocaleTag)
        return fallback ?: result
    }

    @Throws(NotFoundException::class)
    override fun getTextArray(@ArrayRes id: Int): Array<CharSequence> {
        val result = base.getTextArray(id)
        val fallback = resolver.resolveTextArray(id, context, currentLocaleTag)
        return fallback ?: result
    }
}
