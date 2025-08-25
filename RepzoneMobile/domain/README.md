domain/
└─ src/
    └─ commonMain/
        └─ kotlin/
            └─ com/repzone/domain/
                ├─ model/            # Entity & Value Object'ler
                │   ├─ Customer.kt
                │   ├─ Product.kt
                │   ├─ Order.kt
                │   └─ vo/
                │       ├─ Money.kt
                │       ├─ Quantity.kt
                │       └─ GeoLocation.kt
                │
                ├─ repository/       # Repository arayüzleri
                │   ├─ CustomerRepository.kt
                │   ├─ OrderRepository.kt
                │   ├─ InventoryRepository.kt
                │   └─ VisitRepository.kt
                │
                ├─ usecase/          # İş kuralları (1 sınıf = 1 iş)
                │   ├─ PlaceOrderUseCase.kt
                │   ├─ CollectPaymentUseCase.kt
                │   ├─ GetDailyRouteUseCase.kt
                │   └─ SyncPendingOperationsUseCase.kt
                │
                ├─ error/            # Domain seviyesindeki özel hatalar
                │   ├─ DomainException.kt
                │   ├─ ValidationException.kt
                │   └─ BusinessRuleException.kt
                │
                └─ di/               # (Opsiyonel) Domain bağımsız Koin module
                └─ DomainModule.kt


📌 Açıklamalar

model/
    1-) Entity: Kimliği (ID) olan iş nesneleri (Customer, Product, Order).
    2-) Value Object (vo/): Kimliği olmayan, değerle tanımlanan tipler (Money, Quantity, GeoLocation).
    3-) Hepsi immutable (data class) ve sadece iş kurallarına göre davranır.

repository/
    1-)Verinin nasıl geldiğini bilmez; sadece neyi istediğini tanımlar.

Örn:
interface OrderRepository {
suspend fun save(order: Order)
suspend fun getById(id: String): Order?
suspend fun pendingSync(): List<Order>
}

usecase/
    1-) Tek bir iş akışı = tek bir sınıf (Clean Architecture “Interactor”).
    2-) Genelde operator fun invoke(...) ile çağrılır.

Örn: PlaceOrderUseCase → müşteri kredi limiti aşılmasın kuralını uygular.

error/
    1-) Domain seviyesinde özel exception tipleri.
Örn: CreditLimitExceededException : DomainException()
    2-) UI veya Data katmanı değil, tamamen iş kuralları için.

di/ (opsiyonel)
    1-) Domain’in kendi UseCase’lerini Koin/Hilt modülü olarak tanımlayabilirsin.

Mesela:
val DomainModule = module {
factory { PlaceOrderUseCase(get(), get()) }
factory { CollectPaymentUseCase(get()) }
}


Ama Domain içinde DI zorunlu değil; Sonrasında geliştirebilirz.

✅ Özet
model/ → Entity/VO
repository/ → interface
usecase/ → iş akışları
error/ → domain hataları
di/ → opsiyonel Koin/Hilt modülü

Bu yapı test edilebilirliği yüksek, UI/Data’dan bağımsız, temiz bir Domain katmanı sağlar.