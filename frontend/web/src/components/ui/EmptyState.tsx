interface EmptyStateProps {
  icon: React.ReactNode;
  title: string;
  description?: string;
  ctaLabel?: string;
  onCtaClick?: () => void;
}

export default function EmptyState({ icon, title, description, ctaLabel, onCtaClick }: EmptyStateProps) {
  return (
    <div className="flex flex-col items-center justify-center py-12 text-center px-4">
      <div className="w-40 h-40 flex items-center justify-center text-on-surface-variant mb-4">
        {icon}
      </div>
      <p className="text-on-surface font-semibold text-[22px] leading-7">{title}</p>
      {description && (
        <p className="text-on-surface-variant text-sm mt-2 leading-5">{description}</p>
      )}
      {ctaLabel && onCtaClick && (
        <button
          onClick={onCtaClick}
          className="mt-5 h-12 px-6 bg-primary text-white rounded-xl text-sm font-medium active:bg-primary-600 active:scale-[0.97] transition-all"
          style={{ transitionDuration: 'var(--duration-fast)' }}
        >
          {ctaLabel}
        </button>
      )}
    </div>
  );
}
