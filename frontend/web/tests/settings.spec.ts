import { test, expect } from '@playwright/test';

test.describe('Settings', () => {
  test('settings page loads with all sections', async ({ page }) => {
    await page.goto('/settings');
    await expect(page.getByRole('heading', { name: '설정', exact: true })).toBeVisible();
    await expect(page.getByText('알림 설정')).toBeVisible();
    await expect(page.getByText('소비기한 알림')).toBeVisible();
    await expect(page.getByText('추천 레시피 알림')).toBeVisible();
    await expect(page.getByText('마케팅 알림')).toBeVisible();
    await expect(page.getByText('앱 설정')).toBeVisible();
    await expect(page.getByText('기본 보관방법')).toBeVisible();
    await expect(page.getByText('계정')).toBeVisible();
  });

  test('storage option buttons are clickable', async ({ page }) => {
    await page.goto('/settings');
    await page.getByRole('button', { name: '냉동' }).click();
    // Button should be active (bg-primary class)
    const freezerBtn = page.getByRole('button', { name: '냉동' });
    await expect(freezerBtn).toHaveClass(/bg-primary/);
  });

  test('back button navigates back', async ({ page }) => {
    await page.goto('/');
    await page.goto('/settings');
    await page.locator('button').filter({ has: page.locator('svg.lucide-arrow-left') }).click();
    await expect(page).toHaveURL('/');
  });
});
