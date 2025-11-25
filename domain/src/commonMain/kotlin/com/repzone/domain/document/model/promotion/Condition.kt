package com.repzone.domain.document.model.promotion

import com.repzone.domain.document.model.PromotionContext

/**
 * Promosyon koşulu base interface
 * Her koşul context üzerinde değerlendirilir
 */
interface Condition {
    /**
     * Koşulu değerlendir
     * @param context Değerlendirme konteksti
     * @return true ise koşul sağlanıyor
     */
    fun evaluate(context: PromotionContext): Boolean
}

/**
 * Eşleşme tipi
 */
enum class MatchType {
    /** Herhangi biri eşleşmeli (OR) */
    ANY,
    
    /** Hepsi eşleşmeli (AND) */
    ALL,
    
    /** Hiçbiri eşleşmemeli (NOT) */
    NONE
}

/**
 * Mantıksal operatör
 */
enum class LogicalOperator {
    /** Ve (AND) */
    AND,
    
    /** Veya (OR) */
    OR
}
