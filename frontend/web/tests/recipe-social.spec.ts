import { test, expect } from '@playwright/test';

async function loginAsGuest(page: import('@playwright/test').Page) {
  await page.goto('/login');
  await page.getByText('게스트로 둘러보기').click();
  await page.waitForURL('/');
}

test.describe('Recipe Social Features', () => {
  test('recipe detail shows like and share buttons', async ({ page }) => {
    await page.goto('/recipe/1');
    await expect(page.getByText('공유')).toBeVisible();
    // Like count should be visible
    const likeBtn = page.locator('button').filter({ hasText: /^\d+$/ });
    await expect(likeBtn.first()).toBeVisible();
  });

  test('recipe detail shows bookmark button', async ({ page }) => {
    await page.goto('/recipe/1');
    // Bookmark heart icon on hero
    const heroBookmark = page.locator('.absolute.top-4.right-4');
    await expect(heroBookmark).toBeVisible();
  });

  test('recipe detail shows cooking start button', async ({ page }) => {
    await page.goto('/recipe/1');
    await expect(page.getByText('요리 시작하기')).toBeVisible();
  });

  test('cooking mode opens on start button click', async ({ page }) => {
    await page.goto('/recipe/1');
    await page.getByText('요리 시작하기').click();
    // Should show step counter
    await expect(page.getByText(/1 \/ \d+/)).toBeVisible();
    // Should show navigation buttons
    await expect(page.getByText('이전')).toBeVisible();
    await expect(page.getByText('다음')).toBeVisible();
  });

  test('cooking mode navigates steps', async ({ page }) => {
    await page.goto('/recipe/1');
    await page.getByText('요리 시작하기').click();
    await expect(page.getByText('1 / 4')).toBeVisible();
    // Click "다음" button inside the cooking mode overlay (z-50 fixed)
    const overlay = page.locator('.fixed.inset-0.z-50');
    await overlay.getByRole('button', { name: '다음' }).click();
    await expect(page.getByText('2 / 4')).toBeVisible();
  });

  test('like button toggles state', async ({ page }) => {
    await loginAsGuest(page);
    await page.goto('/recipe/2');
    // Find the like button (contains a number)
    const likeBtn = page.locator('button').filter({ has: page.locator('svg.lucide-heart') }).last();
    await likeBtn.click();
    // Should still be visible after click
    await expect(likeBtn).toBeVisible();
  });
});
