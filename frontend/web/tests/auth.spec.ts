import { test, expect } from '@playwright/test';

test.describe('Authentication', () => {
  test('login page shows social login buttons', async ({ page }) => {
    await page.goto('/login');
    await expect(page.getByText('냉장고 레시피')).toBeVisible();
    await expect(page.getByText('카카오로 시작하기')).toBeVisible();
    await expect(page.getByText('Google로 시작하기')).toBeVisible();
    await expect(page.getByText('게스트로 둘러보기')).toBeVisible();
  });

  test('guest login works and redirects to home', async ({ page }) => {
    await page.goto('/login');
    await page.getByText('게스트로 둘러보기').click();
    await page.waitForURL('/');
    await expect(page.locator('h1')).toContainText('냉장고 레시피');
  });

  test('login page shows error messages from query params', async ({ page }) => {
    await page.goto('/login?error=kakao_denied');
    await expect(page.getByText('카카오 로그인이 취소되었습니다')).toBeVisible();
  });

  test('unauthenticated pages show login prompt', async ({ page }) => {
    await page.goto('/fridge');
    await expect(page.getByText('로그인이 필요해요')).toBeVisible();
    await expect(page.getByText('로그인하기')).toBeVisible();
  });

  test('mypage shows login prompt when not logged in', async ({ page }) => {
    await page.goto('/mypage');
    await expect(page.getByText('로그인이 필요해요')).toBeVisible();
  });
});
