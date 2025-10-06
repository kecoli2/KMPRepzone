package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.PaymentTransactionLogInformationEntity
import com.repzone.domain.model.PaymentTransactionLogInformationModel

class PaymentTransactionLogInformationEntityDbMapper : Mapper<PaymentTransactionLogInformationEntity, PaymentTransactionLogInformationModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: PaymentTransactionLogInformationEntity): PaymentTransactionLogInformationModel {
        return PaymentTransactionLogInformationModel(
            id = from.Id,
            amount = from.Amount,
            customerId = from.CustomerId,
            direction = from.Direction,
            documentGroup = from.DocumentGroup,
            referenceId = from.ReferenceId,
            transactionDate = from.TransactionDate
        )
    }

    override fun fromDomain(domain: PaymentTransactionLogInformationModel): PaymentTransactionLogInformationEntity {
        return PaymentTransactionLogInformationEntity(
            Id = domain.id,
            Amount = domain.amount,
            CustomerId = domain.customerId,
            Direction = domain.direction,
            DocumentGroup = domain.documentGroup,
            ReferenceId = domain.referenceId,
            TransactionDate = domain.transactionDate
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
