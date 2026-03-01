'use client';

import { useState, useRef, useCallback } from 'react';
import { useRouter } from 'next/navigation';
import { api } from '@/lib/api';
import { useAuth } from '@/lib/auth-context';
import EmptyState from '@/components/ui/EmptyState';
import { Camera, Receipt, Check, Plus, Loader2, ArrowLeft } from 'lucide-react';
import { uploadImage } from '@/lib/cloudinary';

type ScanType = 'receipt' | 'photo';
type ScanStep = 'select' | 'preview' | 'processing' | 'results';

interface ScannedItem {
  name: string;
  quantity?: number;
  unit?: string;
  confidence: number;
  selected: boolean;
}

export default function ScanPage() {
  const router = useRouter();
  const { isLoggedIn, isLoading: authLoading } = useAuth();
  const fileInputRef = useRef<HTMLInputElement>(null);

  const [step, setStep] = useState<ScanStep>('select');
  const [scanType, setScanType] = useState<ScanType>('receipt');
  const [imagePreview, setImagePreview] = useState<string | null>(null);
  const [items, setItems] = useState<ScannedItem[]>([]);
  const [processing, setProcessing] = useState(false);
  const [adding, setAdding] = useState(false);

  const handleSelectType = (type: ScanType) => {
    setScanType(type);
    fileInputRef.current?.click();
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      setImagePreview(reader.result as string);
      setStep('preview');
    };
    reader.readAsDataURL(file);

    // Reset input so same file can be re-selected
    e.target.value = '';
  };

  const handleScan = useCallback(async () => {
    if (!imagePreview) return;
    setStep('processing');
    setProcessing(true);

    try {
      // Cloudinary에 스캔 이미지 백업 저장 (비동기, 실패해도 스캔 진행)
      uploadImage(imagePreview, 'scan').catch(() => {});

      const submitRes = await api.submitScan(scanType, imagePreview);
      if (!submitRes?.data) {
        throw new Error('Failed to submit scan');
      }

      const scannedItems: ScannedItem[] = (submitRes.data.items || []).map(
        (item: Record<string, unknown>) => ({
          name: item.name as string,
          quantity: item.quantity as number | undefined,
          unit: item.unit as string | undefined,
          confidence: (item.confidence as number) || 0.8,
          selected: true,
        }),
      );
      setItems(scannedItems);
      setStep('results');
      setProcessing(false);
      return;
    } catch {
      setStep('select');
      setProcessing(false);
      setImagePreview(null);
    }
  }, [imagePreview, scanType]);

  const toggleItem = (index: number) => {
    setItems((prev) =>
      prev.map((item, i) => (i === index ? { ...item, selected: !item.selected } : item)),
    );
  };

  const handleAddToFridge = async () => {
    const selected = items.filter((item) => item.selected);
    if (selected.length === 0) return;

    setAdding(true);
    try {
      // Search and add each item
      for (const item of selected) {
        const searchRes = await api.searchIngredientMaster(item.name);
        const master = searchRes?.data?.[0];
        if (master) {
          await api.addIngredient({
            ingredientId: master.id,
            quantity: item.quantity,
            unit: item.unit || master.defaultUnit,
            storageType: 'fridge',
          });
        }
      }
      router.push('/fridge');
    } catch {
      setAdding(false);
    }
  };

  const handleReset = () => {
    setStep('select');
    setImagePreview(null);
    setItems([]);
  };

  // Auth guard
  if (!authLoading && !isLoggedIn) {
    return (
      <div className="min-h-screen bg-surface-variant">
        <header className="bg-surface px-5 pt-12 pb-6" style={{ boxShadow: 'var(--shadow-level-1)' }}>
          <h1 className="text-[22px] font-semibold text-on-surface">스캔 등록</h1>
        </header>
        <div className="px-4 py-8">
          <EmptyState
            icon={<span className="text-6xl">🔒</span>}
            title="로그인이 필요해요"
            description="스캔 기능을 이용하려면 로그인해주세요"
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
        {step !== 'select' && (
          <button onClick={handleReset} className="p-1 text-on-surface-variant">
            <ArrowLeft size={22} />
          </button>
        )}
        <h1 className="text-[22px] font-semibold text-on-surface">스캔 등록</h1>
      </header>

      <input
        ref={fileInputRef}
        type="file"
        accept="image/*"
        capture="environment"
        className="hidden"
        onChange={handleFileChange}
      />

      <div className="px-4 py-4 pb-24">
        {/* Step 1: Select scan type */}
        {step === 'select' && (
          <div className="space-y-4">
            <p className="text-sm text-on-surface-variant">
              사진으로 식재료를 자동으로 등록해보세요
            </p>

            <button
              onClick={() => handleSelectType('receipt')}
              className="w-full bg-surface rounded-2xl p-5 flex items-start gap-4 active:scale-[0.98] transition-transform"
              style={{ boxShadow: 'var(--shadow-level-1)' }}
            >
              <div className="w-14 h-14 rounded-2xl bg-primary-50 flex items-center justify-center flex-shrink-0">
                <Receipt className="w-7 h-7 text-primary" />
              </div>
              <div className="text-left pt-0.5">
                <h2 className="font-semibold text-on-surface text-base">영수증 스캔</h2>
                <p className="text-sm text-on-surface-variant mt-0.5">
                  영수증을 촬영하여 식재료를 자동으로 등록합니다
                </p>
              </div>
            </button>

            <button
              onClick={() => handleSelectType('photo')}
              className="w-full bg-surface rounded-2xl p-5 flex items-start gap-4 active:scale-[0.98] transition-transform"
              style={{ boxShadow: 'var(--shadow-level-1)' }}
            >
              <div className="w-14 h-14 rounded-2xl bg-secondary-100 flex items-center justify-center flex-shrink-0">
                <Camera className="w-7 h-7 text-secondary-800" />
              </div>
              <div className="text-left pt-0.5">
                <h2 className="font-semibold text-on-surface text-base">사진 인식</h2>
                <p className="text-sm text-on-surface-variant mt-0.5">
                  식재료 사진을 촬영하여 자동으로 인식합니다
                </p>
              </div>
            </button>

            <div className="bg-accent/10 rounded-2xl p-4 mt-2">
              <div className="flex items-start gap-3">
                <span className="text-xl">💡</span>
                <div>
                  <p className="text-sm font-medium text-on-surface mb-0.5">사용 팁</p>
                  <p className="text-xs text-on-surface-variant leading-relaxed">
                    밝은 환경에서 촬영하면 인식률이 높아집니다.
                    영수증의 경우 평평하게 펴서 촬영해주세요.
                  </p>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Step 2: Image preview */}
        {step === 'preview' && imagePreview && (
          <div className="space-y-4">
            <div className="bg-surface rounded-2xl overflow-hidden" style={{ boxShadow: 'var(--shadow-level-1)' }}>
              <img
                src={imagePreview}
                alt="스캔 이미지"
                className="w-full max-h-[400px] object-contain bg-black/5"
              />
            </div>

            <div className="flex gap-3">
              <button
                onClick={handleReset}
                className="flex-1 h-12 bg-surface border border-outline rounded-xl text-sm font-medium text-on-surface active:scale-[0.97] transition-transform"
              >
                다시 촬영
              </button>
              <button
                onClick={handleScan}
                className="flex-1 h-12 bg-primary text-white rounded-xl text-sm font-medium active:scale-[0.97] transition-transform"
              >
                {scanType === 'receipt' ? '영수증 분석' : '식재료 인식'}
              </button>
            </div>
          </div>
        )}

        {/* Step 3: Processing */}
        {step === 'processing' && (
          <div className="flex flex-col items-center justify-center py-20 space-y-4">
            <div className="w-20 h-20 rounded-full bg-primary-50 flex items-center justify-center">
              <Loader2 className="w-10 h-10 text-primary animate-spin" />
            </div>
            <div className="text-center">
              <p className="text-base font-semibold text-on-surface">
                {scanType === 'receipt' ? '영수증 분석 중...' : '식재료 인식 중...'}
              </p>
              <p className="text-sm text-on-surface-variant mt-1">
                AI가 이미지를 분석하고 있습니다
              </p>
            </div>
          </div>
        )}

        {/* Step 4: Results */}
        {step === 'results' && (
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <p className="text-sm text-on-surface-variant">
                <span className="font-semibold text-on-surface">{items.length}개</span> 식재료를 인식했어요
              </p>
              <button
                onClick={() => {
                  const allSelected = items.every((i) => i.selected);
                  setItems((prev) => prev.map((i) => ({ ...i, selected: !allSelected })));
                }}
                className="text-xs text-primary font-medium"
              >
                {items.every((i) => i.selected) ? '전체 해제' : '전체 선택'}
              </button>
            </div>

            <div
              className="bg-surface rounded-2xl overflow-hidden divide-y divide-outline-variant"
              style={{ boxShadow: 'var(--shadow-level-1)' }}
            >
              {items.map((item, index) => (
                <button
                  key={`${item.name}-${index}`}
                  onClick={() => toggleItem(index)}
                  className="w-full flex items-center gap-3 px-4 py-3.5 active:bg-surface-variant transition-colors"
                >
                  <div
                    className={`w-6 h-6 rounded-lg border-2 flex items-center justify-center flex-shrink-0 transition-colors ${
                      item.selected
                        ? 'bg-primary border-primary'
                        : 'bg-surface border-outline'
                    }`}
                  >
                    {item.selected && <Check size={14} className="text-white" />}
                  </div>
                  <div className="flex-1 text-left min-w-0">
                    <p className="text-sm font-medium text-on-surface">{item.name}</p>
                    {item.quantity != null && (
                      <p className="text-xs text-on-surface-variant">
                        {item.quantity}{item.unit || ''}
                      </p>
                    )}
                  </div>
                  <span className="text-xs text-on-surface-variant flex-shrink-0">
                    {Math.round(item.confidence * 100)}%
                  </span>
                </button>
              ))}
            </div>

            {items.length === 0 && (
              <EmptyState
                icon={<span className="text-5xl">🔍</span>}
                title="인식된 식재료가 없어요"
                description="다시 촬영해보세요"
                ctaLabel="다시 촬영하기"
                onCtaClick={handleReset}
              />
            )}

            {items.length > 0 && (
              <button
                onClick={handleAddToFridge}
                disabled={adding || items.filter((i) => i.selected).length === 0}
                className="w-full h-12 bg-primary text-white rounded-xl font-medium text-sm disabled:opacity-40 flex items-center justify-center gap-2 active:scale-[0.97] transition-transform"
              >
                {adding ? (
                  <>
                    <Loader2 size={16} className="animate-spin" />
                    추가 중...
                  </>
                ) : (
                  <>
                    <Plus size={16} />
                    선택한 {items.filter((i) => i.selected).length}개 냉장고에 추가
                  </>
                )}
              </button>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
