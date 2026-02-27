'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { ArrowLeft, User } from 'lucide-react';
import { api } from '@/lib/api';
import { useAuth } from '@/lib/auth-context';

export default function ProfileEditPage() {
  const router = useRouter();
  const { user } = useAuth();
  const [nickname, setNickname] = useState(user?.nickname || '');
  const [saving, setSaving] = useState(false);
  const [saved, setSaved] = useState(false);

  async function handleSave(e: React.FormEvent) {
    e.preventDefault();
    if (!nickname.trim() || saving) return;
    setSaving(true);
    try {
      await api.updateProfile({ nickname: nickname.trim() });
      setSaved(true);
      setTimeout(() => router.back(), 1000);
    } catch {
      // silent
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="min-h-screen bg-surface-variant">
      <header
        className="bg-surface px-4 pt-12 pb-4 flex items-center gap-3"
        style={{ boxShadow: 'var(--shadow-level-1)' }}
      >
        <button onClick={() => router.back()} className="p-2 -ml-2">
          <ArrowLeft size={24} className="text-on-surface" />
        </button>
        <h1 className="text-lg font-semibold text-on-surface">프로필 편집</h1>
      </header>

      <div className="px-4 py-6 space-y-6">
        <div className="flex flex-col items-center gap-3">
          <div className="w-24 h-24 rounded-full bg-primary-100 flex items-center justify-center">
            <User className="w-12 h-12 text-primary" />
          </div>
        </div>

        <form onSubmit={handleSave} className="space-y-4">
          <div>
            <label className="text-xs text-on-surface-variant mb-1 block">이메일</label>
            <input
              type="text"
              value={user?.email || ''}
              disabled
              className="w-full h-14 px-4 border-[1.5px] border-outline rounded-xl text-sm bg-surface-variant text-on-surface-variant"
            />
          </div>

          <div>
            <label className="text-xs text-on-surface-variant mb-1 block">닉네임</label>
            <input
              type="text"
              value={nickname}
              onChange={(e) => setNickname(e.target.value)}
              placeholder="닉네임을 입력하세요"
              className="w-full h-14 px-4 border-[1.5px] border-outline rounded-xl text-sm focus:outline-none focus:border-primary focus:border-2 bg-surface text-on-surface"
            />
          </div>

          <button
            type="submit"
            disabled={saving || !nickname.trim()}
            className="w-full h-12 bg-primary text-white rounded-xl font-medium text-sm disabled:opacity-40 active:bg-primary-600 active:scale-[0.97] transition-all"
            style={{ transitionDuration: 'var(--duration-fast)' }}
          >
            {saved ? '저장 완료!' : saving ? '저장 중...' : '저장'}
          </button>
        </form>
      </div>
    </div>
  );
}
