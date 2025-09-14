# 📡 PI6 – API de Sensores

API REST desenvolvida pelo **Grupo 9 da UNIVESP** para o *Projeto Integrador 6*.  
Ela recebe medições de ruído coletadas por sensores ESP32, persiste os dados em um banco MySQL com suporte a tipos espaciais e expõe endpoints para consulta.

---

## 🔍 Visão Geral

- Sensores registram nível de ruído (LAeq, Lmax, Lmin) e coordenadas geográficas.
- Cada registro é salvo com `UUID` público para consulta posterior.
- Os dados são armazenados em `POINT SRID 4326`, permitindo consultas geoespaciais.
- Documentação interativa com **Swagger**.

---

## 🛠 Stack Tecnológica

| Camada     | Tecnologia                                                |
|------------|-----------------------------------------------------------|
| Linguagem  | Java 17                                                   |
| Framework  | Spring Boot 3.5.5 (Web, Validation, Data JPA, Security)   |
| Banco      | MySQL 8 + Hibernate Spatial + JTS                         |
| Migrações  | Flyway                                                    |
| Docs API   | SpringDoc OpenAPI                                         |
| Build      | Maven                                                     |
| Container  | Docker / Docker Compose                                   |

---

## 📐 Arquitetura
1) Sensor (ESP32)
- Mede o nível de ruído.
- Obtém latitude/longitude.
- Envia POST /noise para a API.
2) API de Sensores
- Valida o payload.
- Converte latitude/longitude para Point (SRID 4326).
- Persiste a medição com referência ao sensor.
- Retorna o identificador público (samplePublicId).
3) MySQL + Hibernate Spatial
- Guarda os dados em tabelas com índices geoespaciais.
- Permite consultas por ponto, raio ou áreas específicas.

---

## 📂 Estrutura de Domínio

- **Owner** – responsável por sensores, com vínculo opcional a `UserAccount`.
- **Sensor** – pertence a um `Owner` e registra leituras.
- **NoiseSample** – amostra de ruído (localização, LAeq, Lmax, Lmin, janela).
- **UserAccount** – contas do sistema (para autenticação futura).

Todas as entidades estendem `AbstractBaseEntity`, que provê:
UUID público, auditoria (`createdAt`, `updatedAt`, etc.) e `version` para controle de concorrência.

---

## 🔌 Endpoints Principais

### `POST /noise`
Recebe uma medição de ruído.

```json
{
  "sensorId": 1,
  "latitude": -23.55052,
  "longitude": -46.633308,
  "laeq": 72.5,
  "lmax": 81.0,
  "lmin": 60.0,
  "windowSeconds": 1,
  "capturedAt": "2025-09-02T21:15:30Z"
}
```
Resposta
```json
{
  "samplePublicId": "a3b1…",
  "sensorPublicId": "c9de…",
  "capturedAt": "2025-09-02T21:15:30Z",
  "laeq": 72.5,
  "lmax": 81.0,
  "lmin": 60.0,
  "windowSeconds": 1,
  "latitude": -23.55052,
  "longitude": -46.633308
}
```
### `GET /noise/{publicId}`
Retorna a amostra correspondente ao `UUID` informado.

### `GET /hello`
Endpoint simples usado para verificação do ambiente.

> A documentação Swagger fica disponível em http://localhost:8080/swagger-ui.html ou em produção no caminho /swagger-ui/index.html.

---

## ▶️ Executando Localmente (Maven)
Pré-requisitos
- JDK 17+
- Maven 3.9+
- MySQL 8 com suporte a dados espaciais
```bash
git clone https://github.com/KlaytonFacre/pi6-univesp-api-sensores.git
cd pi6-univesp-api-sensores

# ajustar credenciais em src/main/resources/application.properties
mvn spring-boot:run
```
A aplicação sobe em `http://localhost:8080` e o Flyway executa as migrações automaticamente.

---

## 🐳 Executando com Docker
### Variáveis obrigatórias
```nginx
DATABASE_HOST
DATABASE_NAME
DATABASE_USER
DATABASE_PASSWORD
```
### Docker Compose (exemplo)
```yaml
services:
  db:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: rootpwd
      MYSQL_DATABASE: api_sensores
    volumes:
      - db_data:/var/lib/mysql

  api:
    image: klaytonf/pi6-api-sensores:latest
    depends_on:
      - db
    environment:
      SPRING_PROFILES_ACTIVE: prod
      DATABASE_HOST: db
      DATABASE_NAME: api_sensores
      DATABASE_USER: root
      DATABASE_PASSWORD: rootpwd
    ports:
      - "8080:8080"

volumes:
  db_data:
```
```bash
docker compose up -d
```

---

## 🤝 Contribuição
1) Faça um fork do repositório.
2) Crie uma branch para sua feature ou correção.
3) Execute mvn test antes de enviar.
4) Abra um Pull Request descrevendo a motivação da mudança.

---

## 👥 Grupo 9 – UNIVESP
- Fernando Augusto Alves Garcia
- João Bosco
- Klayton da Cruz Facre Monteiro
- André Felipe Ribeiro Alves
- Isaias Luiz de Brito Mathias

_📘 Este repositório compõe o Projeto Integrador 6 da UNIVESP._