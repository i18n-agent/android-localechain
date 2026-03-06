# Changelog

## [0.1.0] - 2026-03-06

### Added
- Core locale fallback chain resolution via `LocaleChain.configure()` / `LocaleChain.wrap()`
- Default fallback chains for Portuguese, Spanish (19 Latin American variants), French, German, Italian, Dutch, Norwegian, and Malay
- Configurable `defaultLocale` parameter (defaults to `"en"`)
- Thread-safe implementation with `ReentrantReadWriteLock` and `ConcurrentHashMap`
- Resource interception for `getString()`, `getText()`, `getQuantityString()`, `getQuantityText()`, `getStringArray()`, `getTextArray()`
- Locale-aware `String.format()` for parameterized strings
- Java interop via `@JvmStatic` / `@JvmField` annotations
- Consumer ProGuard rules for R8 compatibility
- Sample app demonstrating Application + Activity integration
- Unit tests (FallbackMap, FallbackResolver, LocaleChain)
- Instrumented tests (ChainResources, Integration)
