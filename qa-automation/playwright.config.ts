import { defineConfig, devices } from '@playwright/test';
import { environment } from './config/environment';

export default defineConfig({
  testDir: './tests',
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 1 : undefined,
  timeout: 30_000,

  expect: {
    timeout: 5_000,
  },

  reporter: [
    ['line'],
    ['html', { open: 'never' }],
  ],

  use: {
    trace: 'on-first-retry',
    screenshot: 'only-on-failure',
    video: 'retain-on-failure',
  },

  projects: [
    {
      name: 'api',
      testMatch: /api\/.*\.spec\.ts/,
      use: {
        baseURL: environment.apiBaseUrl,
      },
    },
    {
      name: 'chromium',
      testMatch: /ui\/.*\.spec\.ts/,
      use: {
        ...devices['Desktop Chrome'],
        baseURL: environment.uiBaseUrl,
      },
    },
    {
      name: 'firefox',
      testMatch: /ui\/.*\.spec\.ts/,
      use: {
        ...devices['Desktop Firefox'],
        baseURL: environment.uiBaseUrl,
      },
    },
    {
      name: 'webkit',
      testMatch: /ui\/.*\.spec\.ts/,
      use: {
        ...devices['Desktop Safari'],
        baseURL: environment.uiBaseUrl,
      },
    },
  ],
});