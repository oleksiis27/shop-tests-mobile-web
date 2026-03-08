# Mobile Web Testing Framework

Automated mobile web testing framework for [shop-app](https://github.com) e-commerce application. Tests run on real mobile devices via BrowserStack, verifying the shopping flow through mobile Chrome (Android) and Safari (iOS).

## Tech Stack

| Tool | Purpose |
|------|---------|
| Java 21 | Language |
| Appium 2.x | Mobile automation protocol |
| Selenide | Fluent element interactions on top of Appium |
| Cucumber (JUnit 5) | BDD scenarios in Gherkin |
| BrowserStack | Cloud real device farm |
| Allure Report | Test reporting with screenshots |
| REST Assured | API-based test data setup |
| Gradle (Kotlin DSL) | Build tool |
| Owner | Configuration from properties / env vars |
| DataFaker | Test data generation |
| GitHub Actions | CI/CD with matrix strategy |

## Architecture

```
┌─────────────────────────────────────────────────────┐
│                  Cucumber Features                   │
│         (login, products, cart, checkout)            │
├─────────────────────────────────────────────────────┤
│                 Step Definitions                     │
│   CommonSteps · LoginSteps · ProductSteps           │
│   CartSteps · CheckoutSteps · TestContext            │
├──────────────────────┬──────────────────────────────┤
│     Page Objects      │       API Helpers            │
│  BaseMobilePage       │  AuthApi · ProductApi        │
│  HomePage             │  CartApi · OrderApi          │
│  ProductPage          │  ApiHelper                   │
│  CartPage             │  TestDataHelper              │
│  LoginPage            │                              │
│  RegisterPage         │                              │
│  OrdersPage           │                              │
├──────────────────────┴──────────────────────────────┤
│              DriverFactory + Config                  │
│        AppConfig · BrowserStackConfig                │
├─────────────────────────────────────────────────────┤
│            Appium / Selenide WebDriver               │
├────────────────────┬────────────────────────────────┤
│   Local Emulator   │   BrowserStack Real Devices     │
│  (Android / iOS)   │  Samsung Galaxy S23 · iPhone 15 │
└────────────────────┴────────────────────────────────┘
```

## Project Structure

```
src/
├── main/java/com/shop/
│   ├── config/
│   │   ├── AppConfig.java              # Owner interface: URLs, timeouts, run mode
│   │   ├── BrowserStackConfig.java     # BrowserStack credentials, devices
│   │   └── DriverFactory.java          # Creates Appium driver (local / BrowserStack)
│   ├── pages/
│   │   ├── BaseMobilePage.java         # Scroll, swipe, wait, navigation, screenshot
│   │   ├── HomePage.java              # Product catalog, search, filters, pagination
│   │   ├── ProductPage.java           # Product details, add to cart
│   │   ├── CartPage.java             # Cart items, quantity, remove, checkout
│   │   ├── LoginPage.java            # Email/password login
│   │   ├── RegisterPage.java         # User registration
│   │   └── OrdersPage.java           # Order history
│   ├── helpers/
│   │   ├── GestureHelper.java         # Touch gestures: swipe, tap, long press
│   │   ├── ApiHelper.java            # REST Assured request specs
│   │   └── TestDataHelper.java       # Faker-based data generation
│   └── api/
│       ├── AuthApi.java              # Register, login, get token
│       ├── ProductApi.java           # CRUD products, categories
│       ├── CartApi.java              # Add/update/remove cart items
│       └── OrderApi.java            # Create/list orders
└── test/
    ├── java/com/shop/
    │   ├── runners/
    │   │   ├── SmokeRunner.java       # @smoke tag — 5 critical scenarios
    │   │   └── RegressionRunner.java  # @regression tag — all 17 scenarios
    │   ├── steps/
    │   │   ├── TestContext.java       # Shared state via PicoContainer DI
    │   │   ├── CommonSteps.java      # Setup: register user, login, cart via API
    │   │   ├── LoginSteps.java       # Login, register, logout UI steps
    │   │   ├── ProductSteps.java     # Browse, search, product details steps
    │   │   ├── CartSteps.java        # Cart management steps
    │   │   └── CheckoutSteps.java    # Checkout and order verification steps
    │   └── hooks/
    │       └── Hooks.java            # Driver lifecycle, screenshot on failure
    └── resources/
        ├── features/                 # Gherkin scenarios
        ├── application.properties    # App config
        └── browserstack.properties   # Device config
```

## Test Scenarios

| Feature | Scenarios | Smoke |
|---------|-----------|-------|
| Login & Registration | Login, invalid login, register, logout | 2 |
| Browse Products | Catalog, search, filter, details, scroll | 2 |
| Shopping Cart | Add, update qty, remove, clear, persistence | 1 |
| Checkout | Full flow, empty cart, order history | 1 |
| **Total** | **17** | **6** |

## Prerequisites

- **Java 21+**
- **Docker & Docker Compose** — to run shop-app locally
- **shop-app** running at `localhost:3000` (frontend) and `localhost:8000` (backend)
- **BrowserStack account** — for cloud device testing ([free trial](https://www.browserstack.com/))
- **Appium 2.x** (optional) — only for local emulator mode

## Quick Start

### 1. Clone and start shop-app

```bash
git clone <shop-app-repo-url> ../shop-app
cd ../shop-app
docker compose up -d
# Wait for http://localhost:8000/api/health to return OK
```

### 2. Configure BrowserStack credentials

```bash
cp .env.example .env
# Edit .env with your BrowserStack username and access key
```

Or export directly:

```bash
export BROWSERSTACK_USERNAME=your_username
export BROWSERSTACK_ACCESS_KEY=your_access_key
```

### 3. Run tests on BrowserStack

```bash
# All tests on Android
./gradlew test -Drun.mode=browserstack -Dplatform=android

# All tests on iOS
./gradlew test -Drun.mode=browserstack -Dplatform=ios

# Smoke tests only
./gradlew smokeTest -Drun.mode=browserstack -Dplatform=android

# Regression suite
./gradlew regressionTest -Drun.mode=browserstack -Dplatform=android
```

### 4. Run tests on local emulator

Requires Appium server and Android emulator / iOS simulator:

```bash
# Start Appium
appium --port 4723

# Run tests
./gradlew test -Drun.mode=local -Dplatform=android
```

### 5. View Allure report

```bash
./gradlew allureServe
```

## Configuration

### System Properties

| Property | Values | Default | Description |
|----------|--------|---------|-------------|
| `run.mode` | `local`, `browserstack` | `local` | Execution environment |
| `platform` | `android`, `ios` | `android` | Target platform |
| `base.url` | URL | `http://localhost:3000` | Frontend URL |
| `api.url` | URL | `http://localhost:8000` | Backend API URL |

### Environment Variables

| Variable | Description |
|----------|-------------|
| `BROWSERSTACK_USERNAME` | BrowserStack account username |
| `BROWSERSTACK_ACCESS_KEY` | BrowserStack access key |

### BrowserStack Devices

| Platform | Device | OS Version | Browser |
|----------|--------|------------|---------|
| Android | Samsung Galaxy S23 | 13.0 | Chrome |
| iOS | iPhone 15 | 17 | Safari |

Configurable in `src/test/resources/browserstack.properties`.

## CI/CD

GitHub Actions workflow runs on:
- Push to `main`
- Pull requests to `main`
- Weekly schedule (Monday 8:00 UTC)
- Manual trigger

The pipeline:
1. Starts shop-app via Docker Compose
2. Opens BrowserStack Local tunnel (devices access localhost)
3. Runs tests on Android and iOS in parallel (matrix strategy)
4. Generates Allure report with history
5. Deploys reports to GitHub Pages

### Required GitHub Secrets

| Secret | Description |
|--------|-------------|
| `BROWSERSTACK_USERNAME` | BrowserStack username |
| `BROWSERSTACK_ACCESS_KEY` | BrowserStack access key |

## Test Design Principles

- **Independent tests** — each scenario sets up its own data via API (`@Before`) and does not depend on other tests
- **API for setup, UI for verification** — test data created through REST API for speed, assertions done through mobile UI
- **Page Object pattern** — mobile-specific page objects with scroll/swipe support
- **BDD with Cucumber** — business-readable scenarios in Gherkin
- **Allure reporting** — `@Step` annotations on all page object methods, screenshot on failure
