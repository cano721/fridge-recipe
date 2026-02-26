# 03. 디자인 시스템 스펙

## 1. 브랜드 아이덴티티

### 1.1 브랜드 철학

> "일상의 재료로 특별한 한 끼를"

냉장고에 있는 평범한 재료들이 특별한 요리로 변신하는 경험을 제공합니다.

### 1.2 디자인 원칙

| 원칙 | 설명 |
|------|------|
| **Fresh First** | 신선한 초록색 톤으로 식재료의 생동감을 전달 |
| **Effortless Discovery** | 최소 터치로 레시피 발견, 인지 부하 최소화 |
| **Warm Precision** | 따뜻하면서도 정확한 정보 전달 (소비기한, 영양 정보) |
| **Honest Feedback** | OCR/인식 결과에 대한 투명한 신뢰도 표시 |
| **Inclusive by Default** | WCAG 2.1 AA 접근성 기본 준수 |

---

## 2. 컬러 시스템

### 2.1 Primary: Herb Green

식재료의 신선함과 건강함을 상징합니다.

| 단계 | HEX | 용도 |
|------|-----|------|
| 50 | #F0FAF0 | 배경 틴트 |
| 100 | #DCEFDC | 선택 상태 배경 |
| 200 | #B8DFB8 | 호버 상태 |
| 300 | #8FCC8F | 비활성 강조 |
| 400 | #5FB85F | 보조 강조 |
| **500** | **#3DA63D** | **메인 Primary** |
| 600 | #2E8C2E | 눌림 상태 |
| 700 | #226622 | 다크 강조 |
| 800 | #174717 | 다크 모드 텍스트 |
| 900 | #0D2A0D | 다크 모드 배경 |

### 2.2 Secondary: Warm Apricot

따뜻한 음식과 요리의 즐거움을 표현합니다.

| 단계 | HEX | 용도 |
|------|-----|------|
| 50 | #FFF8F0 | 배경 틴트 |
| 100 | #FEECD8 | 알림 배경 |
| 200 | #FFD9B3 | 호버 배경 (밝은 살구) |
| 300 | #F9C47F | 호버 |
| 400 | #FFB366 | 보조 강조 (중간 살구) |
| **500** | **#F08C1A** | **메인 Secondary** |
| 600 | #CC7010 | 눌림 상태 |
| 700 | #A5560A | 강조 |
| 800 | #CC6B00 | 다크 강조 (어두운 살구) |
| 900 | #994F00 | 다크 모드 텍스트 (매우 어두운 살구) |
| Dark 300 | #FFB84D | 다크 모드 메인 |
| Dark 500 | #F08C1A | 다크 모드 강조 |

### 2.3 Accent: Tomato Red

긴급한 알림과 중요 액션에 사용합니다.

| 모드 | HEX | 용도 |
|------|-----|------|
| **메인 (Light)** | **#E8321E** | 긴급 알림, 중요 액션 |
| **Dark** | **#FF6B5C** | 다크 모드 가독성 확보 (밝은 토마토) |

### 2.4 시맨틱 컬러

| 용도 | Light 메인 | Light Container | Dark 메인 | Dark Container |
|------|-----------|----------------|----------|---------------|
| Success | #388E3C | #C8E6C9 | #66BB6A | #1B5E20 |
| Warning | #EF6C00 | #FFE0B2 | #FFA726 | #E65100 |
| Error | #C62828 | #FFCDD2 | #EF5350 | #B71C1C |
| Info | #1565C0 | #BBDEFB | #42A5F5 | #0D47A1 |

### 2.5 소비기한 전용 컬러

| 상태 | Light HEX | Dark HEX | 조건 |
|------|-----------|----------|------|
| Safe (안전) | #4CAF50 | #81C784 | D-4 이상 |
| Soon (임박) | #FF9800 | #FFB74D | D-3 ~ D-2 |
| Urgent (긴급) | #F44336 | #EF5350 | D-1 ~ 당일 |
| Expired (만료) | #757575 | #9E9E9E | 만료됨 |

### 2.6 Surface 컬러

| 용도 | Light | Dark |
|------|-------|------|
| Background | #FAFAF8 | #121210 |
| Surface | #FFFFFF | #1E1E1C |
| Surface Variant | #F4F4F0 | #2A2A28 |
| On Background | #1A1A18 | #E8E8E4 |
| On Surface | #1A1A18 | #E8E8E4 |
| On Surface Variant | #4A4A44 | #A0A09A |
| Outline | #BDBDB5 | #5A5A54 |
| Outline Variant | #E0E0D8 | #3A3A36 |
| Inverse Surface | #2E2E2A | #E8E8E4 |
| Inverse On Surface | #E8E8E4 | #2E2E2A |
| Inverse Primary | #6EC96E | #226622 |

---

## 3. 타이포그래피

### 3.1 폰트 패밀리

- **Primary**: Pretendard Variable (한국어/영문 통합)
- **Monospace**: JetBrains Mono (코드, 수량 표시)

### 3.2 타입 스케일 (Material Design 3)

| 스타일 | 크기 | 두께 | 행간 | 자간 | 용도 |
|--------|------|------|------|------|------|
| Display Large | 57sp | 400 | 64 | -0.25 | 스플래시, 온보딩 |
| Headline Large | 32sp | 600 | 40 | 0 | 페이지 타이틀 |
| Headline Medium | 28sp | 600 | 36 | 0 | 섹션 타이틀 |
| Headline Small | 24sp | 600 | 32 | 0 | 카드 타이틀 |
| Title Large | 22sp | 600 | 28 | 0 | App Bar 타이틀 |
| Title Medium | 16sp | 500 | 24 | 0.15 | 섹션 헤더 |
| Title Small | 14sp | 500 | 20 | 0.1 | 서브 헤더 |
| Body Large | 16sp | 400 | 24 | 0.5 | 본문 (레시피 단계) |
| Body Medium | 14sp | 400 | 20 | 0.25 | 본문 기본 |
| Body Small | 12sp | 400 | 16 | 0.4 | 보조 텍스트 |
| Label Large | 14sp | 500 | 20 | 0.1 | 버튼 텍스트 |
| Label Medium | 12sp | 500 | 16 | 0.5 | 배지, 탭 |
| Label Small | 11sp | 500 | 16 | 0.5 | 캡션, 타임스탬프 |

---

## 4. 스페이싱 시스템

### 4.1 기본 단위: 4dp

| 토큰 | 값 | 용도 |
|------|-----|------|
| none | 0 | - |
| xxs | 2dp | 미세 간격 |
| xs | 4dp | 아이콘-텍스트 간격 |
| sm | 8dp | 컴포넌트 내부 패딩 |
| md | 12dp | 카드 내부 패딩 |
| base | 16dp | 화면 좌우 마진, 섹션 간격 |
| lg | 20dp | 섹션 간 간격 |
| xl | 24dp | 큰 섹션 간격 |
| xxl | 32dp | 페이지 섹션 구분 |
| xxxl | 40dp | 주요 블록 간격 |
| mega | 48dp | - |
| giga | 64dp | - |

---

## 5. 컴포넌트 스펙

### 5.1 버튼

#### Primary Button (Filled)

```
높이: 48dp
패딩: 좌우 24dp
Border Radius: 12dp
배경: Primary-500 (#3DA63D)
텍스트: White, labelLarge (14sp, 500)
눌림: Primary-600, scale 0.97
비활성: 배경 40% 투명도, 텍스트 60% 투명도
```

#### Secondary Button (Outlined)

```
높이: 48dp
패딩: 좌우 24dp
Border: 1.5dp Primary-500
Border Radius: 12dp
배경: 투명
텍스트: Primary-600, labelLarge
눌림: Primary-50 배경
```

#### Text Button

```
높이: 40dp
패딩: 좌우 12dp
배경: 투명
텍스트: Primary-600, labelLarge
눌림: Primary-100 ripple
```

### 5.2 텍스트 필드 (Input)

```
높이: 56dp
패딩: 좌 16dp, 우 12dp (아이콘 영역)
Border: 1.5dp Outline (#BDBDB5)
Border Radius: 12dp
포커스 Border: 2dp Primary-500
에러 Border: 2dp Error-Main
Label: bodySmall, On-Surface-Variant (필드 위로 float)
Placeholder: bodyMedium, On-Surface-Variant 60%
입력 텍스트: bodyLarge, On-Surface
에러 메시지: bodySmall, Error-Main (하단 4dp 간격)
```

### 5.3 카드

#### 식재료 카드

```
크기: 전체 너비 - 32dp (좌우 16dp 마진)
높이: 72dp
Border Radius: 16dp
배경: Surface (#FFF)
Elevation: Level 1
패딩: 12dp

구조:
[아이콘 48dp] [재료명 + 수량] [소비기한 배지] [>]
```

#### 레시피 카드 (세로형)

```
너비: (화면 너비 - 48dp) / 2
높이: 자동 (이미지 + 텍스트)
Border Radius: 20dp
배경: Surface
Elevation: Level 1

구조:
┌──────────────┐
│  이미지    [♡]│  3:2 비율, 우상단 북마크 토글
│  (썸네일)     │  ♡ 터치 영역: 48x48dp
├──────────────┤
│  레시피명     │  titleSmall, maxLines 2
│  ⏱20분 ★4.8  │  bodySmall
│  ██████ 80%  │  매칭률 진행바
└──────────────┘
```

### 5.4 소비기한 배지

```
높이: 24dp
패딩: 좌우 8dp
Border Radius: 6dp
텍스트: labelSmall (11sp, 500)

Safe:    배경 Success 15%, 텍스트 Success-Dark, 아이콘 ✓ (check_circle)
Soon:    배경 Warning 15%, 텍스트 Warning-Dark, 아이콘 ⏳ (hourglass_top)
Urgent:  배경 Error 15%, 텍스트 Error-Dark, 볼드, 아이콘 ⚠ (warning)
Expired: 배경 #F0F0F0, 텍스트 #757575, 취소선, 아이콘 ✕ (cancel)
```

> WCAG 1.4.1 준수: 색상 외에 아이콘 심볼로도 상태를 구분하여 색각 이상 사용자 지원

### 5.5 네비게이션

#### Bottom Navigation Bar

```
높이: 80dp (Safe Area 포함)
배경: Surface
Elevation: Level 2
아이템 수: 4 (홈, 냉장고, 레시피, 마이페이지)

아이콘: 24dp, Outlined(비선택) / Filled(선택)
라벨: labelMedium (12sp)
선택 색상: Primary-500
비선택 색상: On-Surface-Variant

선택 인디케이터:
  배경: Primary-100, Border Radius full
  크기: 64 x 32dp
```

#### Top App Bar

```
높이: 64dp
배경: Background (기본), Surface (스크롤 시)
Title: titleLarge (22sp, 600)
Navigation Icon: 48dp 터치 영역
Action Icons: 48dp 터치 영역, 최대 3개
```

### 5.6 FAB (Floating Action Button)

#### Standard FAB

```
크기: 56 × 56dp
Border Radius: 16dp (large)
배경: primaryContainer (#DCEFDC)
아이콘: 24dp, onPrimaryContainer (#0D2A0D)
Elevation: Level 3
눌림: Primary-200, scale 0.95
```

#### Small FAB (보조 액션)

```
크기: 40 × 40dp
Border Radius: 12dp
배경: secondaryContainer
아이콘: 24dp
Elevation: Level 2
```

#### Extended FAB (텍스트 포함)

```
높이: 56dp
패딩: 좌 16dp (아이콘), 우 20dp
Border Radius: 16dp
배경: primaryContainer
아이콘: 24dp + 간격 12dp + labelLarge 텍스트
Elevation: Level 3
```

### 5.7 Chip

#### Filter Chip (다중 선택)

```
높이: 32dp
패딩: 좌우 12dp (아이콘 시 좌 8dp)
Border: 1dp Outline
Border Radius: 8dp (sm)
텍스트: labelLarge
비선택: 투명 배경, On-Surface-Variant 텍스트
선택: secondaryContainer 배경, On-Secondary-Container 텍스트, leading ✓ 아이콘 18dp
```

#### Suggestion Chip (단일 선택 / 추천)

```
높이: 32dp
패딩: 좌우 12dp
Border: 1dp Outline
Border Radius: 8dp
텍스트: labelLarge
눌림: Primary-100 배경
```

#### Input Chip (입력 값 태그)

```
높이: 32dp
패딩: 좌 8dp (아이콘), 우 4dp (X 버튼)
Border: 1dp Outline
Border Radius: 8dp
텍스트: labelLarge
trailing: close 아이콘 18dp, 48dp 터치 영역
```

### 5.8 Dialog

```
너비: 280 ~ 560dp (화면 너비 - 48dp, 최대 560dp)
Border Radius: xxl (28dp)
배경: Surface
Elevation: Level 4
패딩: 24dp
타이틀: headlineSmall
본문: bodyMedium, 상단 16dp
버튼 영역: 상단 24dp, 우측 정렬
  - 확인: Text Button (Primary)
  - 취소: Text Button (On-Surface-Variant)
Scrim: #000000, 40% 투명도
```

### 5.9 Bottom Sheet

```
Border Radius: xxl (28dp) - 상단만
배경: Surface
Elevation: Level 5
핸들: 32 × 4dp, On-Surface-Variant 40%, Border Radius full
핸들 상단 패딩: 12dp
콘텐츠 패딩: 좌우 16dp
Peek Height: 256dp (기본)
최대 높이: 화면 높이 - 72dp (상태바 + Safe Area)
Scrim: #000000, 40% 투명도
드래그 dismiss 임계값: 150dp 이상 하향 드래그
```

### 5.10 로딩 & 스켈레톤

#### 스켈레톤 UI

```
색상 (Light): #E8E8E4 → #F4F4F0 (shimmer)
색상 (Dark):  #2A2A28 → #343432 (shimmer)
Shimmer 방향: 135도 대각선, 1.5초 반복
Border Radius: 실제 컴포넌트와 동일
```

### 5.11 빈 상태 (Empty States)

```
구조:
┌──────────────────────────┐
│      [일러스트 이미지]    │  160 × 160dp
│  제목 (titleLarge)       │  "냉장고가 텅 비었어요"
│  설명 (bodyMedium)       │  "재료를 추가하면 맞춤 레시피를 추천해요"
│  [재료 추가하기] (CTA)   │  Primary Button
└──────────────────────────┘

상황별 메시지:
- 냉장고 비었음: "냉장고가 텅 비었어요" + 재료 추가 CTA
- 레시피 없음: "재료가 부족해요" + 재료 추가 제안
- 검색 결과 없음: "검색 결과가 없어요" + 검색어 수정 제안
- 즐겨찾기 없음: "저장한 레시피가 없어요" + 탐색 CTA
```

### 5.12 에러 상태

```
인라인 에러: Error-Main 색상, error_outline 16dp + 텍스트, bodySmall
페이지 레벨: 빈 상태와 동일 구조 + 재시도 버튼
네트워크 에러 배너: 상단 고정, Error-Container 배경, 48dp, wifi_off 아이콘
```

### 5.13 스낵바 (Snackbar)

```
위치: 화면 하단, Bottom Nav 위 8dp
너비: 화면 너비 - 32dp, 최대 480dp
높이: 48dp (단일 행), 68dp (액션 포함)
Border Radius: 12dp
배경: Inverse-Surface (#2E2E2A)
텍스트: bodyMedium, Inverse-On-Surface
자동 닫힘: 3초
```

---

## 6. 아이코노그래피

### 6.1 아이콘 스타일

- **기본**: Material Symbols — Rounded
- **변형**: Outlined (기본), Filled (선택 상태)
- **Weight**: 400 (기본), 500 (강조)

### 6.2 아이콘 크기

| 토큰 | 크기 | 용도 |
|------|------|------|
| xs | 16dp | 배지 내부, 인라인 힌트 |
| sm | 20dp | 보조 아이콘, 리스트 trailing |
| md | 24dp | 기본 아이콘 (대부분의 UI) |
| lg | 32dp | 강조 아이콘, 카드 내부 |
| xl | 48dp | 빈 상태, 카테고리 헤더 |
| xxl | 64dp | 온보딩, 주요 일러스트 |

### 6.3 커스텀 아이콘

**재료 카테고리**

| 카테고리 | 아이콘 ID | 설명 |
|---------|----------|------|
| 냉장고 전체 | ic_fridge | 냉장고 |
| 채소 | ic_vegetable | 당근 |
| 과일 | ic_fruit | 사과 |
| 육류 | ic_meat | 닭 |
| 해산물 | ic_seafood | 생선 |
| 유제품 | ic_dairy | 우유 |
| 달걀 | ic_egg | 달걀 |
| 냉동식품 | ic_frozen | 눈결정 + 식품 |
| 조미료/소스 | ic_seasoning | 소스병 |
| 쌀/곡물 | ic_rice | 쌀 |

**조리 방법**

| 조리법 | 아이콘 ID |
|--------|----------|
| 볶음 | ic_stir_fry |
| 끓임 | ic_boil |
| 굽기 | ic_grill |
| 찜 | ic_steam |
| 튀김 | ic_fry |
| 무침 | ic_mix |

**UI 전용**

| 기능 | 아이콘 ID |
|------|----------|
| 카메라 스캔 | ic_camera_scan |
| 영수증 스캔 | ic_receipt_scan |
| 재료 추가 | ic_add_ingredient |
| 소비기한 | ic_expiry_date |
| 영양 정보 | ic_nutrition |
| 냉장 보관 | ic_refrigerate |
| 냉동 보관 | ic_freeze |
| 상온 보관 | ic_room_temp |
| 인분 | ic_servings |
| 조리 시간 | ic_cook_time |
| 난이도 | ic_difficulty |

---

## 7. 모션 & 애니메이션

### 7.1 모션 원칙

- **Purposeful**: 기능적 의미 없는 애니메이션 금지
- **Responsive**: 터치 피드백 100ms 이내 시작
- **Cohesive**: 동일 유형 전환은 동일 애니메이션
- **Subtle**: 사용자 주의를 빼앗지 않음

### 7.2 지속 시간 토큰

| 토큰 | ms | 용도 |
|------|----|------|
| xfast | 50 | 버튼 ripple 시작 |
| fast | 100 | 체크박스, 토글, 아이콘 전환 |
| short | 150 | 툴팁 표시, 소형 컴포넌트 |
| normal | 200 | 페이드 인/아웃, 다이얼로그 |
| medium | 300 | 페이지 전환, 카드 전개 |
| long | 400 | 바텀 시트, 드로어 진입 |
| xlong | 500 | 온보딩, 특별 전환 |

### 7.3 이징 커브

```kotlin
object Easing {
    val enter = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)      // 빠른 시작, 느린 끝
    val exit = CubicBezierEasing(0.4f, 0.0f, 1.0f, 1.0f)       // 느린 시작, 빠른 끝
    // M3 스펙에서 standard와 emphasized의 기본 easing curve는 동일(CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f))하며,
    // emphasized는 duration이 더 길어 체감 차이를 만듦 (예: emphasized = 400ms, standard = 200~300ms)
    val emphasized = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)  // 과슈팅 스프링, 긴 duration과 함께 사용
    val standard = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)   // 대부분의 상태 전환, 짧은 duration과 함께 사용
    val decelerate = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f) // 감속
    val linear = LinearEasing                                     // 진행 바, 로딩
}
```

### 7.4 주요 애니메이션

| 애니메이션 | 지속 시간 | 이징 | 설명 |
|-----------|----------|------|------|
| 페이지 전환 (Forward) | 300ms | emphasized | slide in from right + alpha |
| 페이지 전환 (Back) | 250ms | enter | 반대 방향 |
| 카드 누름 | 100ms | standard | scale 1.0 → 0.97 |
| 카드 해제 | 150ms | spring | scale 0.97 → 1.0 |
| 찜 버튼 탭 | 300ms | spring | scale 1.0 → 1.3 → 1.0 + 색상 전환 |
| 바텀 시트 진입 | 350ms | enter | translateY + scrim 0.4 |
| 바텀 시트 퇴장 | 250ms | exit | translateY + scrim 0 |
| 스켈레톤 → 콘텐츠 | 200ms | enter | alpha 0 → 1 |
| 리스트 아이템 stagger | 250ms | enter | delay = index × 50ms, max 400ms |

---

## 8. 엘리베이션 & 그림자

### 8.1 엘리베이션 레벨

| 레벨 | dp | 용도 | 그림자 (Light) |
|------|-----|------|---------------|
| Level 0 | 0 | 배경, 기본 Surface | none |
| Level 1 | 1 | 카드 | 0px 1px 2px rgba(0,0,0,0.08) |
| Level 2 | 3 | Bottom Nav, App Bar | 0px 2px 6px rgba(0,0,0,0.10) |
| Level 3 | 6 | FAB, 드롭다운 | 0px 4px 12px rgba(0,0,0,0.12) |
| Level 4 | 8 | 다이얼로그 | 0px 6px 18px rgba(0,0,0,0.14) |
| Level 5 | 12 | 모달, 바텀 시트 | 0px 8px 24px rgba(0,0,0,0.16) |

---

## 9. 테두리 반경 (Border Radius)

| 토큰 | 값 | 적용 |
|------|-----|------|
| none | 0dp | 이미지 전체 화면, 구분선 |
| xs | 4dp | 배지, 태그 |
| sm | 8dp | 칩, 소형 버튼 |
| md | 12dp | 텍스트 필드, 스낵바, 버튼 |
| lg | 16dp | 카드 (기본), FAB |
| xl | 20dp | 레시피 카드 |
| xxl | 28dp | 다이얼로그, 바텀 시트 상단 |
| full | 9999dp | 검색 바, 원형 FAB, 배지 |

---

## 10. 디자인 토큰 (JSON)

```json
{
  "color": {
    "primary": {
      "50": "#F0FAF0", "100": "#DCEFDC", "200": "#B8DFB8",
      "300": "#8FCC8F", "400": "#5FB85F", "500": "#3DA63D",
      "600": "#2E8C2E", "700": "#226622", "800": "#174717", "900": "#0D2A0D"
    },
    "secondary": {
      "50": "#FFF8F0", "100": "#FEECD8", "200": "#FFD9B3",
      "300": "#F9C47F", "400": "#FFB366", "500": "#F08C1A",
      "600": "#CC7010", "700": "#A5560A", "800": "#CC6B00", "900": "#994F00",
      "dark300": "#FFB84D", "dark500": "#F08C1A"
    },
    "accent": { "500": "#E8321E", "dark": "#FF6B5C" },
    "semantic": {
      "success": "#388E3C", "successContainer": "#C8E6C9",
      "successDark": "#66BB6A", "successContainerDark": "#1B5E20",
      "warning": "#EF6C00", "warningContainer": "#FFE0B2",
      "warningDark": "#FFA726", "warningContainerDark": "#E65100",
      "error": "#C62828", "errorContainer": "#FFCDD2",
      "errorDark": "#EF5350", "errorContainerDark": "#B71C1C",
      "info": "#1565C0", "infoContainer": "#BBDEFB",
      "infoDark": "#42A5F5", "infoContainerDark": "#0D47A1"
    },
    "surface": {
      "background": "#FAFAF8", "surface": "#FFFFFF",
      "surfaceVariant": "#F4F4F0", "outline": "#BDBDB5",
      "outlineVariant": "#E0E0D8",
      "onBackground": "#1A1A18", "onSurface": "#1A1A18",
      "onSurfaceVariant": "#4A4A44",
      "inverseSurface": "#2E2E2A",
      "inverseOnSurface": "#E8E8E4",
      "inversePrimary": "#6EC96E"
    },
    "expiry": {
      "safe": "#4CAF50", "safeDark": "#81C784",
      "soon": "#FF9800", "soonDark": "#FFB74D",
      "urgent": "#F44336", "urgentDark": "#EF5350",
      "expired": "#757575", "expiredDark": "#9E9E9E"
    }
  },
  "typography": {
    "fontFamily": { "primary": "Pretendard Variable", "mono": "JetBrains Mono" }
  },
  "spacing": {
    "none": 0, "xxs": 2, "xs": 4, "sm": 8, "md": 12,
    "base": 16, "lg": 20, "xl": 24, "xxl": 32, "xxxl": 40,
    "mega": 48, "giga": 64
  },
  "borderRadius": {
    "none": 0, "xs": 4, "sm": 8, "md": 12,
    "lg": 16, "xl": 20, "xxl": 28, "full": 9999
  },
  "elevation": {
    "level0": 0, "level1": 1, "level2": 3,
    "level3": 6, "level4": 8, "level5": 12
  },
  "duration": {
    "xfast": 50, "fast": 100, "short": 150, "normal": 200,
    "medium": 300, "long": 400, "xlong": 500
  },
  "iconSize": {
    "xs": 16, "sm": 20, "md": 24, "lg": 32, "xl": 48, "xxl": 64
  }
}
```

---

## 11. Compose Multiplatform 테마 구현

### 11.1 컬러 스킴

```kotlin
// Color.kt
object FridgeRecipeColors {
    val Primary50  = Color(0xFFF0FAF0)
    val Primary100 = Color(0xFFDCEFDC)
    val Primary500 = Color(0xFF3DA63D)
    val Primary600 = Color(0xFF2E8C2E)
    val Primary700 = Color(0xFF226622)

    val Secondary500 = Color(0xFFF08C1A)
    val Accent500    = Color(0xFFE8321E)
    val AccentDark   = Color(0xFFFF6B5C)  // 다크 모드용 밝은 토마토

    val Background     = Color(0xFFFAFAF8)
    val Surface        = Color(0xFFFFFFFF)
    val SurfaceVariant = Color(0xFFF4F4F0)
    val OnBackground   = Color(0xFF1A1A18)
    val OnSurface      = Color(0xFF1A1A18)

    val Success = Color(0xFF388E3C)
    val Warning = Color(0xFFEF6C00)
    val Error   = Color(0xFFC62828)

    // Expiry
    val ExpirySafe    = Color(0xFF4CAF50)
    val ExpirySoon    = Color(0xFFFF9800)
    val ExpiryUrgent  = Color(0xFFF44336)
    val ExpiryExpired = Color(0xFF757575)

    // Expiry Dark
    val ExpirySafeDark    = Color(0xFF81C784)
    val ExpirySoonDark    = Color(0xFFFFB74D)
    val ExpiryUrgentDark  = Color(0xFFEF5350)
    val ExpiryExpiredDark = Color(0xFF9E9E9E)
}

// Theme.kt
private val LightColorScheme = lightColorScheme(
    primary            = Color(0xFF3DA63D),
    onPrimary          = Color.White,
    primaryContainer   = Color(0xFFDCEFDC),
    onPrimaryContainer = Color(0xFF0D2A0D),
    secondary          = Color(0xFFF08C1A),
    onSecondary        = Color.White,
    background         = Color(0xFFFAFAF8),
    onBackground       = Color(0xFF1A1A18),
    surface            = Color(0xFFFFFFFF),
    onSurface          = Color(0xFF1A1A18),
    surfaceVariant     = Color(0xFFF4F4F0),
    onSurfaceVariant   = Color(0xFF4A4A44),
    outline            = Color(0xFFBDBDB5),
    error              = Color(0xFFC62828),
    onError            = Color.White,
    inverseSurface     = Color(0xFF2E2E2A),
    inverseOnSurface   = Color(0xFFE8E8E4),
    inversePrimary     = Color(0xFF6EC96E)
)

private val DarkColorScheme = darkColorScheme(
    primary            = Color(0xFF6EC96E),
    onPrimary          = Color(0xFF1A3D1A),
    primaryContainer   = Color(0xFF225522),
    onPrimaryContainer = Color(0xFFC8E6C8),
    secondary          = Color(0xFFFFB84D),
    onSecondary        = Color(0xFF3D2200),
    background         = Color(0xFF121210),
    onBackground       = Color(0xFFE8E8E4),
    surface            = Color(0xFF1E1E1C),
    onSurface          = Color(0xFFE8E8E4),
    surfaceVariant     = Color(0xFF2A2A28),
    onSurfaceVariant   = Color(0xFFA0A09A),
    outline            = Color(0xFF5A5A54),
    // 시맨틱 에러 컬러 (errorDark #EF5350), Accent는 별도 커스텀 토큰으로 관리
    error              = Color(0xFFEF5350),
    onError            = Color(0xFF680003),
    inverseSurface     = Color(0xFFE8E8E4),
    inverseOnSurface   = Color(0xFF2E2E2A),
    inversePrimary     = Color(0xFF226622)
)
```

### 11.2 타이포그래피

```kotlin
// commonMain/Font.kt — KMP expect 선언
expect val PretendardFamily: FontFamily
expect val JetBrainsMonoFamily: FontFamily
```

```kotlin
// androidMain/Font.kt — Android actual 구현
actual val PretendardFamily = FontFamily(
    Font(R.font.pretendard_variable, FontWeight.Normal),
    Font(R.font.pretendard_variable, FontWeight.Medium),
    Font(R.font.pretendard_variable, FontWeight.SemiBold),
    Font(R.font.pretendard_variable, FontWeight.Bold)
)

actual val JetBrainsMonoFamily = FontFamily(
    Font(R.font.jetbrains_mono_regular, FontWeight.Normal),
    Font(R.font.jetbrains_mono_medium, FontWeight.Medium)
)
```

```kotlin
// iosMain/Font.kt — iOS actual 구현
actual val PretendardFamily = FontFamily(
    Font("Pretendard Variable", FontWeight.Normal),
    Font("Pretendard Variable", FontWeight.Medium),
    Font("Pretendard Variable", FontWeight.SemiBold),
    Font("Pretendard Variable", FontWeight.Bold)
)

actual val JetBrainsMonoFamily = FontFamily(
    Font("JetBrainsMono-Regular", FontWeight.Normal),
    Font("JetBrainsMono-Medium", FontWeight.Medium)
)
```

```kotlin
val FridgeRecipeTypography = Typography(
    displayLarge   = TextStyle(fontFamily = PretendardFamily, fontSize = 57.sp, fontWeight = FontWeight.Normal, lineHeight = 64.sp, letterSpacing = (-0.25).sp),
    headlineLarge  = TextStyle(fontFamily = PretendardFamily, fontSize = 32.sp, fontWeight = FontWeight.SemiBold, lineHeight = 40.sp, letterSpacing = 0.sp),
    headlineMedium = TextStyle(fontFamily = PretendardFamily, fontSize = 28.sp, fontWeight = FontWeight.SemiBold, lineHeight = 36.sp, letterSpacing = 0.sp),
    headlineSmall  = TextStyle(fontFamily = PretendardFamily, fontSize = 24.sp, fontWeight = FontWeight.SemiBold, lineHeight = 32.sp, letterSpacing = 0.sp),
    titleLarge     = TextStyle(fontFamily = PretendardFamily, fontSize = 22.sp, fontWeight = FontWeight.SemiBold, lineHeight = 28.sp, letterSpacing = 0.sp),
    titleMedium    = TextStyle(fontFamily = PretendardFamily, fontSize = 16.sp, fontWeight = FontWeight.Medium, lineHeight = 24.sp, letterSpacing = 0.15.sp),
    titleSmall     = TextStyle(fontFamily = PretendardFamily, fontSize = 14.sp, fontWeight = FontWeight.Medium, lineHeight = 20.sp, letterSpacing = 0.1.sp),
    bodyLarge      = TextStyle(fontFamily = PretendardFamily, fontSize = 16.sp, fontWeight = FontWeight.Normal, lineHeight = 24.sp, letterSpacing = 0.5.sp),
    bodyMedium     = TextStyle(fontFamily = PretendardFamily, fontSize = 14.sp, fontWeight = FontWeight.Normal, lineHeight = 20.sp, letterSpacing = 0.25.sp),
    bodySmall      = TextStyle(fontFamily = PretendardFamily, fontSize = 12.sp, fontWeight = FontWeight.Normal, lineHeight = 16.sp, letterSpacing = 0.4.sp),
    labelLarge     = TextStyle(fontFamily = PretendardFamily, fontSize = 14.sp, fontWeight = FontWeight.Medium, lineHeight = 20.sp, letterSpacing = 0.1.sp),
    labelMedium    = TextStyle(fontFamily = PretendardFamily, fontSize = 12.sp, fontWeight = FontWeight.Medium, lineHeight = 16.sp, letterSpacing = 0.5.sp),
    labelSmall     = TextStyle(fontFamily = PretendardFamily, fontSize = 11.sp, fontWeight = FontWeight.Medium, lineHeight = 16.sp, letterSpacing = 0.5.sp),
)
```

### 11.3 Shape

```kotlin
val FridgeRecipeShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small      = RoundedCornerShape(8.dp),
    medium     = RoundedCornerShape(12.dp),
    large      = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

// Material3 Shapes는 5단계만 지원하므로, xl(20dp)는 별도 확장 토큰으로 정의
// 레시피 카드 전용 shape (Border Radius 테이블 xl = 20dp 대응)
val RecipeCardShape = RoundedCornerShape(20.dp)
```

### 11.4 테마 적용

```kotlin
@Composable
fun FridgeRecipeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = FridgeRecipeTypography,
        shapes      = FridgeRecipeShapes,
        content     = content
    )
}
```

---

## 12. 주요 화면 와이어프레임

### 12.1 로그인/온보딩

```
┌────────────────────────────────────┐
│                                    │
│         [앱 로고 + 일러스트]        │  160dp
│                                    │
│     일상의 재료로 특별한 한 끼를    │  headlineMedium
│     냉장고 속 재료가 맛있는         │  bodyLarge
│     레시피로 변합니다               │
│                                    │
│  ┌──────────────────────────────┐  │
│  │  🟡  카카오로 시작하기        │  │  카카오 노란색 버튼
│  └──────────────────────────────┘  │
│  ┌──────────────────────────────┐  │
│  │  G   Google로 시작하기        │  │  Google 흰색 버튼
│  └──────────────────────────────┘  │
│  ┌──────────────────────────────┐  │
│  │  🍎  Apple로 시작하기         │  │  Apple 검정색 버튼
│  └──────────────────────────────┘  │
│                                    │
│  로그인 시 이용약관 및              │  bodySmall, 링크
│  개인정보처리방침에 동의합니다       │
└────────────────────────────────────┘
```

### 12.2 온보딩 (식이 선호도)

```
┌────────────────────────────────────┐
│                           건너뛰기 │
│                                    │
│  식이 선호도를 알려주세요           │  headlineSmall
│  맞춤 레시피 추천에 사용됩니다      │  bodyMedium
│                                    │
│  알레르기가 있나요?                 │  titleMedium
│  [땅콩] [갑각류] [유제품] [밀]     │  Filter Chip (다중 선택)
│  [계란] [대두] [견과류]            │
│                                    │
│  식이 제한이 있나요?                │
│  [채식] [비건] [할랄] [저탄수화물] │
│  [글루텐프리] [저염식]              │
│                                    │
│  [       다음으로 →                ]│  Primary Button
│  ● ○ ○                            │  Page Indicator
└────────────────────────────────────┘
```

### 12.3 홈/대시보드

```
┌────────────────────────────────────┐
│ [로고] 냉장고 레시피            🔔 │  Top App Bar
├────────────────────────────────────┤
│  안녕하세요, 김민지님               │  headlineSmall
│  냉장고에 12가지 재료가 있어요      │  bodyMedium
│                                    │
│  🔍 레시피나 재료를 검색해보세요    │  검색 필드
│                                    │
│  ⚠️ 소비기한 임박 재료              │  섹션 헤더
│  [당근 D-1] [우유 D-2] [계란 D-3] │  수평 스크롤 칩
│                                    │
│  오늘 추천 레시피            더보기 │
│  ┌──────────┐ ┌──────────┐        │
│  │ 김치볶음밥│ │ 된장찌개  │        │  2열 레시피 카드
│  │ ⏱20분    │ │ ⏱30분    │        │
│  └──────────┘ └──────────┘        │
│                                    │
│  카테고리별 탐색                    │
│  [🥩 육류] [🥦 채소] [🐟 해산물]  │  카테고리 칩
│                                    │
│                       [+ 재료추가] │  FAB
├────────────────────────────────────┤
│  [홈] [냉장고] [레시피] [마이페이지]│  Bottom Nav
└────────────────────────────────────┘
```

### 12.4 냉장고 (재료 목록)

```
┌────────────────────────────────────┐
│ 냉장고                       🔍  ⋮│
├────────────────────────────────────┤
│  [전체] [냉장] [냉동] [상온]        │  Tab Bar
├────────────────────────────────────┤
│  12개 재료 · 3개 임박               │
│                                    │
│  임박 재료                          │  Error 계열 섹션
│  │ 🥕 당근   500g   D-2 !!    │   │
│  │ 🥛 우유   1L     D-3 !     │   │
│                                    │
│  채소                               │  카테고리별 그룹
│  │ 🧅 양파    3개    D-14      │ > │
│  │ 🥦 브로콜리 1개   D-10      │ > │
│  │ 🍅 토마토  5개    D-7       │ > │
│                                    │
│                         [+]        │  FAB
├────────────────────────────────────┤
│  [홈] [냉장고] [레시피] [마이페이지]│
└────────────────────────────────────┘
```

### 12.5 재료 추가 (수동 입력)

```
┌────────────────────────────────────┐
│ ← 재료 추가                        │
├────────────────────────────────────┤
│  [📷 카메라로 추가] [🧾 영수증 스캔]│
│                                    │
│  직접 입력                          │
│  ┌──────────────────────────────┐  │
│  │ 재료 이름 *                  │  │  자동완성 검색
│  └──────────────────────────────┘  │
│  최근: [당근] [양파] [달걀] ...    │  빠른 선택 칩
│                                    │
│  카테고리            채소 (자동)   │  마스터 DB에서 자동 매핑, 읽기 전용
│  수량 (선택): [___]                │
│  [개] [g] [kg] [ml] [L] [봉지]    │  단위 선택 칩
│                                    │
│  소비기한 (선택): [___]            │
│  [오늘+3일] [오늘+7일] [직접입력]  │
│                                    │
│  보관 위치: [냉장 ✓] [냉동] [상온] │
│  메모 (선택): [___]               │
│                                    │
│  [       냉장고에 추가하기        ] │  Primary Button
└────────────────────────────────────┘
```

### 12.6 OCR 스캔 결과 확인

```
┌────────────────────────────────────┐
│ ← 스캔 결과 확인                    │
├────────────────────────────────────┤
│  🧾 영수증에서 8개 항목을 찾았어요  │  headlineSmall
│  확인 후 냉장고에 추가해주세요       │  bodyMedium
│                                    │
│  ✅ 높은 신뢰도                     │  섹션 헤더 (Success)
│  ☑ 양파         3개    ✓ 매칭      │
│  ☑ 돼지고기     600g   ✓ 매칭      │
│  ☑ 두부         1모    ✓ 매칭      │
│  ☑ 대파         1단    ✓ 매칭      │
│                                    │
│  ⚠️ 확인 필요                       │  섹션 헤더 (Warning)
│  ☑ "CJ 다시다"  → [조미료] 수정 ▾  │  드롭다운 매칭
│  ☑ "풀무원 두부" → [두부] 중복 ▾   │
│                                    │
│  ❌ 인식 불가                        │  섹션 헤더 (Error)
│  ☐ "키친타올 3겹" → 비식재료 제외  │  비활성
│  ☐ "비닐봉투"    → 비식재료 제외   │
│                                    │
│  [선택 항목 냉장고에 추가 (6개)]   │  Primary Button
└────────────────────────────────────┘
```

### 12.7 레시피 추천 목록

```
┌────────────────────────────────────┐
│ ← 추천 레시피                🔍  ⋮│
├────────────────────────────────────┤
│  [전체] [가능 ✓] [재료추가] [즐겨찾기]│
├────────────────────────────────────┤
│  냉장고 재료로 32개 레시피 가능     │
│  [정렬: 재료 일치순 ▾]            │
│                                    │
│  🥇 재료 100% 일치                 │  그룹 헤더
│  │ [썸네일] 김치볶음밥              │
│  │         ██████████ 100%        │  매칭 바
│  │         ⏱20분 · ★4.8 · 2인    │
│                                    │
│  🥈 재료 80% 일치 (1개 부족)      │
│  │ [썸네일] 된장찌개                │
│  │         ████████░░ 80%         │
│  │         없는 재료: 두부         │
│  │         ⏱30분 · ★4.9          │
│                                    │
│                       [+ 재료추가] │  FAB
├────────────────────────────────────┤
│  [홈] [냉장고] [레시피] [마이페이지]│
└────────────────────────────────────┘
```

### 12.8 레시피 상세

```
┌────────────────────────────────────┐
│                               ← ♡ │  투명 App Bar
│  ┌────────────────────────────────┐│
│  │      레시피 썸네일 이미지       ││  3:2 비율
│  └────────────────────────────────┘│
│  [한식] [볶음]                     │  카테고리 배지
│  김치볶음밥                         │  headlineMedium
│  ★ 4.8 (328개 리뷰)               │
│                                    │
│  ⏱ 20분   👥 2인분   📊 보통      │  메타 정보
│                                    │
│  내 냉장고 재료 일치도              │
│  ████████░░ 80% (4/5 재료)        │
│  없는 재료: 두부 [추가]            │
│                                    │
│  필요한 재료                        │
│  ✅ 김치     200g  (있음)          │
│  ✅ 밥       2공기 (있음)          │
│  ⬜ 두부     100g  (없음)          │
│  ✅ 참기름   1스푼 (있음)          │
│  ✅ 계란     1개   (있음)          │
│                                    │
│  만드는 방법                        │
│  1. 팬에 기름을 두르고 가열합니다.  │
│  2. 김치를 넣고 볶아주세요.        │
│  ...                               │
│                                    │
│  영양 정보 (1인분 기준)             │  접힘/펼침
│  칼로리: 450kcal                   │
│                                    │
│  [       요리 시작하기 🍳         ] │  Primary Button
└────────────────────────────────────┘
```

### 12.9 마이페이지/설정

```
┌────────────────────────────────────┐
│ 마이페이지                       ⚙️│
├────────────────────────────────────┤
│  👤 김민지님                       │
│     minji@example.com              │
│     [프로필 수정]                  │
│                                    │
│  [12 재료수] [32 저장됨] [128 요리]│  통계 카드
│                                    │
│  🔔 알림 설정                    > │
│  🌙 다크 모드                    ○ │
│  🏠 식이 제한 설정               > │
│  📊 내 소비 통계                 > │
│  ♡ 즐겨찾기                    > │
│  ❓ 도움말                       > │
│  ℹ️ 버전 정보            v1.0.0  │
│  [로그아웃]                        │
├────────────────────────────────────┤
│  [홈] [냉장고] [레시피] [마이페이지]│
└────────────────────────────────────┘
```

---

## 13. 접근성 가이드라인

### 13.1 색상 대비율 (WCAG 2.1 AA)

| 조합 | 대비율 | 판정 |
|------|-------|------|
| Primary-500 on White | 4.62:1 | AA 통과 |
| Primary-600 on White | 5.91:1 | AA 통과 |
| On-Surface on Background | 18.2:1 | AAA 통과 |
| Error-Main on White | 5.82:1 | AA 통과 |
| Warning-Main on White | 3.68:1 | AA (대형 텍스트만) |

> Warning 텍스트는 14sp Bold 이상 필수

### 13.2 터치 타겟

- **최소**: 48 × 48dp
- **권장**: 56 × 56dp (주요 액션)
- 시각적 크기 < 48dp이더라도 터치 영역은 48dp 확보

### 13.3 스크린 리더

```kotlin
// 의미론적 레이블
Modifier.semantics {
    contentDescription = "당근, 500그램, 소비기한 2일 남음"
}

// 카드 전체를 하나의 단위로 그룹화
Modifier.semantics(mergeDescendants = true) {}
```

**콘텐츠 설명 패턴**:
- 재료 카드: "{재료명}, {수량}{단위}, 소비기한 {N}일 남음"
- 레시피 카드: "{레시피명}, {조리시간}분, {인원}인분, 별점 {N}점"
- 소비기한 배지: "소비기한 {N}일 남음" / "소비기한 만료"

### 13.4 모션 감소 모드

- `reduceMotion` 감지 시: 애니메이션 → 즉각 전환 (snap)
- 스켈레톤 shimmer → 단색 대체
- 파티클/스프링 효과 생략

### 13.5 텍스트 크기 조정

- 지원 배율: 85% ~ 200%
- sp 단위 사용 (dp가 아닌 sp로 폰트 크기)
- 200% 확대 시 레이아웃 깨짐 방지
- 최소 텍스트 크기: 12sp

---

## 14. 플랫폼별 고려사항

### Android

- Material You Dynamic Color: 옵션 제공 가능 (기본은 고정 팔레트)
- Predictive Back Animation 지원 (Android 14+)
- Haptic Feedback: 재료 추가 성공 시 CONFIRM, 삭제 시 REJECT

### iOS

- SF Symbols 사용 불가 (크로스플랫폼) → Material Symbols로 대체
- Safe Area: notch/Dynamic Island 자동 처리
- Live Activity: 조리 타이머 (Post-MVP)

### Web (Post-MVP)

- Pretendard Variable WOFF2 CDN, `font-display: swap`
- 모바일 우선 설계, 데스크탑 최대 1200dp
- Hover 상태 별도 정의

---

*본 문서는 냉장고 레시피 앱의 MVP 구현을 위한 디자인 시스템 기준 명세입니다. 구현 과정에서 발견되는 엣지 케이스는 이 원칙에 따라 일관되게 해결합니다.*
