package com.repzone.data.repository.imp

import com.repzone.core.enums.StateType
import com.repzone.core.interfaces.IUserSession
import com.repzone.database.SyncCustomerEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.select
import com.repzone.domain.repository.IPriceRepository

class PriceRepositoryImpl(private val iDatabaseManager: IDatabaseManager, iUsrSession: IUserSession): IPriceRepository {

    //region Public Method
    override suspend fun checkPriceListOverRules(priceListId: Long, customerId: Long): Boolean {

        val customerInfo = iDatabaseManager.getSqlDriver().select<SyncCustomerEntity> {
            where {
                criteria("Id", equal = customerId)
                criteria("State", notEqual = StateType.DELETED.ordinal)
            }
        }.firstOrNull() ?: return false

        //TODO BU KISIM DB DE YOK
       /* val rules = iDatabaseManager.getSqlDriver().select<MobileSyncPriceListRuleModel> {
            where {
                criteria("PriceListId", equal = priceListId)
                criteria("State", equal = 1L)
            }
        }.toList()*/
        //TODO OLMADIGINDAN DOLAYI BU KISIM YOK

        /*if (rules.isEmpty()) return true*/
        return true
        /*val ruleValidDict = mutableMapOf<MobileSyncPriceListRuleModel, Boolean>()

        for (rule in rules) {
            var valid = false
            val discoverValues = rule.discover.split("|")

            when (rule.property) {
                PriceListRuleProperty.CustomerId -> {
                    valid = if (discoverValues.size > 1) {
                        false
                    } else {
                        val parsedId = discoverValues[0].toLongOrNull()
                        parsedId != null && customerInfo.id == parsedId
                    }
                }

                PriceListRuleProperty.CustomerGroupId -> {
                    valid = if (discoverValues.size > 1) {
                        false
                    } else {
                        val parsedId = discoverValues[0].toLongOrNull()
                        parsedId != null && customerInfo.groupId == parsedId
                    }
                }

                PriceListRuleProperty.CustomerParentId -> {
                    valid = if (discoverValues.size > 1) {
                        false
                    } else {
                        val parsedId = discoverValues[0].toLongOrNull()
                        parsedId != null && customerInfo.parentId == parsedId
                    }
                }

                PriceListRuleProperty.CustomerCustomFieldId -> {
                    valid = if (discoverValues.size > 1) {
                        val fieldId = discoverValues[0].toLongOrNull()
                        if (fieldId != null) {
                            val fields = db.select<MobileSyncCustomerCustomField> {
                                where {
                                    criteria("EntityId", equal = customerId)
                                    criteria("FieldId", equal = fieldId)
                                    criteria("State", equal = 1L)
                                }
                            }.toList()

                            var fieldValid = false
                            for (field in fields) {
                                val value = field.value.lowercase().replaceAllTurkishChars()
                                val discover = discoverValues[1].lowercase().replaceAllTurkishChars()
                                fieldValid = value == discover

                                if (rule.operator == OperatorKind.NotEqual) {
                                    fieldValid = !fieldValid
                                }
                            }
                            fieldValid
                        } else {
                            false
                        }
                    } else {
                        false
                    }
                }

                PriceListRuleProperty.CustomerDynamicListId -> {
                    valid = if (discoverValues.size > 1) {
                        false
                    } else {
                        val parsedId = discoverValues[0].toLongOrNull()
                        if (parsedId != null) {
                            val exists = db.select<DynamicListItems> {
                                where {
                                    criteria("DynamicListId", equal = parsedId)
                                    criteria("EntityId", equal = customerId)
                                    criteria("State", equal = 1L)
                                }
                            }.any()

                            if (rule.operator == OperatorKind.NotEqual) !exists else exists
                        } else {
                            false
                        }
                    }
                }

                PriceListRuleProperty.CustomerOrganizationId -> {
                    valid = if (discoverValues.size > 1) {
                        false
                    } else {
                        val parsedId = discoverValues[0].toLongOrNull()
                        parsedId != null && customerInfo.organizationId == parsedId
                    }
                }

                PriceListRuleProperty.RepresentativeId -> {
                    valid = if (discoverValues.size > 1) {
                        false
                    } else {
                        val parsedId = discoverValues[0].toLongOrNull()
                        parsedId != null && identity.representativeId == parsedId
                    }
                }

                PriceListRuleProperty.RepresentativeGroupId -> {
                    valid = if (discoverValues.size > 1) {
                        false
                    } else {
                        val parsedId = discoverValues[0].toLongOrNull()
                        parsedId != null && identity.groupId == parsedId
                    }
                }

                PriceListRuleProperty.RepresentativeCustomFieldId -> {
                    valid = if (discoverValues.size > 1) {
                        val fieldId = discoverValues[0].toLongOrNull()
                        if (fieldId != null) {
                            val fields = db.select<MobileSyncRepresentativeCustomField> {
                                where {
                                    criteria("EntityId", equal = identity.representativeId)
                                    criteria("FieldId", equal = fieldId)
                                    criteria("State", equal = 1L)
                                }
                            }.toList()

                            var fieldValid = false
                            for (field in fields) {
                                val value = field.value.lowercase().replaceAllTurkishChars()
                                val discover = discoverValues[1].lowercase().replaceAllTurkishChars()
                                fieldValid = value == discover

                                if (rule.operator == OperatorKind.NotEqual) {
                                    fieldValid = !fieldValid
                                }
                            }
                            fieldValid
                        } else {
                            false
                        }
                    } else {
                        false
                    }
                }

                PriceListRuleProperty.RepresentDynamicListId -> {
                    valid = if (discoverValues.size > 1) {
                        false
                    } else {
                        val parsedId = discoverValues[0].toLongOrNull()
                        if (parsedId != null) {
                            val exists = db.select<DynamicListItems> {
                                where {
                                    criteria("DynamicListId", equal = parsedId)
                                    criteria("EntityId", equal = identity.representativeId)
                                    criteria("State", equal = 1L)
                                }
                            }.any()

                            if (rule.operator == OperatorKind.NotEqual) !exists else exists
                        } else {
                            false
                        }
                    }
                }

                PriceListRuleProperty.RepresentativeOrganizationId -> {
                    valid = if (discoverValues.size > 1) {
                        false
                    } else {
                        val parsedId = discoverValues[0].toLongOrNull()
                        parsedId != null && identity.organizationId == parsedId
                    }
                }
            }

            ruleValidDict[rule] = valid
        }

        if (ruleValidDict.isEmpty()) return true

        if (ruleValidDict.size == 1) {
            return ruleValidDict.values.first()
        }

        var result = ruleValidDict.values.first()
        val entries = ruleValidDict.entries.toList()

        for (index in 1 until entries.size) {
            val rule = entries[index].key
            val currentValid = entries[index].value

            result = if (rule.connector == LogicalConnector.Or) {
                result || currentValid
            } else {
                result && currentValid
            }

            if (!result && rule.connector != LogicalConnector.Or) {
                break
            }
        }

        return result*/

    }
    //endregion

    //region Private Method
    //endregion

}