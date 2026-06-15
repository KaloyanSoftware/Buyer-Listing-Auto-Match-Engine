# Buyer–Listing Auto-Match Engine

## Project Overview

A real estate agency tool that automatically matches new property listings against active buyer profiles the moment a listing is saved. Eliminates the manual "check the spreadsheet" step that causes agents to miss commissions.

**Target users:** Small real estate agencies (1–5 agents) tracking 15–30 active buyers at any time.

---

## Repository Structure

```
/
├── backend/     Spring Boot Java application (REST API + matching engine)
└── frontend/    React SPA (Vite + TypeScript)
```

---

## Backend

**Language / Runtime:** Java 21  
**Framework:** Spring Boot 4.1.0  
**Build tool:** Gradle (Kotlin DSL) — `build.gradle.kts`  
**Base package:** `application.backend`

### Running the backend
```bash
cd backend
./gradlew bootRun
```

### Building the backend
```bash
cd backend
./gradlew build
```

### Dependencies (all in build.gradle.kts)
- `spring-boot-starter-data-jpa` — JPA / Hibernate ORM
- `spring-boot-starter-web` — REST controllers
- `spring-boot-starter-security` — Spring Security
- `io.jsonwebtoken:jjwt-api/impl/jackson:0.12.6` — JWT
- `org.postgresql:postgresql` — JDBC driver
- `org.flywaydb:flyway-database-postgresql` — Flyway (Flyway 10+ PostgreSQL module)
- `org.projectlombok:lombok` — `@Data`, `@Builder`, etc.
- `org.mapstruct:mapstruct:1.6.3` — compile-time entity↔DTO mappers
- `com.resend:resend-java:3.1.0` — transactional email (verify version on first build)

### Backend package structure
```
application.backend/
├── auth/          AuthController, AuthService, dto/
├── config/        SecurityConfig
├── entity/        JPA entities + enums (8 enums, 7 entities)
├── exception/     GlobalExceptionHandler, ApiException
├── repository/    Spring Data JPA repositories (7 interfaces)
└── security/      JwtService, JwtAuthFilter, CurrentAgency, UserDetailsServiceImpl
```

### Database
- **PostgreSQL 16** (hosted on Railway)
- Migrations managed by Flyway (`src/main/resources/db/migration/`)
- Every table carries `agency_id` — all data is scoped to the authenticated agency via JWT

### Auth
- Stateless JWT: token carries `agency_id`, `user_id`, `role` (ADMIN | AGENT)
- All service-layer queries filter by `agency_id` extracted from the JWT

### Matching engine
- Triggered by a Spring `@EventListener` on `ListingCreatedEvent` (fired after a listing is saved)
- Runs hard filters first (budget, property type, location, required features) — any failure = no match
- Scores passing buyers 0–100 on soft criteria (sqm, bedrooms, floor preference, timeline, price headroom)
- Persists results to `buyer_listing_matches` with a `matched_criteria` JSONB snapshot
- Calls `NotificationService` to send agent email via Resend if matches are found

---

## Frontend

**Framework:** React 18 + TypeScript 5  
**Build tool:** Vite 8 (with `@tailwindcss/vite` plugin)  
**Styling:** Tailwind CSS v4 (CSS-based config in `src/index.css`) + shadcn/ui components  
**State:** TanStack Query v5 (server state), React Router v6 (routing)  
**HTTP:** Axios with JWT interceptor (attaches token to every request, redirects on 401)  
**Charts:** Recharts (dashboard metrics)

### Running the frontend
```bash
cd frontend
npm run dev
```

### Building the frontend
```bash
cd frontend
npm run build
```

### Path alias
`@/` maps to `src/` — configured in both `vite.config.ts` and `tsconfig.app.json`.

### Adding shadcn components
shadcn/ui is configured in `components.json`. Add components with:
```bash
cd frontend
npx shadcn@latest add <component-name>
```
If the CLI fails (known issue with Tailwind v4 detection), copy components manually from [ui.shadcn.com](https://ui.shadcn.com/docs/components).

### Frontend directory structure (current state)
```
src/
├── api/
│   ├── client.ts     Axios instance + JWT interceptor + setApiToken()
│   └── auth.ts       authApi.login / authApi.register
├── components/
│   ├── AppLayout.tsx  Sidebar layout (Dashboard / Buyers / Listings nav)
│   └── ui/            shadcn/ui components (add as needed)
├── context/
│   └── AuthContext.tsx In-memory JWT state; useAuth() hook
├── hooks/             Custom React hooks (add as needed)
├── lib/
│   └── utils.ts       cn() helper (clsx + tailwind-merge)
├── pages/
│   ├── LoginPage.tsx
│   ├── RegisterPage.tsx
│   └── DashboardPage.tsx (placeholder)
└── types/
    └── auth.ts        AuthResponse, LoginRequest, RegisterRequest
```

---

## Infrastructure

| Service  | Hosts                        | Cost                         |
|----------|------------------------------|------------------------------|
| Railway  | Spring Boot JAR + PostgreSQL | Free hobby tier / ~$5/mo     |
| Vercel   | React SPA (Vite build)       | Free tier                    |
| GitHub   | Monorepo (public from Day 1) | Free                         |
| Resend   | Transactional email          | 3,000 emails/month free      |

---

## Domain Model (abbreviated)

| Table                  | Purpose                                                   |
|------------------------|-----------------------------------------------------------|
| `agencies`             | Multi-tenant root                                         |
| `users`                | Agents/admins within an agency                            |
| `buyers`               | Buyer profiles with matching criteria                     |
| `buyer_locations`      | Normalized preferred locations (one buyer → many rows)    |
| `listings`             | Properties the agency is selling                          |
| `buyer_listing_matches`| Persisted match results with score + `matched_criteria`   |
| `notification_log`     | Audit log of every email sent                             |

---

## Feature Build Status

| # | Area | Features | Status |
|---|------|----------|--------|
| 1 | **Auth & Infrastructure** | US-001–003 · FT-001–007 — JWT auth, register/login endpoints + pages, Flyway migrations V1–V7, all JPA entities + repos | ✅ Done |
| 2 | **Buyer Management** | US-004–010 · FT-008–016 — Buyer CRUD backend + frontend, multi-location input, buyer detail page | 🔲 Backlog |
| 3 | **Listing Management** | US-011–015 · FT-017–025 — Listing CRUD backend + frontend, listing detail + match view | 🔲 Backlog |
| 4 | **Matching Engine** | US-016–019 · FT-024–026 — ListingCreatedEvent, MatchingService (hard filters + scoring), match action PATCH | 🔲 Backlog |
| 5 | **Notifications** | US-021–022 · FT-028–030 — Resend email dispatch, match email template, cross-agent logic | 🔲 Backlog |
| 6 | **Dashboard** | US-024–026 · FT-032–034 — Metrics endpoint, dashboard page, cold buyers list | 🔲 Backlog |
| 7 | **Nice-to-have** | US-020, US-023, US-027 · FT-027, FT-031 — Retroactive match, cold buyer digest, admin stats | 🔲 Backlog |

> Feature planning tracked in `build-3-user-stories-and-features.xlsx` at repo root.

---

## Notes

- The backend was initialised with Spring Boot **4.1.0** (the spec mentioned 3.3 — actual project uses 4.1.0).
- The backend uses **Gradle** (Kotlin DSL), not Maven as originally noted in the business case.
- Tailwind CSS v4 uses CSS-based configuration — there is no `tailwind.config.js`. All theme tokens live in `src/index.css` under `@layer base`.
- `noUnusedLocals` and `noUnusedParameters` are enabled in `tsconfig.app.json` — prefix unused variables with `_` to suppress errors during development.
