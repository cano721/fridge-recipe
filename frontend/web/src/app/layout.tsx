import type { Metadata } from "next";
import "./globals.css";
import BottomNav from "@/components/layout/BottomNav";
import { AuthProvider } from "@/lib/auth-context";

export const metadata: Metadata = {
  title: "냉장고 레시피",
  description: "냉장고 속 재료로 만드는 맞춤 레시피",
  manifest: "/manifest.json",
  themeColor: "#2E7D32",
  appleWebApp: {
    capable: true,
    statusBarStyle: "default",
    title: "냉장고 레시피",
  },
  viewport: {
    width: "device-width",
    initialScale: 1,
    maximumScale: 1,
    userScalable: false,
  },
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="ko">
      <head>
        <link rel="apple-touch-icon" href="/icons/icon-192.svg" />
      </head>
      <body className="bg-surface-variant min-h-screen">
        <script
          dangerouslySetInnerHTML={{
            __html: `
              if ('serviceWorker' in navigator) {
                window.addEventListener('load', () => {
                  navigator.serviceWorker.register('/sw.js');
                });
              }
            `,
          }}
        />
        <AuthProvider>
          <div className="mx-auto max-w-md min-h-screen bg-surface relative pb-20">
            {children}
            <BottomNav />
          </div>
        </AuthProvider>
      </body>
    </html>
  );
}
