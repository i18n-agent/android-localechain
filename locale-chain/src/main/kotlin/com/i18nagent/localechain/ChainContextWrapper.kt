package com.i18nagent.localechain

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources

internal class ChainContextWrapper(
    base: Context,
    private val chainResources: ChainResources
) : ContextWrapper(base) {

    override fun getResources(): Resources = chainResources
}
