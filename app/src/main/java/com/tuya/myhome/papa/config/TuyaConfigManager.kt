//app/src/main/java/com/tuya/myhome/papa/config/TuyaConfigManager.kt
//ver 1.01-00

package com.tuya.myhome.papa.config

import android.content.Context

class TuyaConfigManager(context: Context) {

    companion object {

        private const val PREF_NAME = "tuya_config"

        private const val KEY_APP_KEY = "app_key"
        private const val KEY_APP_SECRET = "app_secret"
        private const val KEY_DATA_CENTER = "data_center"
        private const val KEY_SHA256 = "sha256"
    }

    private val preferences =
        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )


    fun saveConfig(
        appKey: String,
        appSecret: String,
        dataCenter: String,
        sha256: String
    ): Boolean {

        return preferences.edit()
            .putString(KEY_APP_KEY, appKey.trim())
            .putString(KEY_APP_SECRET, appSecret.trim())
            .putString(KEY_DATA_CENTER, dataCenter.trim())
            .putString(KEY_SHA256, sha256.trim())
            .commit()
    }


    fun getAppKey(): String {

        return preferences.getString(
            KEY_APP_KEY,
            ""
        ) ?: ""
    }


    fun getAppSecret(): String {

        return preferences.getString(
            KEY_APP_SECRET,
            ""
        ) ?: ""
    }


    fun getDataCenter(): String {

        return preferences.getString(
            KEY_DATA_CENTER,
            ""
        ) ?: ""
    }


    fun getSha256(): String {

        return preferences.getString(
            KEY_SHA256,
            ""
        ) ?: ""
    }


    fun isConfigured(): Boolean {

        return getAppKey().isNotBlank() &&
                getAppSecret().isNotBlank() &&
                getDataCenter().isNotBlank()
    }


    fun clearConfig(): Boolean {

        return preferences.edit()
            .clear()
            .commit()
    }
}