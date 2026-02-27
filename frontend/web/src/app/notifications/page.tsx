'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { api } from '@/lib/api';
import { useAuth } from '@/lib/auth-context';
import EmptyState from '@/components/ui/EmptyState';
import { ArrowLeft, AlertTriangle, Clock, Info } from 'lucide-react';

interface Notification {
  id: string;
  type: 'expiry_urgent' | 'expiry_warning' | 'expiry_notice';
  title: string;
  message: string;
  ingredientName: string;
  daysUntilExpiry: number;
}

const typeConfig = {
  expiry_urgent: {
    icon: AlertTriangle,
    color: 'text-danger',
    bg: 'bg-danger/10',
  },
  expiry_warning: {
    icon: Clock,
    color: 'text-accent',
    bg: 'bg-accent/10',
  },
  expiry_notice: {
    icon: Info,
    color: 'text-primary',
    bg: 'bg-primary-50',
  },
};

export default function NotificationsPage() {
  const router = useRouter();
  const { isLoggedIn, isLoading: authLoading } = useAuth();
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (authLoading) return;
    if (!isLoggedIn) {
      setLoading(false);
      return;
    }

    const fetchNotifications = async () => {
      try {
        const res = await api.getNotifications();
        if (res?.data) setNotifications(res.data);
      } catch {
        // silent
      } finally {
        setLoading(false);
      }
    };
    fetchNotifications();
  }, [isLoggedIn, authLoading]);

  if (!authLoading && !isLoggedIn) {
    return (
      <div className="min-h-screen bg-surface-variant">
        <header className="bg-surface px-5 pt-12 pb-4 flex items-center gap-3" style={{ boxShadow: 'var(--shadow-level-1)' }}>
          <button onClick={() => router.back()} className="p-1 text-on-surface-variant">
            <ArrowLeft size={22} />
          </button>
          <h1 className="text-[22px] font-semibold text-on-surface">알림</h1>
        </header>
        <div className="px-4 py-8">
          <EmptyState
            icon={<span className="text-6xl">🔒</span>}
            title="로그인이 필요해요"
            description="알림을 확인하려면 로그인해주세요"
            ctaLabel="로그인하기"
            onCtaClick={() => router.push('/login')}
          />
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-surface-variant">
      <header className="bg-surface px-5 pt-12 pb-4 flex items-center gap-3" style={{ boxShadow: 'var(--shadow-level-1)' }}>
        <button onClick={() => router.back()} className="p-1 text-on-surface-variant">
          <ArrowLeft size={22} />
        </button>
        <h1 className="text-[22px] font-semibold text-on-surface">알림</h1>
        {notifications.length > 0 && (
          <span className="ml-auto text-xs text-on-surface-variant">
            {notifications.length}개
          </span>
        )}
      </header>

      <div className="px-4 py-4 pb-24 space-y-2">
        {loading ? (
          <div className="space-y-2">
            {[1, 2, 3].map((i) => (
              <div key={i} className="bg-surface rounded-2xl p-4 animate-pulse h-20" />
            ))}
          </div>
        ) : notifications.length === 0 ? (
          <EmptyState
            icon={<span className="text-5xl">🔔</span>}
            title="알림이 없어요"
            description="소비기한 임박 식재료가 있으면 여기서 알려드려요"
          />
        ) : (
          notifications.map((notif) => {
            const config = typeConfig[notif.type];
            const Icon = config.icon;
            return (
              <button
                key={notif.id}
                onClick={() => router.push('/fridge')}
                className="w-full bg-surface rounded-2xl p-4 flex items-start gap-3 text-left active:scale-[0.98] transition-transform"
                style={{ boxShadow: 'var(--shadow-level-1)' }}
              >
                <div className={`w-10 h-10 rounded-xl ${config.bg} flex items-center justify-center flex-shrink-0`}>
                  <Icon size={20} className={config.color} />
                </div>
                <div className="flex-1 min-w-0">
                  <p className={`text-sm font-semibold ${config.color}`}>{notif.title}</p>
                  <p className="text-sm text-on-surface mt-0.5">{notif.message}</p>
                </div>
                {notif.daysUntilExpiry <= 2 && (
                  <span className="flex-shrink-0 text-xs font-bold text-danger bg-danger/10 px-2 py-1 rounded-lg">
                    {notif.daysUntilExpiry <= 0 ? '만료' : `D-${notif.daysUntilExpiry}`}
                  </span>
                )}
              </button>
            );
          })
        )}
      </div>
    </div>
  );
}
