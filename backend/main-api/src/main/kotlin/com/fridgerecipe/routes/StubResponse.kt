package com.fridgerecipe.routes

import kotlinx.serialization.Serializable

@Serializable
data class StubResponse(val success: Boolean = false, val error: StubError)

@Serializable
data class StubError(val code: String = "NOT_IMPLEMENTED", val message: String = "Sprint 3에서 구현 예정")

val notImplemented = StubResponse(error = StubError())
