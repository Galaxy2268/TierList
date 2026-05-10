# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

**Run the Ktor backend (port 8080):**
```
.\gradlew.bat :server:run
```

**Run the web app (WebAssembly):**
```
.\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
```

**Build all modules:**
```
.\gradlew.bat build
```

**Run tests:**
```
.\gradlew.bat test
```

**Run tests for a single module:**
```
.\gradlew.bat :shared:test
.\gradlew.bat :server:test
```

## Architecture

This is a **Kotlin Multiplatform (KMP)** project with three modules:

### `shared/`
Common code compiled for JVM (server) and WebAssembly (web client). Use `expect`/`actual` declarations for platform-specific implementations when needed. Put shared business logic, data models, and constants here. `Constants.kt` holds cross-module values like `SERVER_PORT = 8080`.

### `composeApp/`
Web frontend built with **Compose Multiplatform** targeting WebAssembly (`wasmJs`). Source root: `composeApp/src/webMain/kotlin/com/ulyup/tierlist/`. Entry point is `main.kt` (uses `ComposeViewport`), root UI is `App.kt`. Uses Material3 for theming.

### `server/`
**Ktor** HTTP server running on Netty (JVM). Source root: `server/src/main/kotlin/com/ulyup/tierlist/`. `Application.kt` defines routes using the Ktor routing DSL. Starts on `0.0.0.0:8080`.

### Dependency management
All versions are centralized in `gradle/libs.versions.toml`. Reference them in `build.gradle.kts` files via the version catalog (e.g., `libs.ktor.server.core`). Configuration caching and build caching are both enabled in `gradle.properties`.
