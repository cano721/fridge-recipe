# Fridge Recipe - 냉장고 레시피 추천 앱

## 프로젝트 개요

냉장고에 등록된 재료를 기반으로 최적의 레시피를 추천해주는 크로스플랫폼 앱.
영수증 사진이나 식재료 사진을 통해 손쉽게 재료를 등록하고, AI 기반 추천 엔진이 맞춤 레시피를 제안합니다.

## 핵심 기능

- **식재료 관리**: 수동 등록, 영수증 OCR, 사진 인식을 통한 재료 등록
- **레시피 추천**: 보유 재료 기반 매칭률 계산 및 최적 레시피 추천
- **유통기한 관리**: 만료 임박 알림, 유통기한 기반 우선 추천
- **스마트 쇼핑**: 부족 재료 자동 장보기 목록 생성

## 기술 스택 요약

| 계층 | 기술 | 비고 |
|------|------|------|
| **모바일** | Kotlin Multiplatform + Compose Multiplatform | iOS/Android 공유 |
| **메인 백엔드** | Kotlin + Ktor | 인증, CRUD, 비즈니스 로직 |
| **AI 마이크로서비스** | Python + FastAPI | OCR, 이미지 인식, 추천 엔진 |
| **데이터베이스** | PostgreSQL 16 | JSONB, pg_trgm |
| **캐시/브로커** | Redis 7 | 캐시 + 메시지 브로커 |
| **영수증 OCR** | Naver Clova OCR | 한국 영수증 특화 |
| **식재료 인식** | OpenAI GPT-4o Vision | 다품목 인식 |
| **인프라** | AWS (ECS Fargate, RDS, S3) | 서울 리전 |

## 설계 문서 목록

| 문서 | 설명 |
|------|------|
| [01-architecture.md](./01-architecture.md) | 시스템 아키텍처 설계 |
| [02-prd-analysis.md](./02-prd-analysis.md) | 요구사항 분석 및 PRD |
| [03-design-system.md](./03-design-system.md) | 디자인 시스템 스펙 |

## MVP 목표

- **기간**: 3-4개월 (14스프린트)
- **플랫폼**: iOS + Android (모바일 우선, 웹은 Post-MVP)
- **핵심**: 수동 등록 + 영수증 OCR + 레시피 추천 + 유통기한 알림
