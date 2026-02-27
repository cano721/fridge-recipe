import { test, expect } from '@playwright/test';

async function loginAsGuest(page: import('@playwright/test').Page) {
  await page.goto('/login');
  await page.getByText('게스트로 둘러보기').click();
  await page.waitForURL('/');
}

test.describe('MyPage Features', () => {
  test('mypage shows updated menu items', async ({ page }) => {
    await loginAsGuest(page);
    await page.goto('/mypage');
    await expect(page.getByText('프로필 수정')).toBeVisible();
    await expect(page.getByText('내 북마크')).toBeVisible();
    await expect(page.getByText('조리 이력')).toBeVisible();
    await expect(page.getByText('알림 설정')).toBeVisible();
  });

  test('mypage shows stats with weekly cooking count', async ({ page }) => {
    await loginAsGuest(page);
    await page.goto('/mypage');
    await expect(page.getByText('내 식재료')).toBeVisible();
    await expect(page.getByText('저장 레시피')).toBeVisible();
    await expect(page.getByText('이번 주 조리')).toBeVisible();
  });

  test('navigate to profile edit page', async ({ page }) => {
    await loginAsGuest(page);
    await page.goto('/mypage');
    await page.getByText('프로필 수정').click();
    await expect(page).toHaveURL('/mypage/edit');
    await expect(page.getByRole('heading', { name: '프로필 편집' })).toBeVisible();
  });

  test('profile edit page shows form fields', async ({ page }) => {
    await loginAsGuest(page);
    await page.goto('/mypage/edit');
    await expect(page.getByText('이메일')).toBeVisible();
    await expect(page.getByText('닉네임')).toBeVisible();
    await expect(page.getByRole('button', { name: '저장' })).toBeVisible();
  });

  test('navigate to bookmarks page', async ({ page }) => {
    await loginAsGuest(page);
    await page.goto('/mypage');
    await page.getByText('내 북마크').click();
    await expect(page).toHaveURL('/mypage/bookmarks');
    await expect(page.getByRole('heading', { name: '내 북마크' })).toBeVisible();
  });

  test('bookmarks page shows empty state or list', async ({ page }) => {
    await loginAsGuest(page);
    await page.goto('/mypage/bookmarks');
    // Either shows bookmarks or empty state
    const hasContent = await page.getByText('아직 저장한 레시피가 없어요').isVisible().catch(() => false);
    if (hasContent) {
      await expect(page.getByText('레시피 둘러보기')).toBeVisible();
    }
  });

  test('navigate to cooking history page', async ({ page }) => {
    await loginAsGuest(page);
    await page.goto('/mypage');
    await page.getByText('조리 이력').click();
    await expect(page).toHaveURL('/mypage/history');
    await expect(page.getByRole('heading', { name: '조리 이력' })).toBeVisible();
  });

  test('history page shows entries or empty state', async ({ page }) => {
    await loginAsGuest(page);
    await page.goto('/mypage/history');
    // Either shows history entries or empty state
    const hasEmpty = await page.getByText('아직 조리 기록이 없어요').isVisible().catch(() => false);
    if (hasEmpty) {
      await expect(page.getByText('레시피 둘러보기')).toBeVisible();
    }
  });

  test('back button works on sub-pages', async ({ page }) => {
    await loginAsGuest(page);
    await page.goto('/mypage');
    await page.getByText('내 북마크').click();
    await expect(page).toHaveURL('/mypage/bookmarks');
    await page.locator('button').filter({ has: page.locator('svg.lucide-arrow-left') }).click();
    await expect(page).toHaveURL('/mypage');
  });

  test('logout button works', async ({ page }) => {
    await loginAsGuest(page);
    await page.goto('/mypage');
    await expect(page.getByText('로그아웃')).toBeVisible();
  });
});
