"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { Home, Box, UtensilsCrossed, User } from "lucide-react";

const tabs = [
  { href: "/", icon: Home, label: "홈" },
  { href: "/fridge", icon: Box, label: "냉장고" },
  { href: "/recipe", icon: UtensilsCrossed, label: "레시피" },
  { href: "/mypage", icon: User, label: "마이" },
];

export default function BottomNav() {
  const pathname = usePathname();

  return (
    <nav
      className="fixed bottom-0 left-1/2 -translate-x-1/2 w-full max-w-md bg-surface z-50"
      style={{ boxShadow: '0px -2px 6px rgba(0,0,0,0.10)', height: '80px' }}
    >
      <div className="flex h-full">
        {tabs.map(({ href, icon: Icon, label }) => {
          const isActive = pathname === href;
          return (
            <Link
              key={href}
              href={href}
              className="flex flex-col items-center justify-center flex-1 gap-1 relative"
            >
              {/* Selection indicator pill */}
              <div
                className={`flex items-center justify-center rounded-full transition-colors ${
                  isActive ? 'bg-primary-100' : 'bg-transparent'
                }`}
                style={{ width: '64px', height: '32px' }}
              >
                <Icon
                  size={24}
                  className={isActive ? "text-primary" : "text-on-surface-variant"}
                  fill={isActive ? "currentColor" : "none"}
                />
              </div>
              <span
                className={`text-xs font-medium ${
                  isActive ? "text-primary" : "text-on-surface-variant"
                }`}
              >
                {label}
              </span>
            </Link>
          );
        })}
      </div>
    </nav>
  );
}
