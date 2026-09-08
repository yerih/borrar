# Mission

_Defines the project's reason for being. It is the reference point used to decide whether a feature "fits" or not._

## What We Are Building

**Mi Vuelto** is an Android POS payment terminal application that modernizes payment processing for Venezuelan merchants. It replaces legacy terminal interfaces with a modern, intuitive experience for verifying mobile payments, generating digital receipts, and managing transactions in Bolívares (Bs.).

_Main components:_

1. **Payment Verification** — Multi-step flow to validate mobile payment transactions by collecting reference, amount, phone, and bank data.
2. **Digital Receipts** — Generate and display transaction invoices with merchant details, bank information, and formatted currency.
3. **POS Hardware Integration** — Connect to Morefun terminal devices for serial number identification and receipt printing.
4. **Transaction Management** — View history, process digital change, and handle instant debits from a unified home screen.

## Who It Is For

- **Merchants** — Venezuelan business owners using Corpocredit POS terminals who need to verify customer mobile payments quickly and reliably.
- **Terminal Operators** — Staff interacting with the POS device daily; need simple, fast workflows with minimal training.
- **Corpocredit** — The financial services company providing the payment infrastructure; needs a maintainable, secure, and extensible codebase.

## Principles

- **Simplicity** — Every screen should accomplish one clear task. Multi-step flows break complex operations into focused, single-purpose screens.
- **Reliability** — Payment verification must work consistently. Offline caching and retry mechanisms ensure transactions are never lost.
- **Security First** — Financial data demands the highest protection. No credentials in source, encrypted storage, certificate pinning, and tamper detection are non-negotiable.
- **Currency Accuracy** — Bolivar formatting (comma decimal separator, "Bs." prefix) must be correct everywhere. Financial precision is mandatory.
- **Modularity** — Features are independent modules with clear boundaries. Changes to one feature never break another.

## What It Is NOT

- **A consumer payment app** — This is for merchants only, not end customers making payments.
- **A multi-currency wallet** — Only Venezuelan Bolivares (Bs.) is supported. No crypto, no foreign currency.
- **A banking app** — No account management, balance inquiries, or fund transfers between accounts.
- **A web application** — Android-only, targeting specific Morefun POS hardware.
- **A general-purpose POS** — Focused on mobile payment verification, not inventory, sales, or full retail management.
