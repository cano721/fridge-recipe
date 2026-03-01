'use client';

import { useState, useRef } from 'react';
import { useRouter } from 'next/navigation';
import { ArrowLeft, User, Camera, Loader2 } from 'lucide-react';
import { api } from '@/lib/api';
import { useAuth } from '@/lib/auth-context';
import { uploadImage } from '@/lib/cloudinary';

export default function ProfileEditPage() {
  const router = useRouter();
  const { user, refreshUser } = useAuth();
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [nickname, setNickname] = useState(user?.nickname || '');
  const [profileImage, setProfileImage] = useState<string | null>(user?.profileImage || null);
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [saving, setSaving] = useState(false);
  const [saved, setSaved] = useState(false);
  const [uploading, setUploading] = useState(false);

  const handleImageSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    setImageFile(file);
    const reader = new FileReader();
    reader.onload = () => setProfileImage(reader.result as string);
    reader.readAsDataURL(file);
    e.target.value = '';
  };

  async function handleSave(e: React.FormEvent) {
    e.preventDefault();
    if (!nickname.trim() || saving) return;
    setSaving(true);

    try {
      let imageUrl = user?.profileImage || undefined;

      if (imageFile) {
        setUploading(true);
        const result = await uploadImage(imageFile, 'profile');
        imageUrl = result.url;
        setUploading(false);
      }

      await api.updateProfile({
        nickname: nickname.trim(),
        ...(imageUrl !== user?.profileImage && { profileImage: imageUrl }),
      });
      setSaved(true);
      await refreshUser();
      setTimeout(() => router.back(), 1000);
    } catch {
      setUploading(false);
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

      <input
        ref={fileInputRef}
        type="file"
        accept="image/*"
        className="hidden"
        onChange={handleImageSelect}
      />

      <div className="px-4 py-6 space-y-6">
        <div className="flex flex-col items-center gap-3">
          <button
            type="button"
            onClick={() => fileInputRef.current?.click()}
            className="relative group"
          >
            <div className="w-24 h-24 rounded-full bg-primary-100 flex items-center justify-center overflow-hidden">
              {profileImage ? (
                <img src={profileImage} alt="프로필" className="w-full h-full object-cover" />
              ) : (
                <User className="w-12 h-12 text-primary" />
              )}
            </div>
            <div className="absolute bottom-0 right-0 w-8 h-8 rounded-full bg-primary flex items-center justify-center shadow-md">
              <Camera size={14} className="text-white" />
            </div>
          </button>
          <p className="text-xs text-on-surface-variant">사진을 탭하여 변경</p>
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
            className="w-full h-12 bg-primary text-white rounded-xl font-medium text-sm disabled:opacity-40 active:bg-primary-600 active:scale-[0.97] transition-all flex items-center justify-center gap-2"
            style={{ transitionDuration: 'var(--duration-fast)' }}
          >
            {saved ? '저장 완료!' : uploading ? (
              <><Loader2 size={16} className="animate-spin" />이미지 업로드 중...</>
            ) : saving ? '저장 중...' : '저장'}
          </button>
        </form>
      </div>
    </div>
  );
}
