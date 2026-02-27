import { test, expect } from '@playwright/test';

test.describe('Navigation', () => {
  test('home page loads with header and quick actions', async ({ page }) => {
    await page.goto('/');
    await expect(page.locator('h1')).toContainText('냉장고 레시피');
    await expect(page.getByText('식재료 추가')).toBeVisible();
    await expect(page.getByText('영수증 스캔')).toBeVisible();
    await expect(page.getByText('레시피 추천')).toBeVisible();
  });

  test('bottom nav has all tabs', async ({ page }) => {
    await page.goto('/');
    const nav = page.locator('nav');
    await expect(nav.getByText('홈')).toBeVisible();
    await expect(nav.getByText('냉장고')).toBeVisible();
    await expect(nav.getByText('레시피')).toBeVisible();
    await expect(nav.getByText('마이')).toBeVisible();
  });

  test('navigate to fridge page', async ({ page }) => {
    await page.goto('/');
    await page.locator('nav').getByText('냉장고').click();
    await expect(page).toHaveURL('/fridge');
    await expect(page.getByRole('heading', { name: '내 냉장고' })).toBeVisible();
  });

  test('navigate to recipe page', async ({ page }) => {
    await page.goto('/');
    await page.locator('nav').getByText('레시피').click();
    await expect(page).toHaveURL('/recipe');
  });

  test('navigate to mypage', async ({ page }) => {
    await page.goto('/');
    await page.locator('nav').getByText('마이').click();
    await expect(page).toHaveURL('/mypage');
    await expect(page.getByRole('heading', { name: '마이페이지' })).toBeVisible();
  });
});
