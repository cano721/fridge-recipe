import { test, expect } from '@playwright/test';

async function loginAsGuest(page: import('@playwright/test').Page) {
  await page.goto('/login');
  await page.getByText('게스트로 둘러보기').click();
  await page.waitForURL('/');
}

test.describe('Recipe Filters', () => {
  test('recipe page shows filter chip rows', async ({ page }) => {
    await page.goto('/recipe');
    // Cuisine filters
    await expect(page.getByRole('button', { name: '한식' })).toBeVisible();
    await expect(page.getByRole('button', { name: '양식' })).toBeVisible();
    // Difficulty filters
    await expect(page.getByRole('button', { name: '쉬움' })).toBeVisible();
    await expect(page.getByRole('button', { name: '보통' })).toBeVisible();
    await expect(page.getByRole('button', { name: '어려움' })).toBeVisible();
    // Time filters
    await expect(page.getByRole('button', { name: '15분 이내' })).toBeVisible();
    await expect(page.getByRole('button', { name: '30분 이내' })).toBeVisible();
  });

  test('sort options are visible', async ({ page }) => {
    await page.goto('/recipe');
    await expect(page.getByRole('button', { name: '추천순' })).toBeVisible();
    await expect(page.getByRole('button', { name: '평점순' })).toBeVisible();
    await expect(page.getByRole('button', { name: '조리시간순' })).toBeVisible();
    await expect(page.getByRole('button', { name: '최신순' })).toBeVisible();
  });

  test('difficulty filter chip toggles active state', async ({ page }) => {
    await page.goto('/recipe');
    const btn = page.getByRole('button', { name: '쉬움' });
    await btn.click();
    await expect(btn).toContainText('✓');
  });

  test('time filter chip toggles active state', async ({ page }) => {
    await page.goto('/recipe');
    const btn = page.getByRole('button', { name: '30분 이내' });
    await btn.click();
    await expect(btn).toContainText('✓');
  });

  test('search with filters returns results', async ({ page }) => {
    await page.goto('/recipe');
    await page.getByPlaceholder('레시피나 재료를 검색해보세요').fill('김치');
    await page.getByPlaceholder('레시피나 재료를 검색해보세요').press('Enter');
    // Should switch to search results tab
    await expect(page.getByText('검색결과')).toBeVisible();
  });

  test('my ingredients chips shown after login', async ({ page }) => {
    await loginAsGuest(page);
    await page.goto('/recipe');
    // Should show "내 재료" label with ingredient chips
    await expect(page.getByText('내 재료')).toBeVisible();
  });
});
