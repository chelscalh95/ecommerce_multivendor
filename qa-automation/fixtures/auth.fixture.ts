import { expect, test as base } from '@playwright/test';
import { environment } from '../config/environment';

interface CustomerAuth {
  email: string;
  jwt: string;
}

interface AuthFixtures {
  customerAuth: CustomerAuth;
}

export const test = base.extend<AuthFixtures>({
  customerAuth: async ({ request }, use, testInfo) => {
    const uniqueValue = `${Date.now()}-${testInfo.workerIndex}`;
    const email = `qa.api.${uniqueValue}@example.com`;

    const otpResponse = await request.post(
      '/auth/sent/login-signup-otp',
      {
        data: { email },
      },
    );

    expect(
      otpResponse.status(),
      `OTP request failed: ${await otpResponse.text()}`,
    ).toBe(201);

    let emailBody = '';

    await expect
      .poll(
        async () => {
          const mailResponse = await request.get(
            `${environment.mailpitBaseUrl}/view/latest.html`,
            {
              params: {
                query: `to:"${email}"`,
              },
            },
          );

          if (!mailResponse.ok()) {
            return false;
          }

          emailBody = await mailResponse.text();

          return /login otp is\s*-\s*\d{6}/i.test(emailBody);
        },
        {
          message: `Expected an OTP email for ${email}`,
          timeout: 20_000,
          intervals: [250, 500, 1_000],
        },
      )
      .toBe(true);

    const otpMatch = emailBody.match(
      /login otp is\s*-\s*(\d{6})/i,
    );

    expect(otpMatch, 'Expected a six-digit OTP in the email').not.toBeNull();

    const signupResponse = await request.post('/auth/signup', {
      data: {
        fullName: 'API Test Customer',
        email,
        otp: otpMatch![1],
      },
    });

    expect(
      signupResponse.status(),
      `Signup failed: ${await signupResponse.text()}`,
    ).toBe(200);

    const signupBody: { jwt?: string; role?: string } =
      await signupResponse.json();

    expect(signupBody.jwt).toBeTruthy();
    expect(signupBody.role).toBe('ROLE_CUSTOMER');

    await use({
      email,
      jwt: signupBody.jwt!,
    });
  },
});

export { expect } from '@playwright/test';