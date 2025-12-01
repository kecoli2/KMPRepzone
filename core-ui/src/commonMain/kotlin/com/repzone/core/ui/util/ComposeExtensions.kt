package com.repzone.core.ui.util

import androidx.compose.runtime.Composable
import com.repzone.core.enums.DocumentActionType
import com.repzone.core.util.extensions.fromResource
import com.repzone.domain.util.models.VisitActionItem
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.assetspurchasedispatch
import repzonemobile.core.generated.resources.assetspurchaseelectronicdispatch
import repzonemobile.core.generated.resources.assetspurchaseelectronicinvoice
import repzonemobile.core.generated.resources.assetspurchaseelectronicorder
import repzonemobile.core.generated.resources.assetspurchaseinvoice
import repzonemobile.core.generated.resources.assetspurchasereturndispatch
import repzonemobile.core.generated.resources.assetspurchasereturnelectronicdispatch
import repzonemobile.core.generated.resources.assetspurchasereturnelectronicinvoice
import repzonemobile.core.generated.resources.assetspurchasereturnelectronicorder
import repzonemobile.core.generated.resources.assetspurchasereturnorder
import repzonemobile.core.generated.resources.collectionbill
import repzonemobile.core.generated.resources.collectioncash
import repzonemobile.core.generated.resources.collectioncheque
import repzonemobile.core.generated.resources.collectioncreditcard
import repzonemobile.core.generated.resources.collectionmoneyorder
import repzonemobile.core.generated.resources.creditadvice
import repzonemobile.core.generated.resources.damaged_return_electronic_dispatch
import repzonemobile.core.generated.resources.damaged_return_electronic_order
import repzonemobile.core.generated.resources.damagedreturnorder
import repzonemobile.core.generated.resources.debitadvice
import repzonemobile.core.generated.resources.e_invoice_damaged_return
import repzonemobile.core.generated.resources.e_invoice_one_to_one_damaged_return
import repzonemobile.core.generated.resources.e_invoice_vehicle_return
import repzonemobile.core.generated.resources.electronicdispatch
import repzonemobile.core.generated.resources.electronicinvoice
import repzonemobile.core.generated.resources.electronicorder
import repzonemobile.core.generated.resources.givenorder
import repzonemobile.core.generated.resources.invoicedamagedreturn
import repzonemobile.core.generated.resources.invoiceonetoonedamagedreturn
import repzonemobile.core.generated.resources.invoiceonetoonereturn
import repzonemobile.core.generated.resources.invoicevehiclereturn
import repzonemobile.core.generated.resources.nameddeliveryorder
import repzonemobile.core.generated.resources.receivedorder
import repzonemobile.core.generated.resources.returnassetspurchaseinvoice
import repzonemobile.core.generated.resources.returnelectronicdispatch
import repzonemobile.core.generated.resources.returnelectronicinvoice
import repzonemobile.core.generated.resources.returnelectronicorder
import repzonemobile.core.generated.resources.returnwholesaledispatch
import repzonemobile.core.generated.resources.returnwholesaleinvoice
import repzonemobile.core.generated.resources.salesreturnsorder
import repzonemobile.core.generated.resources.wholesaledispatch
import repzonemobile.core.generated.resources.wholesaleinvoice

@Composable
public fun String.getDocumentNameForResource(): String {
    return  when(this){
        "ReceivedOrder" -> Res.string.receivedorder.fromResource()
        "ElectronicOrder" -> Res.string.electronicorder.fromResource()
        "SalesReturnsOrder" -> Res.string.salesreturnsorder.fromResource()
        "DamagedReturnOrder" -> Res.string.damagedreturnorder.fromResource()
        "ReturnElectronicOrder" -> Res.string.returnelectronicorder.fromResource()
        "ReturnWholesaleInvoice" -> Res.string.returnwholesaleinvoice.fromResource()
        "WholesaleInvoice" -> Res.string.wholesaleinvoice.fromResource()
        "ElectronicInvoice" -> Res.string.electronicinvoice.fromResource()
        "ReturnElectronicInvoice" -> Res.string.returnelectronicinvoice.fromResource()
        "WholesaleDispatch" -> Res.string.wholesaledispatch.fromResource()
        "ReturnWholesaleDispatch" -> Res.string.returnwholesaledispatch.fromResource()
        "ElectronicDispatch" -> Res.string.electronicdispatch.fromResource()
        "ReturnElectronicDispatch" -> Res.string.returnelectronicdispatch.fromResource()
        "CollectionBill" -> Res.string.collectionbill.fromResource()
        "CollectionCash" -> Res.string.collectioncash.fromResource()
        "CollectionCheque" -> Res.string.collectioncheque.fromResource()
        "CollectionCreditCard" -> Res.string.collectioncreditcard.fromResource()
        "CollectionMoneyOrder" -> Res.string.collectionmoneyorder.fromResource()
        "DebitAdvice" -> Res.string.debitadvice.fromResource()
        "CreditAdvice" -> Res.string.creditadvice.fromResource()
        "ReturnAssetsPurchaseInvoice" -> Res.string.returnassetspurchaseinvoice.fromResource()
        "AssetsPurchaseInvoice" -> Res.string.assetspurchaseinvoice.fromResource()
        "AssetsPurchaseElectronicInvoice" -> Res.string.assetspurchaseelectronicinvoice.fromResource()
        "AssetsPurchaseReturnElectronicInvoice" -> Res.string.assetspurchasereturnelectronicinvoice.fromResource()
        "GivenOrder" -> Res.string.givenorder.fromResource()
        "AssetsPurchaseElectronicOrder" -> Res.string.assetspurchaseelectronicorder.fromResource()
        "AssetsPurchaseReturnElectronicOrder" -> Res.string.assetspurchasereturnelectronicorder.fromResource()
        "AssetsPurchaseReturnOrder" -> Res.string.assetspurchasereturnorder.fromResource()
        "AssetsPurchaseReturnDispatch" -> Res.string.assetspurchasereturndispatch.fromResource()
        "AssetsPurchaseDispatch" -> Res.string.assetspurchasedispatch.fromResource()
        "AssetsPurchaseElectronicDispatch" -> Res.string.assetspurchaseelectronicdispatch.fromResource()
        "AssetsPurchaseReturnElectronicDispatch" -> Res.string.assetspurchasereturnelectronicdispatch.fromResource()
        "InvoiceVehicleReturn" -> Res.string.invoicevehiclereturn.fromResource()
        "InvoiceOneToOneReturn" -> Res.string.invoiceonetoonereturn.fromResource()
        "InvoiceDamagedReturn" -> Res.string.invoicedamagedreturn.fromResource()
        "InvoiceOneToOneDamagedReturn" -> Res.string.invoiceonetoonedamagedreturn.fromResource()
        "NamedDeliveryOrder" -> Res.string.nameddeliveryorder.fromResource()
        "DamagedReturnElectronicOrder" -> Res.string.damaged_return_electronic_order.fromResource()
        "EInvoiceVehicleReturn" -> Res.string.e_invoice_vehicle_return.fromResource()
        "EInvoiceDamagedReturn" -> Res.string.e_invoice_damaged_return.fromResource()
        "EInvoiceOneToOneDamagedReturn" -> Res.string.e_invoice_one_to_one_damaged_return.fromResource()
        "DamagedReturnElectronicDispatch" -> Res.string.damaged_return_electronic_dispatch.fromResource()

        else -> {
           return this
        }
    }

}