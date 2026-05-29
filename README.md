# TierRank

A tier-list web application where users register, build tier lists, and rank items across the S/A/B/C/D/F tiers. Built as a university Web Application Development project; the full feature list lives in [`REQUIREMENTS.md`](REQUIREMENTS.md).

Both the Ktor JSON backend and the Compose Web (WebAssembly) frontend are implemented and share their API contract through the `shared` module.

## Tech stack

Kotlin Multiplatform project with three modules:

- **`shared/`** — Common code compiled for JVM and WebAssembly. Holds the API contract: `@Serializable` DTOs, enums (`Tier`, `UserRole`), and cross-module constants.
- **`server/`** — Ktor HTTP server on Netty, layered as routes → services → repositories. Uses Exposed (DSL) over SQLite, BCrypt for password hashing, and Ktor `Sessions` (signed cookie) for auth.
- **`composeApp/`** — Compose Multiplatform / Material3 targeting WebAssembly (`wasmJs`). Built with Koin for DI, androidx `navigation-compose` (multiplatform fork) for routing, Ktor client for HTTP, and Coil 3 for image loading. Organized as Clean Architecture (domain / data / presentation) with MVI per feature (`StateFlow<State>` + sealed `Action` + `Event` channel). Includes session-aware bootstrap, localStorage persistence of the last tab + last opened detail, and `?detail={id}` share links.

Dependencies are centralized in [`gradle/libs.versions.toml`](gradle/libs.versions.toml).

## Run the server

From the project root:

```
.\gradlew.bat :server:run
```

The server listens on `http://localhost:8080`. On first run it creates `./data/tierrank.db` (a SQLite file). The `data/` directory is gitignored.

## Run the web app

With the server already running on port 8080, in a second terminal:

```
.\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
```

The dev server opens the app at `http://localhost:8081`. The port matters: the backend's CORS allowlist is locked to that origin, so requests from any other port will be rejected.

## API reference

All endpoints are JSON. Authenticated endpoints require the `TIERRANK_SESSION` cookie set by register/login; in PowerShell that is handled by `-SessionVariable` / `-WebSession`.

### Auth — `/api/auth`

| Method | Path | Auth | Body | Returns |
|---|---|---|---|---|
| POST | `/register` | no | `RegisterRequest` | `UserDto` (201), sets session |
| POST | `/login` | no | `LoginRequest` | `UserDto`, sets session |
| POST | `/logout` | yes | — | 204, clears session |
| GET | `/me` | yes | — | `UserDto` (re-fetched from DB so role changes are visible) |

### Tier lists — `/api/tier_lists` and `/api/users/me/tier_lists`

| Method | Path | Auth | Body | Returns |
|---|---|---|---|---|
| GET | `/api/tier_lists` | no | — | `List<TierListDto>` — public feed |
| GET | `/api/tier_lists/{id}` | optional | — | `TierListDetailDto` (includes items); private lists → 404 unless owner |
| GET | `/api/users/me/tier_lists` | yes | — | `List<TierListDto>` — own lists, public + private |
| POST | `/api/tier_lists` | yes | `CreateTierListRequest` | `TierListDto` (201); 5-list cap for `USER` role |
| PUT | `/api/tier_lists/{id}` | yes | `UpdateTierListRequest` | `TierListDto`; ownership required |
| PATCH | `/api/tier_lists/{id}/visibility` | yes | `UpdateVisibilityRequest` | `TierListDto`; ownership required |
| DELETE | `/api/tier_lists/{id}` | yes | — | 204; CASCADE deletes items |

### Items — nested under `/api/tier_lists/{id}/items`

Items are plain images (URL only) — no name, no notes. Each item carries an optional `tier` (null = unranked) and a `position` within that tier.

| Method | Path | Auth | Body | Returns |
|---|---|---|---|---|
| GET | `/api/tier_lists/{id}/items` | optional | — | `List<ItemDto>`; follows parent visibility |
| POST | `/api/tier_lists/{id}/items/batch` | yes | multipart: up to 25 `image` parts | `CreateItemsBatchResponse` (201) — `created` + `failedFilenames` |
| PUT | `/api/tier_lists/{id}/items/{itemId}` | yes | `UpdateItemRequest` | `ItemDto` (replaces `imageUrl`) |
| PATCH | `/api/tier_lists/{id}/items/{itemId}/move` | yes | `MoveItemRequest` | `ItemDto` — sets `tier` and `position` |
| DELETE | `/api/tier_lists/{id}/items/{itemId}` | yes | — | 204 |

### User — `/api/users/me`

| Method | Path | Auth | Body | Returns |
|---|---|---|---|---|
| GET | `/api/users/me` | yes | — | `UserDto` |
| PATCH | `/api/users/me/upgrade-premium` | yes | — | `UserDto` with `role = PREMIUM`; cookie is re-issued so the new role takes effect without re-login |

## Project structure

```
.
├── shared/         # KMP common: DTOs, enums, constants
│   └── src/commonMain/kotlin/com/ulyup/tier_list/
│       ├── dto/        # AuthDto, TierListDto, ItemDto, ErrorDto
│       └── model/      # Tier, UserRole
├── server/         # Ktor backend (JVM)
│   └── src/main/kotlin/com/ulyup/tier_list/
│       ├── Application.kt          # plugin install + routing
│       ├── auth/                   # Passwords, UserSession, authenticated{}
│       ├── data/                   # repository impls + mappers
│       ├── db/                     # DatabaseFactory + Exposed tables
│       ├── domain/                 # repository + service interfaces, models
│       └── routes/                 # Auth / TierList / Item / User routes
├── composeApp/     # Compose Web (WebAssembly) frontend
│   └── src/webMain/
│       ├── kotlin/com/ulyup/tier_list/
│       │   ├── main/               # main.kt (ComposeViewport) + App.kt + MainScreen + main VM
│       │   ├── core/
│       │   │   ├── browser/        # wasm interop (history, share link)
│       │   │   ├── di/             # Koin modules
│       │   │   ├── mvi/            # base ViewModel + LoadableState helpers
│       │   │   ├── navigation/     # AppNavHost + route definitions + nav helpers
│       │   │   ├── ui/             # shared composables (buttons, scaffold, snackbar, ...)
│       │   │   └── usecase/        # UseCase<P, R> + Resource<T>
│       │   ├── data/               # repository impls + Ktor client + SessionManager,
│       │   │                       # grouped by bounded context:
│       │   │                       # auth / network / preferences / session / tier_list / user
│       │   ├── domain/             # repository + service interfaces, models, exceptions:
│       │   │                       # auth / error / preferences / session / tier_list / user
│       │   ├── feature/            # presentation layer, one package per screen group
│       │   │   ├── auth/           # Login + Register
│       │   │   ├── feed/           # public tier-list feed
│       │   │   ├── mylists/        # user's own lists + Create dialog
│       │   │   ├── tier_list_detail/  # viewer + editor + drag-and-drop
│       │   │   ├── profile/        # profile + premium upgrade
│       │   │   ├── splash/         # bootstrap screen
│       │   │   └── error/          # fatal error screen
│       │   └── theme/              # appColors / appTypography (dark theme)
│       └── composeResources/       # values/strings.xml + drawable/
└── gradle/libs.versions.toml       # dependency version catalog
```