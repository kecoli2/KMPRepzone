package com.repzone.domain.document.service

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.enums.SalesOperationType
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.platform.randomUUID
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toInstant
import com.repzone.domain.common.DomainException
import com.repzone.domain.common.ErrorCode
import com.repzone.domain.document.IPromotionEngine
import com.repzone.domain.document.base.AddLineResult
import com.repzone.domain.document.base.IDocumentLine
import com.repzone.domain.document.base.IDocumentManager
import com.repzone.domain.document.base.UpdateLineResult
import com.repzone.domain.document.model.LineConflict
import com.repzone.domain.document.model.PendingGiftSelection
import com.repzone.domain.document.model.PendingLine
import com.repzone.domain.document.model.ProductInformationModel
import com.repzone.domain.document.model.ProductUnit
import com.repzone.domain.document.model.StockValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.repzone.domain.common.Result
import com.repzone.domain.common.businessRuleException
import com.repzone.domain.common.fold
import com.repzone.domain.document.model.DiscountSlot
import com.repzone.domain.document.model.DiscountType
import com.repzone.domain.document.model.Document
import com.repzone.domain.document.model.DocumentLine
import com.repzone.domain.document.model.DocumentType
import com.repzone.domain.document.model.GiftSelection
import com.repzone.domain.document.model.PromotionContext
import com.repzone.domain.document.model.StockStatus
import com.repzone.domain.model.DistributionControllerModel
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.model.SyncDocumentMapModel
import com.repzone.domain.repository.ICustomerRepository
import com.repzone.domain.repository.IDistributionRepository
import com.repzone.domain.repository.IDocumentMapRepository
import com.repzone.domain.repository.IProductRepository
import com.repzone.domain.util.ProductQueryBuilder
import kotlin.time.ExperimentalTime


/**
 * Belge yöneticisi implementation
 */
@OptIn(ExperimentalTime::class)
class DocumentManager(override val documentType: DocumentType,
                      private val promotionEngine: IPromotionEngine,
                      private val stockValidator: StockValidator,
                      private val lineCalculator: LineDiscountCalculator,
                      private val iCustomerRepository: ICustomerRepository,
                      private val iDocumentMapRepository: IDocumentMapRepository,
                      private val iDistributionRepository: IDistributionRepository,
                      private val iUserSession: IUserSession,
                      private val iProductRepository: IProductRepository
) : IDocumentManager {
    //region Fields
    private var currentCustomer: SyncCustomerModel? = null
    private var documentMapModel: SyncDocumentMapModel? = null
    private var activeDistribution: DistributionControllerModel? = null
    private var productQuery: String? = null
    private var productUnitMap: MutableMap<Int, List<ProductUnit>>? = null
    private val _lines = MutableStateFlow<List<IDocumentLine>>(emptyList())
    override val lines: StateFlow<List<IDocumentLine>> = _lines.asStateFlow()
    private val _pendingConflicts = MutableStateFlow<List<LineConflict>>(emptyList())
    override val pendingConflicts: StateFlow<List<LineConflict>> = _pendingConflicts.asStateFlow()
    private val _pendingGiftSelections = MutableStateFlow<List<PendingGiftSelection>>(emptyList())
    override val pendingGiftSelections: StateFlow<List<PendingGiftSelection>> = _pendingGiftSelections.asStateFlow()
    //endregion Fields

    //region Public Method

    //region ============ Document Operations ============
    override fun toDocument(): Document {
        return Document(
            id = randomUUID(),
            type = documentType,
            number = null,
            customer = currentCustomer!!,
            lines = _lines.value,
            createdAt = now().toInstant(),
            updatedAt = now().toInstant()
        )
    }

    override fun clear() {
        _lines.value = emptyList()
        _pendingConflicts.value = emptyList()
        _pendingGiftSelections.value = emptyList()
        currentCustomer = null
        documentMapModel = null
        activeDistribution = null
        productUnitMap?.clear()
        productUnitMap = null
    }

    override fun getCustomer(): SyncCustomerModel {
        return currentCustomer!!
    }

    override suspend fun setMasterValues(customerId: Long, documentId: Long): Result<IDocumentManager> {
        try {
            currentCustomer = iCustomerRepository.getById(customerId)
            documentMapModel = iDocumentMapRepository.get(documentId.toInt(), currentCustomer!!.organizationId?.toInt() ?: 0)
            activeDistribution = iDistributionRepository.getActiveDistributionListId(currentCustomer, iUserSession.decideWhichOrgIdToBeUsed(currentCustomer!!.organizationId?.toInt() ?: 0))!!
            prepareProductQueryBuilder()
            return Result.Success(this)
        }catch (ex: Exception){
            return Result.Error(DomainException.UnknownException(cause = ex))
        }
    }

    override fun getDocumentMapModel(): SyncDocumentMapModel {
        return documentMapModel!!
    }

    override fun getProductQueryString(): String {
        return productQuery!!
    }
    //endregion ============ Document Operations ============

    //region ============ Line Operations ============

    override suspend fun addLine(product: ProductInformationModel, unit: ProductUnit, quantity: BigDecimal): AddLineResult {

        // Stok kontrolü
        val validation = stockValidator.validate(
            product = product,
            newQuantity = quantity,
            newUnit = unit,
            existingLines = _lines.value,
            currentLineId = null,
            documentType = documentType
        )

        return when (validation) {
            is StockValidationResult.Valid -> {
                val line = createLine(product, unit, quantity, product.vat)
                _lines.value = _lines.value + line
                recalculatePromotions()
                AddLineResult.Success(line)
            }
            is StockValidationResult.Warning -> {
                AddLineResult.NeedsConfirmation(
                    pendingLine = PendingLine(product, unit, quantity),
                    warning = validation
                )
            }
            is StockValidationResult.Blocked -> {
                AddLineResult.Blocked(validation)
            }
        }
    }

    override suspend fun updateLine(lineId: String, newUnit: ProductUnit, newQuantity: BigDecimal): UpdateLineResult {
        val existingLine = _lines.value.find { it.id == lineId }
            ?: return UpdateLineResult.NotFound

        // Product bilgisi gerekli - TODO: Product cache veya repository
        // Şimdilik simplified
        return UpdateLineResult.Success
    }

    override suspend fun removeLine(lineId: String) {
        _lines.value = _lines.value.filter { it.id != lineId }
        recalculatePromotions()
    }
    //endregion ============ Line Operations ============

    //region ============ Discount Operations ============

    override suspend fun applyManualDiscount(lineId: String, slotNumber: Int, type: DiscountType, value: BigDecimal): Result<Unit> {
        return try {
            val lineIndex = _lines.value.indexOfFirst { it.id == lineId }
            if (lineIndex < 0) return Result.Error(businessRuleException(ErrorCode.ERROR_LINE_NOT_FOUND))

            val line = _lines.value[lineIndex]
            val result = lineCalculator.applyManualDiscount(line, slotNumber, type, value)

            result.fold(
                onSuccess = { updatedLine ->
                    _lines.value = _lines.value.toMutableList().apply {
                        set(lineIndex, updatedLine)
                    }
                    Result.Success(Unit)
                },
                onError = { error ->
                    Result.Error(error)
                }
            )
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun clearManualDiscount(lineId: String, slotNumber: Int): Result<Unit> {
        return try {
            val lineIndex = _lines.value.indexOfFirst { it.id == lineId }
            if (lineIndex < 0) return Result.Error(businessRuleException(ErrorCode.ERROR_LINE_NOT_FOUND))

            val line = _lines.value[lineIndex]
            val updatedLine = line.withSlot(slotNumber, DiscountSlot.Empty)

            _lines.value = _lines.value.toMutableList().apply {
                set(lineIndex, updatedLine)
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }
    //endregion ============ Discount Operations ============

    //region ============ Promotion Operations ============
    override suspend fun recalculatePromotions() {
        val context = buildPromotionContext()
        val result = promotionEngine.evaluate(context)

        // İskontola uygula
        _lines.value = _lines.value.map { line ->
            val evaluation = result.lineDiscounts[line.id] ?: return@map line
            var updatedLine = line

            evaluation.autoApplied.forEach { (slot, discount) ->
                // Manuel değilse üzerine yaz
                val currentSlot = line.getSlot(slot)
                if (currentSlot !is DiscountSlot.Applied || !currentSlot.isManual) {
                    updatedLine = updatedLine.withSlot(slot, discount)
                }
            }

            updatedLine
        }

        // Conflict'leri güncelle
        _pendingConflicts.value = result.conflicts

        // Hediye seçimlerini güncelle
        _pendingGiftSelections.value = result.pendingSelections
    }

    override suspend fun resolveConflict(lineId: String, slotNumber: Int, selectedRuleId: String) {
        val conflict = _pendingConflicts.value
            .find { it.lineId == lineId }
            ?.conflicts
            ?.find { it.slotNumber == slotNumber }
            ?: return

        val selectedOption = conflict.options.find { it.ruleId == selectedRuleId }
            ?: return

        val lineIndex = _lines.value.indexOfFirst { it.id == lineId }
        if (lineIndex >= 0) {
            val slot = DiscountSlot.Applied(
                ruleId = selectedOption.ruleId,
                type = selectedOption.discountType,
                value = selectedOption.value,
                description = selectedOption.ruleName,
                calculationMode = null,
                isManual = false
            )
            _lines.value = _lines.value.toMutableList().apply {
                set(lineIndex, get(lineIndex).withSlot(slotNumber, slot))
            }
        }

        // Conflict'i kaldır
        removeConflict(lineId, slotNumber)
    }

    override suspend fun confirmGiftSelection(ruleId: String, selections: List<GiftSelection>) {
        // TODO: Hediye satırlarını ekle
        _pendingGiftSelections.value = _pendingGiftSelections.value.filter { it.ruleId != ruleId }
    }
    //endregion ============ Promotion Operations ============

    //region ============ Stock Operations ============
    override fun getStockStatus(product: ProductInformationModel): StockStatus {
        // TODO: Implement
        return StockStatus(
            totalStock = product.stock,
            usedStock = BigDecimal.ZERO,
            remainingStock = product.stock,
            stockUnit = product.baseUnit.unitName,
            lineBreakdown = emptyList()
        )
    }
    //endregion ============ Stock Operations ============
    //endregion Public Method

    //region Private Method
    //region ============ Private Helpers ============
    private fun createLine(product: ProductInformationModel, unit: ProductUnit, quantity: BigDecimal, vatTate: BigDecimal): DocumentLine {
        return DocumentLine(
            id = randomUUID(),
            productId = product.id,
            productName = product.name,
            unitId = unit.unitId,
            unitName = unit.unitName,
            conversionFactor = unit.multiplier,
            quantity = quantity,
            unitPrice = unit.price,
            vatRate = vatTate
        )
    }
    private fun buildPromotionContext(): PromotionContext {
        return PromotionContext(
            currentLine = null,
            allLines = _lines.value,
            documentTotal = _lines.value.fold(BigDecimal.ZERO) { acc, line -> acc + line.lineTotal },
            customer = currentCustomer!!,
            documentType = documentType,
            customerPurchaseHistory = null
        )
    }
    private fun removeConflict(lineId: String, slotNumber: Int) {
        _pendingConflicts.value = _pendingConflicts.value.map { lineConflict ->
            if (lineConflict.lineId == lineId) {
                lineConflict.copy(
                    conflicts = lineConflict.conflicts.filter { it.slotNumber != slotNumber }
                )
            } else {
                lineConflict
            }
        }.filter { it.conflicts.isNotEmpty() }
    }
    private suspend fun prepareProductQueryBuilder(){
        if(productQuery == null){
            val productQueryParams = iProductRepository.getProductQueryParams(
                salesOperationType = SalesOperationType.SALES,
                currentCustomer = currentCustomer!!,
                customerOrgId = currentCustomer!!.organizationId!!.toInt(),
                distController = activeDistribution!!,
                mfrId = 0,
                notAllowedMfrs = null,
                selectedPrefOrgId = 0
            )
            productQuery = ProductQueryBuilder().buildAllProductsQuery(productQueryParams)
        }

        /*if(productUnitMap == null){

        }*/
    }

    //endregion ============ Private Helpers ============
    //endregion Private Method
}



































