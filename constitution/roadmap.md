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
| 018 | **Login API Integration** — Wire `ApiService.login()` with `LoginViewModel`, implement token storage and session management. | 🔴 Critical |
| 019 | **RemoteDataSource Implementation** — Implement `fetchData()` with real API calls, add proper error handling with `Result<T>` wrapper. | 🔴 Critical |
| 020 | **Check Payment API Integration** — Connect `sendPayment()` to `ApiService.queryTransaction()`, handle real responses and errors. | 🔴 Critical |

---

## In Progress 🚧

_Features with stub implementations that need completion._

| # | Feature | Current State | What's Missing |
|---|---------|---------------|----------------|
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
| Hardcoded API base URL (`api.example.com`) | 🔴 High | `core-data/.../NetworkModule.kt` |
| Keystore credentials in build.gradle | 🔴 High | `app/build.gradle.kts` |
| HTTP body logging in release builds | 🟡 Medium | `core-data/.../NetworkModule.kt` |
| `GetReportUseCase` returns hardcoded data | 🟡 Medium | `core/.../GetReportUseCase.kt` |
| `CheckPaymentViewModel` uses public mutable state | 🟡 Medium | `feature-purchase/.../CheckPaymentViewModel.kt` |
| `BankScreen` has hardcoded bank list | 🟡 Medium | `feature-purchase/.../BankScreen.kt` |
| No `Result<T>` wrapper for error propagation | 🟡 Medium | All data layer |
| Room dependencies unused (no DAO/Entity/DB) | 🟡 Medium | `core-data/build.gradle.kts` |

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
