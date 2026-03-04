# Design Pattern Changes

This document explains the design-pattern refactor applied to the flight reservation codebase and the reasoning behind each choice.

## 1) Factory Pattern (`AircraftFactory`)

### Where it was applied
- Added `Aircraft` abstraction and `AircraftFactory`:
  - `src/main/java/flight/reservation/plane/Aircraft.java`
  - `src/main/java/flight/reservation/plane/AircraftFactory.java`
- Updated aircraft implementations to conform to the abstraction:
  - `PassengerPlane`, `Helicopter`, `PassengerDrone`
- Updated object creation sites (example: `Runner`) to create aircraft via factory.

### Why this pattern fits this codebase
Before refactor, aircraft creation and model validation logic were spread across multiple constructors and call sites, with model strings tightly coupled to concrete classes. This made creation rules harder to maintain and easier to duplicate inconsistently.

The codebase has a clear requirement to support multiple aircraft models with model-specific behavior (capacity, crew size, route compatibility). A factory centralizes model-to-type mapping and gives one controlled entry point for creating valid aircraft objects.

### Benefits
- Centralized creation logic for all aircraft models.
- Easier extension when adding new models/types (single place to register mapping).
- Reduces object-creation duplication in calling code.
- Improves consistency of validation and exception behavior.

### Drawbacks
- Adds an extra abstraction layer that can feel heavy for very small projects.
- If implemented as a large `switch`, factory can grow into a maintenance hotspot.
- Poorly designed factory APIs can hide dependencies and reduce clarity in tests.

## 2) Strategy Pattern (Payment Processing)

### Where it was applied
- Added payment strategy interface and concrete strategies:
  - `src/main/java/flight/reservation/payment/strategy/PaymentStrategy.java`
  - `CreditCardPaymentStrategy.java`
  - `PaypalPaymentStrategy.java`
- Refactored `FlightOrder` to delegate payment execution to strategies via `processPayment(...)`.

### Why this pattern fits this codebase
`FlightOrder` previously handled multiple payment types with payment-specific validation and behavior inside one class. This caused branching and mixed responsibilities (order lifecycle + payment mechanics).

The codebase already supports at least two payment methods and is likely to grow (e.g., wallet, bank transfer). Strategy isolates payment behavior per method and keeps `FlightOrder` focused on order state transitions.

### Benefits
- Removes payment-type branching from `FlightOrder`.
- Improves Single Responsibility Principle: order state and payment logic are separated.
- Easier to add new payment methods without modifying core order flow (Open/Closed Principle).
- More testable: each payment strategy can be unit-tested independently.

### Drawbacks
- Introduces more classes/interfaces, increasing project structure complexity.
- Shared validation rules may become duplicated across strategies if not factored well.
- Developers need to trace one extra indirection layer to follow payment flow.

## Supporting Structural Improvement (Enabler)

Although not a GoF pattern by itself, introducing the `Aircraft` interface was a necessary structural step to make the factory useful and remove type-checking code paths.

### What problem it solved
- Replaced `Object aircraft` and `instanceof` checks in `Flight`/`ScheduledFlight` with polymorphism.
- Capacity and crew logic now lives in aircraft types, not in consumers.

### Effect on pattern quality
- Factory now returns a stable abstraction (`Aircraft`) rather than raw `Object`.
- The model is more extensible and less error-prone when adding new aircraft types.

## Pattern Selection Summary

- Factory was chosen to solve **creation sprawl and model-to-type coupling**.
- Strategy was chosen to solve **behavior branching and mixed payment responsibilities**.
- The two patterns complement each other:
  - Factory standardizes object creation.
  - Strategy standardizes algorithm/behavior variation at runtime.

Together, these changes improve extensibility, maintainability, and testability while accepting moderate increases in class count and abstraction overhead.


