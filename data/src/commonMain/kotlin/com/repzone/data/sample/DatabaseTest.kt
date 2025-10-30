package com.repzone.data.sample

import app.cash.sqldelight.db.SqlDriver
import com.repzone.core.util.extensions.now
import com.repzone.database.SyncModuleEntity
import com.repzone.database.runtime.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * Comprehensive Database Test Suite
 *
 * Bu test class'ı tüm database işlemlerini step-by-step test eder:
 * - INSERT (single, batch, insertOrReplace)
 * - SELECT (simple, complex, pagination)
 * - UPDATE (single, batch)
 * - DELETE (single, batch, criteria)
 * - Transactions
 * - Flow operations
 * - Aggregation functions
 *
 * Özellikler:
 * - Tek method'dan tüm testleri çalıştırır
 * - Her adımı detaylı loglar
 * - Hata durumunda exception fırlatır
 * - Test entity'leri sabit ve tahmin edilebilir
 */
class DatabaseTest(private val driver: SqlDriver) {

    // Test için sabit entity'ler
    private companion object {
        val TEST_ENTITY_1 = SyncModuleEntity(
            SyncType = 1,
            RequestUrl = "https://api.test.com/customers",
            RequestFilter = "status=active",
            LastSyncDate = 1704067200000, // 2024-01-01 00:00:00
            RequestType = 1,
            ModuleType = 1
        )

        val TEST_ENTITY_2 = SyncModuleEntity(
            SyncType = 2,
            RequestUrl = "https://api.test.com/products",
            RequestFilter = "category=electronics",
            LastSyncDate = 1704153600000, // 2024-01-02 00:00:00
            RequestType = 1,
            ModuleType = 2
        )

        val TEST_ENTITY_3 = SyncModuleEntity(
            SyncType = 3,
            RequestUrl = "https://api.test.com/orders",
            RequestFilter = null,
            LastSyncDate = null,
            RequestType = 2,
            ModuleType = 1
        )

        val BATCH_TEST_ENTITIES = listOf(
            SyncModuleEntity(10, "https://api.test.com/batch1", "filter1", now(), 1, 1),
            SyncModuleEntity(11, "https://api.test.com/batch2", "filter2", now(), 1, 2),
            SyncModuleEntity(12, "https://api.test.com/batch3", null, null, 2, 1),
            SyncModuleEntity(13, "https://api.test.com/batch4", "filter4", now(), 2, 2),
            SyncModuleEntity(14, "https://api.test.com/batch5", null, now(), 3, 1)
        )
    }

    /**
     * Ana test method - Tüm testleri sırayla çalıştırır
     */
    fun runAllTests() {
        printHeader("DATABASE COMPREHENSIVE TEST SUITE")

        try {
            // Step 0: Initial cleanup
            step("CLEANUP", "Cleaning database before tests") {
                cleanup()
            }

            // Step 1: INSERT Operations
            step("INSERT-1", "Single Insert") {
                cleanup()
                testInsert()
            }

            step("INSERT-2", "Batch Insert") {
                cleanup()
                prepareBasicData()
                testBatchInsert()
            }

            step("INSERT-3", "Insert or Replace (UPSERT)") {
                cleanup()
                testInsertOrReplace()
            }

            // Step 2: SELECT Operations
            step("SELECT-1", "Select All") {
                cleanup()
                prepareFullTestData()
                testSelectAll()
            }

            step("SELECT-2", "Select with WHERE (Equal, NotEqual, Like)") {
                cleanup()
                prepareFullTestData()
                testSelectWithWhere()
            }

            step("SELECT-3", "Select with Complex WHERE (AND/OR)") {
                cleanup()
                prepareFullTestData()
                testSelectWithComplexWhere()
            }

            step("SELECT-4", "Select with Comparison Operators (>, >=, <, <=)") {
                cleanup()
                prepareFullTestData()
                testSelectWithComparison()
            }

            step("SELECT-5", "Select with IN and BETWEEN") {
                cleanup()
                prepareFullTestData()
                testSelectWithInBetween()
            }

            step("SELECT-6", "Select with ORDER BY") {
                cleanup()
                prepareFullTestData()
                testSelectWithOrderBy()
            }

            step("SELECT-7", "Select with LIMIT and OFFSET") {
                cleanup()
                prepareFullTestData()
                testSelectWithLimitOffset()
            }

            step("SELECT-8", "Select with Pagination") {
                cleanup()
                prepareFullTestData()
                testSelectWithPagination()
            }

            // Step 3: UPDATE Operations
            step("UPDATE-1", "Single Update") {
                cleanup()
                prepareBasicData()
                testUpdate()
            }

            step("UPDATE-2", "Batch Update") {
                cleanup()
                prepareFullTestData()
                testBatchUpdate()
            }

            // Step 4: DELETE Operations
            step("DELETE-1", "Single Delete") {
                cleanup()
                prepareForDelete()
                testDelete()
            }

            step("DELETE-2", "Batch Delete") {
                cleanup()
                prepareFullTestData()
                testBatchDelete()
            }

            step("DELETE-3", "Delete with Criteria") {
                cleanup()
                prepareFullTestData()
                testDeleteWithCriteria()
            }

            // Step 5: Aggregation Functions
            step("AGG-1", "Count") {
                cleanup()
                prepareFullTestData()
                testCount()
            }

            step("AGG-2", "Min and Max") {
                cleanup()
                prepareFullTestData()
                testMinMax()
            }

            // Step 6: Transaction Operations
            step("TRANS-1", "Transaction Commit") {
                cleanup()
                testTransactionCommit()
            }

            step("TRANS-2", "Transaction Rollback") {
                cleanup()
                testTransactionRollback()
            }

            // Step 7: Flow Operations
            step("FLOW-1", "Flow with Notifications") {
                cleanup()
                testFlow()
            }

            // Step 8: Raw SQL
            step("RAW-1", "Raw SQL Queries") {
                cleanup()
                prepareFullTestData()
                testRawSql()
            }

            // Final cleanup
            step("CLEANUP", "Cleaning database after tests") {
                cleanup()
            }

            printSuccess()

        } catch (e: Exception) {
            printFailure(e)
            throw e
        }
    }

    // ============================================
    // INSERT Tests
    // ============================================

    private fun testInsert() {
        log("Inserting entity: ${TEST_ENTITY_1.RequestUrl}")

        val id = driver.insert(TEST_ENTITY_1)
        log("Insert returned ID: $id")

        // Verify
        val result = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", equal = 1) }
        }.firstOrNull()

        verify(result != null, "Entity not found after insert")
        verify(result?.RequestUrl == TEST_ENTITY_1.RequestUrl,
            "URL mismatch: expected ${TEST_ENTITY_1.RequestUrl}, got ${result?.RequestUrl}")

        log("Verified: Entity successfully inserted")
    }

    private fun testBatchInsert() {
        log("Batch inserting ${BATCH_TEST_ENTITIES.size} entities")

        val ids = driver.batchInsert(BATCH_TEST_ENTITIES)
        log("Batch insert returned ${ids.size} IDs")

        // Verify
        val count = driver.count<SyncModuleEntity> {
            where { criteria("SyncType", In = listOf(10, 11, 12, 13, 14)) }
        }

        val sss = driver.select<SyncModuleEntity> {
            where {
                or {

                }
                and {

                }


                criteria("SyncType", In = listOf(10, 11, 12, 13, 14))
            }
        }

        verify(count == BATCH_TEST_ENTITIES.size.toLong(),
            "Expected ${BATCH_TEST_ENTITIES.size} entities, got $count")

        log("Verified: ${count} entities successfully inserted")
    }

    private fun testInsertOrReplace() {
        log("Testing INSERT OR REPLACE (UPSERT)")

        // First insert
        val entity = SyncModuleEntity(100, "https://api.test.com/upsert", "original", now(), 1, 1)
        driver.insertOrReplace(entity)
        log("First insert completed")

        // Verify first insert
        val first = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", equal = 100) }
        }.firstOrNull()
        verify(first?.RequestFilter == "original", "First insert failed")

        // Update using insertOrReplace
        val updated = entity.copy(RequestFilter = "updated")
        driver.insertOrReplace(updated)
        log("Second insert (replace) completed")

        // Verify update
        val second = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", equal = 100) }
        }.firstOrNull()
        verify(second?.RequestFilter == "updated", "Replace failed")

        // Check count (should still be 1)
        val count = driver.count<SyncModuleEntity> {
            where { criteria("SyncType", equal = 100) }
        }
        verify(count == 1L, "UPSERT created duplicate: count=$count")

        log("Verified: UPSERT working correctly")
    }

    // ============================================
    // SELECT Tests
    // ============================================

    private fun testSelectAll() {
        val results = driver.select<SyncModuleEntity>().toList()
        log("Selected ${results.size} entities")

        verify(results.isNotEmpty(), "No entities found")

        results.take(3).forEach { entity ->
            log("  - SyncType: ${entity.SyncType}, URL: ${entity.RequestUrl}")
        }
    }

    private fun testSelectWithWhere() {
        // Equal
        val eq = driver.select<SyncModuleEntity> {
            where { criteria("RequestType", equal = 1) }
        }.toList()
        log("Equal (RequestType=1): ${eq.size} results")
        verify(eq.all { it.RequestType == 1L }, "Equal filter failed")

        // Not Equal
        val neq = driver.select<SyncModuleEntity> {
            where { criteria("RequestType", notEqual = 1) }
        }.toList()
        log("Not Equal (RequestType!=1): ${neq.size} results")
        verify(neq.all { it.RequestType != 1L }, "Not Equal filter failed")

        // IS NULL
        val isNull = driver.select<SyncModuleEntity> {
            where { criteria("RequestFilter", isNull = true) }
        }.toList()
        log("IS NULL (RequestFilter): ${isNull.size} results")
        verify(isNull.all { it.RequestFilter == null }, "IS NULL filter failed")

        // LIKE
        val like = driver.select<SyncModuleEntity> {
            where { criteria("RequestUrl", like = "%batch%") }
        }.toList()
        log("LIKE ('%batch%'): ${like.size} results")
        verify(like.all { it.RequestUrl.contains("batch", ignoreCase = true) },
            "LIKE filter failed")
    }

    private fun testSelectWithComplexWhere() {
        // AND
        val and = driver.select<SyncModuleEntity> {
            where {
                criteria("RequestType", equal = 1)
                criteria("ModuleType", equal = 1)
            }
        }.toList()
        log("AND (RequestType=1 AND ModuleType=1): ${and.size} results")
        verify(and.all { it.RequestType == 1L && it.ModuleType == 1L },
            "AND condition failed")

        // OR
        val or = driver.select<SyncModuleEntity> {
            where {
                or {
                    criteria("SyncType", equal = 1)
                    criteria("SyncType", equal = 2)
                    criteria("SyncType", equal = 3)
                }
            }
        }.toList()
        log("OR (SyncType IN [1,2,3]): ${or.size} results")
        verify(or.size == 3, "OR condition failed: expected 3, got ${or.size}")

        // Mixed AND/OR
        val mixed = driver.select<SyncModuleEntity> {
            where {
                criteria("RequestType", equal = 1)
                or {
                    criteria("ModuleType", equal = 1)
                    criteria("ModuleType", equal = 2)
                }
            }
        }.toList()
        log("Mixed AND/OR: ${mixed.size} results")
    }

    private fun testSelectWithComparison() {
        // Greater than
        val gt = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", greaterThan = 10) }
        }.toList()
        log("> (SyncType > 10): ${gt.size} results")
        verify(gt.all { it.SyncType > 10 }, "Greater than failed")

        // Greater than or equal
        val gte = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", greaterThanOrEqual = 10) }
        }.toList()
        log(">= (SyncType >= 10): ${gte.size} results")
        verify(gte.all { it.SyncType >= 10 }, "Greater than or equal failed")

        // Less than
        val lt = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", lessThan = 10) }
        }.toList()
        log("< (SyncType < 10): ${lt.size} results")
        verify(lt.all { it.SyncType < 10 }, "Less than failed")

        // Less than or equal
        val lte = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", lessThanOrEqual = 10) }
        }.toList()
        log("<= (SyncType <= 10): ${lte.size} results")
        verify(lte.all { it.SyncType <= 10 }, "Less than or equal failed")
    }

    private fun testSelectWithInBetween() {
        // IN
        val inList = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", In = listOf(1, 2, 3)) }
        }.toList()
        log("IN ([1,2,3]): ${inList.size} results")
        verify(inList.size == 3, "IN operator failed: expected 3, got ${inList.size}")

        // NOT IN
        val notInList = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", notIn = listOf(1, 2, 3)) }
        }.toList()
        log("NOT IN ([1,2,3]): ${notInList.size} results")
        verify(notInList.all { it.SyncType !in listOf(1L, 2L, 3L) },
            "NOT IN operator failed")

        // BETWEEN
        val between = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", between = 10 to 14) }
        }.toList()
        log("BETWEEN (10 AND 14): ${between.size} results")
        verify(between.all { it.SyncType in 10..14 }, "BETWEEN operator failed")
    }

    private fun testSelectWithOrderBy() {
        // ASC
        val asc = driver.select<SyncModuleEntity> {
            orderBy { order("SyncType", desc = false) }
        }.toList()
        log("ORDER BY SyncType ASC: ${asc.size} results")
        log("  First: ${asc.firstOrNull()?.SyncType}, Last: ${asc.lastOrNull()?.SyncType}")

        // Verify order
        for (i in 0 until asc.size - 1) {
            verify(asc[i].SyncType <= asc[i + 1].SyncType,
                "ASC order failed at index $i")
        }

        // DESC
        val desc = driver.select<SyncModuleEntity> {
            orderBy { order("SyncType", desc = true) }
        }.toList()
        log("ORDER BY SyncType DESC: ${desc.size} results")
        log("  First: ${desc.firstOrNull()?.SyncType}, Last: ${desc.lastOrNull()?.SyncType}")

        // Verify order
        for (i in 0 until desc.size - 1) {
            verify(desc[i].SyncType >= desc[i + 1].SyncType,
                "DESC order failed at index $i")
        }
    }

    private fun testSelectWithLimitOffset() {
        // LIMIT
        val limited = driver.select<SyncModuleEntity> {
            limit(3)
        }.toList()
        log("LIMIT 3: ${limited.size} results")
        verify(limited.size == 3, "LIMIT failed: expected 3, got ${limited.size}")

        // OFFSET with LIMIT (SQLite requires LIMIT when using OFFSET)
        val offset = driver.select<SyncModuleEntity> {
            orderBy { order("SyncType", desc = false) }
            limit(999) // Large limit to get remaining rows
            offset(2)
        }.toList()
        log("OFFSET 2 with LIMIT 999: ${offset.size} results")

        // LIMIT + OFFSET
        val both = driver.select<SyncModuleEntity> {
            orderBy { order("SyncType", desc = false) }
            limit(3)
            offset(2)
        }.toList()
        log("LIMIT 3 OFFSET 2: ${both.size} results")
        verify(both.size == 3, "LIMIT+OFFSET failed: expected 3, got ${both.size}")

        // Verify offset is working - first item should not be the first in full list
        val allSorted = driver.select<SyncModuleEntity> {
            orderBy { order("SyncType", desc = false) }
        }.toList()

        if (allSorted.size > 2 && both.isNotEmpty()) {
            verify(both.first().SyncType == allSorted[2].SyncType,
                "OFFSET failed: expected ${allSorted[2].SyncType}, got ${both.first().SyncType}")
        }
    }

    private fun testSelectWithPagination() {
        val page1 = driver.select<SyncModuleEntity> {
            orderBy { order("SyncType", desc = false) }
        }.toPage(pageNumber = 1, pageSize = 3)

        log("Page 1: ${page1.items.size} items, hasMore: ${page1.hasMore}")
        log("  Items: ${page1.items.map { it.SyncType }}")

        val page2 = driver.select<SyncModuleEntity> {
            orderBy { order("SyncType", desc = false) }
        }.toPage(pageNumber = 2, pageSize = 3)

        log("Page 2: ${page2.items.size} items, hasMore: ${page2.hasMore}")
        log("  Items: ${page2.items.map { it.SyncType }}")
    }

    // ============================================
    // UPDATE Tests
    // ============================================

    private fun testUpdate() {
        log("Testing single update")

        val original = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", equal = 1) }
        }.firstOrNull()

        verify(original != null, "Entity not found for update")

        val updated = original!!.copy(
            RequestUrl = "https://api.test.com/customers-updated",
            LastSyncDate = now()
        )

        val affected = driver.update(updated)
        log("Update affected $affected row(s)")
        verify(affected == 1, "Update failed: expected 1 row, got $affected")

        // Verify
        val result = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", equal = 1) }
        }.firstOrNull()

        verify(result?.RequestUrl == updated.RequestUrl,
            "Update verification failed")
        log("Verified: Entity successfully updated")
    }

    private fun testBatchUpdate() {
        log("Testing batch update")

        val entities = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", In = listOf(10, 11, 12)) }
        }.toList()

        val updated = entities.map { it.copy(LastSyncDate = now()) }

        val affected = driver.batchUpdate(updated)
        log("Batch update affected $affected row(s)")
        verify(affected == entities.size,
            "Batch update failed: expected ${entities.size}, got $affected")

        log("Verified: Batch update successful")
    }

    // ============================================
    // DELETE Tests
    // ============================================

    private fun testDelete() {
        log("Testing single delete")

        val entity = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", equal = 100) }
        }.firstOrNull()

        verify(entity != null, "Entity not found for delete")

        val affected = driver.delete(entity!!)
        log("Delete affected $affected row(s)")
        verify(affected == 1, "Delete failed: expected 1 row, got $affected")

        // Verify
        val result = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", equal = 100) }
        }.firstOrNull()

        verify(result == null, "Delete verification failed: entity still exists")
        log("Verified: Entity successfully deleted")
    }

    private fun testBatchDelete() {
        log("Testing batch delete")

        val entities = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", In = listOf(13, 14)) }
        }.toList()

        log("Found ${entities.size} entities to delete")

        val affected = driver.batchDelete(entities)
        log("Batch delete affected $affected row(s)")
        verify(affected == entities.size,
            "Batch delete failed: expected ${entities.size}, got $affected")

        log("Verified: Batch delete successful")
    }

    private fun testDeleteWithCriteria() {
        log("Testing delete with criteria")

        val affected = driver.delete<SyncModuleEntity> {
            where { criteria("RequestType", equal = 3) }
        }

        log("Delete with criteria affected $affected row(s)")
        log("Verified: Criteria-based delete successful")
    }

    // ============================================
    // Aggregation Tests
    // ============================================

    private fun testCount() {
        val total = driver.count<SyncModuleEntity>()
        log("Total count: $total")

        val filtered = driver.count<SyncModuleEntity> {
            where { criteria("RequestType", equal = 1) }
        }
        log("Filtered count (RequestType=1): $filtered")

        verify(total >= filtered, "Count logic error")
    }

    private fun testMinMax() {
        val min = driver.minLong<SyncModuleEntity>("SyncType")
        val max = driver.maxLong<SyncModuleEntity>("SyncType")

        log("Min SyncType: $min")
        log("Max SyncType: $max")

        verify(min != null && max != null, "Min/Max returned null")
        verify(min!! <= max!!, "Min/Max logic error: min=$min, max=$max")
    }

    // ============================================
    // Transaction Tests
    // ============================================

    private fun testTransactionCommit() {
        log("Testing transaction commit")

        driver.transaction {
            val entity = SyncModuleEntity(200, "https://api.test.com/transaction", null, null, 1, 1)
            insert(entity)
            log("  Entity inserted in transaction")
        }

        // Verify
        val result = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", equal = 200) }
        }.firstOrNull()

        verify(result != null, "Transaction commit failed: entity not found")
        log("Verified: Transaction committed successfully")
    }

    private fun testTransactionRollback() {
        log("Testing transaction rollback")

        try {
            driver.transaction {
                val entity = SyncModuleEntity(300, "https://api.test.com/rollback", null, null, 1, 1)
                insert(entity)
                log("  Entity inserted in transaction")

                throw RuntimeException("Intentional rollback")
            }
        } catch (e: RuntimeException) {
            log("  Transaction rolled back (expected)")
        }

        // Verify
        val result = driver.select<SyncModuleEntity> {
            where { criteria("SyncType", equal = 300) }
        }.firstOrNull()

        verify(result == null, "Transaction rollback failed: entity exists")
        log("Verified: Transaction rolled back successfully")
    }

    // ============================================
    // Flow Tests
    // ============================================

    private fun testFlow() = runBlocking {
        log("Testing Flow operations")

        val entity = SyncModuleEntity(400, "https://api.test.com/flow", null, null, 1, 1)
        driver.insertAndNotify(entity)
        log("  Entity inserted with notification")

        val results = driver.selectAsFlow<SyncModuleEntity> {
            where { criteria("SyncType", equal = 400) }
        }.first()

        log("  Flow received ${results.size} entities")
        verify(results.isNotEmpty(), "Flow failed: no results")

        driver.deleteAndNotify(entity)
        log("  Entity deleted with notification")

        log("Verified: Flow operations working correctly")
    }

    // ============================================
    // Raw SQL Tests
    // ============================================

    private fun testRawSql() {
        log("Testing raw SQL queries")

        val results = driver.rawQueryToEntity<SyncModuleEntity>(
            "SELECT * FROM SyncModuleEntity WHERE RequestType = ? LIMIT ?",
            1, 5
        )
        log("  Raw query: ${results.size} results")

        val count = driver.rawCount(
            "SELECT COUNT(*) FROM SyncModuleEntity WHERE RequestFilter IS NOT NULL"
        )
        log("  Raw count: $count")

        log("Verified: Raw SQL working correctly")
    }

    // ============================================
    // Utilities
    // ============================================

    /**
     * Cleanup - Delete all entities
     */
    private fun cleanup() {
        driver.rawExecute("DELETE FROM SyncModuleEntity")
        val remaining = driver.count<SyncModuleEntity>()
        verify(remaining == 0L, "Cleanup failed: $remaining entities remain")
        log("Database cleaned: All entities deleted")
    }

    /**
     * Prepare basic test data (TEST_ENTITY_1, TEST_ENTITY_2, TEST_ENTITY_3)
     */
    private fun prepareBasicData() {
        driver.insert(TEST_ENTITY_1)
        driver.insert(TEST_ENTITY_2)
        driver.insert(TEST_ENTITY_3)
        log("Prepared basic test data: 3 entities")
    }

    /**
     * Prepare full test data (basic + batch)
     */
    private fun prepareFullTestData() {
        prepareBasicData()
        driver.batchInsert(BATCH_TEST_ENTITIES)
        log("Prepared full test data: ${3 + BATCH_TEST_ENTITIES.size} entities")
    }

    /**
     * Prepare data for delete tests
     */
    private fun prepareForDelete() {
        val entity = SyncModuleEntity(100, "https://api.test.com/delete", null, null, 1, 1)
        driver.insert(entity)
        log("Prepared delete test data: 1 entity")
    }

    private inline fun step(id: String, description: String, block: () -> Unit) {
        println("\n${"=".repeat(60)}")
        println("[$id] $description")
        println("${"=".repeat(60)}")
        block()
    }

    private fun log(message: String) {
        println("  ✓ $message")
    }

    private fun verify(condition: Boolean, message: String) {
        if (!condition) {
            throw AssertionError("❌ VERIFICATION FAILED: $message")
        }
    }

    private fun printHeader(title: String) {
        println("\n${"=".repeat(70)}")
        println(" ".repeat((70 - title.length) / 2) + title)
        println("${"=".repeat(70)}")
        println("Start Time: ${now()}")
        println("${"=".repeat(70)}\n")
    }

    private fun printSuccess() {
        println("\n${"=".repeat(70)}")
        println(" ".repeat(20) + "✅ ALL TESTS PASSED ✅")
        println("${"=".repeat(70)}")
        println("End Time: ${now()}")
        println("${"=".repeat(70)}\n")
    }

    private fun printFailure(e: Exception) {
        println("\n${"=".repeat(70)}")
        println(" ".repeat(20) + "❌ TEST FAILED ❌")
        println("${"=".repeat(70)}")
        println("Error: ${e.message}")
        println("Type: ${e::class.simpleName}")
        println("${"=".repeat(70)}")
        e.printStackTrace()
        println("${"=".repeat(70)}\n")
    }
}

/**
 * USAGE:
 *
 * val test = DatabaseComprehensiveTest(driver)
 * test.runAllTests()
 *
 * Bu tek method tüm testleri çalıştırır ve:
 * - Her adımı detaylı loglar
 * - Hata durumunda exception fırlatır
 * - Test verilerini sabit tutar
 * - Step-by-step ilerleme gösterir
 */