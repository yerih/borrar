# Tech Stack and Conventions

_How the project is built and the rules all code must follow. This is the technical reference that no feature plan should contradict._

---

## Technologies

| Category | Technology | Version | Notes |
|----------|-----------|---------|-------|
| **Language** | Kotlin | 1.9.22 | Strict typing, no `Any` in domain models |
| **Build System** | Android Gradle Plugin | 8.5.0 | Kotlin DSL for build scripts |
| **JVM Target** | Java 21 | 21 | Source and target compatibility |
| **UI Toolkit** | Jetpack Compose | BOM 2023.08.00 | Declarative UI, Material 3 |
| **DI Framework** | Dagger Hilt | 2.48 | Constructor injection, `@HiltViewModel` |
| **Networking** | Retrofit 2 + OkHttp | 2.9.0 / 4.11.0 | Gson converter, logging interceptor |
| **Async** | Kotlin Coroutines | 1.7.3 | `viewModelScope`, `Dispatchers.IO` |
| **Navigation** | Jetpack Navigation Compose | 2.7.1 | Nested graphs, shared ViewModels |
| **Local Database** | Room | 2.5.2 | Not yet implemented (offline cache pending) |
| **Animations** | Lottie | 6.7.1 | Compose integration |
| **Annotation Processing** | KSP | 1.9.22-1.0.17 | Hilt and Room codegen |
| **Min SDK** | Android 8.0 | 26 | POS devices minimum |
| **Target SDK** | Android 14 | 34/35 | Latest stable |

---

## Key Files / Modules

_Map of where everything lives. Only what a newcomer needs to get oriented._

### Module Structure

| Path | Responsibility |
|------|---------------|
| `app/` | Application entry, single activity, global navigation, SDK integration |
| `core/` | Domain models, use cases, repository interfaces (pure Kotlin) |
| `core-data/` | Repository implementations, API service, network DI |
| `core-ui/` | Design system, shared screens, navigation utilities, theme |
| `feature-check-payment/` | Feature screens, ViewModels, nested navigation graphs |

### Key Source Files

| Path | Responsibility |
|------|---------------|
| `app/src/.../MainApplication.kt` | Hilt application, SDK engine initialization |
| `app/src/.../MainActivity.kt` | Single activity hosting Compose navigation |
| `app/src/.../navigation/AppNavigation.kt` | Global NavHost with feature destinations |
| `app/src/.../di/SdkModuleDI.kt` | POS SDK and application DI providers |
| `app/src/.../sdk/ConnectionDeviceEngine.kt` | Morefun Yapi service binding |
| `core/.../domain/repository/DataRepository.kt` | Repository contract |
| `core-data/.../di/NetworkModule.kt` | OkHttp + Retrofit configuration |
| `core-data/.../network/ApiService.kt` | REST API interface |
| `core-data/.../repository/DataRepositoryImpl.kt` | Repository implementation |
| `core-ui/.../theme/` | Color, Type (Lato), Theme definitions |
| `core-ui/.../design/` | Button, Dialog, Keyboard, Loader, Textfield components |
| `feature-home/.../ui/login/LoginViewModel.kt` | MVI ViewModel example |
| `feature-purchase/.../HomeScreen.kt` | Home screen with function grid |

---

## Commands

| Command | Description |
|---------|-------------|
| `./gradlew assembleDebug` | Build debug APK |
| `./gradlew assembleRelease` | Build release APK (signed) |
| `./gradlew installDebug` | Install debug build on device |
| `./gradlew test` | Run unit tests |
| `./gradlew lint` | Run lint checks |

---

## Data Model / Domain

_Core entities and their fields/rules._

| Entity | Fields | Rules |
|--------|--------|-------|
| `CheckPaymentModel` | reference, amount, phone, bank | `amount` stored as cents (integer string), formatted on display |
| `BankModel` | logo, code, name | `code` is 4-digit bank identifier (e.g., "0105") |
| `DetailedReportModel` | isSale, reference, isApproved, pan, brand, date, amount | `amount` formatted with "Bs." prefix |
| `InvoiceModel` | businessName, bank, address, rif, date, time, ref, phone, amount | Read-only after creation |
| `LoginRequest` | username, password, terminalSerial | `terminalSerial` from POS device |
| `TransactionQueryRequest` | reference, amount, date, phone, document, bankId | `bankId` optional for multi-account |

### Currency Rules

- **Storage:** Amounts stored as integer strings (cents), e.g., `"10000"` = Bs. 100,00
- **Display:** German locale formatting with comma decimal: `10000` → `Bs. 100,00`
- **Input:** `DecimalCurrencyVisualTransformation` handles real-time formatting
- **Zero handling:** Leading zeros stripped on input

---

## Conventions

### Naming

- **Packages:** `com.mivuelto.<module>.<layer>.<feature>`
- **Composables:** PascalCase, descriptive: `ButtonFilled`, `LoginScreen`
- **ViewModels:** `<Feature>ViewModel`: `LoginViewModel`, `CheckPaymentViewModel`
- **State:** `<Feature>State`: `LoginState`
- **Intents:** `<Feature>Intent`: `LoginIntent`
- **Effects:** `<Feature>Effect`: `LoginEffect`
- **Files:** Match class name: `LoginScreen.kt` contains `LoginScreen`

### File Organization

```
feature-name/
  ui/
    screen-name/
      ScreenNameScreen.kt
      ScreenNameViewModel.kt
      ScreenNameUIContract.kt
    navigation/
      FeatureNavGraph.kt
  di/
    FeatureModule.kt (if needed)
```

### MVI Pattern (Required for all ViewModels)

```kotlin
// State — immutable data class
data class FeatureState(
    val isLoading: Boolean = false,
    val error: String? = null
)

// Intent — sealed interface for user actions
sealed interface FeatureIntent {
    data class OnValueChanged(val value: String) : FeatureIntent
    object OnSubmitClicked : FeatureIntent
}

// Effect — sealed interface for one-time events
sealed interface FeatureEffect {
    object NavigateToNext : FeatureEffect
    data class ShowError(val message: String) : FeatureEffect
}

// ViewModel — Hilt injection, StateFlow + Channel
@HiltViewModel
class FeatureViewModel @Inject constructor(...) : ViewModel() {
    private val _state = MutableStateFlow(FeatureState())
    val state: StateFlow<FeatureState> = _state.asStateFlow()

    private val _effect = Channel<FeatureEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: FeatureIntent) { ... }
}
```

### Error Handling

- Use `Result<T>` for repository return types
- Never catch exceptions silently — always propagate to UI
- Show user-friendly error messages, log technical details

### Currency Handling

- Always use `DecimalCurrencyVisualTransformation` for amount inputs
- Format display with `"Bs. "` prefix and comma decimal separator
- Parse input by filtering digits only, then divide by 100 for display

### Network Calls

- All API calls in `RemoteDataSource`
- Repository wraps in `Result<T>`
- ViewModels launch in `viewModelScope` with `Dispatchers.IO`

---

## Visual Style

### Colors

| Token | Usage |
|-------|-------|
| `primary` | Brand elements, active states |
| `secondary` | Accents, secondary actions |
| `error` | Validation errors, destructive actions |
| `surface` | Card backgrounds |
| `background` | Screen backgrounds |

### Typography

- **Font Family:** Lato (project-specific)
- **Tokens:** `headlineMedium`, `headlineSmall`, `titleSmall`, `bodySmall`, `invoiceLabel`, `invoiceMerchant`

### Layout Rules

- Single Activity architecture
- Navigation via Jetpack Navigation Compose
- Feature flows use nested navigation graphs
- Shared ViewModels scoped to navigation graph
- Back navigation handled by `BackHandler` in `BaseScreen`

### Component Usage

| Scenario | Component |
|----------|-----------|
| Primary action | `ButtonFilled` |
| Secondary/cancel | `ButtonBorder` |
| Home menu item | `ButtonHome` |
| Amount input | `TextField` + `DecimalCurrencyVisualTransformation` |
| Phone input | `TextField` + `PhoneVisualTransformation` |
| Bank selection | `BankSelector` |
| Loading state | `LoaderScreen` |
| Confirmation | `DialogConfirm` |
| Error message | `DialogMsg` |
| Multi-step form | `SingleFormScreen` |

---

## Hard Constraints

_Security rules, forbidden dependencies, frozen areas._

### Security

- **NEVER** commit keystore credentials to source control
- **NEVER** log HTTP request/response bodies in release builds
- **NEVER** store auth tokens in plaintext (use EncryptedSharedPreferences)
- **NEVER** hardcode API base URLs — use `local.properties` or build config fields
- **ALWAYS** use HTTPS for network calls
- **ALWAYS** validate input before sending to API

### Code Quality

- **NEVER** use `Any` as a return type in domain models
- **NEVER** expose mutable state from ViewModels (use `StateFlow.asStateFlow()`)
- **NEVER** use comments — write self-documenting code
- **NEVER** add dependencies without Architect approval
- **NEVER** create circular module dependencies

### Frozen Areas

- `core/` domain models — changes require spec update
- `core-ui/` design system — changes require Architect review
- `app/src/main/AndroidManifest.xml` — requires Security review for permission changes

### Forbidden Dependencies

- No `kotlin-android-extensions` (deprecated)
- No `findViewById` — Compose only
- No `LiveData` — use `StateFlow`
- No `SharedPreferences` — use `EncryptedSharedPreferences` for sensitive data
