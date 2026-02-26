import { ExpiryStatus } from '@/types';

export function getExpiryStatus(daysUntilExpiry: number | null): ExpiryStatus {
  if (daysUntilExpiry === null) return 'safe';
  if (daysUntilExpiry < 0) return 'expired';
  if (daysUntilExpiry <= 1) return 'urgent';
  if (daysUntilExpiry <= 3) return 'soon';
  return 'safe';
}

export function getExpiryLabel(daysUntilExpiry: number | null): string {
  if (daysUntilExpiry === null) return '';
  if (daysUntilExpiry < 0) return `${Math.abs(daysUntilExpiry)}일 지남`;
  if (daysUntilExpiry === 0) return '오늘 만료';
  if (daysUntilExpiry === 1) return '내일 만료';
  return `${daysUntilExpiry}일 남음`;
}

export function getExpiryIcon(status: ExpiryStatus): string {
  switch (status) {
    case 'safe': return '✓';
    case 'soon': return '⏳';
    case 'urgent': return '⚠';
    case 'expired': return '✕';
  }
}

export function getExpiryColor(status: ExpiryStatus): string {
  switch (status) {
    case 'expired': return 'text-expired bg-expired/15';
    case 'urgent': return 'text-urgent bg-urgent/15';
    case 'soon': return 'text-soon bg-soon/15';
    case 'safe': return 'text-safe bg-safe/15';
  }
}

export function getStorageLabel(type: string): string {
  switch (type) {
    case 'fridge': return '냉장';
    case 'freezer': return '냉동';
    case 'room': return '실온';
    default: return type;
  }
}

export function getStorageEmoji(type: string): string {
  switch (type) {
    case 'fridge': return '❄️';
    case 'freezer': return '🧊';
    case 'room': return '🏠';
    default: return '📦';
  }
}

export function getCategoryEmoji(category: string): string {
  const map: Record<string, string> = {
    '채소': '🥬', '과일': '🍎', '육류': '🥩', '해산물': '🐟',
    '유제품': '🥛', '양념': '🧂', '곡류': '🌾', '가공식품': '🥫',
    '음료': '🥤', '기타': '📦',
  };
  return map[category] || '🍽️';
}

export function getDifficultyLabel(difficulty: string | null): string {
  switch (difficulty) {
    case 'easy': return '쉬움';
    case 'medium': return '보통';
    case 'hard': return '어려움';
    default: return '보통';
  }
}

export function formatCookingTime(minutes: number | null): string {
  if (!minutes) return '';
  if (minutes < 60) return `${minutes}분`;
  const h = Math.floor(minutes / 60);
  const m = minutes % 60;
  return m > 0 ? `${h}시간 ${m}분` : `${h}시간`;
}
