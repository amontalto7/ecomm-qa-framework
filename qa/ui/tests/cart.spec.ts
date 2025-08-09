import { test, expect } from '@playwright/test';

test.describe('Cart & Checkout', () => {
  test('happy path checkout', async ({ page }, testInfo) => {
    testInfo.annotations.push({ type: 'feature', description: 'Checkout' });
    await page.goto('/');
    await page.getByRole('button', { name: 'Load Products' }).click();
    await page.locator('.card').first().getByRole('button', { name: 'Add' }).click();
    await page.getByRole('button', { name: 'Checkout' }).click();
    await expect(page.locator('#status')).toContainText('Order CONFIRMED');
  });

  // Negative UI: checkout with empty cart
  test('checkout shows error when cart is empty', async ({ page }) => {
    await page.goto('/');
    await page.getByRole('button', { name: 'Clear Cart' }).click();
    await page.getByRole('button', { name: 'Checkout' }).click();
    await expect(page.locator('#status')).toContainText('Cart is empty');
  });
});
