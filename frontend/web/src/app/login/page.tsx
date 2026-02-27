'use client';

import { Suspense, useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { useAuth } from '@/lib/auth-context';

const ERROR_MESSAGES: Record<string, string> = {
  kakao_denied: '카카오 로그인이 취소되었습니다.',
  google_denied: 'Google 로그인이 취소되었습니다.',
  kakao_token_failed: '카카오 인증에 실패했습니다. 다시 시도해주세요.',
  google_token_failed: 'Google 인증에 실패했습니다. 다시 시도해주세요.',
  kakao_error: '카카오 로그인 중 오류가 발생했습니다.',
  google_error: 'Google 로그인 중 오류가 발생했습니다.',
  kakao_not_configured: '카카오 로그인이 아직 준비 중입니다.',
  google_not_configured: 'Google 로그인이 아직 준비 중입니다.',
  db_error: '사용자 등록 중 오류가 발생했습니다.',
  no_token: '인증 토큰을 받지 못했습니다.',
};

export default function LoginPage() {
  return (
    <Suspense>
      <LoginContent />
    </Suspense>
  );
}

function LoginContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const { login, isLoggedIn } = useAuth();
  const [loading, setLoading] = useState(false);

  const errorCode = searchParams.get('error');
  const errorMessage = errorCode ? ERROR_MESSAGES[errorCode] || '로그인 중 오류가 발생했습니다.' : null;

  if (isLoggedIn) {
    router.replace('/');
    return null;
  }

  const handleGuestLogin = async () => {
    setLoading(true);
    try {
      const success = await login('demo', 'demo-access-token');
      if (success) {
        router.push('/');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-surface flex flex-col items-center justify-center px-6">
      {/* Logo Area */}
      <div className="flex flex-col items-center mb-10">
        <div
          className="w-20 h-20 rounded-full bg-primary-100 flex items-center justify-center text-4xl mb-5"
          style={{ boxShadow: 'var(--shadow-level-3)' }}
        >
          🍽️
        </div>
        <h1 className="text-[28px] font-semibold text-on-surface">냉장고 레시피</h1>
        <p className="text-sm text-on-surface-variant mt-2 text-center leading-relaxed">
          냉장고 속 재료로 맞춤 레시피를
          <br />
          추천받으세요
        </p>
      </div>

      {/* Error Message */}
      {errorMessage && (
        <div className="w-full mb-4 px-4 py-3 bg-danger/10 rounded-xl">
          <p className="text-sm text-danger text-center">{errorMessage}</p>
        </div>
      )}

      {/* Social Login Buttons */}
      <div className="w-full space-y-3">
        <a
          href="/api/v1/auth/kakao"
          className="w-full h-12 flex items-center justify-center gap-2 rounded-xl font-medium text-sm text-[#3C1E1E] active:scale-[0.97] transition-transform"
          style={{ backgroundColor: '#FEE500', transitionDuration: 'var(--duration-fast)' }}
        >
          <svg width="18" height="18" viewBox="0 0 18 18" fill="none">
            <path
              d="M9 1C4.582 1 1 3.836 1 7.327c0 2.214 1.474 4.157 3.694 5.266l-.94 3.467a.3.3 0 00.46.327l4.015-2.65c.254.018.51.028.771.028 4.418 0 8-2.836 8-6.338S13.418 1 9 1z"
              fill="#3C1E1E"
            />
          </svg>
          카카오로 시작하기
        </a>

        <a
          href="/api/v1/auth/google"
          className="w-full h-12 flex items-center justify-center gap-2 rounded-xl border-[1.5px] border-outline bg-white font-medium text-sm text-on-surface active:scale-[0.97] transition-transform"
          style={{ transitionDuration: 'var(--duration-fast)' }}
        >
          <svg width="18" height="18" viewBox="0 0 18 18">
            <path d="M17.64 9.2c0-.637-.057-1.251-.164-1.84H9v3.481h4.844a4.14 4.14 0 01-1.796 2.716v2.259h2.908c1.702-1.567 2.684-3.875 2.684-6.615z" fill="#4285F4" />
            <path d="M9 18c2.43 0 4.467-.806 5.956-2.18l-2.908-2.259c-.806.54-1.837.86-3.048.86-2.344 0-4.328-1.584-5.036-3.711H.957v2.332A8.997 8.997 0 009 18z" fill="#34A853" />
            <path d="M3.964 10.71A5.41 5.41 0 013.682 9c0-.593.102-1.17.282-1.71V4.958H.957A8.997 8.997 0 000 9c0 1.452.348 2.827.957 4.042l3.007-2.332z" fill="#FBBC05" />
            <path d="M9 3.58c1.321 0 2.508.454 3.44 1.345l2.582-2.58C13.463.891 11.426 0 9 0A8.997 8.997 0 00.957 4.958L3.964 7.29C4.672 5.163 6.656 3.58 9 3.58z" fill="#EA4335" />
          </svg>
          Google로 시작하기
        </a>
      </div>

      {/* Divider */}
      <div className="w-full flex items-center gap-3 my-5">
        <div className="flex-1 h-px bg-outline-variant" />
        <span className="text-xs text-on-surface-variant">또는</span>
        <div className="flex-1 h-px bg-outline-variant" />
      </div>

      {/* Guest Login */}
      <button
        onClick={handleGuestLogin}
        disabled={loading}
        className="w-full h-12 flex items-center justify-center rounded-xl border-[1.5px] border-outline text-sm font-medium text-on-surface-variant active:scale-[0.97] transition-transform disabled:opacity-50"
        style={{ transitionDuration: 'var(--duration-fast)' }}
      >
        {loading ? '로그인 중...' : '게스트로 둘러보기'}
      </button>

      {/* Bottom Links */}
      <div className="absolute bottom-8 flex items-center gap-3 text-xs text-on-surface-variant">
        <button className="hover:text-on-surface transition-colors">이용약관</button>
        <span>|</span>
        <button className="hover:text-on-surface transition-colors">개인정보처리방침</button>
      </div>
    </div>
  );
}
