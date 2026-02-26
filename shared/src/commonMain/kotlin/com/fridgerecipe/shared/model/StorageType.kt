package com.fridgerecipe.shared.model

import kotlinx.serialization.Serializable

@Serializable
enum class StorageType {
    FRIDGE,
    FREEZER,
    ROOM
}
