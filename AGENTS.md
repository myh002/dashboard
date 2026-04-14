# AGENTS.md

## Project Structure

Monorepo with two independent subprojects:
- `dashboard-vue/` — Vue 3 frontend (port 5173 dev, proxies `/api` to backend:8080)
- `dashboard-spring/` — Spring Boot backend (port 8080)
- `Docker/` — docker-compose for MySQL + backend + frontend

## Frontend (dashboard-vue)

```bash
npm run dev      # Dev server at http://localhost:5173
npm run build    # TypeScript check via vue-tsc, then Vite build → dist/
npm run test     # Vitest with jsdom (single run)
npm run test:watch
```

- **SCSS**: Vite config uses `api: 'modern-compiler'` with `@use` (not `@import`). Add SCSS vars/mixins via `additionalData`.
- **No typecheck command** — `npm run build` runs `vue-tsc` as part of the build step.
- **API base URL**: Configure via `VITE_API_BASE_URL` env var (default `/api`).

## Backend (dashboard-spring)

```bash
mvn spring-boot:run          # Dev
mvn clean package -DskipTests # Build jar
```

- **Java 21** (pom.xml override, not Java 17 as README suggests)
- **MyBatis-Plus**: XML mappers at `src/main/resources/mapper/*.xml`
- **Cache**: Caffeine (30min expiry, 500 max entries) — configured in `application.yml`
- **DB defaults**: MySQL at `172.20.8.28:3306/dashboard`, user `appuser`

## Docker

```bash
cd Docker && docker-compose up -d
```

- MySQL (3306) → backend (8080) → frontend (80)
- Backend healthcheck: `curl -f http://localhost:8080/actuator/health`
- `.env` in `Docker/` is git-ignored — do not commit secrets

## Testing

- Frontend: Vitest with jsdom, globals enabled. No other test framework.
- Backend: `mvn test` (not run by default in `package` goal, use `-DskipTests` to skip)

## API Response Format

All endpoints return `ResultData<T>`: `{ code: 0, message: "success", data: {...} }`. Code non-zero = error.

## Key File Locations

| File | Purpose |
|------|---------|
| `dashboard-vue/vite.config.ts` | Vite proxy, SCSS compiler options |
| `dashboard-spring/src/main/resources/application.yml` | DB, cache, MyBatis config |
| `Docker/docker-compose.yml` | Full stack orchestration |
