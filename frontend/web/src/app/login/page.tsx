'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/lib/auth-context';

export default function LoginPage() {
  const router = useRouter();
  const { login, isLoggedIn } = useAuth();
  const [loading, setLoading] = useState(false);

  // Already logged in, redirect to home
  if (isLoggedIn) {
    router.replace('/');
    return null;
  }

  const handleSocialLogin = (provider: string) => {
    alert(`${provider} 로그인은 준비 중입니다`);
  };

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
        <div className="w-20 h-20 rounded-full bg-primary-100 flex items-center justify-center text-4xl mb-5"
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

      {/* Social Login Buttons */}
      <div className="w-full space-y-3">
        <button
          onClick={() => handleSocialLogin('카카오')}
          className="w-full h-12 flex items-center justify-center gap-2 rounded-xl font-medium text-sm text-[#3C1E1E] active:scale-[0.97] transition-transform"
          style={{ backgroundColor: '#FEE500', transitionDuration: 'var(--duration-fast)' }}
        >
          <span className="text-lg">💬</span>
          카카오로 시작하기
        </button>

        <button
          onClick={() => handleSocialLogin('Google')}
          className="w-full h-12 flex items-center justify-center gap-2 rounded-xl border-[1.5px] border-outline bg-white font-medium text-sm text-on-surface active:scale-[0.97] transition-transform"
          style={{ transitionDuration: 'var(--duration-fast)' }}
        >
          <span className="text-lg">G</span>
          Google로 시작하기
        </button>

        <button
          onClick={() => handleSocialLogin('Apple')}
          className="w-full h-12 flex items-center justify-center gap-2 rounded-xl bg-black font-medium text-sm text-white active:scale-[0.97] transition-transform"
          style={{ transitionDuration: 'var(--duration-fast)' }}
        >
          <span className="text-lg"></span>
          Apple로 시작하기
        </button>
      </div>

      {/* Guest Login */}
      <button
        onClick={handleGuestLogin}
        disabled={loading}
        className="mt-6 h-10 px-3 text-sm text-primary-600 font-medium active:bg-primary-100 rounded-xl transition-colors disabled:opacity-50"
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
