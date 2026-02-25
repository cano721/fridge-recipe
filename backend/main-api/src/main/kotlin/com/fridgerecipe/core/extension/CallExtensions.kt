package com.fridgerecipe.core.extension

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*

fun RoutingCall.userId(): Long {
    val principal = principal<JWTPrincipal>()
        ?: throw IllegalStateException("No JWT principal found")
    return principal.payload.getClaim("userId").asLong()
}
