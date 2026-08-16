# E-Commerce QA Automation

A TypeScript automation framework for testing the multivendor
e-commerce application across its UI, REST API, and MySQL database.

## Current Coverage

- Backend availability through its OpenAPI document
- API tests executed independently of browser projects
- Cross-browser UI projects for Chromium, Firefox, and WebKit
- TypeScript static validation
- HTML reports, traces, screenshots, and video failure artifacts

## Prerequisites

- Node.js 24 LTS
- Docker Desktop
- Docker Compose
- The application containers running locally

## Local Services

| Service | URL |
|---|---|
| Frontend | `http://localhost:3000` |
| Backend API | `http://localhost:5454` |
| OpenAPI document | `http://localhost:5454/v3/api-docs` |
| Mailpit | `http://localhost:8025` |
| MySQL | `localhost:3306` |

## Installation

```bash
npm ci
npx playwright install