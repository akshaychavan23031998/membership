# FirstClub Membership Service

A Spring Boot backend service for managing a subscription-based membership program for FirstClub.

This project supports membership plans, membership tiers, configurable tier benefits, subscription lifecycle operations, tier eligibility evaluation, and basic concurrency handling using optimistic locking.

---

## 1. Problem Statement

FirstClub wants a backend system for a membership program where users can subscribe to membership plans and receive tier-based benefits.

The system should support:

* Monthly, Quarterly, and Yearly membership plans.
* Tiered membership levels such as Silver, Gold, and Platinum.
* Configurable benefits for each tier.
* User subscription actions such as subscribe, upgrade/downgrade tier, cancel subscription, and track current membership.
* Tier movement based on rules such as:

    * Number of orders more than a threshold.
    * Total monthly order value.
    * User belonging to a specific cohort.
* Demo-ready APIs.
* Clean abstractions, extensibility, modularity, and basic concurrency handling.

---

## 2. Tech Stack

* Java 17
* Spring Boot 3.5.14
* Spring Web
* Spring Data JPA
* Hibernate
* H2 In-Memory Database
* Lombok
* Bean Validation
* Swagger / OpenAPI

---

## 3. How to Run the Project

### Prerequisites

Make sure the following are installed:

* Java 17
* Maven
* IntelliJ IDEA / VS Code / any Java IDE

### Steps

Clone or open the project folder.

Run the application using:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Or run this class directly from IntelliJ:

```text
src/main/java/com/firstclub/membership/MembershipApplication.java
```

The application starts on:

```text
http://localhost:8080
```

---

## 4. Swagger API Documentation

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

This page lists all available APIs and allows testing them directly from the browser.

---

## 5. H2 Database Console

H2 console is enabled for database inspection.

Open:

```text
http://localhost:8080/h2-console
```

Use these values:

```text
JDBC URL: jdbc:h2:mem:firstclubdb
Username: sa
Password: 
```

Password is empty.

The database is in-memory, so data resets every time the application restarts.

---

## 6. Seeded Demo Data

The project includes a `DataSeeder` class that automatically inserts demo data on startup.

### Users

| User ID | Name        | Email                                           | Cohort            |
| ------- | ----------- | ----------------------------------------------- | ----------------- |
| 1       | Rahul       | [rahul@example.com](mailto:rahul@example.com)   | PREMIUM_CUSTOMERS |
| 2       | Normal User | [normal@example.com](mailto:normal@example.com) | REGULAR           |

### Membership Plans

| ID | Type      | Price | Duration |
| -- | --------- | ----: | -------: |
| 1  | MONTHLY   |   299 |  30 days |
| 2  | QUARTERLY |   799 |  90 days |
| 3  | YEARLY    |  2499 | 365 days |

### Membership Tiers

| ID | Type     | Rank |
| -- | -------- | ---: |
| 1  | SILVER   |    1 |
| 2  | GOLD     |    2 |
| 3  | PLATINUM |    3 |

### Benefits

| Tier     | Benefit Type      | Benefit Value                       |
| -------- | ----------------- | ----------------------------------- |
| SILVER   | FREE_DELIVERY     | Eligible orders above ₹499          |
| GOLD     | DISCOUNT          | 10% discount on selected categories |
| PLATINUM | PRIORITY_SUPPORT  | 24x7 priority support               |
| PLATINUM | EXCLUSIVE_COUPONS | PLATINUM20                          |

### Tier Rules

| Tier     | Rule Type           | Rule Value        |
| -------- | ------------------- | ----------------- |
| GOLD     | ORDER_COUNT         | 5                 |
| GOLD     | COHORT              | PREMIUM_CUSTOMERS |
| PLATINUM | MONTHLY_ORDER_VALUE | 10000             |

### User Order Stats

| User ID | Order Count | Total Monthly Order Value |
| ------- | ----------: | ------------------------: |
| 1       |           6 |                     12000 |
| 2       |           2 |                      1500 |

Based on this demo data, User 1 is eligible for `PLATINUM`, and User 2 falls back to `SILVER`.

---

## 7. API List

Base URL:

```text
http://localhost:8080/api/memberships
```

### 7.1 Get Membership Plans

```http
GET /api/memberships/plans
```

Example response:

```json
[
  {
    "id": 1,
    "type": "MONTHLY",
    "price": 299,
    "durationInDays": 30,
    "active": true
  },
  {
    "id": 2,
    "type": "QUARTERLY",
    "price": 799,
    "durationInDays": 90,
    "active": true
  },
  {
    "id": 3,
    "type": "YEARLY",
    "price": 2499,
    "durationInDays": 365,
    "active": true
  }
]
```

Purpose:

This API allows the client/frontend to show available membership plans to the user.

---

### 7.2 Get Membership Tiers

```http
GET /api/memberships/tiers
```

Example response:

```json
[
  {
    "id": 1,
    "type": "SILVER",
    "rankValue": 1,
    "active": true
  },
  {
    "id": 2,
    "type": "GOLD",
    "rankValue": 2,
    "active": true
  },
  {
    "id": 3,
    "type": "PLATINUM",
    "rankValue": 3,
    "active": true
  }
]
```

Purpose:

This API allows the client/frontend to show available membership tiers.

---

### 7.3 Get Benefits for a Tier

```http
GET /api/memberships/tiers/{tierId}/benefits
```

Example:

```http
GET /api/memberships/tiers/3/benefits
```

Example response:

```json
[
  {
    "id": 3,
    "tier": {
      "id": 3,
      "type": "PLATINUM",
      "rankValue": 3,
      "active": true
    },
    "benefitType": "PRIORITY_SUPPORT",
    "benefitValue": "24x7 priority support",
    "active": true
  },
  {
    "id": 4,
    "tier": {
      "id": 3,
      "type": "PLATINUM",
      "rankValue": 3,
      "active": true
    },
    "benefitType": "EXCLUSIVE_COUPONS",
    "benefitValue": "PLATINUM20",
    "active": true
  }
]
```

Purpose:

This API shows benefits unlocked by a specific tier.

---

### 7.4 Subscribe to a Plan and Tier

```http
POST /api/memberships/subscribe
```

Request body:

```json
{
  "userId": 1,
  "planType": "YEARLY",
  "tierType": "GOLD"
}
```

Example response:

```json
{
  "subscriptionId": 1,
  "userId": 1,
  "planType": "YEARLY",
  "tierType": "GOLD",
  "startDate": "2026-06-07T23:31:49.514949",
  "expiryDate": "2027-06-07T23:31:49.514949",
  "status": "ACTIVE"
}
```

Purpose:

This API creates a new active subscription for the user.

Business logic:

* Validates that the user exists.
* Validates that the selected plan exists and is active.
* Validates that the selected tier exists and is active.
* Prevents duplicate active subscriptions for the same user.
* Calculates expiry date based on selected plan duration.
* Saves the subscription as `ACTIVE`.

Duplicate subscription response:

```json
{
  "error": "User already has an active subscription"
}
```

---

### 7.5 Get Current Active Subscription

```http
GET /api/memberships/users/{userId}/subscription
```

Example:

```http
GET /api/memberships/users/1/subscription
```

Example response:

```json
{
  "subscriptionId": 1,
  "userId": 1,
  "planType": "YEARLY",
  "tierType": "GOLD",
  "startDate": "2026-06-07T23:31:49.514949",
  "expiryDate": "2027-06-07T23:31:49.514949",
  "status": "ACTIVE"
}
```

Purpose:

This API tracks the current membership and expiry of the user.

If no active subscription exists:

```json
{
  "error": "Active subscription not found"
}
```

---

### 7.6 Upgrade or Downgrade Tier

```http
PUT /api/memberships/users/{userId}/tier
```

Example:

```http
PUT /api/memberships/users/1/tier
```

Request body:

```json
{
  "tierType": "PLATINUM"
}
```

Example response:

```json
{
  "subscriptionId": 1,
  "userId": 1,
  "planType": "YEARLY",
  "tierType": "PLATINUM",
  "startDate": "2026-06-07T23:31:49.514949",
  "expiryDate": "2027-06-07T23:31:49.514949",
  "status": "ACTIVE"
}
```

Purpose:

This API supports upgrade and downgrade of membership tier.

Business logic:

* Finds the active subscription of the user.
* Finds the requested active tier.
* Updates only the tier.
* Keeps plan and expiry date unchanged.

---

### 7.7 Cancel Subscription

```http
DELETE /api/memberships/users/{userId}/subscription
```

Example:

```http
DELETE /api/memberships/users/1/subscription
```

Example response:

```text
Subscription cancelled successfully
```

Purpose:

This API cancels the current active subscription.

Important design decision:

The subscription row is not deleted from the database. Its status is changed from:

```text
ACTIVE -> CANCELLED
```

This preserves subscription history.

After cancellation, calling current subscription API returns:

```json
{
  "error": "Active subscription not found"
}
```

---

### 7.8 Evaluate User Tier Eligibility

```http
GET /api/memberships/users/{userId}/tier-eligibility
```

Example:

```http
GET /api/memberships/users/1/tier-eligibility
```

Example response:

```json
{
  "userId": 1,
  "eligibleTier": "PLATINUM",
  "reason": "User matched Platinum tier rules"
}
```

Purpose:

This API evaluates which tier the user is eligible for based on configurable rules.

For seeded data:

* User 1 has 6 orders.
* User 1 has monthly order value 12000.
* Platinum rule requires monthly order value 10000.
* Therefore User 1 is eligible for Platinum.

---

## 8. Architecture

The project follows a layered architecture.

```text
controller  -> REST API layer
service     -> Business logic layer
repository  -> Database access layer
entity      -> Database table models
dto         -> Request and response models
enums       -> Fixed domain values
strategy    -> Extensible tier eligibility logic
exception   -> Centralized error handling
config      -> Startup demo data seeding
```

Request flow:

```text
Client / Swagger / Postman
        ↓
Controller
        ↓
Service
        ↓
Repository
        ↓
H2 Database
```

---

## 9. Entity Design

### User

Represents a customer.

Fields:

* id
* name
* email
* cohort

The `cohort` field supports cohort-based tier rules.

### MembershipPlan

Represents available subscription plans.

Fields:

* id
* type
* price
* durationInDays
* active

Plan types:

* MONTHLY
* QUARTERLY
* YEARLY

### MembershipTier

Represents membership levels.

Fields:

* id
* type
* rankValue
* active

Tier types:

* SILVER
* GOLD
* PLATINUM

`rankValue` helps compare tier hierarchy.

### TierBenefit

Represents configurable tier benefits.

Fields:

* id
* tier
* benefitType
* benefitValue
* active

This makes benefits configurable without changing code.

### Subscription

Represents a user's membership subscription.

Fields:

* id
* user
* plan
* tier
* startDate
* expiryDate
* status
* version

Status values:

* ACTIVE
* CANCELLED
* EXPIRED

### TierRule

Represents configurable rules for tier eligibility.

Fields:

* id
* tierType
* ruleType
* ruleValue
* active

Rule types:

* ORDER_COUNT
* MONTHLY_ORDER_VALUE
* COHORT

### UserOrderStats

Represents simplified order statistics for a user for a month.

Fields:

* id
* userId
* orderCount
* totalOrderValue
* month

In a real system, this data would likely come from an Order Service or Analytics Service.

---

## 10. Strategy Pattern for Tier Eligibility

Tier eligibility is implemented using the Strategy Pattern.

Interface:

```text
TierEligibilityStrategy
```

Implementations:

```text
OrderCountEligibilityStrategy
MonthlyValueEligibilityStrategy
CohortEligibilityStrategy
```

Why this design?

Instead of putting all rule checks in one large if-else block, each rule type has its own class.

Example:

* `OrderCountEligibilityStrategy` handles `ORDER_COUNT`.
* `MonthlyValueEligibilityStrategy` handles `MONTHLY_ORDER_VALUE`.
* `CohortEligibilityStrategy` handles `COHORT`.

This makes the system extensible.

If a new rule is added in the future, such as:

```text
REFERRAL_COUNT
PAYMENT_METHOD
CITY_BASED_RULE
```

we can add a new strategy class without modifying existing strategy classes.

This improves:

* Modularity
* Maintainability
* Testability
* Extensibility

---

## 11. Concurrency Handling

The `Subscription` entity includes:

```java
@Version
private Long version;
```

This enables optimistic locking.

Why this matters:

Two requests may try to update the same subscription at the same time.

Example:

```text
Request 1: Upgrade subscription to Platinum
Request 2: Cancel subscription
```

With optimistic locking, Hibernate tracks the row version. If one request updates the row first, the second conflicting update can be detected instead of silently overwriting data.

This shows basic concurrency awareness in the subscription lifecycle.

---

## 12. Validation and Error Handling

Request DTOs use validation annotations like:

```java
@NotNull
```

Example:

```java
private Long userId;
private PlanType planType;
private TierType tierType;
```

If required fields are missing, Spring validation rejects the request.

Custom exceptions:

```text
ResourceNotFoundException
BadRequestException
```

Global handler:

```text
GlobalExceptionHandler
```

This returns clean error responses.

Example:

```json
{
  "error": "User not found"
}
```

or:

```json
{
  "error": "User already has an active subscription"
}
```

---

## 13. Important Design Decisions

### 1. Benefits are configurable

Benefits are stored in the `TierBenefit` table instead of being hardcoded.

This allows changing benefits without code changes.

### 2. Tier rules are configurable

Rules are stored in `TierRule`.

This allows changing eligibility thresholds without code changes.

Example:

Change Gold requirement from 5 orders to 8 orders by updating rule value.

### 3. Strategy Pattern is used for rule evaluation

This avoids large if-else blocks and allows adding new rule types easily.

### 4. Subscription cancellation is soft cancellation

The subscription is marked as `CANCELLED`, not deleted.

This preserves history.

### 5. Optimistic locking is added

The `@Version` field protects subscription updates from concurrency issues.

### 6. H2 is used for demo

H2 makes the project easy to run and evaluate without external database setup.

---

## 14. Example Demo Flow

Use Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

Recommended demo steps:

### Step 1: Get plans

```http
GET /api/memberships/plans
```

Shows Monthly, Quarterly, Yearly plans.

### Step 2: Get tiers

```http
GET /api/memberships/tiers
```

Shows Silver, Gold, Platinum tiers.

### Step 3: Get benefits for Platinum

```http
GET /api/memberships/tiers/3/benefits
```

Shows Platinum benefits.

### Step 4: Check eligibility

```http
GET /api/memberships/users/1/tier-eligibility
```

Expected:

```json
{
  "userId": 1,
  "eligibleTier": "PLATINUM",
  "reason": "User matched Platinum tier rules"
}
```

### Step 5: Subscribe user

```http
POST /api/memberships/subscribe
```

Body:

```json
{
  "userId": 1,
  "planType": "YEARLY",
  "tierType": "GOLD"
}
```

### Step 6: Get current subscription

```http
GET /api/memberships/users/1/subscription
```

Shows active subscription with expiry.

### Step 7: Upgrade tier

```http
PUT /api/memberships/users/1/tier
```

Body:

```json
{
  "tierType": "PLATINUM"
}
```

### Step 8: Cancel subscription

```http
DELETE /api/memberships/users/1/subscription
```

Expected:

```text
Subscription cancelled successfully
```

### Step 9: Confirm cancellation

```http
GET /api/memberships/users/1/subscription
```

Expected:

```json
{
  "error": "Active subscription not found"
}
```

---

## 15. Future Improvements

Possible improvements for production:

* Add authentication and authorization.
* Add payment integration.
* Add subscription renewal.
* Add scheduled job to mark expired subscriptions as `EXPIRED`.
* Add audit logs.
* Add pagination for listing APIs.
* Add unit and integration tests.
* Use PostgreSQL/MySQL instead of H2.
* Add admin APIs to configure plans, tiers, benefits, and rules.
* Add event publishing for subscription changes.
* Add distributed locking or idempotency keys for payment/subscription workflows.
* Integrate with real Order Service for order count and order value.

---

## 16. Completed Requirements Checklist

| Requirement                          | Status |
| ------------------------------------ | ------ |
| Monthly, Quarterly, Yearly plans     | Done   |
| Plan pricing                         | Done   |
| Tiered membership                    | Done   |
| Configurable tier benefits           | Done   |
| Subscribe to plan + tier             | Done   |
| Upgrade/downgrade tier               | Done   |
| Cancel subscription                  | Done   |
| Track current membership and expiry  | Done   |
| Tier movement by order count         | Done   |
| Tier movement by monthly order value | Done   |
| Tier movement by cohort              | Done   |
| Functional APIs                      | Done   |
| Demo data                            | Done   |
| Swagger documentation                | Done   |
| Modular architecture                 | Done   |
| Extensible rule evaluation           | Done   |
| Basic concurrency handling           | Done   |

---

## 17. Project Summary

This project implements a complete backend service for FirstClub's membership program.

It supports subscription plans, membership tiers, configurable benefits, user subscription lifecycle, tier eligibility evaluation, and clean API responses.

The design focuses on modularity and extensibility through layered architecture, DTOs, repositories, services, and the Strategy Pattern for tier rules.

The system is demo-ready with H2 database seeding and Swagger API documentation.
