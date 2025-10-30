package com.repzone.data.sample
import app.cash.sqldelight.db.SqlDriver
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toInstant
import com.repzone.database.SyncModuleEntity
import com.repzone.database.runtime.Page
import com.repzone.database.runtime.batchDelete
import com.repzone.database.runtime.batchInsert
import com.repzone.database.runtime.batchInsertChunked
import com.repzone.database.runtime.batchUpdate
import com.repzone.database.runtime.count
import com.repzone.database.runtime.countAsFlow
import com.repzone.database.runtime.delete
import com.repzone.database.runtime.insert
import com.repzone.database.runtime.insertAndNotify
import com.repzone.database.runtime.maxLong
import com.repzone.database.runtime.minLong
import com.repzone.database.runtime.rawCount
import com.repzone.database.runtime.rawQuery
import com.repzone.database.runtime.rawQueryToEntity
import com.repzone.database.runtime.rawTransaction
import com.repzone.database.runtime.select
import com.repzone.database.runtime.selectAsFlow
import com.repzone.database.runtime.selectFirstAsFlow
import com.repzone.database.runtime.toPage
import com.repzone.database.runtime.transaction
import com.repzone.database.runtime.transactionWithResult
import com.repzone.database.runtime.update
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

/**
 * SyncModuleEntity için kapsamlı kullanım örnekleri
 *
 * Bu class, veritabanı işlemleri için örnek kodlar içerir.
 * Temel CRUD işlemlerinden karmaşık sorgulara kadar her şey burada!
 */
@OptIn(ExperimentalTime::class)
class SyncModuleEntitySamples(private val driver: SqlDriver) {

    // ============================================
    // SECTION 1: BASIC CRUD OPERATIONS
    // ============================================

    /**
     * 1.1 - Basit INSERT
     * Tek bir entity ekleme
     */
    fun basicInsert() {
        val entity = SyncModuleEntity(
            SyncType = 1,
            RequestUrl = "https://api.example.com/customers",
            RequestFilter = "status=active",
            LastSyncDate = now(),
            RequestType = 1,
            ModuleType = 1
        )

        val id = driver.insert(entity)
        println("Inserted with ID: $id")
    }

    /**
     * 1.2 - Batch INSERT
     * Birden fazla entity'yi aynı anda ekleme
     */
    fun batchInsert() {
        val entities = listOf(
            SyncModuleEntity(10, "https://api.example.com/products", "category=electronics", null, 1, 1),
            SyncModuleEntity(11, "https://api.example.com/orders", "date>2024-01-01", null, 1, 2),
            SyncModuleEntity(12, "https://api.example.com/inventory", null, null, 2, 1)
        )

        val ids = driver.batchInsert(entities)
        println("Inserted ${ids.size} entities")
    }

    /**
     * 1.3 - Basit SELECT
     * Tüm kayıtları getirme
     */
    fun basicSelect() {
        val modules = driver.select<SyncModuleEntity>().toList()

        modules.forEach { module ->
            println("Module ${module.SyncType}: ${module.RequestUrl}")
        }
    }

    /**
     * 1.4 - Tek kayıt getirme
     * ID'ye göre tek kayıt
     */
    fun selectById() {
        val module = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", equal = 1)
            }
        }.firstOrNull()

        if (module != null) {
            println("Found: ${module.RequestUrl}")
        } else {
            println("Module not found")
        }
    }

    /**
     * 1.5 - UPDATE
     * Mevcut entity'yi güncelleme
     */
    fun basicUpdate() {
        // Önce entity'yi getir
        val module = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", equal = 1)
            }
        }.firstOrNull()

        // Güncelle
        module?.let {
            val updated = it.copy(
                RequestUrl = "https://new-api.example.com/customers",
                LastSyncDate = now()
            )

            val affected = driver.update(updated)
            println("Updated $affected row(s)")
        }
    }

    /**
     * 1.6 - DELETE
     * Entity'yi silme
     */
    fun basicDelete() {
        val module = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", equal = 1)
            }
        }.firstOrNull()

        module?.let {
            val affected = driver.delete(it)
            println("Deleted $affected row(s)")
        }
    }

    // ============================================
    // SECTION 2: ADVANCED WHERE QUERIES
    // ============================================

    /**
     * 2.1 - Basit WHERE koşulları
     * Equal, Not Equal, IS NULL, IS NOT NULL
     */
    fun simpleWhereConditions() {
        // Equal
        val modules1 = driver.select<SyncModuleEntity> {
            where {
                criteria("RequestType", equal = 1)
            }
        }.toList()
        println("RequestType = 1: ${modules1.size} modules")

        // Not Equal
        val modules2 = driver.select<SyncModuleEntity> {
            where {
                criteria("ModuleType", notEqual = 1)
            }
        }.toList()
        println("ModuleType != 1: ${modules2.size} modules")

        // IS NULL
        val modules3 = driver.select<SyncModuleEntity> {
            where {
                criteria("RequestFilter", isNull = true)
            }
        }.toList()
        println("RequestFilter IS NULL: ${modules3.size} modules")

        // IS NOT NULL
        val modules4 = driver.select<SyncModuleEntity> {
            where {
                criteria("LastSyncDate", isNull = false)
            }
        }.toList()
        println("LastSyncDate IS NOT NULL: ${modules4.size} modules")
    }

    /**
     * 2.2 - LIKE ve NOT LIKE
     * String arama
     */
    fun likeOperators() {
        // LIKE - içeren
        val modules1 = driver.select<SyncModuleEntity> {
            where {
                criteria("RequestUrl", like = "%api.example.com%")
            }
        }.toList()
        println("URL contains 'api.example.com': ${modules1.size} modules")

        // NOT LIKE - içermeyen
        val modules2 = driver.select<SyncModuleEntity> {
            where {
                criteria("RequestUrl", notLike = "%test%")
            }
        }.toList()
        println("URL not contains 'test': ${modules2.size} modules")

        // Starts with
        val modules3 = driver.select<SyncModuleEntity> {
            where {
                criteria("RequestUrl", like = "https://api%")
            }
        }.toList()
        println("URL starts with 'https://api': ${modules3.size} modules")

        // Ends with
        val modules4 = driver.select<SyncModuleEntity> {
            where {
                criteria("RequestUrl", like = "%/customers")
            }
        }.toList()
        println("URL ends with '/customers': ${modules4.size} modules")
    }

    /**
     * 2.3 - Karşılaştırma operatörleri
     * >, >=, <, <=
     */
    fun comparisonOperators() {
        // Greater than
        val modules1 = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", greaterThan = 10)
            }
        }.toList()
        println("SyncType > 10: ${modules1.size} modules")

        // Greater than or equal
        val modules2 = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", greaterThanOrEqual = 10)
            }
        }.toList()
        println("SyncType >= 10: ${modules2.size} modules")

        // Less than
        val modules3 = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", lessThan = 10)
            }
        }.toList()
        println("SyncType < 10: ${modules3.size} modules")

        // Less than or equal
        val modules4 = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", lessThanOrEqual = 10)
            }
        }.toList()
        println("SyncType <= 10: ${modules4.size} modules")
    }

    /**
     * 2.4 - IN ve NOT IN
     * Liste içinde/dışında kontrol
     */
    fun inOperators() {
        // IN
        val modules1 = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", In = listOf(1, 2, 3, 4, 5))
            }
        }.toList()
        println("SyncType IN (1,2,3,4,5): ${modules1.size} modules")

        // NOT IN
        val modules2 = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", notIn = listOf(999, 1000, 1001))
            }
        }.toList()
        println("SyncType NOT IN (999,1000,1001): ${modules2.size} modules")
    }

    /**
     * 2.5 - BETWEEN
     * Aralık sorgusu
     */
    @OptIn(ExperimentalTime::class)
    fun betweenOperator() {
        // Numeric range
        val modules1 = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", between = 1 to 10)
            }
        }.toList()
        println("SyncType BETWEEN 1 AND 10: ${modules1.size} modules")

        // Date range - Son 7 günün kayıtları
        val sevenDaysAgo = now().toInstant().minus(7.days).toEpochMilliseconds()
        val now = now()

        val modules2 = driver.select<SyncModuleEntity> {
            where {
                criteria("LastSyncDate", between = sevenDaysAgo to now)
            }
        }.toList()
        println("Last 7 days: ${modules2.size} modules")
    }

    /**
     * 2.6 - AND koşulları
     * Birden fazla koşul (hepsi true olmalı)
     */
    fun andConditions() {
        val modules = driver.select<SyncModuleEntity> {
            where {
                criteria("RequestType", equal = 1)
                criteria("ModuleType", equal = 1)
                criteria("RequestFilter", isNull = false)
            }
        }.toList()

        println("RequestType=1 AND ModuleType=1 AND RequestFilter IS NOT NULL: ${modules.size} modules")
    }

    /**
     * 2.7 - OR koşulları
     * Birden fazla koşul (en az biri true olmalı)
     */
    fun orConditions() {
        val modules = driver.select<SyncModuleEntity> {
            where {
                or {
                    criteria("SyncType", equal = 1)
                    criteria("SyncType", equal = 2)
                    criteria("SyncType", equal = 3)
                }
            }
        }.toList()

        println("SyncType IN (1,2,3) via OR: ${modules.size} modules")
    }

    /**
     * 2.8 - Karmaşık AND/OR kombinasyonları
     * İç içe koşullar
     */
    fun complexAndOrConditions() {
        // (RequestType = 1 OR RequestType = 2) AND ModuleType = 1
        val modules = driver.select<SyncModuleEntity> {
            where {
                or {
                    criteria("RequestType", equal = 1)
                    criteria("RequestType", equal = 2)
                }
                criteria("ModuleType", equal = 1)
            }
        }.toList()

        println("Complex: ${modules.size} modules")
    }

    /**
     * 2.9 - NOT operatörü
     * Koşulun tersini alma
     */
    fun notOperator() {
        // NOT (RequestType = 1 AND ModuleType = 1)
        val modules = driver.select<SyncModuleEntity> {
            where {
                not {
                    criteria("RequestType", equal = 1)
                    criteria("ModuleType", equal = 1)
                }
            }
        }.toList()

        println("NOT (RequestType=1 AND ModuleType=1): ${modules.size} modules")
    }

    // ============================================
    // SECTION 3: ORDERING & PAGINATION
    // ============================================

    /**
     * 3.1 - ORDER BY
     * Sıralama
     */
    fun orderBy() {
        // Ascending
        val modules1 = driver.select<SyncModuleEntity> {
            orderBy {
                order("SyncType", desc = false)
            }
            limit(10)
        }.toList()
        println("First 10 ASC: ${modules1.map { it.SyncType }}")

        // Descending
        val modules2 = driver.select<SyncModuleEntity> {
            orderBy {
                order("SyncType", desc = true)
            }
            limit(10)
        }.toList()
        println("First 10 DESC: ${modules2.map { it.SyncType }}")

        // Multiple columns
        val modules3 = driver.select<SyncModuleEntity> {
            orderBy {
                order("RequestType", desc = false)
                order("ModuleType", desc = true)
            }
        }.toList()
        println("Multi-column order: ${modules3.size} modules")
    }

    /**
     * 3.2 - LIMIT
     * Sonuç sayısını sınırlama
     */
    fun limitResults() {
        val modules = driver.select<SyncModuleEntity> {
            limit(5)
        }.toList()

        println("Limited to 5: ${modules.size} modules")
    }

    /**
     * 3.3 - OFFSET
     * İlk N kaydı atlama
     */
    fun offsetResults() {
        // İlk sayfa (0-9)
        val page1 = driver.select<SyncModuleEntity> {
            orderBy {
                order("SyncType", desc = false)
            }
            limit(10)
            offset(0)
        }.toList()

        // İkinci sayfa (10-19)
        val page2 = driver.select<SyncModuleEntity> {
            orderBy {
                order("SyncType", desc = false)
            }
            limit(10)
            offset(10)
        }.toList()

        println("Page 1: ${page1.map { it.SyncType }}")
        println("Page 2: ${page2.map { it.SyncType }}")
    }

    /**
     * 3.4 - Pagination helper
     * Sayfalama yardımcısı
     */
    fun pagination() {
        val pageSize = 10
        var currentPage = 1

        while (true) {
            val page = driver.select<SyncModuleEntity> {
                orderBy {
                    order("SyncType", desc = false)
                }
            }.toPage(pageNumber = currentPage, pageSize = pageSize)

            println("Page $currentPage: ${page.items.size} items, hasMore: ${page.hasMore}")

            if (!page.hasMore) break
            currentPage++
        }
    }

    // ============================================
    // SECTION 4: AGGREGATE FUNCTIONS
    // ============================================

    /**
     * 4.1 - COUNT
     * Kayıt sayısı
     */
    fun countRecords() {
        // Total count
        val total = driver.count<SyncModuleEntity>()
        println("Total modules: $total")

        // Filtered count
        val filtered = driver.count<SyncModuleEntity> {
            where {
                criteria("RequestType", equal = 1)
            }
        }
        println("RequestType=1 modules: $filtered")
    }

    /**
     * 4.2 - MAX & MIN
     * En yüksek ve en düşük değer
     */
    fun maxMin() {
        val maxSyncType = driver.maxLong<SyncModuleEntity>("SyncType")
        val minSyncType = driver.minLong<SyncModuleEntity>("SyncType")

        println("Max SyncType: $maxSyncType")
        println("Min SyncType: $minSyncType")

        // With filter
        val maxFiltered = driver.maxLong<SyncModuleEntity>("SyncType") {
            where {
                criteria("RequestType", equal = 1)
            }
        }
        println("Max SyncType (RequestType=1): $maxFiltered")
    }

    // ============================================
    // SECTION 5: BATCH OPERATIONS
    // ============================================

    /**
     * 5.1 - Batch UPDATE
     * Birden fazla entity'yi güncelleme
     */
    fun batchUpdate() {
        // Tüm modülleri getir
        val modules = driver.select<SyncModuleEntity> {
            where {
                criteria("RequestType", equal = 1)
            }
        }.toList()

        // LastSyncDate'i güncelle
        val updated = modules.map { module ->
            module.copy(LastSyncDate = now())
        }

        val affected = driver.batchUpdate(updated)
        println("Batch updated $affected modules")
    }

    /**
     * 5.2 - Batch DELETE
     * Birden fazla entity'yi silme
     */
    fun batchDelete() {
        // Test modüllerini getir
        val modules = driver.select<SyncModuleEntity> {
            where {
                criteria("SyncType", greaterThan = 1000)
            }
        }.toList()

        val affected = driver.batchDelete(modules)
        println("Batch deleted $affected modules")
    }

    /**
     * 5.3 - DELETE with criteria
     * Koşula göre toplu silme (daha performanslı)
     */
    fun deleteWithCriteria() {
        val affected = driver.delete<SyncModuleEntity> {
            where {
                criteria("ModuleType", equal = 99)
            }
        }

        println("Deleted $affected test modules")
    }

    /**
     * 5.4 - Chunked batch operations
     * Büyük listeler için parça parça işlem
     */
    fun chunkedBatchInsert() {
        // 10,000 module oluştur
        val largeList = List(10000) { index ->
            SyncModuleEntity(
                SyncType = 10000 + index.toLong(),
                RequestUrl = "https://api.example.com/module$index",
                RequestFilter = null,
                LastSyncDate = null,
                RequestType = 1,
                ModuleType = 1
            )
        }

        // 100'er 100'er ekle
        val ids = driver.batchInsertChunked(
            entities = largeList,
            chunkSize = 1000
        )

        println("Inserted ${ids.size} modules in chunks")
    }

    // ============================================
    // SECTION 6: TRANSACTIONS
    // ============================================

    /**
     * 6.1 - Basit transaction
     * Birden fazla işlemi atomik olarak yapma
     */
    fun simpleTransaction() {
        driver.transaction {
            // Multiple inserts
            val module1 = SyncModuleEntity(100, "https://trans1.com", null, null, null, null)
            val module2 = SyncModuleEntity(101, "https://trans2.com", null, null, null, null)

            insert(module1)
            insert(module2)

            // Hata olursa her ikisi de rollback edilir
        }

        println("Transaction completed")
    }

    /**
     * 6.2 - Transaction with rollback
     * Hata durumunda geri alma
     */
    fun transactionWithRollback() {
        try {
            driver.transaction {
                val module = SyncModuleEntity(200, "https://rollback.com", null, null, null, null)
                insert(module)

                // Simüle error
                throw Exception("Something went wrong!")

                // Bu satır çalışmaz
            }
        } catch (e: Exception) {
            println("Transaction rolled back: ${e.message}")
        }
    }

    /**
     * 6.3 - Transaction with result
     * İşlem sonucu döndürme
     */
    fun transactionWithResult() {
        val (id1, id2) = driver.transactionWithResult {
            val module1 = SyncModuleEntity(300, "https://result1.com", null, null, null, null)
            val module2 = SyncModuleEntity(301, "https://result2.com", null, null, null, null)

            val id1 = insert(module1)
            val id2 = insert(module2)

            id1 to id2
        }

        println("Inserted IDs: $id1, $id2")
    }

    /**
     * 6.4 - Complex transaction
     * Karmaşık işlemler
     */
    fun complexTransaction() {
        driver.transaction {
            // 1. Eski kayıtları sil
            delete<SyncModuleEntity> {
                where {
                    criteria("LastSyncDate", isNull = true)
                }
            }

            // 2. Yeni kayıtlar ekle
            val newModules = List(10) { index ->
                SyncModuleEntity(
                    SyncType = 400 + index.toLong(),
                    RequestUrl = "https://new-module$index.com",
                    RequestFilter = null,
                    LastSyncDate = now(),
                    RequestType = 1,
                    ModuleType = 1
                )
            }
            driver.batchInsert(newModules)

            // 3. Mevcut kayıtları güncelle
            val existing = select<SyncModuleEntity> {
                where {
                    criteria("RequestType", equal = 1)
                }
            }.toList()

            val updated = existing.map { it.copy(LastSyncDate = now()) }
            driver.batchUpdate(updated)
        }

        println("Complex transaction completed")
    }

    // ============================================
    // SECTION 7: RAW SQL
    // ============================================

    /**
     * 7.1 - Raw SQL query
     * Ham SQL sorgusu
     */
    fun rawSqlQuery() {
        val modules = driver.rawQueryToEntity<SyncModuleEntity>(
            """
            SELECT * FROM SyncModuleEntity 
            WHERE RequestType = ? AND ModuleType = ?
            ORDER BY SyncType DESC
            LIMIT ?
            """,
            1, 1, 10
        )

        println("Raw query: ${modules.size} modules")
    }

    /**
     * 7.2 - Raw SQL aggregate
     * Ham SQL toplama fonksiyonu
     */
    fun rawSqlAggregate() {
        val count = driver.rawCount(
            "SELECT COUNT(*) FROM SyncModuleEntity WHERE LastSyncDate IS NOT NULL"
        )

        println("Synced modules: $count")
    }

    /**
     * 7.3 - Complex raw SQL
     * Karmaşık SQL
     */
    fun complexRawSql() {
        val results = driver.rawQuery(
            """
            SELECT 
                RequestType,
                ModuleType,
                COUNT(*) as Count,
                MAX(LastSyncDate) as LastSync
            FROM SyncModuleEntity
            GROUP BY RequestType, ModuleType
            HAVING COUNT(*) > 1
            ORDER BY Count DESC
            """
        )

        results.forEach { row ->
            println("Type: ${row["column_0"]}, Count: ${row["column_2"]}")
        }
    }

    /**
     * 7.4 - Raw SQL transaction
     * Ham SQL ile transaction
     */
    fun rawSqlTransaction() {
        driver.rawTransaction {
            execute(
                "INSERT INTO SyncModuleEntity (SyncType, RequestUrl, RequestType, ModuleType) VALUES (?, ?, ?, ?)",
                500, "https://raw1.com", 1, 1
            )

            execute(
                "UPDATE SyncModuleEntity SET LastSyncDate = ? WHERE SyncType = ?",
                now(), 500
            )
        }

        println("Raw SQL transaction completed")
    }

    // ============================================
    // SECTION 8: FLOW / REACTIVE
    // ============================================

    /**
     * 8.1 - Observe with Flow
     * Flow ile değişiklikleri dinleme
     */
    fun observeWithFlow(scope: CoroutineScope) {
        scope.launch {
            driver.selectAsFlow<SyncModuleEntity> {
                where {
                    criteria("RequestType", equal = 1)
                }
                orderBy {
                    order("SyncType", desc = false)
                }
            }.collect { modules ->
                println("Flow update: ${modules.size} modules")
                // UI'ı güncelle
            }
        }
    }

    /**
     * 8.2 - Observe single entity
     * Tek entity'yi dinleme
     */
    fun observeSingleEntity(scope: CoroutineScope, syncType: Int) {
        scope.launch {
            driver.selectFirstAsFlow<SyncModuleEntity> {
                where {
                    criteria("SyncType", equal = syncType)
                }
            }.collect { module ->
                if (module != null) {
                    println("Module updated: ${module.RequestUrl}")
                } else {
                    println("Module deleted")
                }
            }
        }
    }

    /**
     * 8.3 - Observe count
     * Sayıyı dinleme
     */
    fun observeCount(scope: CoroutineScope) {
        scope.launch {
            driver.countAsFlow<SyncModuleEntity> {
                where {
                    criteria("LastSyncDate", isNull = false)
                }
            }.collect { count ->
                println("Synced modules: $count")
                // Badge'i güncelle
            }
        }
    }

    /**
     * 8.4 - Insert with notification
     * Flow'ları tetikleyen insert
     */
    fun insertWithNotification() {
        val module = SyncModuleEntity(
            SyncType = 600,
            RequestUrl = "https://notify.com",
            RequestFilter = null,
            LastSyncDate = now(),
            RequestType = 1,
            ModuleType = 1
        )

        driver.insertAndNotify(module)
        // Tüm flow'lar otomatik tetiklenir
    }

    // ============================================
    // SECTION 9: REAL-WORLD USE CASES
    // ============================================

    /**
     * 9.1 - Sync işlemi
     * API'den veri çekip kaydetme
     */
    fun syncFromApi() {
        // API'den gelen veriler (simüle)
        val apiModules = listOf(
            ApiModule(1, "https://api.example.com/customers", "status=active", 1, 1),
            ApiModule(2, "https://api.example.com/products", null, 1, 2),
            ApiModule(3, "https://api.example.com/orders", "date>2024", 2, 1)
        )

        driver.transaction {
            // Eski kayıtları sil
            delete<SyncModuleEntity> {
                where {
                    criteria("SyncType", In = apiModules.map { it.id })
                }
            }

            // Yeni kayıtları ekle
            val entities = apiModules.map { api ->
                SyncModuleEntity(
                    SyncType = api.id.toLong(),
                    RequestUrl = api.url,
                    RequestFilter = api.filter,
                    LastSyncDate = now(),
                    RequestType = api.requestType.toLong(),
                    ModuleType = api.moduleType.toLong()
                )
            }

            driver.batchInsert(entities)
        }

        println("Sync completed: ${apiModules.size} modules")
    }

    /**
     * 9.2 - Sync durumu kontrolü
     * Hangi modüller sync edilmeli?
     */
    fun checkSyncStatus() {
        val oneDayAgo = now().toInstant().minus(1.days).toEpochMilliseconds()

        // Son 24 saatte sync edilmemiş modüller
        val needsSync = driver.select<SyncModuleEntity> {
            where {
                or {
                    criteria("LastSyncDate", isNull = true)
                    criteria("LastSyncDate", lessThan = oneDayAgo)
                }
            }
        }.toList()

        println("Modules need sync: ${needsSync.size}")

        needsSync.forEach { module ->
            println("  - ${module.RequestUrl}")
        }
    }

    /**
     * 9.3 - Filtreleme ve arama
     * Kullanıcı araması
     */
    fun searchModules(searchText: String): List<SyncModuleEntity> {
        val results = driver.select<SyncModuleEntity> {
            where {
                or {
                    criteria("RequestUrl", like = "%$searchText%")
                    criteria("RequestFilter", like = "%$searchText%")
                }
            }
            orderBy {
                order("SyncType", desc = false)
            }
        }.toList()

        println("Search results: ${results.size} modules")
        return results
    }

    /**
     * 9.4 - Dashboard istatistikleri
     * Özet bilgiler
     */
    fun dashboardStats() {
        // Toplam modül sayısı
        val total = driver.count<SyncModuleEntity>()

        // Sync edilmiş modüller
        val synced = driver.count<SyncModuleEntity> {
            where {
                criteria("LastSyncDate", isNull = false)
            }
        }

        // En son sync tarihi
        val lastSync = driver.maxLong<SyncModuleEntity>("LastSyncDate")

        // RequestType'a göre dağılım
        val byRequestType = driver.rawQuery(
            """
            SELECT RequestType, COUNT(*) as Count
            FROM SyncModuleEntity
            GROUP BY RequestType
            """
        )

        println("Dashboard Stats:")
        println("  Total: $total")
        println("  Synced: $synced")
        println("  Last sync: $lastSync")
        println("  By type: $byRequestType")
    }

    /**
     * 9.5 - Temizlik işlemi
     * Eski kayıtları silme
     */
    fun cleanupOldRecords() {
        val thirtyDaysAgo = now().toInstant().minus(30.days).toEpochMilliseconds()

        val deleted = driver.delete<SyncModuleEntity> {
            where {
                criteria("LastSyncDate", lessThan = thirtyDaysAgo)
            }
        }

        println("Cleaned up $deleted old records")
    }

    /**
     * 9.6 - Toplu güncelleme
     * Tüm modülleri sync olarak işaretle
     */
    fun markAllAsSynced() {
        val modules = driver.select<SyncModuleEntity>().toList()

        val updated = modules.map { module ->
            module.copy(LastSyncDate = now())
        }

        val affected = driver.batchUpdate(updated)
        println("Marked $affected modules as synced")
    }

    /**
     * 9.7 - Koşullu güncelleme
     * Sadece belirli modülleri güncelle
     */
    fun conditionalUpdate() {
        driver.transaction {
            // RequestType = 1 olanları getir
            val modules = select<SyncModuleEntity> {
                where {
                    criteria("RequestType", equal = 1)
                    criteria("LastSyncDate", isNull = true)
                }
            }.toList()

            // Güncelle
            val updated = modules.map { module ->
                module.copy(
                    LastSyncDate = now(),
                    RequestUrl = module.RequestUrl.replace("http://", "https://")
                )
            }

            driver.batchUpdate(updated)
        }
    }

    /**
     * 9.8 - Paginated loading
     * Sayfa sayfa yükleme (UI için)
     */
    fun loadModulesPaginated(page: Int, pageSize: Int = 20): Page<SyncModuleEntity> {
        val result = driver.select<SyncModuleEntity> {
            where {
                criteria("RequestType", equal = 1)
            }
            orderBy {
                order("SyncType", desc = false)
            }
        }.toPage(pageNumber = page, pageSize = pageSize)

        println("Page $page: ${result.items.size} items, hasMore: ${result.hasMore}")
        return result
    }

    /**
     * 9.9 - Conflict resolution
     * Çakışma çözümü
     */
    fun resolveConflicts() {
        // Aynı URL'e sahip modüller (conflict)
        val allModules = driver.select<SyncModuleEntity>().toList()
        val duplicates = allModules.groupBy { it.RequestUrl }
            .filter { it.value.size > 1 }

        driver.transaction {
            duplicates.forEach { (url, modules) ->
                // En yeni olanı tut
                val newest = modules.maxByOrNull { it.LastSyncDate ?: 0L }
                val toDelete = modules.filter { it.SyncType != newest?.SyncType }

                toDelete.forEach { module ->
                    delete(module)
                }

                println("Resolved conflict for $url: kept ${newest?.SyncType}, deleted ${toDelete.size}")
            }
        }
    }

    /**
     * 9.10 - Export/Import
     * Veri dışa/içe aktarma
     */
    fun exportModules(): List<SyncModuleEntity> {
        return driver.select<SyncModuleEntity> {
            orderBy {
                order("SyncType", desc = false)
            }
        }.toList()
    }

    fun importModules(modules: List<SyncModuleEntity>) {
        driver.transaction {
            // Tüm mevcut kayıtları sil
            delete<SyncModuleEntity> {
                where {
                    criteria("SyncType", greaterThan = 0)
                }
            }

            // Yeni kayıtları ekle
            driver.batchInsert(modules)
        }

        println("Imported ${modules.size} modules")
    }
}

// Helper data class
data class ApiModule(
    val id: Int,
    val url: String,
    val filter: String?,
    val requestType: Int,
    val moduleType: Int
)

/**
 * USAGE EXAMPLES:
 *
 * val samples = SyncModuleEntitySamples(driver)
 *
 * // Basic operations
 * samples.basicInsert()
 * samples.basicSelect()
 * samples.basicUpdate()
 *
 * // Advanced queries
 * samples.simpleWhereConditions()
 * samples.complexAndOrConditions()
 *
 * // Real-world use cases
 * samples.syncFromApi()
 * samples.dashboardStats()
 * samples.cleanupOldRecords()
 */