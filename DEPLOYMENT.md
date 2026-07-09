# Render + Neon Deployment

## Required Environment Variables

| Variable | Description | Example |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | Activates the docker profile | `docker` |
| `DB_URL` | Neon PostgreSQL connection URL | `jdbc:postgresql://ep-xxx.neon.tech/reservations_db?sslmode=require` |
| `DB_USERNAME` | Database user | `neondb_owner` |
| `DB_PASSWORD` | Database password | `your-neon-password` |
| `DDL_AUTO` | Hibernate schema mode | `validate` or `update` |

## Notes

- `DB_POOL_SIZE` defaults to `5` (suitable for Neon free tier).
- Neon requires `?sslmode=require` appended to the JDBC URL.
- The Dockerfile sets `SPRING_PROFILES_ACTIVE=docker` by default; override it in Render's environment settings if needed.
