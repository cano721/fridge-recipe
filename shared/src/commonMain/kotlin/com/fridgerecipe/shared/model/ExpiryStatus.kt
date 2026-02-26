package com.fridgerecipe.shared.model

import kotlinx.serialization.Serializable

@Serializable
enum class ExpiryStatus {
    SAFE,       // D-4 이상
    SOON,       // D-2 ~ D-3
    URGENT,     // D-0 ~ D-1
    EXPIRED     // 만료
}
