//app/src/main/java/com/tuya/myhome/papa/app/TuyaMyHomeApplication.kt
//ver 1.01-01

package com.tuya.myhome.papa.app

import android.app.Application
import com.tuya.myhome.papa.config.TuyaConfigManager
import com.tuya.myhome.papa.tuya.TuyaSdkManager

class TuyaMyHomeApplication : Application() {

    override fun onCreate() {

        super.onCreate()


        initializeTuyaSdk()
    }


    private fun initializeTuyaSdk() {

        val configManager =
            TuyaConfigManager(this)


        val appKey =
            configManager.getAppKey()


        val appSecret =
            configManager.getAppSecret()


        if (!configManager.isConfigured()) {

            return
        }


        TuyaSdkManager.initialize(
            application = this,
            appKey = appKey,
            appSecret = appSecret
        )
    }
}