package com.fridgerecipe.shared.util

object Validation {
    const val MAX_INGREDIENTS_PER_USER = 200
    const val MAX_BATCH_SIZE = 50
    const val MAX_MEMO_LENGTH = 200
    const val MAX_NICKNAME_LENGTH = 50

    fun isValidNickname(nickname: String): Boolean =
        nickname.isNotBlank() && nickname.length <= MAX_NICKNAME_LENGTH

    fun isValidQuantity(quantity: Double?): Boolean =
        quantity == null || quantity > 0

    fun isValidMemo(memo: String?): Boolean =
        memo == null || memo.length <= MAX_MEMO_LENGTH
}
