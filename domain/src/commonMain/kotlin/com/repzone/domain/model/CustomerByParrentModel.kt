package com.repzone.domain.model

data class CustomerByParrentModel(val masterCustomer: CustomerItemModel,
                                  val parrentCustomers: List<CustomerItemModel>)
