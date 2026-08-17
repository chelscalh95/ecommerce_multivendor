import { expect, test } from '../../fixtures/auth.fixture';

test.describe('Authenticated customer API access', () => {
  test('customer JWT authorizes cart and wishlist requests', async ({
    request,
    customerAuth,
  }) => {
    const headers = {
      Authorization: `Bearer ${customerAuth.jwt}`,
    };

    const [cartResponse, wishlistResponse] = await Promise.all([
      request.get('/api/cart', { headers }),
      request.get('/api/wishlist', { headers }),
    ]);

    expect(
      cartResponse.status(),
      `Cart response: ${await cartResponse.text()}`,
    ).toBe(200);

    expect(
      wishlistResponse.status(),
      `Wishlist response: ${await wishlistResponse.text()}`,
    ).toBe(200);

    const cart = await cartResponse.json();
    const wishlist = await wishlistResponse.json();

    expect(Array.isArray(cart.cartItems)).toBe(true);
    expect(Array.isArray(wishlist.products)).toBe(true);
  });
});