export const environment = {
  uiBaseUrl: process.env.UI_BASE_URL ?? 'http://localhost:3000',
  apiBaseUrl: process.env.API_BASE_URL ?? 'http://localhost:5454',
} as const;