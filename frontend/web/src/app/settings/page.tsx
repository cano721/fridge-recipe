'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { ArrowLeft } from 'lucide-react';
import { api } from '@/lib/api';

interface NotificationSettings {
  expiryAlert: boolean;
  recipeRecommendation: boolean;
  marketing: boolean;
}

type StorageType = 'fridge' | 'freezer' | 'room';

const storageOptions: { value: StorageType; label: string }[] = [
  { value: 'fridge', label: '냉장' },
  { value: 'freezer', label: '냉동' },
  { value: 'room', label: '실온' },
];

function Toggle({
  checked,
  onChange,
}: {
  checked: boolean;
  onChange: (v: boolean) => void;
}) {
  return (
    <button
      onClick={() => onChange(!checked)}
      className={`relative w-12 h-6 rounded-full transition-colors duration-200 flex-shrink-0 ${
        checked ? 'bg-primary' : 'bg-gray-300'
      }`}
    >
      <span
        className={`absolute top-1 w-4 h-4 bg-white rounded-full shadow transition-transform duration-200 ${
          checked ? 'translate-x-7' : 'translate-x-1'
        }`}
      />
    </button>
  );
}

export default function SettingsPage() {
  const router = useRouter();
  const [notifications, setNotifications] = useState<NotificationSettings>({
    expiryAlert: true,
    recipeRecommendation: true,
    marketing: false,
  });
  const [defaultStorage, setDefaultStorage] = useState<StorageType>('fridge');

  const updateNotification = (key: keyof NotificationSettings) => (value: boolean) => {
    setNotifications((prev) => ({ ...prev, [key]: value }));
  };

  const handleLogout = () => {
    api.clearToken();
    router.push('/login');
  };

  const handleWithdraw = () => {
    if (confirm('정말 탈퇴하시겠습니까? 모든 데이터가 삭제됩니다.')) {
      api.clearToken();
      router.push('/login');
    }
  };

  return (
    <div className="min-h-screen bg-surface-variant">
      {/* Header */}
      <header className="bg-surface px-4 pt-12 pb-4 flex items-center gap-3 shadow-sm">
        <button
          onClick={() => router.back()}
          className="p-2 rounded-xl hover:bg-surface-variant transition-colors"
        >
          <ArrowLeft className="w-5 h-5 text-on-surface" />
        </button>
        <h1 className="text-lg font-bold text-on-surface">설정</h1>
      </header>

      <div className="px-4 py-4 space-y-4">
        {/* 알림 설정 */}
        <section className="bg-surface rounded-2xl shadow-sm overflow-hidden">
          <div className="px-5 py-3 border-b border-outline">
            <h2 className="text-sm font-bold text-on-surface-variant uppercase tracking-wide">
              알림 설정
            </h2>
          </div>

          <div className="divide-y divide-outline">
            <div className="flex items-center justify-between px-5 py-4">
              <div className="flex-1 mr-4">
                <p className="text-sm font-medium text-on-surface">소비기한 알림</p>
                <p className="text-xs text-on-surface-variant mt-0.5">
                  소비기한이 임박한 식재료를 알려드려요
                </p>
              </div>
              <Toggle
                checked={notifications.expiryAlert}
                onChange={updateNotification('expiryAlert')}
              />
            </div>

            <div className="flex items-center justify-between px-5 py-4">
              <div className="flex-1 mr-4">
                <p className="text-sm font-medium text-on-surface">추천 레시피 알림</p>
                <p className="text-xs text-on-surface-variant mt-0.5">
                  냉장고 재료로 만들 수 있는 레시피를 알려드려요
                </p>
              </div>
              <Toggle
                checked={notifications.recipeRecommendation}
                onChange={updateNotification('recipeRecommendation')}
              />
            </div>

            <div className="flex items-center justify-between px-5 py-4">
              <div className="flex-1 mr-4">
                <p className="text-sm font-medium text-on-surface">마케팅 알림</p>
                <p className="text-xs text-on-surface-variant mt-0.5">
                  이벤트 및 프로모션 정보를 받아보세요
                </p>
              </div>
              <Toggle
                checked={notifications.marketing}
                onChange={updateNotification('marketing')}
              />
            </div>
          </div>
        </section>

        {/* 앱 설정 */}
        <section className="bg-surface rounded-2xl shadow-sm overflow-hidden">
          <div className="px-5 py-3 border-b border-outline">
            <h2 className="text-sm font-bold text-on-surface-variant uppercase tracking-wide">
              앱 설정
            </h2>
          </div>

          <div className="px-5 py-4">
            <p className="text-sm font-medium text-on-surface mb-3">기본 보관방법</p>
            <div className="flex gap-2">
              {storageOptions.map((opt) => (
                <button
                  key={opt.value}
                  onClick={() => setDefaultStorage(opt.value)}
                  className={`flex-1 py-2 rounded-full text-sm font-medium transition-colors ${
                    defaultStorage === opt.value
                      ? 'bg-primary text-white'
                      : 'bg-surface-variant text-on-surface-variant'
                  }`}
                >
                  {opt.label}
                </button>
              ))}
            </div>
          </div>
        </section>

        {/* 계정 */}
        <section className="bg-surface rounded-2xl shadow-sm overflow-hidden">
          <div className="px-5 py-3 border-b border-outline">
            <h2 className="text-sm font-bold text-on-surface-variant uppercase tracking-wide">
              계정
            </h2>
          </div>

          <div className="divide-y divide-outline">
            <button
              onClick={handleLogout}
              className="w-full px-5 py-4 text-left text-sm font-medium text-on-surface active:bg-surface-variant transition-colors"
            >
              로그아웃
            </button>
            <button
              onClick={handleWithdraw}
              className="w-full px-5 py-4 text-left text-xs text-danger active:bg-surface-variant transition-colors"
            >
              회원탈퇴
            </button>
          </div>
        </section>
      </div>
    </div>
  );
}
