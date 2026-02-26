package com.fridgerecipe.core.security

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

val ApplicationCall.userId: Long
    get() = principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()

val ApplicationCall.userIdOrNull: Long?
    get() = principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asLong()
