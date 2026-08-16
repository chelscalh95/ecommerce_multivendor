import { expect, test } from '@playwright/test';

test.describe('Backend API availability', () => {
  test('publishes a valid OpenAPI document', async ({ request }) => {
    const response = await request.get('/v3/api-docs');

    expect(response.status()).toBe(200);
    expect(response.headers()['content-type']).toContain('application/json');

    const openApiDocument = await response.json();

    expect(openApiDocument.openapi).toMatch(/^3\./);
    expect(openApiDocument.info?.title).toBeTruthy();
    expect(Object.keys(openApiDocument.paths ?? {}).length).toBeGreaterThan(0);
  });
});