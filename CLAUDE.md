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

### Key dependencies (to be added as needed)
- `spring-boot-starter-data-jpa` — JPA / Hibernate ORM
- `spring-boot-starter-webmvc` — REST controllers
- `spring-boot-starter-security` — Spring Security + JWT auth
- PostgreSQL JDBC driver
- Flyway — schema migrations
- Lombok — boilerplate reduction (`@Data`, `@Builder`, etc.)
- MapStruct — compile-time entity↔DTO mappers

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

### Key directories (to be created as features are built)
```
src/
├── api/          Axios instance + typed API call functions
├── components/
│   └── ui/       shadcn/ui components
├── hooks/        Custom React hooks
├── lib/
│   └── utils.ts  cn() helper (clsx + tailwind-merge)
├── pages/        Route-level page components
└── types/        Shared TypeScript types mirroring backend DTOs
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

## Core Features (build order)

1. **Auth** — JWT login, agency registration
2. **Buyer CRUD** — profiles with criteria + preferred locations
3. **Listing CRUD** — property details + `ListingCreatedEvent` trigger
4. **Matching Engine** — `MatchingService` listener, hard filters + soft scoring
5. **Notification emails** — Resend integration, `NotificationLog`
6. **Match action tracking** — PENDING → CONTACTED → VIEWING_SCHEDULED → DISMISSED
7. **Dashboard** — stat cards, recent listings with match counts, cold buyer list
8. **(Nice-to-have)** Retroactive match on new buyer add
9. **(Nice-to-have)** Cold buyer alert — weekly scheduled email

---

## Notes

- The backend was initialised with Spring Boot **4.1.0** (the spec mentioned 3.3 — actual project uses 4.1.0).
- The backend uses **Gradle** (Kotlin DSL), not Maven as originally noted in the business case.
- Tailwind CSS v4 uses CSS-based configuration — there is no `tailwind.config.js`. All theme tokens live in `src/index.css` under `@layer base`.
- `noUnusedLocals` and `noUnusedParameters` are enabled in `tsconfig.app.json` — prefix unused variables with `_` to suppress errors during development.
