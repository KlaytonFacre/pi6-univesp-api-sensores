# 📡 PI6 – Plataforma de Monitoramento de Ruído Urbano

Este repositório contém o código-fonte da **API de Sensores** desenvolvida pelo grupo 9 no **Projeto Integrador 6 (UNIVESP)**.  
O objetivo é criar uma solução de hardware e software escalável para **monitorar a poluição sonora** em ambientes urbanos, permitindo o registro e análise de medições de ruído associadas a dados de geolocalização (latitude/longitude).

---

## 🎯 Objetivos do Projeto Integrador

Tema:
> **"Desenvolver uma plataforma de hardware e/ou software escalável que solucione um problema local. Utilizar as metodologias de desenvolvimento de software e hardware vistas até o momento. Desenvolver um plano de negócios para o resultado do projeto."**

Principais metas do grupo:
- Propor uma solução que auxilie na identificação e no acompanhamento de **poluição sonora** em áreas urbanas.
- Desenvolver um **sistema distribuído** onde sensores de baixo custo (ESP32) coletam dados de **nível de ruído (dB)** e os enviam para uma **API centralizada**.
- Estruturar um **plano de negócios** para garantir a viabilidade da plataforma.
- Utilizar metodologias de desenvolvimento vistas até o momento, com ênfase em **boas práticas de engenharia de software**.

---

## 🛠️ Stack Tecnológica

- **Backend**: [Spring Boot 3.5.5](https://spring.io/projects/spring-boot)
- **Banco de Dados**: MySQL 8 (com suporte espacial via Hibernate Spatial)
- **ORM**: Hibernate / JPA
- **Migrations**: Flyway
- **Geoprocessamento**: [JTS Topology Suite](https://locationtech.github.io/jts/)
- **Validação**: Bean Validation (Jakarta Validation)
- **Documentação**: Swagger/OpenAPI (via Springdoc)
- **Gerenciamento de dependências**: Maven
- **Hardware integrado**: ESP32 coletando medições de ruído e coordenadas (lat/long)
- **Controle de versão**: Git + GitHub

---

## 📐 Arquitetura

1. **ESP32**
    - Realiza a leitura do nível de ruído em dB.
    - Envia os dados via Wi-Fi para a API REST.
    - Inclui no payload as coordenadas de geolocalização.

2. **API de Sensores (este projeto)**
    - Exposta em REST (JSON).
    - Recebe os dados de medição (`sensorId`, `latitude`, `longitude`, `noiseDb`).
    - Converte coordenadas em objeto geoespacial (`Point` – SRID 4326).
    - Persiste no MySQL com índices espaciais.
    - Retorna resposta simplificada ao cliente.

3. **Banco de Dados MySQL + Hibernate Spatial**
    - Tabela `medicao` com índice espacial (`SPATIAL INDEX`) para consultas rápidas.
    - Suporte a consultas geográficas (pontos dentro de raio, área, proximidade).

---

## 📊 Endpoints Principais

### `POST /medicoes`
Recebe uma medição enviada por um sensor.

**Request (JSON):**
```json
{
  "sensorId": 1,
  "latitude": -23.55052,
  "longitude": -46.633308,
  "noiseDb": 72.5
}
```

**Response (JSON):**
```json
{
  "id": 1,
  "timestamp": "2025-09-02T21:15:30",
  "sensorId": 1,
  "latitude": -23.55052,
  "longitude": -46.633308,
  "noiseDb": 72.5
}
```

## Swagger UI

Após iniciar a aplicação, a documentação interativa estará disponível em:

`http://localhost:8080/swagger-ui.html`

## Passos para rodar localmente usando maven
Pré-requisitos:

- JDK 17+
- Maven 3.9+
- MySQL 8 com extensão espacial habilitada

### Clonar o repositório
```bash
git clone https://github.com/KlaytonFacre/pi6-univesp-api-sensores.git
cd pi6-univesp-api-sensores
```

### Rodar migrations do Flyway e iniciar a API
```bash
mvn spring-boot:run
```
A aplicação sobe por padrão em http://localhost:8080.

## Como rodar com Docker (passo a passo)

Esta seção explica como executar a API em containers, reaproveitando um MySQL já existente (ex.: MySQL8) e garantindo configuração por variáveis de ambiente no perfil prod.

### Pré-requisitos

- Docker / Docker Desktop instalado.
- Um MySQL 8.x acessível:
  - Pode ser um container já existente (ex.: MySQL8) ou um banco externo.
  - O schema (banco) de aplicação (ex.: api_sensores) deve existir.
  - Recomendado: usuário específico do app (evitar root em produção).

Para criar rapidamente um usuário e dar acesso ao schema:
```bash
docker exec -it MySQL8 mysql -uroot -p \
  -e "CREATE USER IF NOT EXISTS 'api_user'@'%' IDENTIFIED BY 'ApiPassw0rd!'; \
      CREATE DATABASE IF NOT EXISTS api_sensores; \
      GRANT ALL PRIVILEGES ON api_sensores.* TO 'api_user'@'%'; \
      FLUSH PRIVILEGES;"
```

### Variáveis de Ambiente (obrigatórias)

A imagem exige as quatro variáveis (validadas no entrypoint), além do SPRING_PROFILES_ACTIVE=prod:

- `DATABASE_HOST` – host do MySQL (ex.: MySQL8, db, host.docker.internal, IP, etc.)
- `DATABASE_NAME` – nome do schema (ex.: api_sensores)
- `DATABASE_USER` – usuário do banco (ex.: api_user)
- `DATABASE_PASSWORD` – senha do banco

No `application-prod.properties` a URL é montada como:
```dockerfile
jdbc:mysql://${DATABASE_HOST}:${DATABASE_PORT:3306}/${DATABASE_NAME}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo

```

### Opção A) Usando seu MySQL já existente em rede bridge compartilhada (recomendado)

Crie (uma vez) uma rede bridge definida pelo usuário:

```bash
docker network create pi6-net
```

Conecte seu container MySQL existente a essa rede:

```bash
docker network connect pi6-net MySQL8
```

Suba a API na mesma rede, passando as variáveis obrigatórias:

```bash
docker run -d --name pi6-api --network pi6-net -p 8080:8080 \
-e SPRING_PROFILES_ACTIVE=prod \
-e DATABASE_HOST=MySQL8 \
-e DATABASE_NAME=api_sensores \
-e DATABASE_USER=api_user \
-e DATABASE_PASSWORD=ApiPassw0rd! \
klaytonf/pi6-api-sensores:latest
```


Verifique:

```bash
docker ps --format "table {{.Names}}\t{{.Ports}}\t{{.Status}}"
docker logs -f pi6-api
```


Acesse: `http://localhost:8080`

Se você usa Springdoc/OpenAPI: `http://localhost:8080/swagger-ui/index.html`

Se usa Actuator (opcional): `http://localhost:8080/actuator/health`

Dica: liste/inspecione redes e conexões:

```bash
docker network ls
docker network inspect pi6-net
```

### Opção B) Docker Compose reaproveitando o mesmo MySQL existente (rede externa)

Garanta que a rede pi6-net existe e que o MySQL8 está conectado (passos da Opção A).

Crie um arquivo `.env` (no diretório do compose) com as variáveis:

```dockerfile
DOCKER_NETWORK=pi6-net
DATABASE_HOST=MySQL8
DATABASE_NAME=api_sensores
DATABASE_USER=api_user
DATABASE_PASSWORD=ApiPassw0rd!
```

Crie `docker-compose.yml` apenas da API, importando a rede externa:

```yaml
services:
  api:
    image: klaytonf/pi6-api-sensores:latest
    networks: [appnet]
    ports:
      - "8080:8080"
    environment:
    SPRING_PROFILES_ACTIVE: prod
    DATABASE_HOST: ${DATABASE_HOST:?Defina DATABASE_HOST no .env}
    DATABASE_NAME: ${DATABASE_NAME:?Defina DATABASE_NAME no .env}
    DATABASE_USER: ${DATABASE_USER:?Defina DATABASE_USER no .env}
  DATABASE_PASSWORD: ${DATABASE_PASSWORD:?Defina DATABASE_PASSWORD no .env}

networks:
  appnet:
    external: true
    name: ${DOCKER_NETWORK:?Defina DOCKER_NETWORK no .env}
```


Suba a API:

```bash
docker compose up -d api
docker compose logs -f api
```

### Opção C) MySQL no host (sem container MySQL)

Docker Desktop (Windows/Mac): use `host.docker.internal` como `DATABASE_HOST`.

Linux: use o gateway da bridge (172.17.0.1) ou adicione:

`--add-host=host.docker.internal:host-gateway` e aponte para `host.docker.internal`.

Exemplo:

```bash
docker run -d --name pi6-api -p 8080:8080 \
-e SPRING_PROFILES_ACTIVE=prod \
-e DATABASE_HOST=host.docker.internal \
-e DATABASE_NAME=api_sensores \
-e DATABASE_USER=api_user \
-e DATABASE_PASSWORD=ApiPassw0rd! \
klaytonf/pi6-api-sensores:latest
```

### Opção D) Docker Compose com API + MySQL (ambiente de testes “do zero”)

Ideal **para quem não tem MySQL instalado e só quer subir tudo e testar**.
O Compose cria a rede, o banco e a API automaticamente.

1) Crie o arquivo .env no mesmo diretório do compose (**Não commite este arquivo com senhas reais**).
```bash
# Banco / credenciais de teste
MYSQL_ROOT_PASSWORD=Password01
DATABASE_NAME=api_sensores
DATABASE_USER=api_user
DATABASE_PASSWORD=ApiPassw0rd!

# (opcional) Ajuste de timezone, se quiser
TZ=America/Sao_Paulo
 
```

2) Crie o `docker-compose.yml`
```yaml
services:
  db:
    image: mysql:8.0
    container_name: pi6-db
    command: [
      "--default-authentication-plugin=caching_sha2_password",
      "--character-set-server=utf8mb4",
      "--collation-server=utf8mb4_0900_ai_ci",
      "--skip-log-bin"
    ]
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD:?Defina MYSQL_ROOT_PASSWORD no .env}
      MYSQL_DATABASE: ${DATABASE_NAME:?Defina DATABASE_NAME no .env}
      MYSQL_USER: ${DATABASE_USER:?Defina DATABASE_USER no .env}
      MYSQL_PASSWORD: ${DATABASE_PASSWORD:?Defina DATABASE_PASSWORD no .env}
      TZ: ${TZ:-America/Sao_Paulo}
    volumes:
      - db_data:/var/lib/mysql
      # (opcional) scripts .sql iniciais: criar views, seeds, etc.
      # - ./docker/mysql/init:/docker-entrypoint-initdb.d
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "127.0.0.1", "-uroot", "-p${MYSQL_ROOT_PASSWORD}"]
      interval: 5s
      timeout: 3s
      retries: 20
    restart: unless-stopped

  api:
    image: klaytonf/pi6-api-sensores:latest
    container_name: pi6-api
    depends_on:
      db:
        condition: service_healthy
    environment:
      SPRING_PROFILES_ACTIVE: prod
      # A API exige estas 4 variáveis (validadas no entrypoint)
      DATABASE_HOST: db
      DATABASE_NAME: ${DATABASE_NAME:?Defina DATABASE_NAME no .env}
      DATABASE_USER: ${DATABASE_USER:?Defina DATABASE_USER no .env}
      DATABASE_PASSWORD: ${DATABASE_PASSWORD:?Defina DATABASE_PASSWORD no .env}
      # (opcional) porta padrão 3306 — só defina se quiser mudar
      # DATABASE_PORT: "3306"
      # (opcional) timezone do container
      TZ: ${TZ:-America/Sao_Paulo}
    ports:
      - "8080:8080"
    restart: unless-stopped

volumes:
  db_data:

```

> Por padrão, o Compose cria uma rede bridge interna e a API acessa o MySQL pelo nome do serviço db.
O `depends_on: condition: service_healthy` garante que a API só inicia após o banco estar aceitando conexões.

3) Subir, verificar e acessar
```bash
docker compose up -d
docker compose logs -f db
docker compose logs -f api 
```

- Acesse a API: `http://localhost:8080`
  - Swagger (se habilitado): `http://localhost:8080/swagger-ui/index.html`
  - Actuator (se habilitado): `http://localhost:8080/actuator/health`

4) Atualizar para a última imagem
```bash
docker compose pull
docker compose up -d --force-recreate
```

5) Encerrar e limpar
- Para apenas parar os serviços: `docker compose down`

- Para parar e apagar dados do MySQL (volume): `docker compose down -v`

Notas

- Flyway: as migrações estão empacotadas no JAR e `spring.flyway.enabled=true` está setado no `application-prod.properties`, elas rodam automaticamente na primeira subida.
- Apple Silicon/ARM: se tiver problemas de compatibilidade, adicione platform: linux/amd64 no serviço db (raramente necessário com MySQL 8 oficial).
- Apenas testes: as senhas de exemplo não devem ser usadas em produção. Para produção, use secrets/variáveis seguras e usuário não-root (já contemplado acima com api_user).

### Troubleshooting rápido

1) Porta não abre

- Confirme publicação: docker ps → deve aparecer 0.0.0.0:8080->8080/tcp.
- Garanta que a app está ouvindo em 0.0.0.0 (não 127.0.0.1).

- Cheque logs: docker logs -f pi6-api.

2) Erro de conexão com o banco

- Teste DNS/rede: API e MySQL na mesma rede (ex.: pi6-net).
- Confirme variáveis: DATABASE_* corretas.
- Teste ping ao host do banco a partir da API: `docker exec -it pi6-api sh -lc "getent hosts $DATABASE_HOST || ping -c1 $DATABASE_HOST || true"`
- Verifique permissões do usuário MySQL ('api_user'@'%').

### Notas de Segurança/Prod

- Evite root para a aplicação; use um usuário do app restrito ao schema.
- Não commite senhas: use .env local, variáveis seguras, secrets do orquestrador.
- Backups: se usar volume para o MySQL, configure política de backup do volume.

## Grupo 9 – UNIVESP

- Fernando Augusto Alves Garcia
- João Bosco
- Klayton da Cruz Facre Monteiro
- André Felipe Ribeiro Alves
- Isaias Luiz de Brito Mathias

📖 Observação: Este repositório faz parte do Projeto Integrador 6, componente curricular da UNIVESP.