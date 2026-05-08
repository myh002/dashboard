# AGENTS.md

## Project Structure

Monorepo with two independent subprojects:

- `dashboard-vue/` — Vue 3 frontend (port 5173 dev, proxies `/api` to backend:8080)
- `dashboard-spring/` — Spring Boot backend (port 8080)
- `Docker/` — docker-compose for MySQL + backend + frontend

## Frontend (dashboard-vue)

### Development Commands

```bash
npm run dev      # Dev server at http://localhost:5173
npm run build    # TypeScript check via vue-tsc, then Vite build → dist/
npm run test     # Vitest with jsdom (single run)
npm run test:watch
```

- **SCSS**: Vite config uses `api: 'modern-compiler'` with `@use` (not `@import`). Add SCSS vars/mixins via `additionalData`.
- **No typecheck command** — `npm run build` runs `vue-tsc` as part of the build step.
- **API base URL**: Configure via `VITE_API_BASE_URL` env var (default `/api`).

### Frontend Pages (d:\project\dashboard\dashboard-vue\src\views)

| 页面             | 路由            | 主要数据来源                                           |
| -------------- | ------------- | ------------------------------------------------ |
| **Home**       | `/`           | faculty, talent, condition, research, discipline |
| **Faculty**    | `/faculty`    | 教师队伍（职称、学历、导师）                                   |
| **Talent**     | `/talent`     | 人才培养（学生数、毕业就业）                                   |
| **Discipline** | `/discipline` | 学科建设（学位点、专业、学科评估）                                |
| **Research**   | `/research`   | 科学研究（项目、论文、专利）                                   |
| **Service**    | `/service`    | 社会服务（就业、国际交流）                                    |
| **Finance**    | `/finance`    | 财务资产（经费、场地、设备）                                   |
| **Hr**         | `/hr`         | 人事信息（人员统计、人才队伍）                                  |
| **Party**      | `/party`      | 党建信息（党员、统战、青年团）                                  |
| **Safety**     | `/safety`     | 安全统计                                             |

## Backend (dashboard-spring)

### Development Commands

```bash
mvn spring-boot:run          # Dev
mvn clean package -DskipTests # Build jar
```

- **Java 21** (pom.xml override, not Java 17 as README suggests)
- **MyBatis-Plus**: XML mappers at `src/main/resources/mapper/*.xml`
- **Cache**: Caffeine (30min expiry, 500 max entries) — configured in `application.yml`
- **DB defaults**: MySQL at `172.20.8.28:3306/dashboard`, user `appuser`

### Backend Services & Controllers

| Service               | Controller           | API路径             | 主要功能           |
| --------------------- | -------------------- | ----------------- | -------------- |
| FacultyServiceImpl    | FacultyController    | `/api/faculty`    | 教师队伍、职称分布、导师数据 |
| TalentServiceImpl     | TalentController     | `/api/talent`     | 学生统计、培养条件、国际化  |
| DisciplineServiceImpl | DisciplineController | `/api/discipline` | 学科评估、学位点、专业设置  |
| ResearchServiceImpl   | ResearchController   | `/api/research`   | 科研项目、论文、专利、奖项  |
| ServiceServiceImpl    | ServiceController    | `/api/service`    | 就业数据、国际交流、社会服务 |
| FinanceServiceImpl    | FinanceController    | `/api/finance`    | 经费趋势、资产概况、场地数据 |
| HrServiceImpl         | HrController         | `/api/hr`         | 人事统计、人才称号、团队数据 |
| PartyServiceImpl      | PartyController      | `/api/party`      | 党员数据、统战工作、青年团  |
| ConditionServiceImpl  | ConditionController  | `/api/condition`  | 办学条件、资产设备      |

## Database

### Tables

- `dict_metric` — 指标字典（id, category\_id, metric\_name, unit）
- `dict_category` — 指标分类（id, dept\_name, category\_name）
- `dept_stats` — 部门统计数据（metric\_id, dept\_name, year, value）

### Key Departments (dict\_category.dept\_name)

- 国际教育学院、科技处、人事处、统战部、校办、教务处、研究生院、图书馆、国资处、组织部

### Data Years

- 可用年份：2020, 2021, 2022, 2023, 2024
- 默认查询年份：2024

## MySQL MCP Tool Usage

### Available MCP: `mcp_MySQL_execute_sql`

### Example Queries

**1. 查询指标字典**

```sql
SELECT id, metric_name, unit FROM dict_metric WHERE category_id = 16 ORDER BY id
```

**2. 查询某部门某年的所有指标数据**

```sql
SELECT d.metric_id, m.metric_name, d.value
FROM dept_stats d
JOIN dict_metric m ON d.metric_id = m.id
WHERE d.dept_name = '人事处' AND d.year = 2024
ORDER BY d.metric_id
```

**3. 查询特定指标数据**

```sql
SELECT d.metric_id, m.metric_name, d.year, d.value
FROM dept_stats d
JOIN dict_metric m ON d.metric_id = m.id
WHERE d.metric_id IN (42, 43, 50, 51, 52) AND d.year = 2024
```

**4. 查询某分类下的所有指标**

```sql
SELECT m.id, m.metric_name, d.year, d.value
FROM dict_metric m
LEFT JOIN dept_stats d ON m.id = d.metric_id AND d.year = 2024
WHERE m.category_id = 22
ORDER BY m.id
```

**5. 检查指标数据是否为空**

```sql
SELECT d.metric_id, m.metric_name, d.value
FROM dept_stats d
JOIN dict_metric m ON d.metric_id = m.id
WHERE d.dept_name = '教务处' AND d.year = 2024 AND d.value IS NULL
```

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

| File                                                  | Purpose                           |
| ----------------------------------------------------- | --------------------------------- |
| `dashboard-vue/vite.config.ts`                        | Vite proxy, SCSS compiler options |
| `dashboard-spring/src/main/resources/application.yml` | DB, cache, MyBatis config         |
| `Docker/docker-compose.yml`                           | Full stack orchestration          |

## Important Notes

### Data Issues & Solutions

- 数据库中部分指标值为 NULL 或 0，需要使用 `IFNULL(s.value, 0)` 处理
- 指标ID在不同数据版本间可能变化，修改前需先查询数据库确认
- 使用 `intOf()` 和 `longOf()` 工具方法处理可能的 NULL 返回值

### Metric ID Reference (v2 Database)

| 部门     | 指标ID范围  | 主要指标                                                  |
| ------ | ------- | ----------------------------------------------------- |
| 人事处    | 42-54   | 教师数(42), 专任教师(43), 职称(50-53), 外聘教师(54)                |
| 教务处    | 67-107  | 学生(71-72), 专业(78-82), 特色专业(90-96), 教改(97-107)         |
| 研究生院   | 108-129 | 导师(108-109), 研究生(110-113), 学位点(119-123), 就业(128-129)  |
| 科技处    | 12-41   | 项目(12-16), 平台(17-21), 经费(22-23), 论文(27-34), 专利(36-41) |
| 国资处    | 3-11    | 面积(3,5-7), 设备(8-11)                                   |
| 组织部    | 68      | 党员数                                                   |
| 统战部    | 59-66   | 民主党派                                                  |
| 校办     | 67      | 学院数                                                   |
| 图书馆    | 29, 131 | 数据库(29), 图书(131)                                      |
| 国际教育学院 | 1-2     | 合作办学, 国际生                                             |

### Metric ID Reference - Commonly Used

| 指标名称     | 部门   | 指标ID    |
| -------- | ---- | ------- |
| 总教师数     | 人事处  | 42      |
| 专任教师数    | 人事处  | 43      |
| 正高级职称    | 人事处  | 50      |
| 副高级职称    | 人事处  | 51      |
| 本科专业总数   | 教务处  | 78      |
| 省级教学成果奖  | 教务处  | 103     |
| 省级教改工程项目 | 教务处  | 105     |
| 硕士生导师数   | 研究生院 | 108     |
| 博士生导师数   | 研究生院 | 109     |
| 就业人数/率   | 研究生院 | 128/129 |
| 固定资产     | 国资处  | 7       |
| 设备价值     | 国资处  | 9       |
| 数据库数量    | 图书馆  | 29      |
| 图书总册数    | 图书馆  | 131     |

<br />

重要！！！完成代码修改后查看修改，对代码进行审查保证数据正常渲染
