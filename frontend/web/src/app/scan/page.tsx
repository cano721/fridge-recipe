"use client";

import { useState, useRef } from "react";

interface ScanOption {
  emoji: string;
  title: string;
  description: string;
  accept: string;
}

const SCAN_OPTIONS: ScanOption[] = [
  {
    emoji: "🧾",
    title: "영수증 스캔",
    description: "영수증을 촬영하여 식재료를 자동으로 등록합니다",
    accept: "image/*",
  },
  {
    emoji: "📷",
    title: "사진 인식",
    description: "식재료 사진을 촬영하여 자동으로 인식합니다",
    accept: "image/*",
  },
];

export default function ScanPage() {
  const [showReady, setShowReady] = useState(false);
  const receiptInputRef = useRef<HTMLInputElement>(null);
  const photoInputRef = useRef<HTMLInputElement>(null);

  const inputRefs = [receiptInputRef, photoInputRef];

  function handleCardClick(index: number) {
    // TODO: Implement actual upload logic once backend OCR API keys are configured
    setShowReady(true);
    setTimeout(() => setShowReady(false), 2500);
    // inputRefs[index].current?.click();
  }

  return (
    <div className="min-h-screen bg-surface">
      {/* Header */}
      <div className="px-4 pt-12 pb-6">
        <h1 className="text-xl font-bold text-on-surface">스캔 등록</h1>
        <p className="text-sm text-on-surface-variant mt-1">
          사진으로 식재료를 자동으로 등록해보세요
        </p>
      </div>

      {/* Option cards */}
      <div className="px-4 space-y-4">
        {SCAN_OPTIONS.map((option, index) => (
          <div key={option.title} className="relative">
            <button
              onClick={() => handleCardClick(index)}
              className="w-full bg-surface rounded-2xl p-6 border border-outline text-left
                hover:border-primary/40 hover:shadow-md active:scale-98
                transition-all duration-150"
            >
              <div className="flex items-start gap-4">
                <div className="w-14 h-14 rounded-2xl bg-primary/10 flex items-center justify-center shrink-0">
                  <span className="text-3xl">{option.emoji}</span>
                </div>
                <div className="flex-1 min-w-0 pt-1">
                  <h2 className="font-semibold text-on-surface text-base mb-1">
                    {option.title}
                  </h2>
                  <p className="text-sm text-on-surface-variant leading-relaxed">
                    {option.description}
                  </p>
                </div>
              </div>

              {/* TODO: Implement actual upload logic */}
              <input
                ref={inputRefs[index]}
                type="file"
                accept={option.accept}
                capture="environment"
                className="hidden"
                onChange={(e) => {
                  // TODO: call upload API once OCR backend is ready
                  console.log("File selected:", e.target.files?.[0]?.name);
                }}
              />
            </button>
          </div>
        ))}

        {/* Info banner */}
        <div className="bg-accent/10 rounded-2xl p-4 mt-2">
          <div className="flex items-start gap-3">
            <span className="text-xl">💡</span>
            <div>
              <p className="text-sm font-medium text-accent mb-0.5">사용 팁</p>
              <p className="text-xs text-on-surface-variant leading-relaxed">
                밝은 환경에서 촬영하면 인식률이 높아집니다.
                영수증의 경우 평평하게 펴서 촬영해주세요.
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* "준비 중" overlay */}
      {showReady && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm">
          <div className="bg-surface rounded-2xl px-8 py-6 mx-8 flex flex-col items-center gap-3 shadow-xl">
            <span className="text-4xl">🚧</span>
            <p className="font-semibold text-on-surface text-center">준비 중입니다</p>
            <p className="text-sm text-on-surface-variant text-center leading-relaxed">
              OCR 기능은 현재 준비 중입니다.{"\n"}
              곧 이용하실 수 있습니다.
            </p>
          </div>
        </div>
      )}
    </div>
  );
}
