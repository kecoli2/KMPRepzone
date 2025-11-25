package com.repzone.core.util.extensions

import androidx.compose.runtime.Composable
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

fun String.quote() = "'${replace("'", "''")}'"

@Composable
fun StringResource.fromResource(): String{
    return stringResource(this)
}

@Composable
fun StringResource.fromResource(vararg formatArgs: Any): String{
    return stringResource(this, formatArgs)
}

fun String.toBigDecimalOrNull(): BigDecimal? {
    return try {
        BigDecimal.parseString(this)
    }catch (e: Exception){
        null
    }
}