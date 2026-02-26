interface LoadingSpinnerProps {
  fullHeight?: boolean;
}

export default function LoadingSpinner({ fullHeight = false }: LoadingSpinnerProps) {
  return (
    <div className={`flex items-center justify-center ${fullHeight ? "min-h-screen" : "py-12"}`}>
      <div className="w-8 h-8 border-4 border-outline border-t-primary rounded-full animate-spin" />
    </div>
  );
}

interface SkeletonProps {
  className?: string;
}

export function Skeleton({ className = '' }: SkeletonProps) {
  return <div className={`skeleton-shimmer rounded-2xl ${className}`} />;
}

export function SkeletonCard() {
  return (
    <div className="bg-surface rounded-[20px] overflow-hidden" style={{ boxShadow: 'var(--shadow-level-1)' }}>
      <div className="skeleton-shimmer h-36" />
      <div className="p-3 space-y-2">
        <div className="skeleton-shimmer h-4 w-3/4 rounded" />
        <div className="skeleton-shimmer h-3 w-1/2 rounded" />
        <div className="skeleton-shimmer h-2 w-full rounded-full" />
      </div>
    </div>
  );
}

export function SkeletonListItem() {
  return (
    <div className="bg-surface rounded-2xl px-4 py-3 flex items-center gap-3" style={{ boxShadow: 'var(--shadow-level-1)' }}>
      <div className="skeleton-shimmer w-12 h-12 rounded-xl flex-shrink-0" />
      <div className="flex-1 space-y-2">
        <div className="skeleton-shimmer h-4 w-2/3 rounded" />
        <div className="skeleton-shimmer h-3 w-1/3 rounded" />
      </div>
      <div className="skeleton-shimmer h-6 w-16 rounded-[6px]" />
    </div>
  );
}
