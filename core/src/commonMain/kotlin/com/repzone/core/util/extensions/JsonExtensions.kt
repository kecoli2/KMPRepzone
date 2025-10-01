package com.repzone.core.util

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * JSON parsing için yapılandırılmış Json instance
 */
val jsonParser = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
    prettyPrint = false
    coerceInputValues = true
}

/**
 * Pretty print için yapılandırılmış Json instance
 */
val prettyJsonParser = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
    prettyPrint = true
    coerceInputValues = true
}

/**
 * JSON string'ini belirtilen model tipine cast eder
 *
 * @param T Hedef model tipi (Serializable olmalı)
 * @return Başarılı ise model instance'ı, hata durumunda null
 *
 * Örnek kullanım:
 * ```
 * val jsonString = """{"name":"John","age":30}"""
 * val user = jsonString.toModel<User>()
 * ```
 */
inline fun <reified T> String.toModel(): T? {
    return try {
        jsonParser.decodeFromString<T>(this)
    } catch (e: SerializationException) {
        println("JSON Serialization Error: ${e.message}")
        null
    } catch (e: IllegalArgumentException) {
        println("JSON Parse Error: ${e.message}")
        null
    } catch (e: Exception) {
        println("Unexpected Error: ${e.message}")
        null
    }
}

/**
 * JSON string'ini belirtilen model tipine cast eder (exception fırlatır)
 *
 * @param T Hedef model tipi (Serializable olmalı)
 * @return Model instance'ı
 * @throws SerializationException Parse hatası durumunda
 *
 * Örnek kullanım:
 * ```
 * try {
 *     val user = jsonString.toModelOrThrow<User>()
 * } catch (e: SerializationException) {
 *     // Hata yönetimi
 * }
 * ```
 */
inline fun <reified T> String.toModelOrThrow(): T {
    return jsonParser.decodeFromString(this)
}

/**
 * JSON string'ini belirtilen model tipine cast eder
 * Hata durumunda default değer döner
 *
 * @param T Hedef model tipi (Serializable olmalı)
 * @param default Hata durumunda dönecek değer
 * @return Model instance'ı veya default değer
 *
 * Örnek kullanım:
 * ```
 * val user = jsonString.toModelOrDefault(User(name = "Unknown"))
 * ```
 */
inline fun <reified T> String.toModelOrDefault(default: T): T {
    return toModel() ?: default
}

/**
 * JSON string'ini belirtilen model tipine cast eder
 * Hata durumunda lambda fonksiyonundan değer döner
 *
 * @param T Hedef model tipi (Serializable olmalı)
 * @param onError Hata durumunda çalışacak lambda
 * @return Model instance'ı veya lambda'dan dönen değer
 *
 * Örnek kullanım:
 * ```
 * val user = jsonString.toModelOrElse {
 *     User(name = "Error", age = 0)
 * }
 * ```
 */
inline fun <reified T> String.toModelOrElse(onError: (Exception) -> T): T {
    return try {
        jsonParser.decodeFromString(this)
    } catch (e: Exception) {
        onError(e)
    }
}

/**
 * JSON string'inin geçerli olup olmadığını kontrol eder
 *
 * @return JSON geçerli ise true
 */
fun String.isValidJson(): Boolean {
    return try {
        jsonParser.parseToJsonElement(this)
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * JSON array string'ini liste olarak parse eder
 *
 * @param T Liste elemanlarının tipi
 * @return Parse edilmiş liste veya null
 *
 * Örnek kullanım:
 * ```
 * val jsonArray = """[{"name":"John"},{"name":"Jane"}]"""
 * val users = jsonArray.toModelList<User>()
 * ```
 */
inline fun <reified T> String.toModelList(): List<T>? {
    return try {
        jsonParser.decodeFromString<List<T>>(this)
    } catch (e: Exception) {
        println("JSON List Parse Error: ${e.message}")
        null
    }
}

// ============================================
// MODEL TO JSON EXTENSIONS
// ============================================

/**
 * Model'i JSON string'ine çevirir
 *
 * @param T Model tipi (Serializable olmalı)
 * @return JSON string veya null
 *
 * Örnek kullanım:
 * ```
 * val user = User(name = "John", age = 30)
 * val jsonString = user.toJson()
 * ```
 */
inline fun <reified T> T.toJson(): String? {
    return try {
        jsonParser.encodeToString(kotlinx.serialization.serializer(), this)
    } catch (e: SerializationException) {
        println("JSON Serialization Error: ${e.message}")
        null
    } catch (e: Exception) {
        println("Unexpected Error: ${e.message}")
        null
    }
}

/**
 * Model'i JSON string'ine çevirir (exception fırlatır)
 *
 * @param T Model tipi (Serializable olmalı)
 * @return JSON string
 * @throws SerializationException Serialization hatası durumunda
 */
inline fun <reified T> T.toJsonOrThrow(): String {
    return jsonParser.encodeToString(kotlinx.serialization.serializer(), this)
}

/**
 * Model'i pretty (okunabilir) JSON string'ine çevirir
 *
 * @param T Model tipi (Serializable olmalı)
 * @return Formatlanmış JSON string veya null
 *
 * Örnek kullanım:
 * ```
 * val user = User(name = "John", age = 30)
 * val prettyJson = user.toPrettyJson()
 * // {
 * //   "name": "John",
 * //   "age": 30
 * // }
 * ```
 */
inline fun <reified T> T.toPrettyJson(): String? {
    return try {
        prettyJsonParser.encodeToString(kotlinx.serialization.serializer(), this)
    } catch (e: Exception) {
        println("JSON Pretty Print Error: ${e.message}")
        null
    }
}

/**
 * Model'i JSON string'ine çevirir
 * Hata durumunda default string döner
 *
 * @param T Model tipi (Serializable olmalı)
 * @param default Hata durumunda dönecek string
 * @return JSON string veya default değer
 */
inline fun <reified T> T.toJsonOrDefault(default: String = "{}"): String {
    return toJson() ?: default
}

/**
 * Model'i JSON string'ine çevirir
 * Hata durumunda lambda fonksiyonundan string döner
 *
 * @param T Model tipi (Serializable olmalı)
 * @param onError Hata durumunda çalışacak lambda
 * @return JSON string veya lambda'dan dönen değer
 */
inline fun <reified T> T.toJsonOrElse(onError: (Exception) -> String): String {
    return try {
        jsonParser.encodeToString(kotlinx.serialization.serializer(), this)
    } catch (e: Exception) {
        onError(e)
    }
}

/**
 * Liste'yi JSON array string'ine çevirir
 *
 * @param T Liste elemanlarının tipi
 * @return JSON array string veya null
 *
 * Örnek kullanım:
 * ```
 * val users = listOf(User("John", 30), User("Jane", 25))
 * val jsonArray = users.toJsonArray()
 * ```
 */
inline fun <reified T> List<T>.toJsonArray(): String? {
    return try {
        jsonParser.encodeToString(kotlinx.serialization.serializer(), this)
    } catch (e: Exception) {
        println("JSON Array Serialization Error: ${e.message}")
        null
    }
}

/**
 * Liste'yi pretty JSON array string'ine çevirir
 */
inline fun <reified T> List<T>.toPrettyJsonArray(): String? {
    return try {
        prettyJsonParser.encodeToString(kotlinx.serialization.serializer(), this)
    } catch (e: Exception) {
        println("JSON Array Pretty Print Error: ${e.message}")
        null
    }
}