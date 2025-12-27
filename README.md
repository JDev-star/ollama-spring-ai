# AI Advisor (Spring Boot + Spring AI + Ollama) — Offline Exception Fix Recommendations

This project demonstrates a fully offline setup where a Spring Boot application:
- exposes a simple AI query endpoint (`/ai/query`) backed by **Ollama**
- intentionally throws exceptions (e.g., `/null`)
- generates **AI repair advice asynchronously** and stores it in memory
- lets you poll the advice later via `/ai/advice/{id}`

No external internet calls are required at runtime. The LLM runs locally via Ollama.

---

## Modules

- `ai-advisor-starter`
    - auto-configures a `ChatClient`
    - provides exception handling (`@RestControllerAdvice`) to generate advice
    - provides `AiAdviceStore` (in-memory)

- `ai-advisor-web`
    - demo web app exposing endpoints:
        - `/system/status`
        - `/ai/query`
        - `/null`
        - `/ai/advice/{id}`

---

## Requirements

- Java 21
- Maven 3.9+
- Docker + Docker Compose (recommended) for Ollama

---

## 1) Start Ollama (Docker)

### Docker Compose


```yaml
version: "3.9"

services:
  ollama:
    image: ollama/ollama:latest
    container_name: ollama
    restart: unless-stopped
    ports:
      - "11434:11434"
    volumes:
      - ollama-data:/root/.ollama

volumes:
  ollama-data:
```
### bash   
docker compose up -d