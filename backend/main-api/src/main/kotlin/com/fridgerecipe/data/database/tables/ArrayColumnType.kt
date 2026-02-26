package com.fridgerecipe.data.database.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table

class TextArrayColumnType : ColumnType<List<String>>() {
    override fun sqlType() = "TEXT[]"
    override fun valueFromDB(value: Any): List<String> = when (value) {
        is java.sql.Array -> (value.array as Array<*>).filterNotNull().map { it.toString() }
        is Array<*> -> value.filterNotNull().map { it.toString() }
        else -> emptyList()
    }
    override fun notNullValueToDB(value: List<String>): Any = value.toTypedArray()
}

class LongArrayColumnType : ColumnType<List<Long>>() {
    override fun sqlType() = "BIGINT[]"
    override fun valueFromDB(value: Any): List<Long> = when (value) {
        is java.sql.Array -> (value.array as Array<*>).filterNotNull().map { (it as Number).toLong() }
        is Array<*> -> value.filterNotNull().map { (it as Number).toLong() }
        else -> emptyList()
    }
    override fun notNullValueToDB(value: List<Long>): Any = value.toTypedArray()
}

class IntArrayColumnType : ColumnType<List<Int>>() {
    override fun sqlType() = "INT[]"
    override fun valueFromDB(value: Any): List<Int> = when (value) {
        is java.sql.Array -> (value.array as Array<*>).filterNotNull().map { (it as Number).toInt() }
        is Array<*> -> value.filterNotNull().map { (it as Number).toInt() }
        else -> emptyList()
    }
    override fun notNullValueToDB(value: List<Int>): Any = value.toTypedArray()
}

fun Table.textArray(name: String): Column<List<String>> = registerColumn(name, TextArrayColumnType())
fun Table.longArray(name: String): Column<List<Long>> = registerColumn(name, LongArrayColumnType())
fun Table.intArray(name: String): Column<List<Int>> = registerColumn(name, IntArrayColumnType())
