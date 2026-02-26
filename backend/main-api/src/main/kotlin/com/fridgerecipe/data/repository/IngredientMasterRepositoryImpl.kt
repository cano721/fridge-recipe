package com.fridgerecipe.data.repository

import com.fridgerecipe.data.database.tables.IngredientMaster
import com.fridgerecipe.domain.model.IngredientMasterItem
import com.fridgerecipe.domain.repository.IngredientMasterRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class IngredientMasterRepositoryImpl : IngredientMasterRepository {
    private fun ResultRow.toItem() = IngredientMasterItem(
        id = this[IngredientMaster.id].value,
        name = this[IngredientMaster.name],
        category = this[IngredientMaster.category],
        iconUrl = this[IngredientMaster.iconUrl],
        defaultUnit = this[IngredientMaster.defaultUnit],
        defaultExpiryDays = this[IngredientMaster.defaultExpiryDays],
        aliases = this[IngredientMaster.aliases]
    )

    override suspend fun findById(id: Long): IngredientMasterItem? = newSuspendedTransaction {
        IngredientMaster.selectAll().where { IngredientMaster.id eq id }.singleOrNull()?.toItem()
    }

    override suspend fun searchByName(query: String, limit: Int): List<IngredientMasterItem> = newSuspendedTransaction {
        // 1단계: 정확 이름 매칭
        val exactMatches = IngredientMaster.selectAll()
            .where { IngredientMaster.name eq query }
            .map { it.toItem() }
        if (exactMatches.isNotEmpty()) return@newSuspendedTransaction exactMatches.take(limit)

        // 2단계: alias 배열 포함 검색 (SQL injection 방지: PreparedStatement 파라미터 사용)
        val aliasMatches = mutableListOf<IngredientMasterItem>()
        exec(
            "SELECT id, name, category, icon_url, default_unit, default_expiry_days, aliases FROM ingredient_master WHERE ? = ANY(aliases) LIMIT ?",
            args = listOf(
                Pair(VarCharColumnType(), query),
                Pair(IntegerColumnType(), limit)
            )
        ) { rs ->
            while (rs.next()) {
                aliasMatches.add(
                    IngredientMasterItem(
                        id = rs.getLong("id"),
                        name = rs.getString("name"),
                        category = rs.getString("category"),
                        iconUrl = rs.getString("icon_url"),
                        defaultUnit = rs.getString("default_unit"),
                        defaultExpiryDays = rs.getInt("default_expiry_days").takeIf { !rs.wasNull() },
                        aliases = (rs.getArray("aliases")?.array as? Array<*>)
                            ?.mapNotNull { it?.toString() } ?: emptyList()
                    )
                )
            }
        }
        if (aliasMatches.isNotEmpty()) return@newSuspendedTransaction aliasMatches

        // 3단계: pg_trgm 유사도 검색 (SQL injection 방지: PreparedStatement 파라미터 사용)
        val trgmMatches = mutableListOf<IngredientMasterItem>()
        exec(
            "SELECT id, name, category, icon_url, default_unit, default_expiry_days, aliases FROM ingredient_master WHERE similarity(name, ?) > 0.3 ORDER BY similarity(name, ?) DESC LIMIT ?",
            args = listOf(
                Pair(VarCharColumnType(), query),
                Pair(VarCharColumnType(), query),
                Pair(IntegerColumnType(), limit)
            )
        ) { rs ->
            while (rs.next()) {
                trgmMatches.add(
                    IngredientMasterItem(
                        id = rs.getLong("id"),
                        name = rs.getString("name"),
                        category = rs.getString("category"),
                        iconUrl = rs.getString("icon_url"),
                        defaultUnit = rs.getString("default_unit"),
                        defaultExpiryDays = rs.getInt("default_expiry_days").takeIf { !rs.wasNull() },
                        aliases = (rs.getArray("aliases")?.array as? Array<*>)
                            ?.mapNotNull { it?.toString() } ?: emptyList()
                    )
                )
            }
        }
        trgmMatches
    }

    override suspend fun findCategories(): List<String> = newSuspendedTransaction {
        IngredientMaster.select(IngredientMaster.category)
            .withDistinct()
            .orderBy(IngredientMaster.category, SortOrder.ASC)
            .map { it[IngredientMaster.category] }
    }
}
