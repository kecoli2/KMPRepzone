package com.repzone.domain.document.base

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.coroutines.flow.StateFlow
import com.repzone.domain.common.Result
import com.repzone.domain.document.model.DiscountType
import com.repzone.domain.document.model.Document
import com.repzone.domain.document.model.DocumentType
import com.repzone.domain.document.model.GiftSelection
import com.repzone.domain.document.model.LineConflict
import com.repzone.domain.document.model.PendingGiftSelection
import com.repzone.domain.document.model.PendingLine
import com.repzone.domain.document.model.PendingUpdate
import com.repzone.domain.document.model.ProductInformationModel
import com.repzone.domain.document.model.ProductUnit
import com.repzone.domain.document.model.StockStatus
import com.repzone.domain.document.model.StockValidationResult
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.model.SyncDocumentMapModel
import com.repzone.domain.util.ProductQueryBuilder

/**
 * Belge yöneticisi contract'ı
 * Satır ekleme, güncelleme, iskonto ve promosyon işlemleri
 */
interface IDocumentManager {

    //region ============ Fields ============
    /** Belge tipi */
    val documentType: DocumentType

    /** Satırlar */
    val lines: StateFlow<List<IDocumentLine>>

    /** Seçimli iskonto conflict'leri */
    val pendingConflicts: StateFlow<List<LineConflict>>

    /** Kullanıcı seçimi bekleyen hediyeler */
    val pendingGiftSelections: StateFlow<List<PendingGiftSelection>>
    //endregion ============ Fields ============

    //region ============ Line Operations ============

    /**
     * Yeni satır ekler
     */
    suspend fun addLine(product: ProductInformationModel, unit: ProductUnit, quantity: BigDecimal): AddLineResult

    /**
     * Mevcut satırı günceller (miktar veya birim değişikliği)
     */
    suspend fun updateLine(lineId: String, newUnit: ProductUnit, newQuantity: BigDecimal): UpdateLineResult

    /**
     * Satırı siler
     */
    suspend fun removeLine(lineId: String)

    //endregion ============ Line Operations ============

    //region ============ Discount Operations ============

    /**
     * Manuel iskonto uygular
     */
    suspend fun applyManualDiscount(lineId: String, slotNumber: Int, type: DiscountType, value: BigDecimal): Result<Unit>

    /**
     * Manuel iskontoyu temizler
     */
    suspend fun clearManualDiscount(lineId: String, slotNumber: Int): Result<Unit>

    //endregion ============ Discount Operations ============

    //region ============ Promotion Operations ============

    /**
     * Tüm promosyonları yeniden hesaplar
     */
    suspend fun recalculatePromotions()

    /**
     * İskonto conflict'ini çözer
     */
    suspend fun resolveConflict(
        lineId: String,
        slotNumber: Int,
        selectedRuleId: String
    )

    /**
     * Hediye seçimini onaylar
     */
    suspend fun confirmGiftSelection(ruleId: String, selections: List<GiftSelection>)

    //endregion ============ Promotion Operations ============

    //region ============ Stock Operations ============

    /**
     * Ürün için stok durumunu getirir
     */
    fun getStockStatus(product: ProductInformationModel): StockStatus

    //endregion ============ Stock Operations ============

    //region ============ Document Operations ============
    /**
     * Belgeyi oluşturur
     */
    fun toDocument(): Document

    /**
     * Manager'ı temizler
     */
    fun clear()

    fun getCustomer(): SyncCustomerModel
    suspend fun setMasterValues(customerId: Long, documentId: Long): Result<IDocumentManager>

    fun getDocumentMapModel(): SyncDocumentMapModel
    fun  getProductQueryString(): String

    fun getProductUnitMap(): MutableMap<Int, List<ProductUnit>>

    //endregion ============ Document Operations ============
}

//region ============ Sealed Classes ============

/**
 * Satır ekleme sonucu
 */
sealed interface AddLineResult {
    data class Success(val line: IDocumentLine) : AddLineResult
    data class NeedsConfirmation(val pendingLine: PendingLine, val warning: StockValidationResult.Warning) : AddLineResult
    data class Blocked(val error: StockValidationResult.Blocked) : AddLineResult
    object NotFound : AddLineResult
}

/**
 * Satır güncelleme sonucu
 */
sealed interface UpdateLineResult {
    object Success : UpdateLineResult
    data class NeedsConfirmation(val pendingUpdate: PendingUpdate, val warning: StockValidationResult.Warning) : UpdateLineResult
    data class Blocked(val error: StockValidationResult.Blocked) : UpdateLineResult
    object NotFound : UpdateLineResult
}
//endregion ============ Sealed Classes ============