'use client';

import ExpiryBadge from '@/components/ui/ExpiryBadge';
import EmptyState from '@/components/ui/EmptyState';
import { Skeleton, SkeletonCard, SkeletonListItem } from '@/components/ui/LoadingSpinner';
import { Home, Box, UtensilsCrossed, User, Plus } from 'lucide-react';

export default function DesignSystemPage() {
  return (
    <div className="min-h-screen bg-background p-4 pb-20 max-w-full overflow-x-hidden">
      <h1 className="text-[28px] font-semibold mb-1">디자인 시스템</h1>
      <p className="text-sm text-on-surface-variant mb-8">냉장고 레시피 앱 디자인 가이드</p>

      {/* ── Colors ── */}
      <Section title="1. 컬러 시스템">
        {/* Primary */}
        <SubSection title="Primary: Herb Green">
          <div className="grid grid-cols-5 gap-2">
            {[
              ['50', 'bg-primary-50', '#F0FAF0'],
              ['100', 'bg-primary-100', '#DCEFDC'],
              ['200', 'bg-primary-200', '#B8DFB8'],
              ['300', 'bg-primary-300', '#8FCC8F'],
              ['400', 'bg-primary-400', '#5FB85F'],
              ['500', 'bg-primary', '#3DA63D'],
              ['600', 'bg-primary-600', '#2E8C2E'],
              ['700', 'bg-primary-700', '#226622'],
              ['800', 'bg-primary-800', '#174717'],
              ['900', 'bg-primary-900', '#0D2A0D'],
            ].map(([step, cls, hex]) => (
              <div key={step} className="text-center">
                <div className={`${cls} h-12 rounded-lg mb-1`} />
                <div className="text-[10px] font-medium">{step}</div>
                <div className="text-[9px] text-on-surface-variant">{hex}</div>
              </div>
            ))}
          </div>
        </SubSection>

        {/* Secondary */}
        <SubSection title="Secondary: Warm Apricot">
          <div className="grid grid-cols-5 gap-2">
            {[
              ['50', 'bg-secondary-50', '#FFF8F0'],
              ['100', 'bg-secondary-100', '#FEECD8'],
              ['200', 'bg-secondary-200', '#FFD9B3'],
              ['300', 'bg-secondary-300', '#F9C47F'],
              ['400', 'bg-secondary-400', '#FFB366'],
              ['500', 'bg-secondary', '#F08C1A'],
              ['600', 'bg-secondary-600', '#CC7010'],
              ['700', 'bg-secondary-700', '#A5560A'],
              ['800', 'bg-secondary-800', '#CC6B00'],
              ['900', 'bg-secondary-900', '#994F00'],
            ].map(([step, cls, hex]) => (
              <div key={step} className="text-center">
                <div className={`${cls} h-12 rounded-lg mb-1`} />
                <div className="text-[10px] font-medium">{step}</div>
                <div className="text-[9px] text-on-surface-variant">{hex}</div>
              </div>
            ))}
          </div>
        </SubSection>

        {/* Accent & Semantic */}
        <SubSection title="Accent & Semantic">
          <div className="grid grid-cols-5 gap-2">
            {[
              ['Accent', 'bg-accent', '#E8321E'],
              ['Success', 'bg-success', '#388E3C'],
              ['Warning', 'bg-warning', '#EF6C00'],
              ['Danger', 'bg-danger', '#C62828'],
              ['Info', 'bg-info', '#1565C0'],
            ].map(([name, cls, hex]) => (
              <div key={name} className="text-center">
                <div className={`${cls} h-12 rounded-lg mb-1`} />
                <div className="text-[10px] font-medium">{name}</div>
                <div className="text-[9px] text-on-surface-variant">{hex}</div>
              </div>
            ))}
          </div>
          <p className="text-[10px] text-on-surface-variant mt-2">Semantic Container</p>
          <div className="grid grid-cols-4 gap-2 mt-1">
            {[
              ['Success', 'bg-success-container'],
              ['Warning', 'bg-warning-container'],
              ['Danger', 'bg-danger-container'],
              ['Info', 'bg-info-container'],
            ].map(([name, cls]) => (
              <div key={name} className="text-center">
                <div className={`${cls} h-8 rounded-lg mb-1`} />
                <div className="text-[9px] text-on-surface-variant">{name}</div>
              </div>
            ))}
          </div>
        </SubSection>

        {/* Expiry */}
        <SubSection title="소비기한 전용 컬러">
          <div className="grid grid-cols-4 gap-2">
            {[
              ['Safe', 'bg-safe', '#4CAF50', 'D-4 이상'],
              ['Soon', 'bg-soon', '#FF9800', 'D-3~D-2'],
              ['Urgent', 'bg-urgent', '#F44336', 'D-1~당일'],
              ['Expired', 'bg-expired', '#757575', '만료됨'],
            ].map(([name, cls, hex, desc]) => (
              <div key={name} className="text-center">
                <div className={`${cls} h-12 rounded-lg mb-1`} />
                <div className="text-[10px] font-medium">{name}</div>
                <div className="text-[9px] text-on-surface-variant">{hex}</div>
                <div className="text-[9px] text-on-surface-variant">{desc}</div>
              </div>
            ))}
          </div>
        </SubSection>

        {/* Surface */}
        <SubSection title="Surface 컬러">
          <div className="space-y-2">
            {[
              ['Background', 'bg-background', '#FAFAF8', 'border border-outline-variant'],
              ['Surface', 'bg-surface', '#FFFFFF', 'border border-outline-variant'],
              ['Surface Variant', 'bg-surface-variant', '#F4F4F0', ''],
              ['On Surface', 'bg-on-surface', '#1A1A18', ''],
              ['On Surface Variant', 'bg-on-surface-variant', '#4A4A44', ''],
              ['Outline', 'bg-outline', '#BDBDB5', ''],
              ['Outline Variant', 'bg-outline-variant', '#E0E0D8', ''],
              ['Inverse Surface', 'bg-inverse-surface', '#2E2E2A', ''],
              ['Inverse On Surface', 'bg-inverse-on-surface', '#E8E8E4', ''],
            ].map(([name, cls, hex, extra]) => (
              <div key={name} className="flex items-center gap-3">
                <div className={`${cls} ${extra} w-10 h-10 rounded-lg shrink-0`} />
                <div>
                  <div className="text-xs font-medium">{name}</div>
                  <div className="text-[10px] text-on-surface-variant">{hex}</div>
                </div>
              </div>
            ))}
          </div>
        </SubSection>
      </Section>

      {/* ── Typography ── */}
      <Section title="2. 타이포그래피">
        <p className="text-xs text-on-surface-variant mb-4">Pretendard Variable</p>
        <div className="space-y-4 bg-surface rounded-2xl p-4" style={{ boxShadow: 'var(--shadow-level-1)' }}>
          <div>
            <span className="text-[10px] text-on-surface-variant">Headline Large · 32sp · 600</span>
            <p className="text-[32px] font-semibold leading-10">페이지 타이틀</p>
          </div>
          <div>
            <span className="text-[10px] text-on-surface-variant">Headline Medium · 28sp · 600</span>
            <p className="text-[28px] font-semibold leading-9">섹션 타이틀</p>
          </div>
          <div>
            <span className="text-[10px] text-on-surface-variant">Headline Small · 24sp · 600</span>
            <p className="text-2xl font-semibold leading-8">카드 타이틀</p>
          </div>
          <div>
            <span className="text-[10px] text-on-surface-variant">Title Large · 22sp · 600</span>
            <p className="text-[22px] font-semibold leading-7">App Bar 타이틀</p>
          </div>
          <div>
            <span className="text-[10px] text-on-surface-variant">Title Medium · 16sp · 500</span>
            <p className="text-base font-medium leading-6">섹션 헤더</p>
          </div>
          <div>
            <span className="text-[10px] text-on-surface-variant">Body Large · 16sp · 400</span>
            <p className="text-base leading-6">본문 텍스트 (레시피 단계 설명)</p>
          </div>
          <div>
            <span className="text-[10px] text-on-surface-variant">Body Medium · 14sp · 400</span>
            <p className="text-sm leading-5">본문 기본 텍스트입니다</p>
          </div>
          <div>
            <span className="text-[10px] text-on-surface-variant">Body Small · 12sp · 400</span>
            <p className="text-xs leading-4">보조 텍스트, 설명</p>
          </div>
          <div>
            <span className="text-[10px] text-on-surface-variant">Label Large · 14sp · 500</span>
            <p className="text-sm font-medium leading-5">버튼 텍스트</p>
          </div>
          <div>
            <span className="text-[10px] text-on-surface-variant">Label Small · 11sp · 500</span>
            <p className="text-[11px] font-medium leading-4">캡션, 타임스탬프</p>
          </div>
        </div>
      </Section>

      {/* ── Spacing ── */}
      <Section title="3. 스페이싱 시스템">
        <p className="text-xs text-on-surface-variant mb-3">기본 단위: 4dp</p>
        <div className="space-y-2 bg-surface rounded-2xl p-4" style={{ boxShadow: 'var(--shadow-level-1)' }}>
          {[
            ['xxs', '2dp', 'w-0.5'],
            ['xs', '4dp', 'w-1'],
            ['sm', '8dp', 'w-2'],
            ['md', '12dp', 'w-3'],
            ['base', '16dp', 'w-4'],
            ['lg', '20dp', 'w-5'],
            ['xl', '24dp', 'w-6'],
            ['xxl', '32dp', 'w-8'],
            ['xxxl', '40dp', 'w-10'],
            ['mega', '48dp', 'w-12'],
          ].map(([name, val, cls]) => (
            <div key={name} className="flex items-center gap-3">
              <div className="w-12 text-xs font-medium text-right">{name}</div>
              <div className={`${cls} h-4 bg-primary rounded`} />
              <div className="text-[10px] text-on-surface-variant">{val}</div>
            </div>
          ))}
        </div>
      </Section>

      {/* ── Elevation ── */}
      <Section title="4. 엘리베이션 & 그림자">
        <div className="space-y-4">
          {[
            ['Level 0', '0dp', 'none', '배경, 기본 Surface'],
            ['Level 1', '1dp', 'var(--shadow-level-1)', '카드'],
            ['Level 2', '3dp', 'var(--shadow-level-2)', 'Bottom Nav, App Bar'],
            ['Level 3', '6dp', 'var(--shadow-level-3)', 'FAB, 드롭다운'],
            ['Level 4', '8dp', 'var(--shadow-level-4)', '다이얼로그'],
            ['Level 5', '12dp', 'var(--shadow-level-5)', '모달, 바텀 시트'],
          ].map(([name, dp, shadow, usage]) => (
            <div key={name} className="flex items-center gap-4">
              <div
                className="w-20 h-16 bg-surface rounded-2xl flex items-center justify-center"
                style={{ boxShadow: shadow }}
              >
                <span className="text-[10px] font-medium text-on-surface-variant">{dp}</span>
              </div>
              <div>
                <div className="text-xs font-medium">{name}</div>
                <div className="text-[10px] text-on-surface-variant">{usage}</div>
              </div>
            </div>
          ))}
        </div>
      </Section>

      {/* ── Border Radius ── */}
      <Section title="5. 테두리 반경">
        <div className="flex flex-wrap gap-3">
          {[
            ['xs', '4dp', 'rounded-[4px]'],
            ['sm', '8dp', 'rounded-lg'],
            ['md', '12dp', 'rounded-xl'],
            ['lg', '16dp', 'rounded-2xl'],
            ['xl', '20dp', 'rounded-[20px]'],
            ['xxl', '28dp', 'rounded-[28px]'],
            ['full', '9999', 'rounded-full'],
          ].map(([name, val, cls]) => (
            <div key={name} className="text-center">
              <div className={`w-16 h-16 bg-primary-100 ${cls} flex items-center justify-center`}>
                <span className="text-[9px] font-medium text-primary-700">{val}</span>
              </div>
              <div className="text-[10px] font-medium mt-1">{name}</div>
            </div>
          ))}
        </div>
      </Section>

      {/* ── Components ── */}
      <Section title="6. 컴포넌트">
        {/* Buttons */}
        <SubSection title="버튼">
          <div className="space-y-3">
            <button className="w-full h-12 bg-primary text-white text-sm font-medium rounded-xl active:bg-primary-600 active:scale-[0.97] transition-all" style={{ transitionDuration: 'var(--duration-fast)' }}>
              Primary Button
            </button>
            <button className="w-full h-12 border-[1.5px] border-primary text-primary-600 text-sm font-medium rounded-xl active:bg-primary-50 transition-colors" style={{ transitionDuration: 'var(--duration-fast)' }}>
              Secondary Button (Outlined)
            </button>
            <button className="w-full h-10 text-primary-600 text-sm font-medium rounded-xl active:bg-primary-100 transition-colors" style={{ transitionDuration: 'var(--duration-fast)' }}>
              Text Button
            </button>
            <button className="w-full h-12 bg-primary/40 text-white/60 text-sm font-medium rounded-xl cursor-not-allowed">
              Disabled Button
            </button>
          </div>
        </SubSection>

        {/* Input */}
        <SubSection title="텍스트 필드 (56dp, 12dp radius)">
          <div className="space-y-3">
            <div>
              <label className="text-xs text-on-surface-variant mb-1 block">기본</label>
              <input
                type="text"
                placeholder="식재료를 검색하세요"
                className="w-full h-14 px-4 text-base border-[1.5px] border-outline rounded-xl bg-surface focus:border-primary focus:border-2 outline-none placeholder:text-on-surface-variant/60 transition-colors"
              />
            </div>
            <div>
              <label className="text-xs text-on-surface-variant mb-1 block">에러</label>
              <input
                type="text"
                defaultValue="잘못된 입력"
                className="w-full h-14 px-4 text-base border-2 border-danger rounded-xl bg-surface outline-none transition-colors"
              />
              <p className="text-xs text-danger mt-1 flex items-center gap-1">
                <span className="text-sm">⚠</span> 올바른 값을 입력해주세요
              </p>
            </div>
          </div>
        </SubSection>

        {/* FAB */}
        <SubSection title="FAB (Floating Action Button)">
          <div className="flex items-center gap-4">
            {/* Standard FAB: 56x56dp, 16dp radius */}
            <div className="text-center">
              <button
                className="w-14 h-14 rounded-2xl flex items-center justify-center active:scale-95 active:bg-primary-200 transition-all"
                style={{ backgroundColor: '#DCEFDC', boxShadow: 'var(--shadow-level-3)' }}
              >
                <Plus size={24} className="text-primary-900" />
              </button>
              <span className="text-[10px] text-on-surface-variant mt-1 block">Standard</span>
            </div>
            {/* Small FAB: 40x40dp, 12dp radius */}
            <div className="text-center">
              <button
                className="w-10 h-10 rounded-xl bg-secondary-100 flex items-center justify-center active:scale-95 transition-transform"
                style={{ boxShadow: 'var(--shadow-level-2)' }}
              >
                <Plus size={24} className="text-secondary-700" />
              </button>
              <span className="text-[10px] text-on-surface-variant mt-1 block">Small</span>
            </div>
            {/* Extended FAB */}
            <div className="text-center">
              <button
                className="h-14 px-5 rounded-2xl flex items-center gap-3 active:scale-95 transition-transform"
                style={{ backgroundColor: '#DCEFDC', boxShadow: 'var(--shadow-level-3)' }}
              >
                <Plus size={24} className="text-primary-900" />
                <span className="text-sm font-medium text-primary-900">재료 추가</span>
              </button>
              <span className="text-[10px] text-on-surface-variant mt-1 block">Extended</span>
            </div>
          </div>
        </SubSection>

        {/* Cards */}
        <SubSection title="카드">
          {/* 식재료 카드 */}
          <p className="text-xs text-on-surface-variant mb-2">식재료 카드 (72dp, Level 1)</p>
          <div className="bg-surface rounded-2xl p-3 flex items-center gap-3 mb-4" style={{ boxShadow: 'var(--shadow-level-1)' }}>
            <div className="w-12 h-12 bg-primary-50 rounded-xl flex items-center justify-center text-2xl">🥬</div>
            <div className="flex-1 min-w-0">
              <div className="text-sm font-medium">배추</div>
              <div className="text-xs text-on-surface-variant">1포기 · 냉장</div>
            </div>
            <ExpiryBadge daysUntilExpiry={2} />
          </div>

          {/* 레시피 카드 */}
          <p className="text-xs text-on-surface-variant mb-2">레시피 카드 (20dp radius, Level 1)</p>
          <div className="grid grid-cols-2 gap-3">
            <div className="bg-surface rounded-[20px] overflow-hidden" style={{ boxShadow: 'var(--shadow-level-1)' }}>
              <div className="aspect-[3/2] bg-gradient-to-br from-primary-200 to-primary-400 relative flex items-center justify-center text-3xl">
                🍲
                <button className="absolute top-2 right-2 w-12 h-12 bg-white/80 rounded-full flex items-center justify-center text-sm">♡</button>
              </div>
              <div className="p-3">
                <p className="text-sm font-medium line-clamp-2">된장찌개</p>
                <div className="flex items-center gap-2 mt-1 text-xs text-on-surface-variant">
                  <span>⏱ 25분</span>
                  <span>★ 4.5</span>
                </div>
                <div className="mt-2 flex items-center gap-2">
                  <div className="flex-1 h-1.5 bg-outline-variant rounded-full overflow-hidden">
                    <div className="h-full bg-primary rounded-full" style={{ width: '80%' }} />
                  </div>
                  <span className="text-[10px] font-medium text-primary">80%</span>
                </div>
              </div>
            </div>
            <div className="bg-surface rounded-[20px] overflow-hidden" style={{ boxShadow: 'var(--shadow-level-1)' }}>
              <div className="aspect-[3/2] bg-gradient-to-br from-secondary-200 to-secondary-400 relative flex items-center justify-center text-3xl">
                🍛
                <button className="absolute top-2 right-2 w-12 h-12 bg-white/80 rounded-full flex items-center justify-center text-sm text-accent">♥</button>
              </div>
              <div className="p-3">
                <p className="text-sm font-medium line-clamp-2">김치볶음밥</p>
                <div className="flex items-center gap-2 mt-1 text-xs text-on-surface-variant">
                  <span>⏱ 15분</span>
                  <span>★ 4.8</span>
                </div>
                <div className="mt-2 flex items-center gap-2">
                  <div className="flex-1 h-1.5 bg-outline-variant rounded-full overflow-hidden">
                    <div className="h-full bg-safe rounded-full" style={{ width: '100%' }} />
                  </div>
                  <span className="text-[10px] font-medium text-safe">100%</span>
                </div>
              </div>
            </div>
          </div>
        </SubSection>

        {/* Expiry Badges */}
        <SubSection title="소비기한 배지 (WCAG 1.4.1 - 아이콘+컬러)">
          <div className="flex flex-wrap gap-2">
            <ExpiryBadge daysUntilExpiry={7} />
            <ExpiryBadge daysUntilExpiry={2} />
            <ExpiryBadge daysUntilExpiry={0} />
            <ExpiryBadge daysUntilExpiry={-3} />
          </div>
          <p className="text-[10px] text-on-surface-variant mt-2">
            6dp radius · 24dp height · 11sp labelSmall · 아이콘으로 색각 이상 사용자 지원
          </p>
        </SubSection>

        {/* Chips */}
        <SubSection title="칩 (8dp radius)">
          <p className="text-[10px] text-on-surface-variant mb-2">Filter Chip (다중 선택)</p>
          <div className="flex gap-2 mb-4">
            <button className="h-8 px-3 rounded-lg bg-secondary-100 text-on-surface text-sm font-medium border border-transparent">✓ 전체</button>
            <button className="h-8 px-3 rounded-lg bg-transparent text-on-surface-variant text-sm font-medium border border-outline">냉장</button>
            <button className="h-8 px-3 rounded-lg bg-transparent text-on-surface-variant text-sm font-medium border border-outline">냉동</button>
            <button className="h-8 px-3 rounded-lg bg-transparent text-on-surface-variant text-sm font-medium border border-outline">실온</button>
          </div>
          <p className="text-[10px] text-on-surface-variant mb-2">Suggestion Chip (단일 선택)</p>
          <div className="flex gap-2 overflow-x-auto">
            <button className="h-8 px-3 rounded-lg bg-secondary-100 text-on-surface text-sm font-medium border border-transparent whitespace-nowrap">✓ 전체</button>
            <button className="h-8 px-3 rounded-lg bg-transparent text-on-surface-variant text-sm font-medium border border-outline whitespace-nowrap">한식</button>
            <button className="h-8 px-3 rounded-lg bg-transparent text-on-surface-variant text-sm font-medium border border-outline whitespace-nowrap">중식</button>
            <button className="h-8 px-3 rounded-lg bg-transparent text-on-surface-variant text-sm font-medium border border-outline whitespace-nowrap">일식</button>
            <button className="h-8 px-3 rounded-lg bg-transparent text-on-surface-variant text-sm font-medium border border-outline whitespace-nowrap">양식</button>
          </div>
        </SubSection>

        {/* Bottom Nav */}
        <SubSection title="하단 네비게이션 (80dp, Level 2)">
          <div className="bg-surface rounded-xl flex justify-around py-2" style={{ boxShadow: 'var(--shadow-level-2)', height: '80px' }}>
            {[
              { Icon: Home, label: '홈', active: true },
              { Icon: Box, label: '냉장고', active: false },
              { Icon: UtensilsCrossed, label: '레시피', active: false },
              { Icon: User, label: '마이', active: false },
            ].map(({ Icon, label, active }) => (
              <div key={label} className="flex flex-col items-center justify-center gap-1">
                <div
                  className={`flex items-center justify-center rounded-full ${active ? 'bg-primary-100' : 'bg-transparent'}`}
                  style={{ width: '64px', height: '32px' }}
                >
                  <Icon size={24} className={active ? 'text-primary' : 'text-on-surface-variant'} fill={active ? 'currentColor' : 'none'} />
                </div>
                <span className={`text-xs font-medium ${active ? 'text-primary' : 'text-on-surface-variant'}`}>{label}</span>
              </div>
            ))}
          </div>
          <p className="text-[10px] text-on-surface-variant mt-2">선택 인디케이터: Primary-100 bg · 64x32dp pill · rounded-full</p>
        </SubSection>

        {/* Bottom Sheet */}
        <SubSection title="바텀 시트 (28dp radius, Level 5, Scrim 40%)">
          <div className="relative bg-on-surface/10 rounded-xl overflow-hidden" style={{ height: '200px' }}>
            <div className="absolute inset-0 bg-black/40" />
            <div
              className="absolute inset-x-0 bottom-0 bg-surface rounded-t-[28px] p-5"
              style={{ boxShadow: 'var(--shadow-level-5)' }}
            >
              <div className="w-8 h-1 bg-on-surface-variant/40 rounded-full mx-auto mb-4" />
              <p className="text-sm font-bold text-on-surface mb-1">바텀 시트 타이틀</p>
              <p className="text-xs text-on-surface-variant">핸들: 32×4dp, Scrim: #000 40%</p>
            </div>
          </div>
        </SubSection>

        {/* Dialog */}
        <SubSection title="다이얼로그 (28dp radius, Level 4)">
          <div className="bg-surface rounded-[28px] p-6 max-w-[280px]" style={{ boxShadow: 'var(--shadow-level-4)' }}>
            <h3 className="text-2xl font-semibold text-on-surface mb-4">삭제할까요?</h3>
            <p className="text-sm text-on-surface-variant mb-6">이 식재료를 냉장고에서 삭제합니다.</p>
            <div className="flex justify-end gap-2">
              <button className="h-10 px-3 text-sm font-medium text-on-surface-variant rounded-xl active:bg-surface-variant">취소</button>
              <button className="h-10 px-3 text-sm font-medium text-primary rounded-xl active:bg-primary-100">삭제</button>
            </div>
          </div>
        </SubSection>

        {/* Snackbar */}
        <SubSection title="스낵바 (12dp radius, Inverse Surface)">
          <div className="bg-inverse-surface rounded-xl h-12 flex items-center justify-between px-4 max-w-[480px]">
            <span className="text-sm text-inverse-on-surface">식재료가 추가되었습니다</span>
            <button className="text-sm font-medium text-inverse-primary">실행취소</button>
          </div>
        </SubSection>
      </Section>

      {/* ── Empty States ── */}
      <Section title="7. 빈 상태 & 에러">
        <SubSection title="냉장고 비었음">
          <div className="bg-surface rounded-2xl" style={{ boxShadow: 'var(--shadow-level-1)' }}>
            <EmptyState
              icon={<span className="text-6xl">🧊</span>}
              title="냉장고가 텅 비었어요"
              description="재료를 추가하면 맞춤 레시피를 추천해요"
              ctaLabel="재료 추가하기"
              onCtaClick={() => {}}
            />
          </div>
        </SubSection>
        <SubSection title="검색 결과 없음">
          <div className="bg-surface rounded-2xl" style={{ boxShadow: 'var(--shadow-level-1)' }}>
            <EmptyState
              icon={<span className="text-6xl">🔍</span>}
              title="검색 결과가 없어요"
              description="다른 키워드로 검색해보세요"
            />
          </div>
        </SubSection>
      </Section>

      {/* ── Skeleton & Loading ── */}
      <Section title="8. 로딩 & 스켈레톤">
        <SubSection title="스켈레톤 UI (shimmer: 135°, 1.5s)">
          <div className="space-y-3 mb-4">
            <SkeletonListItem />
            <SkeletonListItem />
          </div>
          <div className="grid grid-cols-2 gap-3">
            <SkeletonCard />
            <SkeletonCard />
          </div>
        </SubSection>
        <SubSection title="기본 스켈레톤">
          <div className="space-y-2">
            <Skeleton className="h-6 w-3/4 rounded-lg" />
            <Skeleton className="h-4 w-full rounded" />
            <Skeleton className="h-4 w-2/3 rounded" />
            <Skeleton className="h-32 w-full rounded-2xl" />
          </div>
        </SubSection>
      </Section>

      {/* ── Motion ── */}
      <Section title="9. 모션 & 애니메이션">
        <div className="bg-surface rounded-2xl p-4 space-y-3" style={{ boxShadow: 'var(--shadow-level-1)' }}>
          {[
            ['xfast', '50ms', '버튼 ripple 시작'],
            ['fast', '100ms', '체크박스, 토글, 아이콘 전환'],
            ['short', '150ms', '툴팁 표시, 소형 컴포넌트'],
            ['normal', '200ms', '페이드 인/아웃, 다이얼로그'],
            ['medium', '300ms', '페이지 전환, 카드 전개'],
            ['long', '400ms', '바텀 시트, 드로어 진입'],
            ['xlong', '500ms', '온보딩, 특별 전환'],
          ].map(([name, ms, usage]) => (
            <div key={name} className="flex items-center gap-3">
              <div className="w-16 text-xs font-medium font-mono text-right">{name}</div>
              <div className="w-12 text-xs text-primary font-mono">{ms}</div>
              <div className="text-[10px] text-on-surface-variant flex-1">{usage}</div>
            </div>
          ))}
        </div>
        <p className="text-[10px] text-on-surface-variant mt-3">
          이징: enter(0,0,0.2,1) · exit(0.4,0,1,1) · standard(0.2,0,0,1) · emphasized(0.2,0,0,1 + 긴 duration)
        </p>
      </Section>

      {/* ── Quick Actions ── */}
      <Section title="10. 퀵 액션 카드">
        <div className="grid grid-cols-3 gap-3">
          {[
            ['🥬', '식재료 추가'],
            ['📸', '영수증 스캔'],
            ['🍳', '레시피 추천'],
          ].map(([emoji, label]) => (
            <div
              key={label}
              className="bg-surface rounded-2xl p-4 flex flex-col items-center gap-2 active:scale-[0.97] transition-transform cursor-pointer"
              style={{ boxShadow: 'var(--shadow-level-1)', transitionDuration: 'var(--duration-fast)' }}
            >
              <span className="text-2xl">{emoji}</span>
              <span className="text-xs font-medium text-on-surface">{label}</span>
            </div>
          ))}
        </div>
      </Section>
    </div>
  );
}

function Section({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <section className="mb-10">
      <h2 className="text-lg font-semibold mb-4 text-on-surface">{title}</h2>
      {children}
    </section>
  );
}

function SubSection({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div className="mb-6">
      <h3 className="text-sm font-medium text-on-surface-variant mb-3">{title}</h3>
      {children}
    </div>
  );
}
