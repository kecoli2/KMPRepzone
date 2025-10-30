package com.repzone.data.sample

import app.cash.sqldelight.db.SqlDriver
import com.repzone.core.util.extensions.now
import com.repzone.database.SyncModuleEntity
import com.repzone.database.runtime.batchDelete
import com.repzone.database.runtime.batchInsert
import com.repzone.database.runtime.batchUpdate
import com.repzone.database.runtime.count
import com.repzone.database.runtime.delete
import com.repzone.database.runtime.deleteAndNotify
import com.repzone.database.runtime.insert
import com.repzone.database.runtime.insertAndNotify
import com.repzone.database.runtime.maxLong
import com.repzone.database.runtime.minLong
import com.repzone.database.runtime.rawCount
import com.repzone.database.runtime.rawExecute
import com.repzone.database.runtime.rawQueryToEntity
import com.repzone.database.runtime.select
import com.repzone.database.runtime.selectAsFlow
import com.repzone.database.runtime.toPage
import com.repzone.database.runtime.transaction
import com.repzone.database.runtime.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class DatabaseTest(private val driver: SqlDriver) {

    fun runAllTests() {
        println("========================================")
        println("Starting Database Tests")
        println("========================================\n")

        // Cleanup before tests - TÜM TABLOYU TEMİZLE
        cleanupAll()

        try {
            // Run all tests
            testInsert()
            testBatchInsert()
            testSelect()
            testSelectWithWhere()
            testSelectWithComplexWhere()
            testSelectWithOrderBy()
            testSelectWithLimit()
            testSelectWithOffset()
            testSelectWithPagination()
            testUpdate()
            testBatchUpdate()
            testDelete()
            testBatchDelete()
            testDeleteWithCriteria()
            testCount()
            testCountWithFilter()
            testAggregates()
            testTransaction()
            testTransactionRollback()
            testGroupBy()
            testRawSql()
            testFlow()
            testBetweenOperator()
            testInOperator()
            testNotInOperator()
            testNotLikeOperator()
            testGreaterThanOrEqual()
            testLessThanOrEqual()
            testNotOperator()

            println("\n========================================")
            println("✅ All Tests Passed Successfully!")
            println("========================================")
        } catch (e: Exception) {
            println("\n========================================")
            println("❌ Test Failed: ${e.message}")
            println("========================================")
            e.printStackTrace()
        } finally {
            // Cleanup after tests
            cleanupAll()
        }
    }

    // ============================================
    // INSERT Tests
    // ============================================

    private fun testInsert() {
        println("TEST: Insert Single Entity")

        val entity = SyncModuleEntity(
            SyncType = 1,
            RequestUrl = "https://api.example.com/module1",
            RequestFilter = "filter1",
            LastSyncDate = now(),
            RequestType = 1,
            ModuleType = 1
        )

        val id = driver.insert(entity)
        println("✓ Inserted with ID: $id")

        // Verify
        val result = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", equal = 1)
            }
        }.firstOrNull()

        if (result == null) {
            throw AssertionError("Insert failed! Entity not found")
        }

        if (result.RequestUrl != entity.RequestUrl) {
            throw AssertionError("Insert failed! URL mismatch: expected ${entity.RequestUrl}, got ${result.RequestUrl}")
        }

        println("✓ Verified: ${result.RequestUrl}\n")
    }

    private fun testBatchInsert() {
        println("TEST: Batch Insert")

        val entities = listOf(
            SyncModuleEntity(10, "https://api.example.com/module10", "filter10", null, 1, 1),
            SyncModuleEntity(11, "https://api.example.com/module11", "filter11", null, 1, 2),
            SyncModuleEntity(12, "https://api.example.com/module12", null, null, 2, 1),
            SyncModuleEntity(13, "https://api.example.com/module13", null, null, 2, 2),
            SyncModuleEntity(14, "https://api.example.com/module14", "filter14", null, 3, 1)
        )

        val ids = driver.batchInsert(entities)
        println("✓ Batch inserted ${ids.size} entities")

        // Verify
        val count = driver.count<SyncModuleEntity> {
            where {
                criteria("SyncType", In = listOf(10, 11, 12, 13, 14))
            }
        }

        if (count != 5L) {
            throw AssertionError("Batch insert failed! Expected 5, got $count")
        }

        println("✓ Verified: $count entities\n")
    }

    // ============================================
    // SELECT Tests
    // ============================================

    private fun testSelect() {
        println("TEST: Select All")

        val results = driver.select<SyncModuleEntity>().toList()
        println("✓ Found ${results.size} entities")

        if (results.isEmpty()) {
            throw AssertionError("Select all failed! No entities found")
        }

        results.take(3).forEach { entity ->
            println("  - SyncType: ${entity.SyncType}, URL: ${entity.RequestUrl}")
        }
        println()
    }

    private fun testSelectWithWhere() {
        println("TEST: Select with WHERE")

        // Equal
        val result1 = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", equal = 1)
            }
        }.toList()
        println("✓ Equal: Found ${result1.size} with SyncType=1")

        if (result1.isEmpty()) {
            throw AssertionError("WHERE Equal failed! No entities found")
        }

        // Not Equal
        val result2 = driver.select<SyncModuleEntity> {
            where {
                criteria("RequestType", notEqual = 1)
            }
        }.toList()
        println("✓ Not Equal: Found ${result2.size} with RequestType!=1")

        // IS NULL
        val result3 = driver.select<SyncModuleEntity> {
            where {
                criteria("RequestFilter", isNull = true)
            }
        }.toList()
        println("✓ IS NULL: Found ${result3.size} with null RequestFilter")

        // IS NOT NULL
        val result4 = driver.select<SyncModuleEntity> {
            where {
                criteria("LastSyncDate", isNull = false)
            }
        }.toList()
        println("✓ IS NOT NULL: Found ${result4.size} with non-null LastSyncDate")

        // LIKE
        val result5 = driver.select<SyncModuleEntity> {
            where {
                criteria("RequestUrl", like = "%module1%")
            }
        }.toList()
        println("✓ LIKE: Found ${result5.size} with URL containing 'module1'\n")
    }

    private fun testSelectWithComplexWhere() {
        println("TEST: Select with Complex WHERE (AND/OR)")

        // AND
        val result1 = driver.select<SyncModuleEntity> {
            where {
                criteria("RequestType", equal = 1)
                criteria("ModuleType", equal = 1)
                criteria("RequestFilter", isNull = false)
            }
        }.toList()
        println("✓ AND: Found ${result1.size} entities")

        // OR
        val result2 = driver.select<SyncModuleEntity> {
            where {
                or {
                    criteria("SyncType", equal = 1)
                    criteria("SyncType", equal = 10)
                    criteria("SyncType", equal = 11)
                }
            }
        }.toList()
        println("✓ OR: Found ${result2.size} entities")

        if (result2.size < 3) {
            throw AssertionError("WHERE OR failed! Expected at least 3 entities, got ${result2.size}")
        }

        // Mixed AND/OR
        val result3 = driver.select<SyncModuleEntity> {
            where {
                criteria("RequestType", equal = 1)
                or {
                    criteria("ModuleType", equal = 1)
                    criteria("ModuleType", equal = 2)
                }
            }
        }.toList()
        println("✓ AND + OR: Found ${result3.size} entities\n")
    }

    private fun testSelectWithOrderBy() {
        println("TEST: Select with ORDER BY")

        // ASC
        val result1 = driver.select<SyncModuleEntity> {
            orderBy {
                order("SyncType", desc = false)
            }
            limit(5)
        }.toList()
        println("✓ ORDER BY ASC: ${result1.map { it.SyncType }}")

        // Verify ASC order
        for (i in 0 until result1.size - 1) {
            if (result1[i].SyncType > result1[i + 1].SyncType) {
                throw AssertionError("ORDER BY ASC failed! Not in ascending order")
            }
        }

        // DESC
        val result2 = driver.select<SyncModuleEntity> {
            orderBy {
                order("SyncType", desc = true)
            }
            limit(5)
        }.toList()
        println("✓ ORDER BY DESC: ${result2.map { it.SyncType }}")

        // Verify DESC order
        for (i in 0 until result2.size - 1) {
            if (result2[i].SyncType < result2[i + 1].SyncType) {
                throw AssertionError("ORDER BY DESC failed! Not in descending order")
            }
        }

        // Multiple columns
        val result3 = driver.select<SyncModuleEntity> {
            orderBy {
                order("RequestType", desc = false)
                order("ModuleType", desc = true)
            }
            limit(5)
        }.toList()
        println("✓ Multiple ORDER BY: Found ${result3.size} entities\n")
    }

    private fun testSelectWithLimit() {
        println("TEST: Select with LIMIT")

        val result = driver.select<SyncModuleEntity> {
            limit(3)
        }.toList()

        if (result.size > 3) {
            throw AssertionError("LIMIT failed! Expected max 3, got ${result.size}")
        }

        println("✓ LIMIT 3: Found ${result.size} entities\n")
    }

    private fun testSelectWithOffset() {
        println("TEST: Select with OFFSET")

        val result1 = driver.select<SyncModuleEntity> {
            orderBy {
                order("SyncType", desc = false)
            }
            limit(3)
            offset(0)
        }.toList()

        val result2 = driver.select<SyncModuleEntity> {
            orderBy {
                order("SyncType", desc = false)
            }
            limit(3)
            offset(3)
        }.toList()

        println("✓ OFFSET 0: ${result1.map { it.SyncType }}")
        println("✓ OFFSET 3: ${result2.map { it.SyncType }}")

        // Verify offset worked (no overlap)
        val overlapping = result1.map { it.SyncType }.intersect(result2.map { it.SyncType }.toSet())
        if (overlapping.isNotEmpty()) {
            throw AssertionError("OFFSET failed! Found overlapping items: $overlapping")
        }

        println()
    }

    private fun testSelectWithPagination() {
        println("TEST: Pagination")

        val page1 = driver.select<SyncModuleEntity> {
            orderBy {
                order("SyncType", desc = false)
            }
        }.toPage(pageNumber = 1, pageSize = 3)

        println("✓ Page 1: ${page1.items.size} items, hasMore: ${page1.hasMore}")

        if (page1.hasMore) {
            val page2 = driver.select<SyncModuleEntity> {
                orderBy {
                    order("SyncType", desc = false)
                }
            }.toPage(pageNumber = 2, pageSize = 3)

            println("✓ Page 2: ${page2.items.size} items, hasMore: ${page2.hasMore}")

            // Verify no overlap
            val overlapping = page1.items.map { it.SyncType }.intersect(page2.items.map { it.SyncType }.toSet())
            if (overlapping.isNotEmpty()) {
                throw AssertionError("Pagination failed! Found overlapping items: $overlapping")
            }
        }
        println()
    }

    // ============================================
    // UPDATE Tests
    // ============================================

    private fun testUpdate() {
        println("TEST: Update Single Entity")

        // Get entity
        val entity = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", equal = 1)
            }
        }.firstOrNull()

        if (entity == null) {
            throw AssertionError("Update test failed! Entity with SyncType=1 not found")
        }

        // Update
        val updated = entity.copy(
            RequestUrl = "https://updated.example.com/module1",
            LastSyncDate = now()
        )

        val affected = driver.update(updated)
        println("✓ Updated $affected row(s)")

        if (affected == 0) {
            throw AssertionError("Update failed! No rows affected")
        }

        // Verify
        val result = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", equal = 1)
            }
        }.firstOrNull()

        if (result?.RequestUrl != updated.RequestUrl) {
            throw AssertionError("Update verification failed! URL not updated")
        }

        println("✓ Verified: ${result.RequestUrl}\n")
    }

    private fun testBatchUpdate() {
        println("TEST: Batch Update")

        val entities = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", In = listOf(10, 11, 12))
            }
        }.toList()

        if (entities.isEmpty()) {
            throw AssertionError("Batch update test failed! No entities found")
        }

        val updated = entities.map { entity ->
            entity.copy(LastSyncDate = now())
        }

        val affected = driver.batchUpdate(updated)
        println("✓ Batch updated $affected row(s)")

        if (affected == 0) {
            throw AssertionError("Batch update failed! No rows affected")
        }

        println()
    }

    // ============================================
    // DELETE Tests
    // ============================================

    private fun testDelete() {
        println("TEST: Delete Single Entity")

        // Insert test entity
        val entity = SyncModuleEntity(
            SyncType = 999,
            RequestUrl = "https://to-delete.com",
            RequestFilter = null,
            LastSyncDate = null,
            RequestType = null,
            ModuleType = null
        )
        driver.insert(entity)

        // Delete
        val affected = driver.delete(entity)
        println("✓ Deleted $affected row(s)")

        if (affected == 0) {
            throw AssertionError("Delete failed! No rows affected")
        }

        // Verify
        val result = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", equal = 999)
            }
        }.firstOrNull()

        if (result != null) {
            throw AssertionError("Delete verification failed! Entity still exists")
        }

        println("✓ Verified: Entity deleted\n")
    }

    private fun testBatchDelete() {
        println("TEST: Batch Delete")

        // Insert test entities
        val entities = listOf(
            SyncModuleEntity(100, "https://delete1.com", null, null, null, null),
            SyncModuleEntity(101, "https://delete2.com", null, null, null, null),
            SyncModuleEntity(102, "https://delete3.com", null, null, null, null)
        )
        driver.batchInsert(entities)

        // Delete
        val affected = driver.batchDelete(entities)
        println("✓ Batch deleted $affected row(s)")

        if (affected != entities.size) {
            throw AssertionError("Batch delete failed! Expected ${entities.size} rows, got $affected")
        }

        println()
    }

    private fun testDeleteWithCriteria() {
        println("TEST: Delete with Criteria")

        // Insert test entities
        val entities = listOf(
            SyncModuleEntity(200, "https://criteria1.com", null, null, null, 99),
            SyncModuleEntity(201, "https://criteria2.com", null, null, null, 99),
            SyncModuleEntity(202, "https://criteria3.com", null, null, null, 99)
        )
        driver.batchInsert(entities)

        // Delete by criteria
        val affected = driver.delete<SyncModuleEntity> {
            where {
                criteria("ModuleType", equal = 99)
            }
        }

        println("✓ Deleted $affected row(s) with ModuleType=99")

        if (affected != entities.size) {
            throw AssertionError("Delete with criteria failed! Expected ${entities.size} rows, got $affected")
        }

        println()
    }

    // ============================================
    // AGGREGATE Tests
    // ============================================

    private fun testCount() {
        println("TEST: COUNT")

        val total = driver.count<SyncModuleEntity>()
        println("✓ Total count: $total")

        if (total == 0L) {
            throw AssertionError("COUNT failed! Expected > 0, got 0")
        }

        val filtered = driver.count<SyncModuleEntity> {
            where {
                criteria("RequestType", equal = 1)
            }
        }
        println("✓ Filtered count: $filtered\n")
    }

    private fun testCountWithFilter() {
        println("TEST: COUNT with Complex Filter")

        val count = driver.count<SyncModuleEntity> {
            where {
                criteria("RequestFilter", isNull = false)
                or {
                    criteria("RequestType", equal = 1)
                    criteria("RequestType", equal = 2)
                }
            }
        }

        println("✓ Complex filter count: $count\n")
    }

    private fun testAggregates() {
        println("TEST: Aggregate Functions")

        // COUNT
        val count = driver.count<SyncModuleEntity>()
        println("✓ COUNT: $count")

        // MAX
        val maxSyncType = driver.maxLong<SyncModuleEntity>("SyncType")
        println("✓ MAX SyncType: $maxSyncType")

        if (maxSyncType == null) {
            throw AssertionError("MAX failed! Returned null")
        }

        // MIN
        val minSyncType = driver.minLong<SyncModuleEntity>("SyncType")
        println("✓ MIN SyncType: $minSyncType")

        if (minSyncType == null) {
            throw AssertionError("MIN failed! Returned null")
        }

        if (maxSyncType < minSyncType) {
            throw AssertionError("Aggregate verification failed! MAX < MIN")
        }

        println()
    }

    // ============================================
    // TRANSACTION Tests
    // ============================================

    private fun testTransaction() {
        println("TEST: Transaction")

        driver.transaction {
            val entity1 = SyncModuleEntity(300, "https://trans1.com", null, null, null, null)
            val entity2 = SyncModuleEntity(301, "https://trans2.com", null, null, null, null)

            insert(entity1)
            insert(entity2)
        }

        // Verify
        val count = driver.count<SyncModuleEntity> {
            where {
                criteria("SyncType", In = listOf(300, 301))
            }
        }

        if (count != 2L) {
            throw AssertionError("Transaction failed! Expected 2 entities, got $count")
        }

        println("✓ Transaction committed: $count entities\n")
    }

    private fun testTransactionRollback() {
        println("TEST: Transaction Rollback")

        try {
            driver.transaction {
                val entity = SyncModuleEntity(400, "https://rollback.com", null, null, null, null)
                insert(entity)

                // Force error
                throw Exception("Test rollback")
            }
        } catch (e: Exception) {
            println("✓ Transaction rolled back: ${e.message}")
        }

        // Verify rollback
        val result = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", equal = 400)
            }
        }.firstOrNull()

        if (result != null) {
            throw AssertionError("Rollback verification failed! Entity should not exist")
        }

        println("✓ Verified: Entity not inserted\n")
    }

    // ============================================
    // GROUP BY Tests
    // ============================================

    private fun testGroupBy() {
        println("TEST: GROUP BY")

        val results = driver.select<SyncModuleEntity> {
            groupBy {
                groupBy("RequestType")
            }
        }.toList()

        println("✓ GROUP BY RequestType: ${results.size} groups\n")
    }

    // ============================================
    // RAW SQL Tests
    // ============================================

    private fun testRawSql() {
        println("TEST: Raw SQL")

        // Raw query to entity
        val results = driver.rawQueryToEntity<SyncModuleEntity>(
            "SELECT * FROM SyncModuleEntity WHERE RequestType = ? LIMIT ?",
            1, 5
        )
        println("✓ Raw query: Found ${results.size} entities")

        // Raw count
        val count = driver.rawCount(
            "SELECT COUNT(*) FROM SyncModuleEntity WHERE RequestFilter IS NOT NULL"
        )
        println("✓ Raw count: $count\n")
    }

    // ============================================
    // FLOW Tests
    // ============================================

    private fun testFlow() = runBlocking {
        println("TEST: Flow")

        // Insert with notification
        val entity = SyncModuleEntity(500, "https://flow.com", null, null, null, null)
        driver.insertAndNotify(entity)

        // Get flow
        val results = driver.selectAsFlow<SyncModuleEntity> {
            where {
                criteria("SyncType", equal = 500)
            }
        }.first()

        println("✓ Flow: Found ${results.size} entities")

        if (results.isEmpty()) {
            throw AssertionError("Flow failed! No entities found")
        }

        // Cleanup
        driver.deleteAndNotify(entity)
        println("✓ Flow cleanup completed\n")
    }

    // ============================================
    // NEW OPERATOR Tests
    // ============================================

    private fun testBetweenOperator() {
        println("TEST: BETWEEN Operator")

        val results = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", between = 10 to 14)
            }
        }.toList()

        println("✓ BETWEEN 10 AND 14: Found ${results.size} entities")
        println("  SyncTypes: ${results.map { it.SyncType }}")

        // Verify all results are in range
        results.forEach { entity ->
            if (entity.SyncType < 10 || entity.SyncType > 14) {
                throw AssertionError("BETWEEN failed! SyncType ${entity.SyncType} is out of range")
            }
        }

        println()
    }

    private fun testInOperator() {
        println("TEST: IN Operator")

        val results = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", In = listOf(1, 10, 11))
            }
        }.toList()

        println("✓ IN (1, 10, 11): Found ${results.size} entities")

        if (results.size != 3) {
            throw AssertionError("IN operator failed! Expected 3 entities, got ${results.size}")
        }

        println()
    }

    private fun testNotInOperator() {
        println("TEST: NOT IN Operator")

        val allCount = driver.count<SyncModuleEntity>()

        val results = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", notIn = listOf(1, 10, 11, 12, 13, 14))
            }
        }.toList()

        println("✓ NOT IN (1,10,11,12,13,14): Found ${results.size} of $allCount entities")

        // Verify no excluded IDs in results
        results.forEach { entity ->
            if (entity.SyncType in listOf(1L, 10L, 11L, 12L, 13L, 14L)) {
                throw AssertionError("NOT IN failed! Found excluded SyncType: ${entity.SyncType}")
            }
        }

        println()
    }

    private fun testNotLikeOperator() {
        println("TEST: NOT LIKE Operator")

        val results = driver.select<SyncModuleEntity> {
            where {
                criteria("RequestUrl", notLike = "%module1%")
            }
        }.toList()

        println("✓ NOT LIKE '%module1%': Found ${results.size} entities")

        // Verify no results contain "module1"
        results.forEach { entity ->
            if (entity.RequestUrl.contains("module1", ignoreCase = true)) {
                throw AssertionError("NOT LIKE failed! Found 'module1' in URL: ${entity.RequestUrl}")
            }
        }

        println()
    }

    private fun testGreaterThanOrEqual() {
        println("TEST: >= Operator")

        val results = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", greaterThanOrEqual = 10)
            }
        }.toList()

        println("✓ SyncType >= 10: Found ${results.size} entities")

        // Verify all >= 10
        results.forEach { entity ->
            if (entity.SyncType < 10) {
                throw AssertionError(">= failed! SyncType ${entity.SyncType} is < 10")
            }
        }

        println()
    }

    private fun testLessThanOrEqual() {
        println("TEST: <= Operator")

        val results = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", lessThanOrEqual = 14)
            }
        }.toList()

        println("✓ SyncType <= 14: Found ${results.size} entities")

        // Verify all <= 14
        results.forEach { entity ->
            if (entity.SyncType > 14) {
                throw AssertionError("<= failed! SyncType ${entity.SyncType} is > 14")
            }
        }

        println()
    }

    private fun testNotOperator() {
        println("TEST: NOT Operator")

        val results = driver.select<SyncModuleEntity> {
            where {
                not {
                    criteria("RequestType", equal = 1)
                    criteria("ModuleType", equal = 1)
                }
            }
        }.toList()

        println("✓ NOT (RequestType=1 AND ModuleType=1): Found ${results.size} entities")

        // Verify NOT condition
        results.forEach { entity ->
            if (entity.RequestType == 1L && entity.ModuleType == 1L) {
                throw AssertionError("NOT operator failed! Found entity with RequestType=1 AND ModuleType=1")
            }
        }

        println()
    }

    // ============================================
    // CLEANUP
    // ============================================

    private fun cleanupAll() {
        try {
            driver.rawExecute("DELETE FROM SyncModuleEntity")
            val remaining = driver.count<SyncModuleEntity>()
            if (remaining > 0) {
                throw AssertionError("Cleanup failed! ${remaining} entities still remain")
            }
            println("✓ Table cleaned: All entities deleted\n")
        } catch (e: Exception) {
            println("✗ Cleanup failed: ${e.message}\n")
            throw e
        }
    }
}

// Extension for button trigger
fun runDatabaseTests(driver: SqlDriver) {
    val test = DatabaseTest(driver)
    test.runAllTests()
}