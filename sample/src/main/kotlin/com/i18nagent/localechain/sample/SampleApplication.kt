package com.i18nagent.localechain.sample

import android.app.Application
import com.i18nagent.localechain.LocaleChain

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LocaleChain.configure()
    }
}
