# Tier List — Project Description

**Web Application Development Course**

- **Application Name:** Tier List
- **Student:** Daniels Muļukins (dm25053)

> Source of truth: `tier_list_dm25053.pdf` (approved). This document mirrors that spec.
> **Deviation note:** items in this implementation are *plain images* — the `name` field is dropped from `TierlistItem` even though the approved spec lists it. Decision made for product simplicity; no other deviations.

## 1. Application Overview

Tier List is a web application for entertainment and personal information saving. It allows users to create and manage tier lists — ranked collections of items organized into rating tiers (S, A, B, C, D). The application targets hobbyists, gamers, movie enthusiasts, and anyone who wants to visually compare and rank items within any category.

### Main Tasks
- Create, edit, and delete tier lists with custom categories
- Add items to a tier list and assign them to a rating tier
- See other users' public tier lists

## 2. Typical Use Cases

**User Admin** — A registered user logs in and creates a new tier list called "Best RPG Games". Enters the tier list from the All Tier Lists screen. They add items (images) and drag them into the S and A tiers respectively.

**User Viewer** — A registered user can view other users' public tier lists and add them to favorites to place them on top of the list.

## 3. Development Environment

| Component | Technology |
|-----------|-----------|
| UI / Frontend | Kotlin, Compose Multiplatform |
| Backend | Kotlin, Ktor (REST API, MVC architecture) |
| Database | SQLite |
| IDE | IntelliJ IDEA |
| Version Control | Git / GitHub |

## 4. MVC Architecture & Database Model

### Models
- **User** — id, username, email, password, role (USER / PREMIUM)
- **Tierlist** — id, userId, title, isPublic, createdAt
- **TierlistItem** — id, tierlistId, imageUrl, tier (S/A/B/C/D/F), position
  - *Note: approved spec includes a `name` field; this implementation drops it (items are plain images).*

### Controllers (Ktor Route Handlers)
- **AuthController** — register, login, logout, session management
- **TierlistController** — CRUD for tierlists, visibility toggle
- **ItemController** — CRUD for items, tier assignment
- **UserController** — profile view, role upgrade to premium

### Views (Compose Multiplatform Screens)
- **HomeScreen** — browsable feed of public tier lists
- **LoginScreen / RegisterScreen** — authentication forms
- **MyTierlistsScreen** — list of the user's own tier lists
- **TierlistEditorScreen** — tier board with item management