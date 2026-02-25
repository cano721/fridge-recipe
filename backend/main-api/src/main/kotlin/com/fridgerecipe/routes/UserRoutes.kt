package com.fridgerecipe.routes

import com.fridgerecipe.core.extension.userId
import com.fridgerecipe.data.database.UsersTable
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

@Serializable
data class UpdateProfileRequest(
    val nickname: String? = null,
)

fun Route.userRoutes() {
    route("/users") {
        authenticate("auth-jwt") {
            get("/me") {
                val userId = call.userId()
                val user = transaction {
                    UsersTable.selectAll()
                        .where { UsersTable.id eq userId }
                        .firstOrNull()
                }
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf(
                        "success" to false,
                        "error" to mapOf("code" to "USER_NOT_FOUND", "message" to "사용자를 찾을 수 없습니다.")
                    ))
                    return@get
                }
                call.respond(HttpStatusCode.OK, mapOf(
                    "success" to true,
                    "data" to mapOf(
                        "id" to user[UsersTable.id].value,
                        "email" to user[UsersTable.email],
                        "nickname" to user[UsersTable.nickname],
                        "profileImage" to user[UsersTable.profileImage],
                    )
                ))
            }

            put("/me") {
                val userId = call.userId()
                val request = call.receive<UpdateProfileRequest>()
                transaction {
                    UsersTable.update({ UsersTable.id eq userId }) {
                        request.nickname?.let { name -> it[nickname] = name }
                    }
                }
                call.respond(HttpStatusCode.OK, mapOf("success" to true))
            }

            delete("/me") {
                val userId = call.userId()
                transaction {
                    UsersTable.deleteWhere { UsersTable.id eq userId }
                }
                call.respond(HttpStatusCode.OK, mapOf("success" to true))
            }
        }
    }
}
