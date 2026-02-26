package com.fridgerecipe.auth

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

interface OAuthClient {
    suspend fun getUserInfo(accessToken: String): OAuthUserInfo
}

class KakaoOAuthClient : OAuthClient {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
    }

    override suspend fun getUserInfo(accessToken: String): OAuthUserInfo {
        val response: JsonObject = client.get("https://kapi.kakao.com/v2/user/me") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()

        val id = response["id"]?.jsonPrimitive?.long ?: throw IllegalStateException("카카오 사용자 ID 없음")
        val kakaoAccount = response["kakao_account"]?.jsonObject
        val profile = kakaoAccount?.get("profile")?.jsonObject

        return OAuthUserInfo(
            provider = OAuthProvider.KAKAO,
            oauthId = id.toString(),
            email = kakaoAccount?.get("email")?.jsonPrimitive?.contentOrNull,
            nickname = profile?.get("nickname")?.jsonPrimitive?.content ?: "사용자",
            profileImage = profile?.get("profile_image_url")?.jsonPrimitive?.contentOrNull
        )
    }
}

class GoogleOAuthClient : OAuthClient {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
    }

    override suspend fun getUserInfo(accessToken: String): OAuthUserInfo {
        val response: JsonObject = client.get("https://www.googleapis.com/oauth2/v2/userinfo") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()

        return OAuthUserInfo(
            provider = OAuthProvider.GOOGLE,
            oauthId = response["id"]?.jsonPrimitive?.content ?: throw IllegalStateException("Google 사용자 ID 없음"),
            email = response["email"]?.jsonPrimitive?.contentOrNull,
            nickname = response["name"]?.jsonPrimitive?.content ?: "사용자",
            profileImage = response["picture"]?.jsonPrimitive?.contentOrNull
        )
    }
}

class AppleOAuthClient : OAuthClient {
    override suspend fun getUserInfo(accessToken: String): OAuthUserInfo {
        val parts = accessToken.split(".")
        if (parts.size != 3) throw IllegalArgumentException("유효하지 않은 Apple ID Token")

        val payload = String(java.util.Base64.getUrlDecoder().decode(parts[1]))
        val json = Json.parseToJsonElement(payload).jsonObject

        return OAuthUserInfo(
            provider = OAuthProvider.APPLE,
            oauthId = json["sub"]?.jsonPrimitive?.content ?: throw IllegalStateException("Apple 사용자 ID 없음"),
            email = json["email"]?.jsonPrimitive?.contentOrNull,
            nickname = json["email"]?.jsonPrimitive?.content?.substringBefore("@") ?: "사용자",
            profileImage = null
        )
    }
}
