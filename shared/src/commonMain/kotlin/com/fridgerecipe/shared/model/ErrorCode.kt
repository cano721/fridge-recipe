package com.fridgerecipe.shared.model

import kotlinx.serialization.Serializable

@Serializable
enum class ErrorCode(val httpStatus: Int, val message: String) {
    // Auth
    AUTH_INVALID_TOKEN(401, "유효하지 않은 토큰"),
    AUTH_TOKEN_EXPIRED(401, "만료된 토큰"),
    AUTH_OAUTH_FAILED(401, "OAuth 인증 실패"),
    AUTH_REFRESH_REVOKED(401, "폐기된 Refresh Token"),

    // Ingredient
    INGREDIENT_NOT_FOUND(404, "식재료를 찾을 수 없음"),
    INGREDIENT_DUPLICATE(409, "중복 식재료 (합산 필요)"),
    INGREDIENT_MASTER_NOT_FOUND(404, "마스터 DB에 없는 재료"),
    INGREDIENT_LIMIT_EXCEEDED(400, "사용자당 최대 200개 초과"),

    // Recipe
    RECIPE_NOT_FOUND(404, "레시피를 찾을 수 없음"),
    RECIPE_ALREADY_BOOKMARKED(409, "이미 즐겨찾기됨"),

    // Scan
    SCAN_NOT_FOUND(404, "스캔 결과 없음"),
    SCAN_PROCESSING(202, "처리 중 (폴링 필요)"),
    SCAN_FAILED(500, "스캔 처리 실패"),
    SCAN_DAILY_LIMIT(429, "일일 스캔 횟수 초과"),

    // User
    USER_NOT_FOUND(404, "사용자를 찾을 수 없음"),

    // Common
    VALIDATION_FAILED(400, "입력 검증 실패"),
    RATE_LIMIT_EXCEEDED(429, "요청 제한 초과"),
    INTERNAL_ERROR(500, "서버 내부 오류")
}
