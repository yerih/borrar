# Mi Vuelto

**Mi Vuelto** is an Android Point of Sale (POS) payment terminal application for processing transactions in **Bolívares (Bs.)**, Venezuela's currency. Built for **Corpocredit**, the app runs on Morefun (Yapi) POS devices and provides merchants with mobile payment verification, transaction history, and digital change capabilities.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Module Structure](#module-structure)
- [Getting Started](#getting-started)
- [POS Device Integration](#pos-device-integration)
- [API Reference](#api-reference)
- [Roadmap](#roadmap)

---

## Overview

Mi Vuelto is designed to modernize POS payment terminals used by Venezuelan merchants. The application replaces legacy terminal interfaces with a modern Jetpack Compose UI, enabling merchants to verify mobile payments, generate digital receipts, and manage transaction records — all in compliance with Venezuelan financial regulations and currency formatting.

---

## Features

| Feature | Status | Description |
|---------|--------|-------------|
| **Check Payment** | ✅ Implemented | Multi-step payment verification: reference → amount → phone → bank |
| **Digital Change** | 🚧 Stub | Currency exchange functionality |
| **Instant Debit** | 🚧 Stub | Direct debit processing |
| **Historical** | 🚧 Stub | Transaction history viewer |
| **Settings** | 🚧 Stub | Terminal configuration |
| **Invoice Generation** | ✅ Implemented | Digital receipt with merchant/bank details |
| **POS Printer Support** | 🔌 Ready | SDK integration for Morefun devices |

---

## Architecture

The project follows **Clean Architecture** with **Modularized Design** principles, organized into distinct layers with unidirectional dependencies:

```
┌─────────────────────────────────────────────────────────┐
│                    :app (Application)                    │
│  Single Activity │ Navigation │ SDK Integration │ Hilt  │
├─────────────────────────────────────────────────────────┤
│               :feature-purchase (Feature)                │
│  UI Screens │ ViewModels │ Feature Navigation Graphs   │
├──────────────────────┬──────────────────────────────────┤
│    :core (Domain)    │       :core-ui (Shared UI)       │
│  Models │ UseCases   │  Theme │ Components │ Navigation │
│  Repository Contract │  Screens │ Extensions │ Design Sys│
├──────────────────────┴──────────────────────────────────┤
│                   :core-data (Data)                      │
│  API Service │ Repository Impl │ DI │ Network │ Room    │
└─────────────────────────────────────────────────────────┘
```

### Design Principles

- **Separation of Concerns**: Domain layer is pure Kotlin with no Android dependencies
- **Dependency Inversion**: Domain defines interfaces; Data layer implements them
- **Single Responsibility**: Each module has a focused purpose
- **MVI Pattern**: ViewModels use `StateFlow` for state and `Channel<Effect>` for one-time events
- **Shared ViewModels**: Navigation graph-scoped ViewModels for multi-step flows

---

## Tech Stack

| Category | Technology | Version |
|----------|-----------|---------|
| Language | Kotlin | 1.9.22 |
| UI Toolkit | Jetpack Compose | BOM 2023.08.00 |
| Design System | Material 3 | Latest |
| DI Framework | Dagger Hilt | 2.48 |
| Networking | Retrofit 2 + OkHttp | 2.9.0 / 4.11.0 |
| Local Database | Room | 2.5.2 |
| Async | Kotlin Coroutines | 1.7.3 |
| Navigation | Jetpack Navigation Compose | 2.7.1 |
| Animations | Lottie | 6.7.1 |
| Build | Android Gradle Plugin | 8.5.0 |
| Annotation Processing | KSP | 1.9.22-1.0.17 |
| Min SDK | Android 8.0+ | 26 |
| Target SDK | Android 14 | 34/35 |

---

## Module Structure

### `:app` — Application Module
Application entry point, single-activity architecture, app-level DI, and global navigation host.

| Component | Description |
|-----------|-------------|
| `MainApplication` | Hilt application, SDK engine initialization |
| `MainActivity` | Single activity hosting Compose navigation |
| `AppNavigation` | Global NavHost with feature destinations |
| `SdkModuleDI` | Provides SDK engine and application instance |

### `:core` — Domain Module
Pure Kotlin domain layer containing business models, use cases, and repository interfaces.

| Component | Description |
|-----------|-------------|
| `CheckPaymentModel` | Payment verification data (reference, amount, phone, bank) |
| `BankModel` | Bank information (logo, code, name) |
| `DetailedReportModel` | Transaction report data |
| `DataRepository` | Repository contract |
| `GetDataUseCase` | Generic data retrieval use case |
| `GetReportUseCase` | Report generation use case |
| `ProcessPurchaseUseCase` | Purchase processing business logic |
| `SerialNumberHolder` | Terminal serial number state holder |

### `:core-data` — Data Module
Network and persistence layer implementing domain repository contracts.

| Component | Description |
|-----------|-------------|
| `ApiService` | Retrofit API interface (login, banks, configs, transactions) |
| `RemoteDataSource` | Remote data fetching |
| `DataRepositoryImpl` | Repository implementation |
| `NetworkModule` | OkHttp + Retrofit DI providers |
| `RepositoryModule` | Repository binding (Impl → Interface) |
| Request Models | `LoginRequest`, `TransactionQueryRequest` |
| Response Models | `LoginResponse`, `BankResponse`, `TransactionResponse`, `ConfigResponse` |

### `:core-ui` — Shared UI Module
Reusable design system, navigation utilities, and shared screen components.

| Category | Components |
|----------|-----------|
| **Theme** | `CorpoCreditTheme`, Color, Type (Lato font) |
| **Screens** | `BaseScreen`, `SingleFormScreen`, `AccessScreen`, `ErrorScreen`, `LoaderScreen`, `LoaderResponseScreen` |
| **Buttons** | `ButtonFilled`, `ButtonBorder`, `ButtonHome`, `ButtonKeyboard`, `ButtonLoader`, `ButtonReport`, `ButtonSelectors` |
| **Inputs** | `TextFieldCustom`, `OutlinedTextFieldCustom`, `DecimalCurrencyVisualTransformation`, `PhoneVisualTransformation` |
| **Selectors** | `BankSelector` |
| **Dialogs** | `DialogConfirm`, `DialogMsg` |
| **Navigation** | `NavFeature`, `NavCommand`, `NavArgs` |
| **Design** | `HeaderAndFooterLogos`, `IconMsg`, `Loader`, `LottieAnim`, `ShimmerBrush`, `SnackBar`, `NavAnimation` |
| **Keyboard** | Custom `Keyboard` component |
| **Extensions** | Currency formatting, phone formatting, ViewModel sharing, navigation helpers |

### `:feature-purchase` — Feature Module
Complete feature implementation with screens, ViewModels, and nested navigation.

| Flow | Screens |
|------|---------|
| Login | `LoginScreen`, `LoginViewModel` (MVI) |
| Home | `HomeScreen` (function grid) |
| Check Payment | `ReferenceScreen` → `AmountScreen` → `PhoneScreen` → `BankScreen` → Loader → `InvoiceScreen` |
| Other Features | `DigitalChangeScreen`, `HistoricalScreen`, `SettingScreen` |

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 21
- Android SDK 34
- Morefun POS device (MF360/MF919) or emulator for testing

### Installation

```bash
git clone <repository-url>
cd mi-vuelto
```

Open the project in Android Studio and sync Gradle dependencies.

### Configuration

1. Replace the placeholder API base URL in `core-data/src/main/java/.../NetworkModule.kt`:
   ```kotlin
   .baseUrl("https://your-production-api.com/")
   ```

2. Configure signing credentials via `local.properties`:
   ```properties
   storeFile=path/to/keystore.jks
   storePassword=your_password
   keyAlias=your_alias
   keyPassword=your_key_password
   ```

---

## POS Device Integration

The app integrates with **Morefun Yapi SDK** for POS hardware features:

| Feature | Implementation |
|---------|---------------|
| Device Engine | `DeviceServiceEngine` via AIDL service binding |
| Serial Number | `DeviceInfoConstrants.COMMON_SN` |
| Device Support | MF360, MF919 models |
| Connection | `connectToService()` suspend function with death link monitoring |

### Supported Devices

| Model | Status |
|-------|--------|
| MF360 | ✅ Supported |
| MF919 | ✅ Supported |
| Other | Fallback serial mode |

---

## API Reference

### Authentication
| Endpoint | Method | Description |
|----------|--------|-------------|
| `app/login` | POST | Authenticate terminal with username, password, serial |

### Data
| Endpoint | Method | Description |
|----------|--------|-------------|
| `banks` | GET | Retrieve bank list |
| `configs` | GET | Fetch configuration by type |

### Transactions
| Endpoint | Method | Description |
|----------|--------|-------------|
| `transactions/query` | POST | Query payment transaction |
| `router/movimientos` | GET | Get bank movements |

---

## Roadmap

- [ ] Implement login API integration with token management
- [ ] Complete Digital Change feature
- [ ] Complete Instant Debit feature  
- [ ] Implement Historical with real transaction data
- [ ] Add printer receipt generation
- [ ] Implement offline caching with Room
- [ ] Add unit and integration tests
- [ ] Security hardening (certificate pinning, encrypted storage)
- [ ] ProGuard/R8 minification for release builds
- [ ] CI/CD pipeline with automated testing

---

## Development Methodology

This project follows **Spec-Driven Development (SDD)**:

1. **Specification** — Features are defined by detailed specs before implementation
2. **Design** — Architecture and component design follow the specification
3. **Implementation** — Code is written to fulfill the spec
4. **Verification** — Implementation is validated against the specification

---

## License

Proprietary — Corpocredit
