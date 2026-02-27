import { test, expect } from '@playwright/test';

async function loginAsGuest(page: import('@playwright/test').Page) {
  await page.goto('/login');
  await page.getByText('게스트로 둘러보기').click();
  await page.waitForURL('/');
}

test.describe('Scan', () => {
  test('shows scan options after login', async ({ page }) => {
    await loginAsGuest(page);
    await page.goto('/scan');
    await expect(page.getByText('스캔 등록')).toBeVisible();
    await expect(page.getByText('영수증 스캔')).toBeVisible();
    await expect(page.getByText('사진 인식')).toBeVisible();
  });

  test('shows usage tip', async ({ page }) => {
    await loginAsGuest(page);
    await page.goto('/scan');
    await expect(page.getByText('사용 팁')).toBeVisible();
    await expect(page.getByText('밝은 환경에서 촬영하면')).toBeVisible();
  });

  test('shows login prompt when not logged in', async ({ page }) => {
    await page.goto('/scan');
    await expect(page.getByText('로그인이 필요해요')).toBeVisible();
  });
});
