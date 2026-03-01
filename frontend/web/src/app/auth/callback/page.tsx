'use client';

import { Suspense, useEffect } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { useAuth } from '@/lib/auth-context';
import { api } from '@/lib/api';

export default function AuthCallbackPage() {
  return (
    <Suspense>
      <CallbackContent />
    </Suspense>
  );
}

function CallbackContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const { refreshUser } = useAuth();

  useEffect(() => {
    const code = searchParams.get('code');
    if (!code) {
      router.replace('/login?error=no_code');
      return;
    }

    fetch('/api/v1/auth/exchange', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ code }),
    })
      .then((res) => res.json())
      .then((data) => {
        if (data.success && data.data) {
          api.setToken(data.data.accessToken);
          if (data.data.refreshToken) {
            localStorage.setItem('refreshToken', data.data.refreshToken);
          }
          refreshUser().then(() => {
            router.replace('/');
          });
        } else {
          router.replace('/login?error=exchange_failed');
        }
      })
      .catch(() => {
        router.replace('/login?error=exchange_failed');
      });
  }, [searchParams, router, refreshUser]);

  return (
    <div className="min-h-screen bg-surface flex items-center justify-center">
      <div className="text-center">
        <div className="w-16 h-16 rounded-full bg-primary-50 flex items-center justify-center mx-auto mb-4">
          <span className="text-3xl">🔐</span>
        </div>
        <p className="text-base font-medium text-on-surface">로그인 처리 중...</p>
      </div>
    </div>
  );
}
