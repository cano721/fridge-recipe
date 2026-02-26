package com.fridgerecipe.data.repository

import com.fridgerecipe.data.database.tables.ScanHistory
import com.fridgerecipe.domain.model.ScanItem
import com.fridgerecipe.domain.model.ScanResult
import com.fridgerecipe.domain.repository.ScanRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ScanRepositoryImpl : ScanRepository {

    private val json = Json { ignoreUnknownKeys = true }

    private fun ResultRowToScanResult(row: org.jetbrains.exposed.sql.ResultRow): ScanResult {
        val resultJson: JsonObject? = row[ScanHistory.result]
        val items: List<ScanItem>? = resultJson?.get("items")?.jsonArray?.map { element ->
            val obj = element.jsonObject
            ScanItem(
                name = obj["name"]?.jsonPrimitive?.content ?: "",
                quantity = obj["quantity"]?.jsonPrimitive?.content?.toDoubleOrNull(),
                unit = obj["unit"]?.jsonPrimitive?.content,
                confidence = obj["confidence"]?.jsonPrimitive?.content?.toDoubleOrNull() ?: 0.0
            )
        }
        val errorMessage: String? = resultJson?.get("errorMessage")?.jsonPrimitive?.content

        return ScanResult(
            id = row[ScanHistory.id].value,
            userId = row[ScanHistory.userId],
            scanType = row[ScanHistory.scanType],
            imageUrl = row[ScanHistory.imageUrl],
            status = row[ScanHistory.status],
            items = items,
            errorMessage = errorMessage,
            createdAt = row[ScanHistory.createdAt]
        )
    }

    override suspend fun create(userId: Long, scanType: String, imageUrl: String?): Long =
        newSuspendedTransaction {
            ScanHistory.insert {
                it[ScanHistory.userId] = userId
                it[ScanHistory.scanType] = scanType
                it[ScanHistory.imageUrl] = imageUrl
                it[ScanHistory.status] = "processing"
            }[ScanHistory.id].value
        }

    override suspend fun findById(id: Long): ScanResult? =
        newSuspendedTransaction {
            ScanHistory.selectAll()
                .where { ScanHistory.id eq id }
                .singleOrNull()
                ?.let { ResultRowToScanResult(it) }
        }

    override suspend fun findByUserId(userId: Long, limit: Int): List<ScanResult> =
        newSuspendedTransaction {
            ScanHistory.selectAll()
                .where { ScanHistory.userId eq userId }
                .limit(limit)
                .map { ResultRowToScanResult(it) }
        }

    override suspend fun updateStatus(
        id: Long,
        status: String,
        items: List<ScanItem>?,
        errorMessage: String?
    ) = newSuspendedTransaction {
        val resultJson: JsonObject? = if (items != null || errorMessage != null) {
            buildJsonObject {
                if (items != null) {
                    put("items", json.parseToJsonElement(Json.encodeToString(items)))
                }
                if (errorMessage != null) {
                    put("errorMessage", errorMessage)
                }
            }
        } else null

        ScanHistory.update({ ScanHistory.id eq id }) {
            it[ScanHistory.status] = status
            if (resultJson != null) {
                it[ScanHistory.result] = resultJson
            }
        }
        Unit
    }
}
