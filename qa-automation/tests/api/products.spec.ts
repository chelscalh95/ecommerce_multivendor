import { expect, test } from '@playwright/test';

interface Product {
  id: number;
  title: string;
  mrpPrice: number;
  sellingPrice: number;
  quantity: number;
  images: string[];
  in_stock: boolean;
  category: {
    id: number;
    name: string;
    categoryId: string;
    level: number;
  };
}

interface ProductPage {
  totalElements: number;
  totalPages: number;
  numberOfElements: number;
  content: Product[];
  first: boolean;
  last: boolean;
  empty: boolean;
}

test.describe('Product catalog API', () => {
  test('returns available mobile products with valid pricing', async ({
    request,
  }) => {
    const response = await request.get('/products', {
      params: {
        category: 'mobiles',
        sort: '',
        brand: '',
        color: '',
        pageNumber: 0,
      },
    });

    const responseBody = await response.text();

    expect(response.status(),`Unexpected response body: ${responseBody}`).toBe(200);
    expect(response.headers()['content-type']).toContain('application/json');

    const productPage: ProductPage = await response.json();

    expect(productPage.empty).toBe(false);
    expect(productPage.content.length).toBeGreaterThan(0);
    expect(productPage.numberOfElements).toBe(productPage.content.length);
    expect(productPage.totalElements).toBeGreaterThanOrEqual(
      productPage.content.length,
    );

    const productIds = productPage.content.map((product) => product.id);
    expect(new Set(productIds).size).toBe(productIds.length);

    for (const product of productPage.content) {
      expect(product.id).toBeGreaterThan(0);
      expect(product.title.trim()).not.toBe('');
      expect(product.category.categoryId).toBe('mobiles');
      expect(product.quantity).toBeGreaterThanOrEqual(0);
      expect(product.mrpPrice).toBeGreaterThan(0);
      expect(product.sellingPrice).toBeGreaterThan(0);
      expect(product.sellingPrice).toBeLessThanOrEqual(product.mrpPrice);
      expect(Array.isArray(product.images)).toBe(true);
      expect(product.in_stock).toBe(product.quantity > 0);
    }
  });
});