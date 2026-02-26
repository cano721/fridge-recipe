import { getExpiryStatus, getExpiryLabel, getExpiryColor, getExpiryIcon } from "@/lib/utils";

interface ExpiryBadgeProps {
  daysUntilExpiry: number | null;
}

export default function ExpiryBadge({ daysUntilExpiry }: ExpiryBadgeProps) {
  const status = getExpiryStatus(daysUntilExpiry);
  const label = getExpiryLabel(daysUntilExpiry);
  const colorClass = getExpiryColor(status);
  const icon = getExpiryIcon(status);

  if (!label) return null;

  return (
    <span
      className={`inline-flex items-center gap-1 px-2 h-6 rounded-[6px] text-[11px] font-medium ${colorClass} ${
        status === 'expired' ? 'line-through' : ''
      } ${status === 'urgent' ? 'font-bold' : ''}`}
    >
      <span className="text-[10px]">{icon}</span>
      {label}
    </span>
  );
}
