# maquina-de-estados

Ejemplo mínimo de una máquina de estados en Spring Boot basado en el ejemplo conceptual de pedidos de la página **Máquinas de estados — Fundamentos teóricos**:

> **estado actual + evento = nuevo estado**

El pedido empieza en `CREATED`. La máquina decide qué eventos son válidos y cuál es el siguiente estado.

## Flujo principal

```text
CREATED -- PAY --> PAID -- SHIP --> SHIPPED
```

También existen estas transiciones:

```text
CREATED -- CANCEL --> CANCELLED
PAID    -- REFUND --> REFUNDED
```

## Tabla de transiciones

| Estado actual | Evento | Condición conceptual | Nuevo estado |
|---|---|---|---|
| `CREATED` | `PAY` | pago autorizado | `PAID` |
| `CREATED` | `CANCEL` | — | `CANCELLED` |
| `PAID` | `SHIP` | stock disponible | `SHIPPED` |
| `PAID` | `REFUND` | — | `REFUNDED` |

Las condiciones aparecen como parte del modelo conceptual de la web; este proyecto mínimo se concentra en la tabla de estados/eventos y asume que esas validaciones externas ya se han realizado.

Una combinación no incluida en la tabla produce `InvalidTransitionException`.

## Requisitos

- Java 21
- Maven 3.9+
- Spring Boot 4.1.1

## Ejecutar

```bash
mvn spring-boot:run
```

## Tests

```bash
mvn test
```

## API de ejemplo

### 1. Crear un pedido

```bash
curl -X POST http://localhost:8080/api/orders
```

Respuesta:

```json
{
  "id": "2e39d8e0-08d1-4bed-b141-375ba6d06f9a",
  "state": "CREATED"
}
```

### 2. Pagar el pedido

```bash
curl -X POST http://localhost:8080/api/orders/<ID>/events \
  -H 'Content-Type: application/json' \
  -d '{"event":"PAY"}'
```

### 3. Enviar el pedido

```bash
curl -X POST http://localhost:8080/api/orders/<ID>/events \
  -H 'Content-Type: application/json' \
  -d '{"event":"SHIP"}'
```

### 4. Consultar el estado

```bash
curl http://localhost:8080/api/orders/<ID>
```

Para explorar las ramas alternativas usa `CANCEL` desde `CREATED` o `REFUND` desde `PAID`.

## Diseño

- `FileState`: estados posibles del pedido (`CREATED`, `PAID`, `SHIPPED`, `CANCELLED`, `REFUNDED`).
- `FileEvent`: eventos de negocio (`PAY`, `CANCEL`, `SHIP`, `REFUND`).
- `FileStateMachine`: única clase que conoce las transiciones permitidas.
- `FileWorkflowService`: conserva el estado actual del ejemplo en memoria.
- `StateMachineController`: API REST bajo `/api/orders` para probar la máquina.

Los nombres de las clases se mantienen para conservar la estructura original del repositorio; el dominio del ejemplo es ahora el pedido descrito en la página teórica.
