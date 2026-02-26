import type { Metadata } from "next";
import "./globals.css";
import BottomNav from "@/components/layout/BottomNav";

export const metadata: Metadata = {
  title: "냉장고 레시피",
  description: "냉장고 속 재료로 만드는 맞춤 레시피",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="ko">
      <body className="bg-surface-variant min-h-screen">
        <div className="mx-auto max-w-md min-h-screen bg-surface relative pb-20">
          {children}
          <BottomNav />
        </div>
      </body>
    </html>
  );
}
