# Dashboard Spring Backend

学院数据可视化大屏后端服务，基于 Spring Boot 3.2 + MyBatis-Plus + MySQL 构建，为前端 Vue 大屏页面提供数据接口。

## Tech Stack

- **Framework**: Spring Boot 3.2.4
- **ORM**: MyBatis-Plus 3.5.6
- **Database**: MySQL 8.3
- **Language**: Java 17
- **Build**: Maven 3.9

## Project Structure

```
src/main/java/com/dashboard/
├── controller/          # RESTful API Controllers
├── service/             # Service Interfaces
│   └── impl/            # Service Implementations
├── mapper/              # MyBatis Mapper Interfaces
├── pojo/                # Data Objects
│   ├── dto/             # Data Transfer Objects
│   └── entity/          # MyBatis-Plus Entity
└── common/              # Common Classes (ResultData, etc.)
```

## API Endpoints

All APIs return unified `ResultData<T>` wrapped responses.

### Faculty (/api/faculty)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/faculty?year=2025` | Get faculty data for specified year |

### Research (/api/research)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/research?year=2025` | Get research data for specified year |

### Discipline (/api/discipline)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/discipline?year=2025` | Get discipline data for specified year |

### Talent (/api/talent)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/talent?year=2025` | Get talent data for specified year |
| GET | `/api/talent/yearly` | Get yearly talent data (2020–2025) |

### Condition (/api/condition)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/condition?year=2025` | Get condition data for specified year |

### Finance (/api/finance)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/finance` | Get yearly financial data (2020–2025) |

## Data Sources

Data is fetched from multiple department metric tables via MyBatis mappers:

| Department | Metrics | Description |
|------------|---------|-------------|
| 国资处 | 134–142 | Land, lab, teaching area, fixed assets, equipment |
| 图书馆 | 49–51 | Books, e-books, databases |
| 校办 | 47 | Department count |
| 教务处 | 65–82, 97–101 | Undergraduate, courses, reforms |
| 研究生院 | 102–133 | Master/PhD students, supervisors |
| 外事办 | 132–133 | International programs |
| 科技处 | 11 | Research funding |

## Configuration

Configuration is managed via `application.yml` and environment variables:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:mysql://host:3306/dashboard}
    username: ${SPRING_DATASOURCE_USERNAME:appuser}
    password: ${SPRING_DATASOURCE_PASSWORD:password}
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | MySQL JDBC URL | `jdbc:mysql://172.20.8.28:3306/dashboard` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `appuser` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `myh10086` |

## Getting Started

### Prerequisites

- JDK 17+
- MySQL 8+
- Maven 3.9+

### Local Development

1. Configure database connection in `src/main/resources/application.yml`
2. Run the application:

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

### Build

```bash
mvn clean package -DskipTests
```

Output: `target/dashboard-spring-1.0.0.jar`

## Docker Deployment

```bash
cd Docker
docker-compose up -d
```

This starts three services:

- **mysql**: MySQL 8.3 on port 3306
- **backend**: Spring Boot on port 8080
- **frontend**: Nginx on port 80 (proxies `/api` to backend)

### Docker Resource Limits

| Service | CPU Limit | Memory Limit |
|---------|-----------|--------------|
| MySQL | 1.0 core | 1024 MB |
| Backend | 1.0 core | 512 MB |
| Frontend | 0.5 core | 256 MB |

## Response Format

All API responses follow this structure:

```json
{
  "code": 0,
  "message": "success",
  "data": { ... }
}
```

- `code: 0` — Success
- `code: non-zero` — Error (message field contains error detail)
