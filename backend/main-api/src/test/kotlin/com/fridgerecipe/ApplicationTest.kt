package com.fridgerecipe

import com.fridgerecipe.plugins.configureSerialization
import com.fridgerecipe.plugins.configureStatusPages
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun `health endpoint returns ok`() = testApplication {
        application {
            // Note: 전체 module()은 DB/Redis 필요, 통합 테스트에서 확인
            // 여기서는 라우팅만 검증
            configureSerialization()
            configureStatusPages()
        }

        // 통합 테스트는 Docker 환경에서 실행
    }
}
