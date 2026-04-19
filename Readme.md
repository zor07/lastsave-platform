# lastsave-platform

Backend service for the lastsave project.

## Stack

- Kotlin + Spring Boot 3
- PostgreSQL
- Docker
- Telegram Bot
- GitHub OAuth

## Development
```bash
docker compose up -d
```

## Environment Variables

| Variable | Required | Default | Description |
|---|---|---|---|
| `DB_HOST` | yes | — | PostgreSQL host |
| `DB_PORT` | yes | — | PostgreSQL port |
| `DB_NAME` | yes | — | Database name |
| `DB_USER` | yes | — | Database user |
| `DB_PASSWORD` | yes | — | Database password |
| `GITHUB_TOKEN` | yes | — | Fine-grained PAT for GitHub API (repo management, secrets, collaborators) |
| `GITHUB_PR_TOKEN` | yes | — | Classic PAT for reading PR diffs (`repo` scope required) |
| `GITHUB_ORG` | yes | — | GitHub organization name |
| `GITHUB_CLIENT_ID` | yes | — | GitHub OAuth App client ID |
| `GITHUB_CLIENT_SECRET` | yes | — | GitHub OAuth App client secret |
| `TELEGRAM_BOT_TOKEN` | yes | — | Telegram bot token from @BotFather |
| `TELEGRAM_BOT_USERNAME` | yes | — | Telegram bot username |
| `TELEGRAM_BOT_PROXY_HOST` | no | — | SOCKS5 proxy host for Telegram (needed when Telegram is blocked) |
| `TELEGRAM_BOT_PROXY_PORT` | no | `0` | SOCKS5 proxy port |
| `APP_BASE_URL` | yes | — | Public base URL of the platform (injected into student repos as Actions secret) |
| `CI_PR_WEBHOOK_TOKEN` | yes | — | Shared secret between platform and student repo CI for PR review webhooks |
| `OPENAI_API_KEY` | yes | — | OpenAI API key for AI code review |
| `OPENAI_BASE_URL` | no | `https://api.proxyapi.ru/openai/v1` | OpenAI-compatible API base URL |
| `OPENAI_MODEL` | no | `gpt-4o` | Model to use for AI code review |
| `SCHEDULER_ENABLED` | no | `false` | Enable progress scheduler (auto-enabled in `prod` profile) |
| `SPRING_PROFILES_ACTIVE` | no | — | Set to `prod` to enable scheduler unconditionally |

## API

Swagger UI доступен по адресу: `http://localhost:8080/swagger-ui/index.html`

## License

MIT
