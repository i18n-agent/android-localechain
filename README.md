# LocaleChain for Android

Smart locale fallback chains for Android — because pt-BR users deserve pt-PT, not English.

## The Problem

Android's resource system fallback is OS-controlled. When `pt-BR` translations are missing, Android skips `pt-PT` entirely and shows English (or whatever your default locale is).

The same thing happens with `es-MX` -> `es`, `fr-CA` -> `fr`, `de-AT` -> `de`, and every other regional variant.

Your users see English when a perfectly good translation exists in a sibling locale.

## The Solution

Two lines of setup. Zero changes to your existing localization code.

LocaleChain wraps your Activity's `Context` and intercepts string lookups (`getString()`, `getText()`, `getQuantityString()`). When a translation is missing for the current locale, it walks a configurable fallback chain before giving up. It works with everything that calls `Resources` under the hood:

- `getString(R.string.key)` in code
- `android:text="@string/key"` in XML layouts
- `stringResource(R.string.key)` in Jetpack Compose
- `getQuantityString()` for plurals

## Installation

**Gradle** (Maven Central):

```kotlin
dependencies {
    implementation("com.i18nagent:locale-chain-android:0.1.0")
}
```

## Quick Start

```kotlin
// 1. Application.onCreate() — configure chains once
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        LocaleChain.configure()
    }
}

// 2. BaseActivity — wrap context per Activity
open class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleChain.wrap(newBase))
    }
}
```

That's it. A `pt-BR` user will now see `pt-PT` translations when `pt-BR` is not available.

## Custom Configuration

### Default (zero config)

```kotlin
LocaleChain.configure()
```

Uses all built-in fallback chains. Covers Portuguese, Spanish, French, German, Italian, Dutch, Norwegian, and Malay regional variants.

### With overrides (merge with defaults)

```kotlin
LocaleChain.configure(
    overrides = mapOf("pt-BR" to listOf("pt-PT", "pt"))
)
```

Your overrides replace matching keys in the default map. All other defaults remain.

### Full custom (replace defaults)

```kotlin
LocaleChain.configure(
    fallbacks = mapOf(
        "pt-BR" to listOf("pt-PT", "pt"),
        "es-MX" to listOf("es-419", "es")
    ),
    mergeDefaults = false
)
```

Only the chains you specify will be active. No defaults.

## Default Fallback Map

### Portuguese

| Locale | Fallback Chain |
|--------|---------------|
| pt-BR | pt-PT -> pt -> (default) |
| pt-PT | pt -> (default) |

### Spanish

| Locale | Fallback Chain |
|--------|---------------|
| es-419 | es -> (default) |
| es-MX | es-419 -> es -> (default) |
| es-AR | es-419 -> es -> (default) |
| es-CO | es-419 -> es -> (default) |
| es-CL | es-419 -> es -> (default) |
| es-PE | es-419 -> es -> (default) |
| es-VE | es-419 -> es -> (default) |
| es-EC | es-419 -> es -> (default) |
| es-GT | es-419 -> es -> (default) |
| es-CU | es-419 -> es -> (default) |
| es-BO | es-419 -> es -> (default) |
| es-DO | es-419 -> es -> (default) |
| es-HN | es-419 -> es -> (default) |
| es-PY | es-419 -> es -> (default) |
| es-SV | es-419 -> es -> (default) |
| es-NI | es-419 -> es -> (default) |
| es-CR | es-419 -> es -> (default) |
| es-PA | es-419 -> es -> (default) |
| es-UY | es-419 -> es -> (default) |
| es-PR | es-419 -> es -> (default) |

### French

| Locale | Fallback Chain |
|--------|---------------|
| fr-CA | fr -> (default) |
| fr-BE | fr -> (default) |
| fr-CH | fr -> (default) |
| fr-LU | fr -> (default) |
| fr-MC | fr -> (default) |
| fr-SN | fr -> (default) |
| fr-CI | fr -> (default) |
| fr-ML | fr -> (default) |
| fr-CM | fr -> (default) |
| fr-MG | fr -> (default) |
| fr-CD | fr -> (default) |

### German

| Locale | Fallback Chain |
|--------|---------------|
| de-AT | de -> (default) |
| de-CH | de -> (default) |
| de-LU | de -> (default) |
| de-LI | de -> (default) |

### Italian

| Locale | Fallback Chain |
|--------|---------------|
| it-CH | it -> (default) |

### Dutch

| Locale | Fallback Chain |
|--------|---------------|
| nl-BE | nl -> (default) |

### Norwegian

| Locale | Fallback Chain |
|--------|---------------|
| nb | no -> (default) |
| nn | nb -> no -> (default) |

### Malay

| Locale | Fallback Chain |
|--------|---------------|
| ms-MY | ms -> (default) |
| ms-SG | ms -> (default) |
| ms-BN | ms -> (default) |

## How It Works

1. `configure()` stores the fallback chain configuration in memory.
2. `wrap()` wraps your Activity's `Context` with a custom `ContextWrapper` that intercepts `Resources` calls.
3. When a string is not found in the current locale (detected by comparing against the default locale's value), LocaleChain walks the fallback chain in order.
4. Each fallback locale's `Resources` is created via `createConfigurationContext()` and cached for subsequent lookups.
5. All shared state is protected by `ReentrantReadWriteLock` for thread safety.

## Java Usage

```java
// Application
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocaleChain.configure();
    }
}

// Activity
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleChain.wrap(newBase));
    }
}
```

## FAQ

**Is this safe for production?**
Yes. The `ContextWrapper` pattern is used by many production libraries including Calligraphy, AppLocale, and Google's own AppCompat.

**Performance impact?**
Negligible. Fallback resources are loaded lazily and cached. The fallback path only triggers when the current locale genuinely does not have a translation.

**Jetpack Compose compatibility?**
Yes. Compose's `stringResource()` calls `Resources.getString()` internally, so it works automatically.

**What about plurals?**
Works with `getQuantityString()` and plurals defined in `plurals.xml`.

**Can I deactivate it?**
Yes. Call `LocaleChain.reset()` to clear configuration. The next `wrap()` call will be a no-op pass-through.

**Minimum Android version?**
API 21 (Android 5.0).

## Contributing

- Open issues for bugs or feature requests.
- PRs welcome, especially for adding new locale fallback chains.
- Run `./gradlew :locale-chain:test` for unit tests.
- Run `./gradlew :locale-chain:connectedAndroidTest` for instrumented tests (requires device/emulator).

## License

MIT License - see [LICENSE](LICENSE) file.

Built by [i18nagent.ai](https://i18nagent.ai)
