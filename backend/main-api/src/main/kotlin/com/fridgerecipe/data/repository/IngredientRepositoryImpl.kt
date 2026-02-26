package com.fridgerecipe.data.repository

import com.fridgerecipe.data.database.tables.IngredientMaster
import com.fridgerecipe.data.database.tables.UserIngredients
import com.fridgerecipe.domain.model.UserIngredient
import com.fridgerecipe.domain.repository.IngredientRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class IngredientRepositoryImpl : IngredientRepository {
    private fun ResultRow.toUserIngredient() = UserIngredient(
        id = this[UserIngredients.id].value,
        userId = this[UserIngredients.userId],
        ingredientId = this[UserIngredients.ingredientId],
        ingredientName = this.getOrNull(IngredientMaster.name),
        category = this.getOrNull(IngredientMaster.category),
        iconUrl = this.getOrNull(IngredientMaster.iconUrl),
        quantity = this[UserIngredients.quantity],
        unit = this[UserIngredients.unit],
        expiryDate = this[UserIngredients.expiryDate],
        storageType = this[UserIngredients.storageType],
        memo = this[UserIngredients.memo],
        registeredVia = this[UserIngredients.registeredVia],
        expiredNotified = this[UserIngredients.expiredNotified],
        createdAt = this[UserIngredients.createdAt],
        updatedAt = this[UserIngredients.updatedAt]
    )

    private fun baseQuery() = UserIngredients.join(
        IngredientMaster, JoinType.LEFT,
        additionalConstraint = { UserIngredients.ingredientId eq IngredientMaster.id }
    )

    override suspend fun findByUserId(userId: Long, page: Int, size: Int): List<UserIngredient> = newSuspendedTransaction {
        baseQuery().selectAll()
            .where { UserIngredients.userId eq userId }
            .orderBy(UserIngredients.createdAt, SortOrder.DESC)
            .limit(size).offset(((page - 1) * size).toLong())
            .map { it.toUserIngredient() }
    }

    override suspend fun countByUserId(userId: Long): Long = newSuspendedTransaction {
        UserIngredients.selectAll().where { UserIngredients.userId eq userId }.count()
    }

    override suspend fun findById(id: Long): UserIngredient? = newSuspendedTransaction {
        baseQuery().selectAll().where { UserIngredients.id eq id }.singleOrNull()?.toUserIngredient()
    }

    override suspend fun findByUserAndIngredient(
        userId: Long,
        ingredientId: Long,
        storageType: String,
        expiryDate: String?
    ): UserIngredient? = newSuspendedTransaction {
        val baseCondition = (UserIngredients.userId eq userId) and
            (UserIngredients.ingredientId eq ingredientId) and
            (UserIngredients.storageType eq storageType)

        val condition = if (expiryDate == null) {
            baseCondition and UserIngredients.expiryDate.isNull()
        } else {
            baseCondition and (UserIngredients.expiryDate eq expiryDate)
        }

        UserIngredients.selectAll().where { condition }.singleOrNull()?.let {
            UserIngredient(
                id = it[UserIngredients.id].value,
                userId = it[UserIngredients.userId],
                ingredientId = it[UserIngredients.ingredientId],
                quantity = it[UserIngredients.quantity],
                unit = it[UserIngredients.unit],
                expiryDate = it[UserIngredients.expiryDate],
                storageType = it[UserIngredients.storageType],
                memo = it[UserIngredients.memo],
                registeredVia = it[UserIngredients.registeredVia],
                expiredNotified = it[UserIngredients.expiredNotified],
                createdAt = it[UserIngredients.createdAt],
                updatedAt = it[UserIngredients.updatedAt]
            )
        }
    }

    override suspend fun create(ingredient: UserIngredient): UserIngredient = newSuspendedTransaction {
        val id = UserIngredients.insertAndGetId {
            it[userId] = ingredient.userId
            it[ingredientId] = ingredient.ingredientId
            it[quantity] = ingredient.quantity
            it[unit] = ingredient.unit
            it[expiryDate] = ingredient.expiryDate
            it[storageType] = ingredient.storageType
            it[memo] = ingredient.memo
            it[registeredVia] = ingredient.registeredVia
        }
        ingredient.copy(id = id.value)
    }

    override suspend fun update(ingredient: UserIngredient): UserIngredient? = newSuspendedTransaction {
        UserIngredients.update({ UserIngredients.id eq ingredient.id }) {
            it[quantity] = ingredient.quantity
            it[unit] = ingredient.unit
            it[expiryDate] = ingredient.expiryDate
            it[storageType] = ingredient.storageType
            it[memo] = ingredient.memo
        }
        findById(ingredient.id)
    }

    override suspend fun delete(id: Long): Boolean = newSuspendedTransaction {
        UserIngredients.deleteWhere { UserIngredients.id eq id } > 0
    }

    override suspend fun deleteBatch(ids: List<Long>, userId: Long): Int = newSuspendedTransaction {
        UserIngredients.deleteWhere {
            (UserIngredients.id inList ids) and (UserIngredients.userId eq userId)
        }
    }

    override suspend fun findExpiring(userId: Long, daysAhead: Int): List<UserIngredient> = newSuspendedTransaction {
        val today = java.time.LocalDate.now().toString()
        val deadline = java.time.LocalDate.now().plusDays(daysAhead.toLong()).toString()
        baseQuery().selectAll().where {
            (UserIngredients.userId eq userId) and
            UserIngredients.expiryDate.isNotNull() and
            (UserIngredients.expiryDate greaterEq today) and
            (UserIngredients.expiryDate lessEq deadline)
        }.orderBy(UserIngredients.expiryDate, SortOrder.ASC)
            .map { it.toUserIngredient() }
    }
}
