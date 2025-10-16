package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.PaymentTransactionLogInformationEntity
import com.repzone.domain.model.PaymentTransactionLogInformationModel

class PaymentTransactionLogInformationEntityDbMapper : Mapper<PaymentTransactionLogInformationEntity, PaymentTransactionLogInformationModel> {
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

}
