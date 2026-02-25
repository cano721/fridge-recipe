package com.fridgerecipe.shared.data.remote.api

object ApiRoutes {
    private const val BASE = "/api/v1"

    object Auth {
        const val LOGIN = "$BASE/auth/login"
        const val REFRESH = "$BASE/auth/refresh"
        const val LOGOUT = "$BASE/auth/logout"
    }

    object Users {
        const val ME = "$BASE/users/me"
        const val PREFERENCES = "$BASE/users/me/preferences"
    }

    object Ingredients {
        const val ROOT = "$BASE/ingredients"
        const val BATCH = "$BASE/ingredients/batch"
        const val SEARCH = "$BASE/ingredients/search"
        const val CATEGORIES = "$BASE/ingredients/categories"
        const val EXPIRING = "$BASE/ingredients/expiring"
        fun byId(id: Long) = "$BASE/ingredients/$id"
    }

    object Scan {
        const val RECEIPT = "$BASE/scan/receipt"
        const val PHOTO = "$BASE/scan/photo"
        fun receiptResult(scanId: Long) = "$BASE/scan/receipt/$scanId"
        fun photoResult(scanId: Long) = "$BASE/scan/photo/$scanId"
    }

    object Recipes {
        const val RECOMMEND = "$BASE/recipes/recommend"
        const val SEARCH = "$BASE/recipes/search"
        const val BOOKMARKS = "$BASE/recipes/bookmarks"
        fun byId(id: Long) = "$BASE/recipes/$id"
        fun bookmark(id: Long) = "$BASE/recipes/$id/bookmark"
    }

    object Notifications {
        const val SETTINGS = "$BASE/notifications/settings"
        const val DEVICE_TOKEN = "$BASE/notifications/device-token"
    }
}
