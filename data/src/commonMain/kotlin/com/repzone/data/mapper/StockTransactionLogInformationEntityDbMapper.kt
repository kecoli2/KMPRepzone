package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.StockTransactionLogInformationEntity
import com.repzone.domain.model.StockTransactionLogInformationModel

class StockTransactionLogInformationEntityDbMapper : Mapper<StockTransactionLogInformationEntity, StockTransactionLogInformationModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: StockTransactionLogInformationEntity): StockTransactionLogInformationModel {
        return StockTransactionLogInformationModel(
            id = from.Id,
            amount = from.Amount,
            direction = from.Direction,
            documentGroup = from.DocumentGroup,
            productId = from.ProductId,
            referenceId = from.ReferenceId,
            transactionDate = from.TransactionDate,
            warehouseType = from.WarehouseType
        )
    }

    override fun fromDomain(domain: StockTransactionLogInformationModel): StockTransactionLogInformationEntity {
        return StockTransactionLogInformationEntity(
            Id = domain.id,
            Amount = domain.amount,
            Direction = domain.direction,
            DocumentGroup = domain.documentGroup,
            ProductId = domain.productId,
            ReferenceId = domain.referenceId,
            TransactionDate = domain.transactionDate,
            WarehouseType = domain.warehouseType
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
