# FuTrack Spring Boot API

Spring Boot REST API for applications. PostgreSQL is the runtime database; the test profile uses in-memory H2.

## Start the database and API

From the repository root, start PostgreSQL with Docker Compose:

```sh
docker compose up -d postgres
```

Then run the API from this `backend` folder with Java 17+:

```sh
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd` in place of `./mvnw`. The API is available at `http://localhost:8080`. Check `GET /api/health` before importing the Postman collection. PostgreSQL defaults are for local development only; override `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` for another environment. For API QA without Docker, run `./mvnw spring-boot:run -Dspring-boot.run.profiles=h2` to use a temporary in-memory H2 database; PostgreSQL remains the default.

## Endpoints

| Method | Path | Purpose |
| --- | --- | --- |
| GET | `/api/health` | Service status |
| GET | `/api/applications?q=&status=&type=&sort=` | List and filter |
| POST | `/api/applications` | Create |
| GET | `/api/applications/{id}` | Read one |
| PUT | `/api/applications/{id}` | Replace fields |
| PATCH | `/api/applications/{id}/status` | Update status |
| DELETE | `/api/applications/{id}` | Delete |

The Postman collection is in `../postman/FuTrack API.postman_collection.json`.

## Tests

Run `./mvnw test` (or `mvnw.cmd test` on Windows). The test profile uses H2 and covers create, search, status update, delete, and validation. Authentication, user accounts, and real resume file storage are not implemented yet; the browser's sign-in is still demo-only.
