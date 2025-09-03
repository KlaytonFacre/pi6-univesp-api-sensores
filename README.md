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
```json{
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

http://localhost:8080/swagger-ui.html

🚀 Como executar o projeto
Pré-requisitos:

- JDK 17+
- Maven 3.9+
- MySQL 8 com extensão espacial habilitada

## Passos
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

## Grupo 9 – UNIVESP

- Fernando Augusto Alves Garcia
- João Bosco
- Klayton da Cruz Facre Monteiro
- André Felipe Ribeiro Alves
- Isaias Luiz de Brito Mathias

📖 Observação: Este repositório faz parte do Projeto Integrador 6, componente curricular da UNIVESP.