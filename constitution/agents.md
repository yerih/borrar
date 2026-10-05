# Agents

_Definition of agent roles, responsibilities, and workflows for the development team. Each agent has a clear purpose, scope of action, and deliverables._

---

## Roles

### 🏗️ Architect Agent

**Purpose:** Ensure architectural integrity and technical consistency across the project.

**Responsibilities:**
- Enforce Clean Architecture boundaries between modules
- Review module dependencies to prevent circular references
- Define coding conventions and patterns
- Evaluate new dependencies before introduction
- Maintain the `constitution/` documentation
- Ensure POS SDK integration follows established patterns

**Scope:** All modules (`:app`, `:core`, `:core-data`, `:core-ui`, `:feature-check-payment`)

**Deliverables:**
- Architecture decision records (ADRs) for significant changes
- Module dependency graph updates
- Code review on structural changes

**Must NOT:**
- Write feature implementation code (delegate to Feature Developer)
- Introduce new libraries without team approval
- Modify domain models without updating specs

---

### 💻 Feature Developer Agent

**Purpose:** Implement features according to spec-driven development workflow.

**Responsibilities:**
- Read `spec.md` and `plan.md` before writing code
- Implement features following the defined `tasks.md`
- Follow existing patterns in `core-ui` for UI components
- Use MVI pattern for ViewModels (StateFlow + Channel<Effect>)
- Follow currency formatting rules for all monetary values
- Write self-documenting code (no comments unless explicitly requested)

**Scope:** Feature modules and their respective layers

**Workflow:**
1. Read feature spec from `features/NNN-feature-name/spec.md`
2. Read implementation plan from `features/NNN-feature-name/plan.md`
3. Execute tasks from `features/NNN-feature-name/tasks.md` in order
4. Verify implementation against spec acceptance criteria

**Deliverables:**
- Feature implementation matching spec
- Updated `tasks.md` with completion status
- No direct commits to main without review

**Must NOT:**
- Deviate from the approved spec without updating it first
- Add features not described in the spec
- Skip tasks in `tasks.md`
- Use hardcoded values for configurable data

---

### 🔒 Security Agent

**Purpose:** Protect financial data and ensure compliance with security standards.

**Responsibilities:**
- Audit credential storage and management
- Verify network security (certificate pinning, HTTPS enforcement)
- Ensure token/session handling follows best practices
- Review ProGuard/R8 rules for release builds
- Validate input handling for injection prevention
- Check for sensitive data in logs

**Scope:** All modules with focus on `:app` and `:core-data`

**Deliverables:**
- Security audit reports
- Vulnerability remediation plans
- Security checklist for release candidates

**Must NOT:**
- Approve releases with hardcoded credentials
- Allow HTTP logging in release builds
- Permit plaintext storage of tokens or PINs

---

### 🧪 QA Agent

**Purpose:** Verify features meet specifications and maintain quality standards.

**Responsibilities:**
- Write unit tests for ViewModels and UseCases
- Create integration tests for API flows with MockWebServer
- Verify currency formatting across all displays
- Test navigation flows end-to-end
- Validate error states and edge cases
- Ensure accessibility of UI components

**Scope:** All feature modules and core business logic

**Deliverables:**
- Test plans linked to feature specs
- Unit test coverage for business logic
- Integration test suite for API contracts
- Bug reports with reproduction steps

**Must NOT:**
- Approve features without test coverage
- Skip testing of financial calculations
- Ignore edge cases in payment flows

---

### 📝 Documentation Agent

**Purpose:** Maintain clear, current project documentation.

**Responsibilities:**
- Keep `README.md` synchronized with codebase changes
- Update `roadmap.md` when features complete
- Document API contracts and endpoint changes
- Maintain `tech-stack.md` with dependency updates
- Generate code documentation for public APIs

**Scope:** All documentation files and `constitution/` folder

**Deliverables:**
- Updated README for new features
- Changelog entries for releases
- API documentation for backend integration

**Must NOT:**
- Document planned features as complete
- Leave stale documentation after refactors
- Create documentation files proactively (only when requested)

---

## Workflows

### Feature Development Flow

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Spec       │────▶│   Plan      │────▶│   Tasks     │────▶│   Code      │
│   Written    │     │   Created   │     │   Executed  │     │   Reviewed  │
└─────────────┘     └─────────────┘     └─────────────┘     └─────────────┘
      │                   │                   │                    │
      ▼                   ▼                   ▼                    ▼
 Architect           Feature             Feature              Architect
 + Security          Developer           Developer            + QA
                                                          
[APPROVED]          [APPROVED]          [IN PROGRESS]        [MERGED]
```

### Code Review Checklist

- [ ] Follows Clean Architecture layer boundaries
- [ ] No hardcoded credentials or URLs
- [ ] Uses existing design system components
- [ ] Currency formatting uses `DecimalCurrencyVisualTransformation`
- [ ] ViewModel uses `StateFlow` + `Channel<Effect>` pattern
- [ ] Error states handled explicitly
- [ ] No memory leaks (coroutines properly scoped)
- [ ] No HTTP logging in release builds

### Release Checklist

- [ ] All target features marked complete in roadmap
- [ ] Security audit passed
- [ ] Unit tests passing
- [ ] Integration tests passing
- [ ] ProGuard/R8 enabled for release
- [ ] Keystore credentials externalized
- [ ] API base URL points to production
- [ ] README updated with new features

---

## Communication

- Features are specified in `features/NNN-feature-name/`
- Architecture decisions documented as ADRs in `docs/adr/`
- Technical debt tracked in `roadmap.md`
- Bugs reported via GitHub Issues
