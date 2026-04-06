# Dashboard Vue Frontend

学院数据可视化大屏前端，基于 Vue 3 + TypeScript + Vite 构建，展示师资、科研、学科、人才、资产等数据。

## Tech Stack

- **Framework**: Vue 3.4 (Composition API + `<script setup>`)
- **Language**: TypeScript 5.4
- **State Management**: Pinia 2.1
- **UI Components**: Element Plus 2.6 + DataV-Vue3 1.7
- **Charts**: ECharts 5.5
- **Build Tool**: Vite 8.0
- **Testing**: Vitest 1.4 + jsdom
- **Styling**: SCSS with CSS variables

## Project Structure

```
src/
├── api/                 # Axios-based API clients
├── components/          # Reusable UI Components
│   └── charts/          # Chart components (Donut, Line, Bar, Rose)
├── layouts/             # Layout components (Navigation)
├── stores/              # Pinia stores
│   ├── data.ts          # Dashboard data store
│   ├── research.ts     # Research data
│   └── service.ts       # Service-related data
├── types/               # TypeScript interfaces
├── utils/               # Utility functions
├── views/Home/          # Home page with all dashboard sections
└── App.vue
```

## Dashboard Modules

| Module | Description |
|--------|-------------|
| 师资队伍 | Faculty count, titles, top talents |
| 科学研究 | Papers, patents, funding trends, projects |
| 学科建设 | Degree points, ESI disciplines, evaluation |
| 人才培养 | Students (undergrad/master/PhD), courses |
| 财政资产 | Campus area, lab area, equipment, books |

## API Integration

### Request Configuration

Base URL is configurable via `VITE_API_BASE_URL` environment variable (defaults to `/api`).

```typescript
// src/api/request.ts
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 15000,
})
```

### Response Interceptor

Unified error handling: responses with `code !== 0` are rejected with the error message.

### Data Flow

```
Backend DB → Service → Controller → API → Store → Vue Component → Page
```

On page mount, `useDataStore.fetchAllData()` parallel-fetches all five data domains:

| Store Action | Endpoint |
|-------------|----------|
| `fetchFacultyData()` | `GET /api/faculty` |
| `fetchResearchData()` | `GET /api/research` |
| `fetchDisciplineData()` | `GET /api/discipline` |
| `fetchTalentData()` | `GET /api/talent` |
| `fetchConditionData()` | `GET /api/condition` |

## Getting Started

### Prerequisites

- Node.js 18+

### Install Dependencies

```bash
npm install
```

### Development

```bash
npm run dev
```

Opens at `http://localhost:5173` (Vite default), with Vite proxy forwarding `/api` requests to the backend.

### Build

```bash
npm run build
```

Outputs static assets to `dist/`, ready for deployment.

### Preview

```bash
npm run preview
```

### Test

```bash
npm run test          # Run tests once
npm run test:watch    # Watch mode
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `VITE_API_BASE_URL` | Backend API base URL | `/api` |

## Docker Deployment

Uses multi-stage build with Nginx Alpine.

```bash
cd Docker
docker-compose up -d
```

Frontend is served on port 80, with `/api` requests proxied to the backend container at `http://backend:8080`.

### Nginx Configuration

- SPA routing: all non-asset requests return `index.html`
- Static assets cached for 1 year
- Backend proxy: `/api` → `http://backend:8080`

### Docker Resource Limits

- CPU: 0.5 cores
- Memory: 256 MB

## Key Components

| Component | Description |
|-----------|-------------|
| `DataCard` | Metric card with single/dual value display |
| `DonutChart` | Donut chart for student level distribution |
| `RoseChart` | Rose chart for faculty title distribution |
| `LineChart` | Multi-series line chart for funding trends |
| `LineChartClean` | Simplified line chart for equipment trends |
| `BarChart` | Bar chart for discipline evaluation distribution |
| `Navigation` | Central navigation panel |
