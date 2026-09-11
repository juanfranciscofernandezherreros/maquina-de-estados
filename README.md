# maquina-de-estados

Ejemplo mínimo de una máquina de estados en Spring Boot basada en la regla:

> **estado actual + evento = nuevo estado**

El microservicio no llama a `nextStep()`. Publica un evento explícito y la máquina decide si la transición es válida y cuál es el nuevo estado.

## Flujo

```text
RECEIVED
   |
   | DATA_READY
   v
DATA_READY
   |
   | FILE_CREATED
   v
FILE_CREATED
   |
   | FILE_SAVED
   v
FILE_SAVED
   |
   | RESPONSE_SENT
   v
RESPONSE_SENT
   |
   | ACK_RECEIVED
   v
FINISHED

Cualquier estado no terminal -- FAIL --> ERROR
```

## Tabla de transiciones

| Estado actual | Evento | Nuevo estado |
|---|---|---|
| `RECEIVED` | `DATA_READY` | `DATA_READY` |
| `DATA_READY` | `FILE_CREATED` | `FILE_CREATED` |
| `FILE_CREATED` | `FILE_SAVED` | `FILE_SAVED` |
| `FILE_SAVED` | `RESPONSE_SENT` | `RESPONSE_SENT` |
| `RESPONSE_SENT` | `ACK_RECEIVED` | `FINISHED` |
| Estado no terminal | `FAIL` | `ERROR` |

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

### 1. Crear un fichero/workflow

```bash
curl -X POST http://localhost:8080/api/files
```

Respuesta:

```json
{
  "id": "2e39d8e0-08d1-4bed-b141-375ba6d06f9a",
  "state": "RECEIVED"
}
```

### 2. Lanzar un evento

```bash
curl -X POST http://localhost:8080/api/files/<ID>/events \
  -H 'Content-Type: application/json' \
  -d '{"event":"DATA_READY"}'
```

Después se pueden enviar, en orden:

```text
FILE_CREATED
FILE_SAVED
RESPONSE_SENT
ACK_RECEIVED
```

### 3. Consultar el estado

```bash
curl http://localhost:8080/api/files/<ID>
```

### 4. Provocar un error de negocio/técnico

```bash
curl -X POST http://localhost:8080/api/files/<ID>/events \
  -H 'Content-Type: application/json' \
  -d '{"event":"FAIL"}'
```

## Diseño

- `FileState`: estados posibles.
- `FileEvent`: sucesos que pueden ocurrir.
- `FileStateMachine`: única clase que conoce las transiciones permitidas.
- `FileWorkflowService`: conserva el estado actual del ejemplo en memoria.
- `StateMachineController`: API REST para probar la máquina.

En un sistema real, `FileWorkflowService` debería sustituir el `ConcurrentHashMap` por persistencia en base de datos y, si interesa auditoría, añadir una tabla de histórico de transiciones.
