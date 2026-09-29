# Vuelto Digital — Referencia de API

Documenta todos los endpoints realmente implementados en el stack Java (`api-gateway`, `auth-service`,
`backoffice-service`, `core-service`, `banking-router-service`) a fecha de la Fase 5 del plan de
ejecución. `backend-node` está deprecado y no se documenta aquí.

## Convenciones generales

- **Base URL**: todo el tráfico externo pasa por `api-gateway` (por defecto `http://localhost:8080`).
  No se debe llamar a los microservicios internos directamente salvo en desarrollo local.
  url_backoffice = http://consultas.corpocredit.app/ 
  url_app_movil  = http://api.consultas.corpocredit.app/

- **Autenticación**: `Authorization: Bearer <token>` en cada request autenticado. Hay **dos esquemas de
  token independientes**, no intercambiables entre sí (ver detalle en cada sección):
  - Token de `auth-service` (login POS / app móvil) — `sub` = `userId`.
  - Token de `backoffice-service` (login backoffice) — `sub` = `username`.
- **Errores**: todos los servicios devuelven el formato default de Spring Boot cuando se lanza
  `ResponseStatusException`:
  ```json
  {
    "timestamp": "2026-09-25T10:00:00.000+00:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "Invalid credentials",
    "path": "/auth/app/login"
  }
  ```
- **Errores de validación** (`@Valid` en el body) devuelven `400` con el mismo formato; el `message`
  incluye el detalle del/los campo(s) inválido(s).
- Los IDs son `string` (UUID generado por la aplicación, `varchar(36)`), no enteros.

## Índice

1. [auth-service — Login POS / App móvil](#1-auth-service--login-pos--app-móvil)
2. [core-service — Motor transaccional (app móvil)](#2-core-service--motor-transaccional-app-móvil)
3. [backoffice-service — Auth](#3-backoffice-service--auth)
4. [backoffice-service — Comercios](#4-backoffice-service--comercios)
5. [backoffice-service — Cuentas de comercio (Pago Móvil)](#5-backoffice-service--cuentas-de-comercio-pago-móvil)
6. [backoffice-service — Usuarios](#6-backoffice-service--usuarios)
7. [backoffice-service — Catálogo (bancos / configs)](#7-backoffice-service--catálogo-bancos--configs)
8. [banking-router-service — interno](#8-banking-router-service--interno-no-consumir-directo)

---

## 1. auth-service — Login POS / App móvil

Base: `/auth` (vía gateway, sin cambios de prefijo). Estos son los únicos endpoints pensados para
consumo directo de la **app móvil / terminal POS**; ejecutan login de operadores de comercio
(roles `ADM`/`CSH` únicamente — `OWN`/`SAM` no pueden loguear aquí).

### POST /auth/app/login

Público (no requiere token).

**Request**
```json
{
  "username": "cajero1",
  "password": "Clave123*",
  "terminalSerial": "POS-0001"
}
```

**Response 200**
```json
{
  "sessionToken": "eyJhbGciOiJIUzI1NiJ9...",
  "expiresAt": "2026-09-25T15:30:00",
  "user": {
    "id": "8f2a...-uuid",
    "username": "cajero1",
    "roleId": "04a3d327-394a-429b-8447-efd8cca2305b",
    "merchantId": "b6c7...-uuid"
  }
}
```
`roleId` es el **id** del rol (no el slug); los roles válidos para este login son `ADM` y `CSH`
(`auth-service/security/Roles.java`). El JWT resultante trae `sub=userId`, claim `commerceId`, claim
`roleId` — este es el token que consume `core-service`.

**Errores**
| Status | Causa |
|---|---|
| 400 | `username`/`password`/`terminalSerial` vacíos |
| 401 | usuario no existe, contraseña incorrecta, usuario inactivo, o terminal (`terminalSerial`) inválido/no pertenece al comercio del usuario — todos devuelven el mismo mensaje genérico `"Invalid credentials"` (evita filtrar cuál validación falló) |
| 403 | el usuario existe y la contraseña es correcta, pero su rol no es `ADM`/`CSH` (`"User role is not allowed to log in from the POS app"`) |
| 423 (`LOCKED`) | 3 intentos fallidos acumulados → bloqueado 1 hora (`"User is locked. Try again later."`) |

### POST /auth/app/logout

Requiere `Authorization: Bearer <sessionToken>`.

**Request**: sin body.

**Response**: `204 No Content`.

Invalida la sesión (`sessions.active = false`) asociada a ese token; no invalida el JWT en sí (sigue
siendo válido criptográficamente hasta su expiración natural — no hay blacklist de tokens todavía).

---

## 2. core-service — Motor transaccional (app móvil)

Base: `/transactions`. Requiere `Authorization: Bearer <token de auth-service>` (el filtro JWT de
`core-service` lee `sub` como `userId` y el claim `commerceId`). Estos endpoints son los que la app
móvil usa para verificar pagos y ver su propio historial — **no exponer en el backoffice**.

### POST /transactions/query

Endpoint **único** para verificar dinero entrante, sea **Pago Móvil** (P2P/P2C) o **Transferencia
bancaria** (subrogado) — `transactionType` decide cuál adaptador de `banking-router-service` se
consulta (`/router/pago-movil` o `/router/transferencia`). Primero revisa BD local, luego el banco.

**Request — Pago Móvil**
```json
{
  "transactionType": "PAGO_MOVIL",
  "reference": "009281129281",
  "amount": 150.50,
  "date": "2026-09-25",
  "phone": "04121234567",
  "document": "V123456789",
  "bankId": "e3f4a5b6-...-uuid"
}
```

**Request — Transferencia (subrogado)**
```json
{
  "transactionType": "TRANSFERENCIA",
  "reference": "12346768",
  "amount": 3500.00,
  "date": "2026-09-25",
  "document": "J013759368",
  "bankId": "e3f4a5b6-...-uuid"
}
```

Campos:
- `transactionType`: obligatorio, `"PAGO_MOVIL"` o `"TRANSFERENCIA"`.
- `reference`: opcional, si viene debe ser `\d{6,}`.
- `amount`: obligatorio, distinto de 0.
- `date`: obligatorio, `YYYY-MM-DD` (se usa como día único de búsqueda para ambos tipos).
- `phone`: opcional, **solo aplica a `PAGO_MOVIL`** (teléfono del pagador, filtro adicional).
- `document`: para `PAGO_MOVIL` es opcional (documento del pagador); para `TRANSFERENCIA` es
  **obligatorio** (documento del ordenante — lo exige la API de Banco Plaza).
- `bankId`: obligatorio **solo si** el comercio tiene más de una cuenta activa. Selecciona la cuenta
  del comercio a usar; para `TRANSFERENCIA` esa cuenta debe tener `accountNumber` configurado (ver
  sección 5), si no → `422`.

**Response 200** — entidad `Transaction` completa (misma forma para ambos tipos):
```json
{
  "id": "0b2c...-uuid",
  "cashRegisterId": null,
  "toPhone": "04121234567",
  "toIdDocument": "123456789",
  "toBankId": "e3f4a5b6-...-uuid",
  "toDocumentType": "074dfe46-...-uuid",
  "fromPhone": "04241112233",
  "fromIdDocument": "987654321",
  "fromBankId": "a5b6c7d8-...-uuid",
  "fromDocumentType": "074dfe46-...-uuid",
  "transactionType": "4740ac2a-...-uuid",
  "amount": 150.50,
  "status": "a526bd58-...-uuid",
  "response": null,
  "referenceNumber": "009281129281",
  "isValidated": true,
  "created": "2026-09-25T09:12:00",
  "updated": "2026-09-25T09:12:00",
  "sessionId": null,
  "idempotencyKey": null,
  "commerceId": "b6c7...-uuid"
}
```
`transactionType` (en la respuesta, campo de la entidad `Transaction` — no confundir con el campo del
mismo nombre en el request) y `status` son **ids de `configs`** (no literales) — resolver contra `GET
/configs?type=TRANSACCIONES` / `GET /configs?type=TRANSACCION_STATUS` si se necesita el valor legible.
Tanto Pago Móvil como Transferencia se persisten con el mismo id de negocio `PAGO` (dinero entrante);
el canal ya se distingue por cuál request se envió, no hay una columna de canal separada hoy.

**Errores**
| Status | Causa |
|---|---|
| 400 | `transactionType` ausente o inválido, `amount = 0`, `document` ausente en `TRANSFERENCIA`, `bankId` requerido y ausente (comercio con >1 cuenta), `bankId` no pertenece al comercio, comercio sin cuentas activas |
| 403 | comercio inactivo |
| 404 | pago/transferencia no encontrado (ni local ni en el banco) / banco no encontrado |
| 422 | el banco de la cuenta no tiene integración API activa, o (solo `TRANSFERENCIA`) la cuenta del comercio no tiene `accountNumber` configurado |
| 500 | inconsistencia interna (banking-router confirmó pero no se pudo releer el registro local) |
| 502/503/504 | error de comunicación con `banking-router-service` (ver sección 8) |

### POST /transactions/send-change

Valida los datos de un envío de vuelto (pago móvil saliente). **Actualmente siempre responde `501`**:
banking-router-service todavía no tiene adaptador para iniciar pagos salientes con Banco Plaza, solo
para verificarlos.

**Request**
```json
{
  "amount": 25.00,
  "phone": "04121234567",
  "bankId": "e3f4a5b6-...-uuid",
  "document": "V123456789",
  "sourceBankId": "a5b6c7d8-...-uuid"
}
```

**Responses**
| Status | Causa |
|---|---|
| 400 | `amount <= 0` |
| 403 | comercio inactivo, o el monto excede `config_users.tx_amount_limit` del usuario |
| 422 | la cuenta origen no tiene integración API activa |
| 400 | `bankId` (destino) no reconocido |
| **501** | **siempre**, tras pasar todas las validaciones — función no implementada todavía |

### GET /transactions/history

Últimos 20 **vueltos** (`transactionType = VUELTO`) del comercio del token, sin paginación ni filtros.

**Response 200**
```json
[
  {
    "id": "...", "amount": 25.00, "referenceNumber": "883921",
    "toPhone": "04121234567", "toBankId": "e3f4a5b6-...-uuid",
    "status": "a526bd58-...-uuid", "created": "2026-09-24T18:03:11", "...": "..."
  }
]
```

### GET /transactions/stats?startDate=&endDate=

Agrega monto y cantidad de **vueltos** por día, para el dashboard (backoffice). Rango máximo: 31 días.

**Request** (query params): `startDate=2026-09-01&endDate=2026-09-25`

**Response 200**
```json
{
  "totalAmount": 4230.75,
  "totalCount": 38,
  "chartData": [
    { "date": "2026-09-01", "amount": 120.00, "count": 2 },
    { "date": "2026-09-02", "amount": 0.00, "count": 0 },
    { "date": "2026-09-25", "amount": 340.50, "count": 5 }
  ]
}
```

**Errores**: `400` si `startDate`/`endDate` ausentes, `endDate < startDate`, o el rango supera 31 días.

---

## 3. backoffice-service — Auth

Base: `/backoffice/auth`. Login exclusivo para roles administrativos (`OWN`/`SAM`/`ADM`); `CSH` es
rechazado aquí (solo puede loguear en `auth-service`, vía app POS).

### POST /backoffice/auth/login

Público.

**Request**
```json
{ "username": "owner", "password": "********" }
```
`username` acepta username **o** email indistintamente.

**Response 200**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "id": "003a579a-...-uuid",
    "username": "owner",
    "email": "victorr2021@gmail.com",
    "fullName": "Owner Sistema",
    "role": "OWN",
    "commerceId": null,
    "firstTime": true
  }
}
```
El JWT resultante trae `sub=username` (no `userId`) y **ningún otro claim** — `backoffice-service`
resuelve el resto de la identidad (rol, comercio, estado) por consulta a BD en cada request, no
confía en claims del token salvo la identidad.

**Errores**
| Status | Causa |
|---|---|
| 401 | usuario no existe, o contraseña incorrecta (mensaje genérico `"Usuario o contraseña incorrectos"` en ambos casos) |
| 403 | rol del usuario no es `OWN`/`SAM`/`ADM` (p. ej. es `CSH`), usuario inactivo, o usuario bloqueado (3 intentos fallidos → 1h) |

### GET /backoffice/users/profile

Requiere token. Devuelve el `User` completo del actor autenticado (ver forma en sección 6).

### PUT /backoffice/users/profile

Requiere token.

**Request**
```json
{
  "username": "owner",
  "email": "owner@corpocredit.com",
  "fullName": "Owner Sistema",
  "password": "NuevaClave456*"
}
```
`password` es opcional; si viene no-vacío, se re-hashea y `firstTime` pasa a `false` (flujo de cambio
de clave en primer login).

**Response 200**: `User` actualizado.

---

## 4. backoffice-service — Comercios

Base: `/backoffice/commerce`. Requiere token de backoffice. Solo `OWN`/`SAM` pueden crear/editar/listar
comercios; `ADM` solo puede leer el suyo propio (`GET /{id}`); `CSH` no tiene acceso (no puede loguear
en backoffice).

### POST /backoffice/commerce

Crea el comercio **y automáticamente su usuario `ADM`** (username = email del comercio, password
default `Clave123*`, `firstTime=true`).

**Request**
```json
{
  "rif": "J413051320",
  "name": "Panadería Central",
  "phone": "04141234567",
  "email": "contacto@panaderiacentral.com",
  "address": "Av. Principal, Caracas"
}
```
`rif`: regex `^[VEJGPC]\d{9}$` (letra + 9 dígitos, sin guion).

**Response 200**
```json
{
  "id": "b6c7d8e9-...-uuid",
  "rif": "J413051320",
  "name": "Panadería Central",
  "phone": "04141234567",
  "email": "contacto@panaderiacentral.com",
  "address": "Av. Principal, Caracas",
  "active": true,
  "createdAt": "2026-09-25T14:00:00Z",
  "updatedAt": "2026-09-25T14:00:00Z"
}
```

**Errores**: `400` RIF con formato inválido · `403` actor no es `OWN`/`SAM` · `409` RIF o email ya
registrado (comercio o usuario).

### GET /backoffice/commerce?rif=&name=&email=&active=&page=0&size=20&sort=name,asc

Listado paginado y filtrado (Spring `Page<Commerce>`). Filtros combinables (`AND`), `rif`/`name`/`email`
con `LIKE` case-insensitive, `active` con igualdad exacta.

**Response 200**
```json
{
  "content": [ { "id": "...", "rif": "J413051320", "name": "Panadería Central", "active": true, "...": "..." } ],
  "totalElements": 42,
  "totalPages": 3,
  "number": 0,
  "size": 20,
  "numberOfElements": 20,
  "first": true,
  "last": false
}
```

### GET /backoffice/commerce/{id}

`404` si no existe. `ADM` solo puede pedir su propio `id` (`403` si intenta otro).

### PUT /backoffice/commerce/{id}

Mismo body que `POST`, mismas validaciones de unicidad (excluyendo el propio id).

### PATCH /backoffice/commerce/{id}/status

**Request**
```json
{ "active": false }
```
Al desactivar: desactiva también **todos los usuarios** del comercio. Al reactivar: reactiva
automáticamente solo al usuario `ADM` más antiguo (los demás quedan inactivos hasta que ese ADM los
reactive manualmente).

**Response 200**: `Commerce` actualizado.

---

## 5. backoffice-service — Cuentas de comercio (Pago Móvil / Transferencias)

Gestión administrativa de las cuentas bancarias registradas por comercio — **esto es lo único de
"cuentas bancarias" que vive en el backoffice**; ninguna operación bancaria real (verificar/enviar) se
ejecuta desde aquí, eso es exclusivo de la app móvil vía `core-service`/`banking-router-service`.

### POST /backoffice/commerce/{commerceId}/accounts

**Request**
```json
{ "bankId": "e3f4a5b6-...-uuid", "phone": "04141234567", "accountNumber": "01380011130110248648" }
```
`phone` es obligatorio (toda cuenta sirve para Pago Móvil, el flujo principal). `accountNumber` es
**opcional**: solo hace falta si el comercio también quiere verificar **Transferencias** (subrogado)
en ese banco — sin él, `POST /transactions/query` con `transactionType: "TRANSFERENCIA"` responde
`422` para esa cuenta.

**Response 200**
```json
{
  "id": "f1a2...-uuid", "commerceId": "b6c7...-uuid", "bankId": "e3f4a5b6-...-uuid",
  "phone": "04141234567", "accountNumber": "01380011130110248648", "active": true
}
```

**Errores**
| Status | Causa |
|---|---|
| 404 | comercio o banco no existe |
| 422 | el banco **no tiene integración API activa** (`bank.apiActive = false`) — hoy solo Banco Plaza califica |
| 409 | el comercio ya tiene una cuenta registrada para ese banco (única por comercio+banco) |
| 403 | actor sin permisos sobre el comercio |

### GET /backoffice/commerce/{commerceId}/accounts

**Response 200**: array simple (sin paginar) de `CommerceAccount`.

### PATCH /backoffice/commerce-accounts/{accountId}/status

Nota: path plural con guion, **distinto** del prefijo `/commerce/{id}/accounts`. Exclusivo del `ADM`
dueño del comercio (ni `OWN`/`SAM` pueden activar/desactivar cuentas de terceros por este endpoint).

**Request**: `{ "active": false }` → **Response 200**: `CommerceAccount` actualizada.

### DELETE /backoffice/commerce-accounts/{accountId}

**Response**: `204 No Content`. Exclusivo del `ADM` dueño del comercio.

---

## 6. backoffice-service — Usuarios

Base: `/backoffice/users`. Jerarquía forzada por el backend (el frontend no debe ofrecer más opciones
de las permitidas):

- `OWN`/`SAM` solo pueden crear/editar/activar usuarios `SAM` (super-administradores del sistema).
- `ADM` solo puede crear/editar/activar usuarios `CSH` **de su propio comercio** (`commerceId` se
  fuerza al del actor, el valor recibido en el body se ignora).
- Nadie puede desactivarse a sí mismo.

### POST /backoffice/users

**Request**
```json
{
  "username": "jperez",
  "email": "jperez@panaderiacentral.com",
  "fullName": "Juan Pérez",
  "commerceId": "b6c7...-uuid",
  "role": "CSH"
}
```
`role` debe ser el único legal para la jerarquía del actor (`SAM` si el actor es `OWN`/`SAM`, `CSH` si
es `ADM`); password se asigna automáticamente `Clave123*`, `firstTime=true`.

**Response 200** — entidad `User` completa (⚠️ incluye el campo `password` con el hash bcrypt, sin
`@JsonIgnore` — el frontend debe ignorarlo explícitamente, no asumir su ausencia):
```json
{
  "id": "f1a2...-uuid",
  "username": "jperez",
  "password": "$2a$10$...",
  "email": "jperez@panaderiacentral.com",
  "fullName": "Juan Pérez",
  "active": true,
  "createdAt": "2026-09-25T14:10:00Z",
  "updatedAt": "2026-09-25T14:10:00Z",
  "role": { "id": "04a3d327-...-uuid", "name": "Cashier", "slug": "CSH" },
  "commerceId": "b6c7...-uuid",
  "loginAttempts": 0,
  "lockedUntil": null,
  "firstTime": true
}
```
Nota: aquí `role` es **objeto anidado** `{id, name, slug}` — distinto de `LoginResponse.user.role`, que
es un string plano.

**Errores**: `400` datos inválidos · `403` rol solicitado no permitido para la jerarquía del actor ·
`409` `username`/`email` duplicado.

### GET /backoffice/users?search=&active=&role=&page=&size=&sort=

`Page<User>` (misma forma que `Page<Commerce>`). Scope automático por rol del actor: `OWN`/`SAM` ven
usuarios `SAM`+`ADM` de todo el sistema; `ADM` ve solo los `CSH` de su propio comercio (no puede ver
otros comercios aunque lo intente por query param). `search` hace `LIKE` sobre `username`/`email`/`fullName`.

### PUT /backoffice/users/{id}

**Request**
```json
{ "username": "jperez", "email": "jperez@panaderiacentral.com", "fullName": "Juan Pérez G." }
```
No permite cambiar `role` ni `password` (para eso: creación de nuevo usuario, o `PUT
/backoffice/users/profile` para la clave propia).

### PATCH /backoffice/users/{id}/status

**Request**: `{ "active": false }` → `403` si el actor intenta desactivarse a sí mismo.

---

## 7. backoffice-service — Catálogo (bancos / configs)

Base: rutas **sin** prefijo `/backoffice` y **sin autenticación** (`permitAll`) — se pueden llamar
antes de login para poblar selects.

### GET /banks?apiActive=

Bancos con `active = true`, cacheado, ordenado por nombre.

- **Sin el parámetro** `apiActive`: devuelve el listado **completo** de bancos activos (para que la
  app móvil llene el select de "banco" al hacer una comprobación de transacción — cualquier banco
  venezolano es un destino/origen válido para mostrar, no solo los integrados).
- **Con `apiActive=true`**: filtra solo los bancos con integración API activa (para que el backoffice
  ofrezca únicamente los bancos con los que realmente se puede crear una cuenta de comercio — ver
  sección 5). Hoy solo Banco Plaza califica.

**Response 200**
```json
[
  { "id": "e3f4a5b6-...-uuid", "code": "0138", "name": "BANCO PLAZA", "apiActive": true },
  { "id": "f4a5b6c7-...-uuid", "code": "0177", "name": "BANCO DE LA FUERZA ARMADA...", "apiActive": false }
]
```
Solo los bancos con `apiActive: true` deben ofrecerse para crear cuentas de comercio (hoy, únicamente
Banco Plaza).

### GET /configs?type=TIPO_DOCUMENTO

**Response 200**
```json
[
  { "key": "40b96c31-...-uuid", "value": "J" },
  { "key": "074dfe46-...-uuid", "value": "V" },
  { "key": "fa124549-...-uuid", "value": "C" },
  { "key": "d43148c5-...-uuid", "value": "G" },
  { "key": "77ede90d-...-uuid", "value": "P" },
  { "key": "43d23679-...-uuid", "value": "E" }
]
```
(`key` es en realidad el `id` de la fila `configs`, el DTO lo renombra).

### GET /configs?type=OPERADORAS

```json
[
  { "key": "...", "value": "0412" }, { "key": "...", "value": "0414" },
  { "key": "...", "value": "0416" }, { "key": "...", "value": "0424" },
  { "key": "...", "value": "0426" }, { "key": "...", "value": "0212" }
]
```

### GET /configs?type=TRANSACCION_STATUS

```json
[ { "key": "a526bd58-...-uuid", "value": "APROBADO" }, { "key": "f196bda7-...-uuid", "value": "RECHAZADO" } ]
```

Otros `type` disponibles en el catálogo base: `TRANSACCIONES` (`PAGO`/`VUELTO`), `MONEDAS`, `BCV`,
`BCV_EUR`, `CAJA_STATUS`.

**Error**: `400` si `type` está ausente (parámetro requerido, sin default).

---

## 8. banking-router-service — interno (no consumir directo)

Base: `/router`. Requiere token (mismo secreto JWT compartido) — **pero este servicio existe para ser
llamado por `core-service`, no directamente por la app móvil ni por el backoffice.** Se documenta por
completitud, no como contrato público.

### POST /router/pago-movil

**Request**
```json
{
  "commerceId": "b6c7...-uuid",
  "bankCode": "0138",
  "documentoDestinatario": "J413051320",
  "telefonoDestinatario": "04141234567",
  "referencia": "009281129281",
  "fecha": "2026-09-25",
  "monto": 150.50,
  "telefonoPagador": "04121234567",
  "documentoPagador": "V123456789"
}
```

**Response 200** (encontrado) / **404** (no encontrado, mismo body con `validado: false`):
```json
{
  "validado": true,
  "fuente": "BANCO",
  "tipoOperacion": "PAGO_MOVIL",
  "referencia": "009281129281",
  "monto": 150.50,
  "fecha": "2026-09-25",
  "mensaje": "Pago móvil verificado en banco y registrado localmente.",
  "transactionId": "0b2c...-uuid"
}
```

### POST /router/transferencia

```json
{
  "commerceId": "b6c7...-uuid",
  "commerceRif": "J413051320",
  "bankCode": "0138",
  "cuenta": "01380011130110248648",
  "moneda": "VES",
  "referencia": "12346768",
  "fechaInicio": "2026-09-01",
  "fechaFin": "2026-09-25",
  "monto": 35098.50,
  "documentoOrdenante": "J013759368",
  "bankCodeOrdenante": "0134"
}
```

Response: mismo `VerificacionResponse` con `tipoOperacion: "TRANSFERENCIA"`.

**Errores comunes a ambos** (mapeados desde `BankIntegrationException`):
| Status | Causa |
|---|---|
| 400 | banco no registrado en el sistema, banco inactivo, fechas inválidas |
| 404 | operación no encontrada (ni local ni en el banco) — se devuelve junto al body con `validado:false` |
| 501 | banco registrado pero sin cliente API implementado |
| 503 | Banco Plaza no disponible (circuit breaker abierto) |
| 502 | error de autenticación/comunicación/respuesta inválida del banco |

---

## Notas de integración

- **App móvil / POS**: usa exclusivamente `auth-service` (`/auth/app/*`) para login y `core-service`
  (`/transactions/*`) para verificación de pagos, envío de vuelto e historial propio.
- **Backoffice**: usa exclusivamente `backoffice-service` (`/backoffice/*`, `/banks`, `/configs`) y
  `core-service` **solo** para lectura de métricas (`GET /transactions/history`, `GET
  /transactions/stats`) — nunca para operaciones bancarias.
- `banking-router-service` no debe exponerse como contrato público; si en el futuro se necesita
  restringir esto a nivel de red, la Fase 4 (API Gateway) ya deja la base para agregar un predicate de
  ruta condicionado por rol/origen si se decide cerrar `/router/**` a tráfico externo.
