import { test, expect } from '@playwright/test';

// Helper to login as guest
async function loginAsGuest(page: import('@playwright/test').Page) {
  await page.goto('/login');
  await page.getByText('게스트로 둘러보기').click();
  await page.waitForURL('/');
}

test.describe('Fridge', () => {
  test('shows fridge content after login', async ({ page }) => {
    await loginAsGuest(page);
    await page.goto('/fridge');
    await expect(page.getByRole('heading', { name: '내 냉장고' })).toBeVisible();
    // Should show storage tabs
    await expect(page.getByRole('button', { name: '전체' })).toBeVisible();
    await expect(page.getByRole('button', { name: '냉장' })).toBeVisible();
    await expect(page.getByRole('button', { name: '냉동' })).toBeVisible();
    await expect(page.getByRole('button', { name: '실온' })).toBeVisible();
  });

  test('storage tabs are clickable', async ({ page }) => {
    await loginAsGuest(page);
    await page.goto('/fridge');
    await page.getByRole('button', { name: '냉장' }).click();
    // Tab should be active (has checkmark)
    await expect(page.getByText('✓')).toBeVisible();
  });

  test('add ingredient button is visible', async ({ page }) => {
    await loginAsGuest(page);
    await page.goto('/fridge');
    // FAB button should exist
    const fab = page.locator('button').filter({ has: page.locator('svg.lucide-plus') });
    await expect(fab).toBeVisible();
  });
});
