# TierRank

A tier-list web application where users register, build tier lists, and rank items across the S/A/B/C/D/F tiers. Built as a university Web Application Development project; the full feature list lives in [`REQUIREMENTS.md`](REQUIREMENTS.md).

This repository currently contains a fully working JSON REST backend. The Compose Web frontend is a stub that will be built in a follow-up phase, reusing every DTO from the `shared` module.

## Tech stack

Kotlin Multiplatform project with three modules:

- **`shared/`** — Common code compiled for JVM and WebAssembly. Holds the API contract: `@Serializable` DTOs, enums (`Tier`, `UserRole`), and cross-module constants.
- **`server/`** — Ktor HTTP server on Netty, layered as routes → services → repositories. Uses Exposed (DSL) over SQLite, BCrypt for password hashing, and Ktor `Sessions` (signed cookie) for auth.
- **`composeApp/`** — Compose Multiplatform target for WebAssembly. Stub only at this stage.

Dependencies are centralized in [`gradle/libs.versions.toml`](gradle/libs.versions.toml).

## Run the server

From the project root:

```
.\gradlew.bat :server:run
```

The server listens on `http://localhost:8080`. On first run it creates `./data/tierrank.db` (a SQLite file). The `data/` directory is gitignored.

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
| POST | `/api/tier_lists/{id}/items` | yes | `CreateItemRequest` | `ItemDto` (201) |
| PUT | `/api/tier_lists/{id}/items/{itemId}` | yes | `UpdateItemRequest` | `ItemDto` (replaces `imageUrl`) |
| PATCH | `/api/tier_lists/{id}/items/{itemId}/move` | yes | `MoveItemRequest` | `ItemDto` — sets `tier` and `position` |
| DELETE | `/api/tier_lists/{id}/items/{itemId}` | yes | — | 204 |

### User — `/api/users/me`

| Method | Path | Auth | Body | Returns |
|---|---|---|---|---|
| GET | `/api/users/me` | yes | — | `UserDto` |
| PATCH | `/api/users/me/upgrade-premium` | yes | — | `UserDto` with `role = PREMIUM`; cookie is re-issued so the new role takes effect without re-login |

```

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
├── composeApp/     # Compose Web (WebAssembly) — stub
└── gradle/libs.versions.toml       # dependency version catalog
```