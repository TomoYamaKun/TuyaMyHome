//app/src/main/java/com/tuya/myhome/papa/tuya/TuyaSdkManager.kt
//ver 1.01-01

package com.tuya.myhome.papa.tuya

import android.app.Application
import com.thingclips.smart.home.sdk.ThingHomeSdk

object TuyaSdkManager {

    enum class InitStatus {

        NOT_INITIALIZED,

        INITIALIZING,

        SUCCESS,

        FAILED,

        CONFIG_NOT_FOUND
    }


    private var currentStatus =
        InitStatus.NOT_INITIALIZED


    private var errorMessage = ""


    fun initialize(
        application: Application,
        appKey: String,
        appSecret: String
    ): Boolean {

        if (currentStatus == InitStatus.SUCCESS) {
            return true
        }


        if (
            appKey.isBlank() ||
            appSecret.isBlank()
        ) {

            currentStatus =
                InitStatus.CONFIG_NOT_FOUND

            errorMessage =
                "App Key または App Secret が未設定です"

            return false
        }


        return try {

            currentStatus =
                InitStatus.INITIALIZING


            ThingHomeSdk.setDebugMode(true)


            ThingHomeSdk.init(
                application,
                appKey,
                appSecret
            )


            currentStatus =
                InitStatus.SUCCESS


            errorMessage = ""


            true

        } catch (e: Exception) {

            currentStatus =
                InitStatus.FAILED


            errorMessage =
                e.message ?: "不明なエラー"


            false
        }
    }


    fun getStatus(): InitStatus {

        return currentStatus
    }


    fun getStatusText(): String {

        return when (currentStatus) {

            InitStatus.NOT_INITIALIZED ->
                "SDK未初期化"

            InitStatus.INITIALIZING ->
                "SDK初期化中"

            InitStatus.SUCCESS ->
                "SDK初期化成功"

            InitStatus.FAILED ->
                "SDK初期化失敗"

            InitStatus.CONFIG_NOT_FOUND ->
                "SDK設定未入力"
        }
    }


    fun getErrorMessage(): String {

        return errorMessage
    }
}