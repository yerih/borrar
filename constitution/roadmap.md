# Roadmap

_Order and status of features. This is the overview of "what's done, what's next, and what's coming." Each entry points to its folder in `features/`._

---

## Done ✅

_Features already implemented and functional in the codebase._

| # | Feature | Summary |
|---|---------|---------|
| 001 | **Architecture Setup** — Modularized Clean Architecture with `:app`, `:core`, `:core-data`, `:core-ui`, `:feature-purchase` modules. |
| 002 | **Design System** — Complete Compose UI component library in `core-ui`: buttons, dialogs, text fields, keyboard, selectors, loaders, animations. |
| 003 | **Theme System** — CorpoCredit theme with Lato typography, Material 3, and custom color tokens. |
| 004 | **Navigation Infrastructure** — Jetpack Navigation Compose with feature-based nested graphs, shared ViewModels, and transition animations. |
| 005 | **Domain Models** — Core business entities: `CheckPaymentModel`, `BankModel`, `DetailedReportModel`, `InvoiceModel`. |
| 006 | **Domain Use Cases** — `GetDataUseCase`, `GetReportUseCase`, `ProcessPurchaseUseCase` (skeleton implementations). |
| 007 | **Repository Contract** — `DataRepository` interface defined in domain layer. |
| 008 | **Network Layer** — Retrofit `ApiService` with login, banks, configs, transactions, and movements endpoints. |
| 009 | **Data Layer** — `DataRepositoryImpl`, `RemoteDataSource`, Hilt `NetworkModule` and `RepositoryModule`. |
| 010 | **POS SDK Integration** — Morefun Yapi device service binding, serial number extraction, engine lifecycle management. |
| 011 | **Login Screen** — MVI implementation with `StateFlow` + `Channel<Effect>`, serial number display, form validation. |
| 012 | **Home Screen** — Function grid menu with navigation to all features, snackbar for unavailable features. |
| 013 | **Check Payment Flow** — Multi-step form: Reference → Amount → Phone → Bank → Loader → Invoice. Shared ViewModel across navigation graph. |
| 014 | **Invoice Screen** — Digital receipt with merchant info, bank details, amount formatting, print/exit actions. |
| 015 | **Currency Formatting** — `DecimalCurrencyVisualTransformation` for Bs. input, `PhoneVisualTransformation` for phone numbers. |
| 016 | **Navigation Extensions** — `sharedViewModel()` for graph-scoped VMs, currency/phone formatting helpers, navigation utilities. |

---

## Next 🔜

_Up next to address. Ideally only one feature "in progress" at a time._

| # | Feature | Summary | Priority |
|---|---------|---------|----------|
| 017 | **Security Hardening** — Move keystore credentials to `local.properties`, remove hardcoded passwords, add certificate pinning, encrypt token storage. | 🔴 Critical |
| 019 | **RemoteDataSource Implementation** — Implement `fetchData()` with real API calls, add proper error handling with `Result<T>` wrapper. | 🔴 Critical |
| 020 | **Check Payment API Integration** — Connect `sendPayment()` to `ApiService.queryTransaction()`, handle real responses and errors. | 🔴 Critical |

---

## In Progress 🚧

_Features with stub implementations that need completion._

| # | Feature | Current State | What's Missing |
|---|---------|---------------|----------------|
| 018 | **Login API Integration** | auth-service wired end-to-end at data/domain layer (see *Auth-service* below). `:app:assembleDebug` green. | Wire `LoginUseCase` into `LoginViewModel`, persist session across process death, add unit tests |
| 021 | **Digital Change Screen** | Basic UI shell | Business logic, API integration, amount calculation |
| 022 | **Instant Debit Screen** | Not started | Full feature implementation |
| 023 | **Historical Screen** | Basic UI shell | Transaction list, filtering, API integration |
| 024 | **Settings Screen** | Basic UI shell | Terminal configuration, API sync |

---

## Backlog / Ideas 💡

_Not yet committed to or strictly ordered. Ideas that adhere to the constitution._

| # | Feature | Value | Effort |
|---|---------|-------|--------|
| 025 | **Offline Caching** | Room database for offline transaction queue and retry capability. | Medium |
| 026 | **Printer Receipt Generation** | Generate print-ready receipts via POS SDK printer API. | Medium |
| 027 | **Error Handling Framework** | Global error states, retry mechanisms, user-friendly error messages. | Medium |
| 028 | **Feature Module Splitting** | Split `feature-purchase` into `feature-login`, `feature-check-payment`, `feature-digital-change`, etc. | Low |
| 029 | **Unit Tests** | Test ViewModels, UseCases, Repository with JUnit + Turbine. | High |
| 030 | **Integration Tests** | End-to-end flow tests for Check Payment with MockWebServer. | High |
| 031 | **ProGuard/R8 Minification** | Code shrinking and obfuscation for release builds. | Low |
| 032 | **CI/CD Pipeline** | Automated build, test, and deploy with GitHub Actions. | Medium |
| 033 | **Loading States** | Skeleton loaders, progress indicators for async operations. | Low |
| 034 | **Transaction Receipt Sharing** | Share invoice as image/PDF via Android share sheet. | Medium |
| 035 | **Multi-language Support** | English/Spanish localization. | Low |
| 036 | **Analytics** | Transaction metrics and error reporting. | Medium |
| 037 | **Tamper Detection** | Root detection, app integrity verification for financial security. | Medium |
| 038 | **Deep Linking** | QR code or URL-based payment initiation. | Medium |

---

## Technical Debt

_Issues to address that are not features per se._

| Issue | Severity | Location |
|-------|----------|----------|
| ~~Hardcoded API base URL (`api.example.com`)~~ | ✅ Resolved | Now `BuildConfig.BASE_URL` in `core-data/build.gradle.kts` |
| Auth token held in memory only — lost on process death | 🟡 Medium | `core-data/.../session/SessionManager.kt` |
| `AuthRepository` throws `AuthException` instead of `Result<T>` — deviation, needs Architect sign-off | 🟡 Medium | `core-data/.../network/AuthException.kt` |
| `ApiService.login` deprecated but not yet removed (duplicate login entry point) | 🟢 Low | `core-data/.../network/ApiService.kt` |
| Keystore credentials in build.gradle | 🔴 High | `app/build.gradle.kts` |
| HTTP body logging in release builds | 🟡 Medium | `core-data/.../NetworkModule.kt` |
| `GetReportUseCase` returns hardcoded data | 🟡 Medium | `core/.../GetReportUseCase.kt` |
| `CheckPaymentViewModel` uses public mutable state | 🟡 Medium | `feature-purchase/.../CheckPaymentViewModel.kt` |
| `BankScreen` has hardcoded bank list | 🟡 Medium | `feature-purchase/.../BankScreen.kt` |
| No `Result<T>` wrapper for error propagation | 🟡 Medium | All data layer |
| Room dependencies unused (no DAO/Entity/DB) | 🟡 Medium | `core-data/build.gradle.kts` |

---

## Recent Work

### auth-service — Login POS / App móvil (#018, data/domain layers)

_Implements API.md § 1 (`documentation/API corpocredit/API.md`): `POST /auth/app/login` and
`POST /auth/app/logout` behind the api-gateway `/auth` base. Backend contract source of truth._

**Added (`:core-data`)**

| Path | Purpose |
|------|---------|
| `network/AuthApiService.kt` | Retrofit contract — `auth/app/login` (public), `auth/app/logout` (`Response<Unit>` for the 204) |
| `network/AuthException.kt` | `ErrorResponse` (Spring default error body) + sealed `AuthException` mapping the spec's error table 1:1: `Validation` 400, `InvalidCredentials` 401, `RoleNotAllowed` 403, `Locked` 423, `Network`, `Server` |
| `datasource/AuthRemoteDataSource.kt` | Thin calls over the contract, translating any `Throwable` into `AuthException` |
| `session/SessionManager.kt` | In-memory JWT holder (`StateFlow`) |
| `repository/AuthRepositoryImpl.kt` | Saves/clears session; local state always cleared after logout |

**Added (`:core`)**

| Path | Purpose |
|------|---------|
| `domain/model/AuthUser.kt`, `AuthSession.kt` | Typed domain models (no `Any`, per constitution) |
| `domain/repository/AuthRepository.kt` | Domain contract + `authorizationHeader()` helper |
| `domain/usecase/LoginUseCase.kt` | Blank-input validation + trim before hitting the network |
| `domain/usecase/LogoutUseCase.kt` | Logout wrapper |

**Changed**

| Path | Change |
|------|--------|
| `core-data/build.gradle.kts` | `BuildConfig.BASE_URL` replaces the hardcoded `api.example.com`; default `http://10.0.2.2:8080/`, overridable with `-PapiBaseUrl=…`. No URLs in code |
| `core-data/.../di/NetworkModule.kt` | Reads `BuildConfig.BASE_URL`, adds 30s timeouts, provides `AuthApiService` |
| `core-data/.../di/RepositoryModule.kt` | Binds `AuthRepository` |
| `core-data/.../network/ApiService.kt` | **Bug fix:** legacy `login` called `app/login`, missing the `/auth` prefix. Corrected and deprecated in favour of `AuthApiService` |
| `network/requests/LoginRequest.kt`, `requests/UserData.kt`, `responses/LoginResponse.kt` | Explicit `@SerializedName` on every field |

**Spec details that constrain the implementation**

- Only `ADM`/`CSH` may log in here; `OWN`/`SAM` get 403.
- `roleId` is the role **id**, not the slug — resolve against the catalog for a readable label.
- `user.merchantId` is the commerce id, carried in the JWT as the `commerceId` claim.
- Logout sets `sessions.active = false` but does **not** revoke the JWT (no blacklist), so the token
  must not be trusted as "revoked" client-side — only its natural expiry applies.
- `expiresAt` arrives as a `LocalDateTime` with no timezone (`2026-09-25T15:30:00`); currently kept
  as `String` and not parsed.
- 401 is intentionally generic ("Invalid credentials") across user-not-found / wrong password /
  inactive user / invalid terminal — the UI must not attempt to distinguish them.

**Verification:** `:core-data:assembleDebug`, `:core:assembleDebug`, `:app:assembleDebug` and
`:core:testDebugUnitTest` all green. `:core-data:testDebugUnitTest` cannot run offline
(`hilt-android-compiler` not in the local cache) — pre-existing, unrelated to this change.

**Open follow-ups**

1. Wire `LoginUseCase` into `LoginViewModel` (MVI: `LoginIntent`/`LoginState`/`LoginEffect`).
2. Real backend base URL still pending — update the `apiBaseUrl` default in
   `core-data/build.gradle.kts` when it arrives.
3. Move the token from `SessionManager` to `EncryptedSharedPreferences`/DataStore so a POS session
   survives process death (part of #017).
4. Decide the `Result<T>` vs `AuthException` question project-wide before #019 is implemented, so
   error handling is not defined twice.

---

## Release Milestones

| Version | Goal | Features |
|---------|------|----------|
| **1.2.0** | Security + Login | #017, #018, #019, #020 |
| **1.3.0** | Feature Completion | #021, #022, #023, #024 |
| **1.4.0** | Offline + Testing | #025, #029, #030 |
| **2.0.0** | Production Ready | #028, #031, #032, #037 |

---

> Every new feature is created as `features/NNN-feature-name/` containing `spec.md`, `plan.md`, and `tasks.md` before writing any code.
