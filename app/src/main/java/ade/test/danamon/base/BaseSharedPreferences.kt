package ade.test.danamon.base

import ade.test.danamon.utils.clear
import ade.test.danamon.utils.copyTo
import ade.test.danamon.utils.remove
import ade.test.danamon.utils.set
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

open class BaseSharedPreferences(val context: Context) {

    private val notEncryptedPreferencesName = "not_encrypted_preferences_filename"
    private val encryptedPreferencesName = "encrypted_preferences_filename"

    private var prefs: SharedPreferences

    init {
        val nonEncryptedPreferences: SharedPreferences =
            context.getSharedPreferences(notEncryptedPreferencesName, Context.MODE_PRIVATE)

        prefs = initializeEncryptedSharedPreferencesManager()
        if (nonEncryptedPreferences.all.isNotEmpty()) {
            // migrate non encrypted shared preferences
            // to encrypted shared preferences and clear them once finished.
            nonEncryptedPreferences.copyTo(prefs)
            nonEncryptedPreferences.clear()
        }
    }

    private fun initializeEncryptedSharedPreferencesManager(): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            encryptedPreferencesName,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun <T : Any?> set(key: String, value: T) {
        prefs.set(key, value)
    }

    fun getString(key: String, defaultValue: String?): String? {
        val value = getValue(key, defaultValue)
        return value as String?
    }

    fun getInt(key: String, defaultValue: Int): Int {
        val value = getValue(key, defaultValue)
        return value as Int
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        val value = getValue(key, defaultValue)
        return value as Boolean
    }

    fun getLong(key: String, defaultValue: Long): Long {
        val value = getValue(key, defaultValue)
        return value as Long
    }

    fun getFloat(key: String, defaultValue: Float): Float {
        val value = getValue(key, defaultValue)
        return value as Float
    }

    fun getValue(key: String, defaultValue: Any?): Any? {
        var value = prefs.all[key]
        value = value ?: defaultValue
        return value
    }

    fun contains(key: String): Boolean {
        return prefs.contains(key)
    }

    fun remove(key: String) {
        prefs.remove(key)
    }

    fun clear() {
        prefs.clear()
    }
}