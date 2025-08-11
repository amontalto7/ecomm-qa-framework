# Full-Stack QA Automation for a Microservices E-Commerce App

[![CI](https://github.com/amontalto7/ecomm-qa-framework/actions/workflows/ci.yml/badge.svg)](https://github.com/amontalto7/ecomm-qa-framework/actions)
[![Allure Report](https://img.shields.io/badge/Allure-Report-8A2BE2)](https://amontalto7.github.io/ecomm-qa-framework/)

UI + API + Contract + Visual + Perf automation, running locally via Docker and in CI/CD.

---

## 📌 What This Project Demonstrates
- **Senior-level QA architecture**: End-to-end testing across UI, API, contract, and performance.
- **Modern tooling**: Playwright, RestAssured, k6, WireMock, Docker, Allure, GitHub Actions.
- **Scalability & CI integration**: Parallel test execution, Dockerized services, PR quality gates.
- **Reporting & visibility**: Unified Allure dashboards and CI artifacts.

---

## 🏗 Architecture Overview
```
UI (Playwright)  →  Shop Web (Vite)  →  Shop API (Node/Express)  →  WireMock (Payment/Shipping stubs)
API (RestAssured) ───────────────────────────────────────────────→  Shop API (and stubs)
Perf (k6) ──────────────────────────────────────────────────────→  Shop API
```

## 🚀 Quick Start
```bash
# 1) Start services (API, Web, WireMock)
docker compose -f docker/docker-compose.yml up -d --build

# 2) Run UI tests (Playwright)
cd qa/ui
npm ci
npx playwright install --with-deps
npx playwright test

# 3) Run API tests (RestAssured)
mvn -f qa/api/pom.xml test

# 4) Perf smoke (k6)
k6 run qa/perf/smoke.js
```

---

## 📊 Reports

- **Live Allure (merged UI + API):** https://amontalto7.github.io/ecomm-qa-framework/
- **CI Run Artifacts (latest):** [Actions → Latest Run → Artifacts](https://github.com/amontalto7/ecomm-qa-framework/actions)

---

## 🖥 View Allure Reports Locally

### Install Allure CLI
- **Windows (Scoop)**:
  ```powershell
  Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
  irm get.scoop.sh | iex
  scoop install allure
  ```
- **Or via NPM**:
  ```bash
  npm install -g allure-commandline --location=global
  ```

### Generate & Open
```bash
# UI (Playwright)
allure generate qa/ui/allure-results -o qa/ui/allure-report --clean
allure open qa/ui/allure-report

# API (RestAssured)
allure generate qa/api/target/allure-results -o qa/api/allure-report --clean
allure open qa/api/allure-report

# Merged (UI + API)
mkdir -p .allure-merged
cp -r qa/ui/allure-results/* .allure-merged/ || true
cp -r qa/api/target/allure-results/* .allure-merged/ || true
allure generate .allure-merged -o .allure-site --clean
allure open .allure-site
```

**Allure contents:**
- Suites by component (UI / API), features/stories, severity
- Timeline and flaky test hints (via retries)
- Attached request/response logs for API tests (via allure-rest-assured)
- Screenshots/videos/traces for UI failures (via Playwright)

---

## 📂 Project Structure
```
ecomm-qa/
  docker/
    docker-compose.yml
    wiremock/
      mappings/*.json
      __files/*.json
  shop-api/            # Node/Express sample API (products/cart/checkout)
  shop-web/            # Minimal storefront (Vite static app served by nginx)
  qa/
    ui/                # Playwright (TypeScript) UI tests
    api/               # Java + RestAssured + TestNG API tests
    perf/              # k6 performance tests
    contract/          # (placeholder) OpenAPI / schema / pact checks
  .github/workflows/ci.yml
```

---

## 🎯 Key Scenarios Covered
- UI: Product browsing, add-to-cart, checkout (end-to-end flow)
- API: Product list, add-to-cart, checkout (+ negative checks)
- Perf: Simple smoke for `/products` endpoint

---

## 📈 Sample Quality Gates
- UI flake rate < 3%
- API schema drift detection on PRs
- p95 checkout latency < 400ms

---

## 🔮 Next Steps
- Expand performance suite to include load & stress tests
- Add contract tests (OpenAPI schema / Pact) and axe-core a11y smoke
- Add visual regression baselines for key pages

---

## 📜 License
MIT
