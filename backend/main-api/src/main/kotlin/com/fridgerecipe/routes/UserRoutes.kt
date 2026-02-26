package com.fridgerecipe.routes

import com.fridgerecipe.domain.service.UserService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
@Serializable
data class UpdateProfileRequest(val nickname: String? = null, val profileImage: String? = null)

@Serializable
data class UpdatePreferencesRequest(val dietaryPrefs: String)

@Serializable
data class UserResponse(
    val id: Long,
    val email: String?,
    val nickname: String,
    val profileImage: String?,
    val oauthProvider: String,
    val dietaryPrefs: String
)

fun Route.userRoutes(userService: UserService) {

    route("/users") {
        get("/me") {
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
            val user = userService.getUser(userId)
            call.respond(ApiSuccess(data = UserResponse(
                id = user.id,
                email = user.email,
                nickname = user.nickname,
                profileImage = user.profileImage,
                oauthProvider = user.oauthProvider,
                dietaryPrefs = user.dietaryPrefs
            )))
        }

        put("/me") {
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
            val request = call.receive<UpdateProfileRequest>()
            val user = userService.updateProfile(userId, request.nickname, request.profileImage)
            call.respond(ApiSuccess(data = UserResponse(
                id = user.id,
                email = user.email,
                nickname = user.nickname,
                profileImage = user.profileImage,
                oauthProvider = user.oauthProvider,
                dietaryPrefs = user.dietaryPrefs
            )))
        }

        put("/me/preferences") {
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
            val request = call.receive<UpdatePreferencesRequest>()
            userService.updatePreferences(userId, request.dietaryPrefs)
            call.respond(ApiSuccess(data = mapOf("message" to "선호도가 업데이트되었습니다.")))
        }

        delete("/me") {
            val userId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asLong()
            userService.deleteAccount(userId)
            call.respond(ApiSuccess(data = mapOf("message" to "회원 탈퇴가 완료되었습니다.")))
        }
    }
}
