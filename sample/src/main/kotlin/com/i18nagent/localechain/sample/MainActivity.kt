package com.i18nagent.localechain.sample

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.i18nagent.localechain.LocaleChain

class MainActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleChain.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)
        val tvFarewell = findViewById<TextView>(R.id.tvFarewell)
        val tvLocale = findViewById<TextView>(R.id.tvLocale)

        tvGreeting.text = getString(R.string.greeting)
        tvFarewell.text = getString(R.string.farewell)
        tvLocale.text = "Current locale: ${resources.configuration.locales[0].toLanguageTag()}"
    }
}
