// /storage/internal_new/project/TuyaMyHome/app/src/main/java/com/tuya/myhome/papa/MyApplication.kt
// ver 1.01-08

package com.tuya.myhome.papa

import android.app.Application
import android.content.Context
import android.util.Log
import com.thingclips.smart.android.base.ThingSmartSdk
import com.thingclips.smart.home.sdk.ThingHomeSdk

class MyApplication : Application() {
    companion object {
        const val APP_VERSION = "1.01-08"

        const val PREF_NAME = "tuya_settings"
        const val KEY_APP_KEY = "app_key"
        const val KEY_APP_SECRET = "app_secret"
        const val KEY_PROJECT_ID = "project_id"

        const val PREF_DEBUG = "tuya_debug"
        const val KEY_DEBUG_LOG = "debug_log"
        const val KEY_LAST_CRASH = "last_crash"

        private const val TAG = "TUYA_DEBUG"

        @Volatile
        var sdkInitialized = false
            private set

        fun addLog(context: Context, message: String) {
            try {
                val pref = context.getSharedPreferences(
                    PREF_DEBUG,
                    Context.MODE_PRIVATE
                )
                val oldLog = pref.getString(KEY_DEBUG_LOG, "") ?: ""
                val newLog = if (oldLog.isEmpty()) {
                    message
                } else {
                    "$oldLog\n$message"
                }
                pref.edit()
                    .putString(KEY_DEBUG_LOG, newLog)
                    .apply()
                Log.d(TAG, message)
            } catch (e: Throwable) {
                Log.e(TAG, "Failed to save debug log", e)
            }
        }

        fun getLog(context: Context): String {
            return try {
                context.getSharedPreferences(
                    PREF_DEBUG,
                    Context.MODE_PRIVATE
                ).getString(KEY_DEBUG_LOG, "") ?: ""
            } catch (e: Throwable) {
                "Failed to read debug log:\n${e.message}"
            }
        }

        fun clearLog(context: Context) {
            try {
                context.getSharedPreferences(
                    PREF_DEBUG,
                    Context.MODE_PRIVATE
                ).edit()
                    .remove(KEY_DEBUG_LOG)
                    .apply()
            } catch (e: Throwable) {
                Log.e(TAG, "Failed to clear debug log", e)
            }
        }

        fun getLoge(context: Context): String {
            return getLog(context)
        }

        fun clearLoge(context: Context) {
            clearLog(context)
        }

        fun saveCrash(context: Context, throwable: Throwable) {
            try {
                val stackTrace = Log.getStackTraceString(throwable)
                context.getSharedPreferences(
                    PREF_DEBUG,
                    Context.MODE_PRIVATE
                ).edit()
                    .putString(KEY_LAST_CRASH, stackTrace)
                    .apply()

                addLog(context, "========================================")
                addLog(context, "FATAL CRASH DETECTED")
                addLog(context, "APP VERSION = $APP_VERSION")
                addLog(context, "========================================")
                addLog(context, stackTrace)
                addLog(context, "========================================")
            } catch (e: Throwable) {
                Log.e(TAG, "Failed to save crash", e)
            }
        }

        fun getLastCrash(context: Context): String {
            return try {
                context.getSharedPreferences(
                    PREF_DEBUG,
                    Context.MODE_PRIVATE
                ).getString(KEY_LAST_CRASH, "") ?: ""
            } catch (e: Throwable) {
                ""
            }
        }

        fun clearLastCrash(context: Context) {
            try {
                context.getSharedPreferences(
                    PREF_DEBUG,
                    Context.MODE_PRIVATE
                ).edit()
                    .remove(KEY_LAST_CRASH)
                    .apply()
            } catch (e: Throwable) {
                Log.e(TAG, "Failed to clear last crash", e)
            }
        }

        fun saveCrashe(context: Context, throwable: Throwable) {
            saveCrash(context, throwable)
        }

        fun getLastCrashe(context: Context): String {
            return getLastCrash(context)
        }

        fun clearLastCrashe(context: Context) {
            clearLastCrash(context)
        }

        fun getAppKey(context: Context): String {
            return try {
                context.getSharedPreferences(
                    PREF_NAME,
                    Context.MODE_PRIVATE
                ).getString(KEY_APP_KEY, "") ?: ""
            } catch (e: Throwable) {
                addLog(
                    context,
                    "getAppKey ERROR: ${e.javaClass.name}: ${e.message}"
                )
                ""
            }
        }

        fun getAppSecret(context: Context): String {
            return try {
                context.getSharedPreferences(
                    PREF_NAME,
                    Context.MODE_PRIVATE
                ).getString(KEY_APP_SECRET, "") ?: ""
            } catch (e: Throwable) {
                addLog(
                    context,
                    "getAppSecret ERROR: ${e.javaClass.name}: ${e.message}"
                )
                ""
            }
        }

        fun getProjectId(context: Context): String {
            return try {
                context.getSharedPreferences(
                    PREF_NAME,
                    Context.MODE_PRIVATE
                ).getString(KEY_PROJECT_ID, "") ?: ""
            } catch (e: Throwable) {
                addLog(
                    context,
                    "getProjectId ERROR: ${e.javaClass.name}: ${e.message}"
                )
                ""
            }
        }

        fun initializeTuyaSdk(application: Application): Boolean {
            try {
                addLog(application, "========================================")
                addLog(application, "TUYA SDK INITIALIZATION START")
                addLog(application, "APP VERSION = $APP_VERSION")
                addLog(
                    application,
                    "Application class = ${application.javaClass.name}"
                )
                addLog(
                    application,
                    "Application hash = ${System.identityHashCode(application)}"
                )

                val appKey = getAppKey(application).trim()
                val appSecret = getAppSecret(application).trim()
                val projectId = getProjectId(application).trim()

                addLog(application, "AppKey length = ${appKey.length}")
                addLog(application, "AppSecret length = ${appSecret.length}")
                addLog(
                    application,
                    "Project ID = ${
                        if (projectId.isEmpty()) "(empty)" else projectId
                    }"
                )

                if (appKey.isEmpty()) {
                    addLog(application, "ERROR: App Key is empty")
                    addLog(application, "SDK initialization skipped")
                    sdkInitialized = false
                    return false
                }

                if (appSecret.isEmpty()) {
                    addLog(application, "ERROR: App Secret is empty")
                    addLog(application, "SDK initialization skipped")
                    sdkInitialized = false
                    return false
                }

                addLog(application, "Checking SDK state BEFORE init...")

                var beforeApplication: Application? = null

                try {
                    beforeApplication = ThingSmartSdk.getApplication()
                    addLog(
                        application,
                        "BEFORE INIT: ThingSmartSdk.getApplication() = " +
                            if (beforeApplication == null) "NULL" else "NOT_NULL"
                    )

                    if (beforeApplication != null) {
                        addLog(
                            application,
                            "BEFORE INIT Application hash = " +
                                System.identityHashCode(beforeApplication)
                        )
                    }
                } catch (e: Throwable) {
                    addLog(
                        application,
                        "BEFORE INIT SDK CHECK ERROR: " +
                            "${e.javaClass.name}: ${e.message}"
                    )
                }

                var beforeMInit = false

                try {
                    val initField =
                        ThingSmartSdk::class.java.getDeclaredField("mInit")
                    initField.isAccessible = true
                    beforeMInit = initField.getBoolean(null)
                    addLog(
                        application,
                        "BEFORE INIT ThingSmartSdk.mInit = $beforeMInit"
                    )
                } catch (e: Throwable) {
                    addLog(
                        application,
                        "BEFORE INIT mInit CHECK ERROR: " +
                            "${e.javaClass.name}: ${e.message}"
                    )
                }

                try {
                    ThingHomeSdk.setDebugMode(true)
                    addLog(application, "ThingHomeSdk Debug Mode = ON")
                } catch (e: Throwable) {
                    addLog(
                        application,
                        "WARNING: setDebugMode failed: " +
                            "${e.javaClass.name}: ${e.message}"
                    )
                }

                if (beforeApplication == null) {
                    addLog(
                        application,
                        "ThingSmartSdk Application is NULL BEFORE init"
                    )
                    addLog(
                        application,
                        "Calling ThingSmartSdk.init(Application) explicitly"
                    )

                    try {
                        ThingSmartSdk.init(application)
                        addLog(
                            application,
                            "ThingSmartSdk.init(Application) RETURNED"
                        )
                    } catch (e: Throwable) {
                        addLog(
                            application,
                            "ThingSmartSdk.init(Application) ERROR: " +
                                "${e.javaClass.name}: ${e.message}"
                        )
                        addLog(
                            application,
                            Log.getStackTraceString(e)
                        )
                    }
                } else {
                    addLog(
                        application,
                        "ThingSmartSdk Application already exists"
                    )
                }

                try {
                    val afterBaseInitApplication =
                        ThingSmartSdk.getApplication()

                    addLog(
                        application,
                        "AFTER ThingSmartSdk.init: " +
                            "ThingSmartSdk.getApplication() = " +
                            if (afterBaseInitApplication == null) {
                                "NULL"
                            } else {
                                "NOT_NULL"
                            }
                    )

                    if (afterBaseInitApplication != null) {
                        addLog(
                            application,
                            "AFTER ThingSmartSdk.init Application hash = " +
                                System.identityHashCode(
                                    afterBaseInitApplication
                                )
                        )
                    }
                } catch (e: Throwable) {
                    addLog(
                        application,
                        "AFTER ThingSmartSdk.init CHECK ERROR: " +
                            "${e.javaClass.name}: ${e.message}"
                    )
                }

                addLog(application, "========================================")
                addLog(application, "SDK INIT CALL START")
                addLog(application, "APP VERSION = $APP_VERSION")
                addLog(
                    application,
                    "Calling ThingHomeSdk.init(Application, AppKey, AppSecret)"
                )

                try {
                    ThingHomeSdk.init(
                        application,
                        appKey,
                        appSecret
                    )

                    addLog(application, "ThingHomeSdk.init() RETURNED")
                } catch (e: Throwable) {
                    addLog(application, "ThingHomeSdk.init() THREW EXCEPTION")
                    addLog(
                        application,
                        "Exception Type = ${e.javaClass.name}"
                    )
                    addLog(
                        application,
                        "Exception Message = ${e.message ?: "null"}"
                    )
                    addLog(
                        application,
                        Log.getStackTraceString(e)
                    )
                    sdkInitialized = false
                    return false
                }

                try {
                    val afterApplication = ThingSmartSdk.getApplication()

                    addLog(
                        application,
                        "AFTER INIT: ThingSmartSdk.getApplication() = " +
                            if (afterApplication == null) "NULL" else "NOT_NULL"
                    )

                    if (afterApplication != null) {
                        addLog(
                            application,
                            "AFTER INIT Application class = " +
                                afterApplication.javaClass.name
                        )
                        addLog(
                            application,
                            "AFTER INIT Application hash = " +
                                System.identityHashCode(afterApplication)
                        )
                        addLog(
                            application,
                            "EXPECTED Application hash = " +
                                System.identityHashCode(application)
                        )

                        if (afterApplication === application) {
                            addLog(
                                application,
                                "APPLICATION CHECK = SAME INSTANCE"
                            )
                        } else {
                            addLog(
                                application,
                                "WARNING: APPLICATION CHECK = DIFFERENT INSTANCE"
                            )
                        }
                    } else {
                        addLog(
                            application,
                            "ERROR: ThingSmartSdk.getApplication() " +
                                "STILL NULL AFTER INIT"
                        )
                    }
                } catch (e: Throwable) {
                    addLog(
                        application,
                        "AFTER INIT SDK CHECK ERROR: " +
                            "${e.javaClass.name}: ${e.message}"
                    )
                }

                try {
                    val initField =
                        ThingSmartSdk::class.java.getDeclaredField("mInit")
                    initField.isAccessible = true
                    val mInit = initField.getBoolean(null)

                    addLog(
                        application,
                        "AFTER INIT ThingSmartSdk.mInit = $mInit"
                    )

                    if (!beforeMInit && mInit) {
                        addLog(
                            application,
                            "ThingSmartSdk.mInit changed FALSE -> TRUE"
                        )
                    }
                } catch (e: Throwable) {
                    addLog(
                        application,
                        "AFTER INIT mInit CHECK ERROR: " +
                            "${e.javaClass.name}: ${e.message}"
                    )
                }

                val verifiedApplication = try {
                    ThingSmartSdk.getApplication()
                } catch (e: Throwable) {
                    addLog(
                        application,
                        "FINAL SDK APPLICATION CHECK ERROR: " +
                            "${e.javaClass.name}: ${e.message}"
                    )
                    null
                }

                if (verifiedApplication == null) {
                    sdkInitialized = false

                    addLog(application, "========================================")
                    addLog(application, "SDK INITIALIZATION INVALID")
                    addLog(application, "APP VERSION = $APP_VERSION")
                    addLog(
                        application,
                        "ThingSmartSdk.getApplication() = NULL after init"
                    )
                    addLog(
                        application,
                        "This means SDK Application context was NOT registered"
                    )
                    addLog(application, "sdkInitialized = FALSE")
                    addLog(application, "========================================")

                    return false
                }

                sdkInitialized = true

                addLog(application, "========================================")
                addLog(application, "TUYA SDK INITIALIZATION SUCCESS")
                addLog(application, "APP VERSION = $APP_VERSION")
                addLog(application, "SDK INITIALIZED = TRUE")
                addLog(application, "========================================")

                return true
            } catch (e: Throwable) {
                sdkInitialized = false

                addLog(application, "========================================")
                addLog(application, "TUYA SDK INITIALIZATION FAILED")
                addLog(application, "APP VERSION = $APP_VERSION")
                addLog(
                    application,
                    "Exception Type = ${e.javaClass.name}"
                )
                addLog(
                    application,
                    "Exception Message = ${e.message ?: "null"}"
                )
                addLog(
                    application,
                    Log.getStackTraceString(e)
                )
                addLog(application, "sdkInitialized = FALSE")
                addLog(application, "========================================")

                return false
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        val defaultHandler =
            Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            try {
                saveCrash(this, throwable)
            } catch (e: Throwable) {
                Log.e(TAG, "Crash handler failed", e)
            }

            defaultHandler?.uncaughtException(
                Thread.currentThread(),
                throwable
            )
        }

        addLog(this, "========================================")
        addLog(this, "APPLICATION START")
        addLog(this, "APP VERSION = $APP_VERSION")
        addLog(this, "MyApplication.onCreate()")
        addLog(
            this,
            "Application class = ${this.javaClass.name}"
        )
        addLog(
            this,
            "Application hash = ${System.identityHashCode(this)}"
        )

        val appKey = getAppKey(this).trim()
        val appSecret = getAppSecret(this).trim()

        addLog(
            this,
            "Startup AppKey length = ${appKey.length}"
        )
        addLog(
            this,
            "Startup AppSecret length = ${appSecret.length}"
        )

        if (appKey.isEmpty() || appSecret.isEmpty()) {
            addLog(this, "Tuya credentials not configured")
            addLog(
                this,
                "Open Settings and enter AppKey / AppSecret"
            )
            sdkInitialized = false
        } else {
            addLog(this, "Tuya credentials found")
            addLog(
                this,
                "Starting Tuya SDK initialization..."
            )

            val result = initializeTuyaSdk(this)

            addLog(
                this,
                "initializeTuyaSdk() returned = $result"
            )
            addLog(
                this,
                "sdkInitialized = $sdkInitialized"
            )
        }

        try {
            val finalApplication =
                ThingSmartSdk.getApplication()

            addLog(
                this,
                "ONCREATE FINAL CHECK: " +
                    "ThingSmartSdk.getApplication() = " +
                    if (finalApplication == null) {
                        "NULL"
                    } else {
                        "NOT_NULL"
                    }
            )

            if (finalApplication != null) {
                addLog(
                    this,
                    "ONCREATE FINAL Application hash = " +
                        System.identityHashCode(finalApplication)
                )
                addLog(
                    this,
                    "ONCREATE EXPECTED Application hash = " +
                        System.identityHashCode(this)
                )
            }
        } catch (e: Throwable) {
            addLog(
                this,
                "ONCREATE FINAL CHECK ERROR: " +
                    "${e.javaClass.name}: ${e.message}"
            )
        }

        addLog(this, "========================================")
    }

    override fun onTerminate() {
        try {
            if (sdkInitialized) {
                ThingHomeSdk.onDestroy()
                sdkInitialized = false
                addLog(this, "ThingHomeSdk.onDestroy()")
                addLog(this, "sdkInitialized = FALSE")
            }
        } catch (e: Throwable) {
            Log.e(
                TAG,
                "ThingHomeSdk.onDestroy failed",
                e
            )
        }

        super.onTerminate()
    }
}