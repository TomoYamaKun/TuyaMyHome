// /storage/internal_new/project/TuyaMyHome/app/src/main/java/com/tuya/myhome/papa/MainActivity.kt
// ver 1.01-09

package com.tuya.myhome.papa

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.thingclips.smart.android.base.ThingSmartSdk
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.callback.IThingGetHomeListCallback
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback
import com.thingclips.smart.sdk.bean.DeviceBean

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "TUYA_DEBUG"
        private const val APP_VERSION = "1.01-09"
    }

    private lateinit var container: LinearLayout
    private lateinit var statusText: TextView
    private lateinit var debugText: TextView
    private val debugLog = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            debug("========================================")
            debug("APP VERSION = $APP_VERSION")
            debug("STEP 1: MainActivity.onCreate START")
            createLayout()
            debug("STEP 2: Layout created")

            loadApplicationDebugLog()

            val lastCrash = MyApplication.getLastCrash(this)

            if (lastCrash.isNotEmpty()) {
                debug("===== PREVIOUS CRASH DETECTED =====")
                debug(lastCrash)
                MyApplication.clearLastCrash(this)
            } else {
                debug("No previous crash log")
            }

            debug("STEP 3: Start SDK state check")
            val sdkReady = checkAndInitializeSdk()

            debug("SDK ready result = $sdkReady")
            debug("MyApplication.sdkInitialized = ${MyApplication.sdkInitialized}")

            if (sdkReady) {
                debug("STEP 4: Start loadDeviceList")
                loadDeviceList()
            } else {
                debug("STEP 4: SKIP loadDeviceList")
                debug("Reason: Tuya SDK is NOT initialized")

                statusText.text =
                    "Tuya SDK is not initialized\n\n" +
                    "Please open Settings and configure\n" +
                    "AppKey / AppSecret."
            }

            debug("========================================")
        } catch (e: Throwable) {
            MyApplication.saveCrash(this, e)
            debug("FATAL ERROR in MainActivity.onCreate")
            debugException(e)

            if (::statusText.isInitialized) {
                statusText.text =
                    "APPLICATION ERROR\n\n" +
                    "${e.javaClass.name}\n\n" +
                    "${e.message}"
            }
        }
    }

    private fun createLayout() {
        debug("createLayout() START")

        val scrollView = ScrollView(this)

        container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(30, 30, 30, 30)
        }

        statusText = TextView(this).apply {
            text =
                "Tuya Smart Life SDK\n" +
                "Initializing...\n" +
                "Version: $APP_VERSION"
            textSize = 18f
            setPadding(10, 10, 10, 20)
        }

        val debugHeader = TextView(this).apply {
            text =
                "===== DEBUG LOG =====\n" +
                "APP VERSION = $APP_VERSION"
            textSize = 16f
            setPadding(10, 20, 10, 10)
        }

        debugText = TextView(this).apply {
            textSize = 12f
            setTextIsSelectable(true)
            setPadding(15, 15, 15, 15)
            setBackgroundColor(0xFF202020.toInt())
            setTextColor(0xFF00FF00.toInt())
        }

        val reloadButton = Button(this).apply {
            text = "Reload Device List"
            setOnClickListener {
                debug("========================================")
                debug("APP VERSION = $APP_VERSION")
                debug("Manual reload requested")

                val sdkReady = checkAndInitializeSdk()

                debug("Reload SDK ready result = $sdkReady")
                debug(
                    "Reload MyApplication.sdkInitialized = " +
                        MyApplication.sdkInitialized
                )

                if (sdkReady) {
                    debug("Reload: SDK READY")
                    loadDeviceList()
                } else {
                    debug("Reload: SKIP loadDeviceList")
                    debug("Reload reason: Tuya SDK is NOT initialized")

                    statusText.text =
                        "Tuya SDK is not initialized\n\n" +
                        "Please open Settings and configure\n" +
                        "AppKey / AppSecret."
                }
            }
        }

        val clearButton = Button(this).apply {
            text = "Clear Debug Log"
            setOnClickListener {
                debugLog.clear()
                MyApplication.clearLog(this@MainActivity)
                updateDebugView()
                debug("Debug log cleared")
                debug("APP VERSION = $APP_VERSION")
            }
        }

        container.addView(statusText)
        container.addView(reloadButton)
        container.addView(clearButton)
        container.addView(debugHeader)
        container.addView(
            debugText,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        scrollView.addView(container)
        setContentView(scrollView)

        debug("createLayout() SUCCESS")
    }

    private fun loadApplicationDebugLog() {
        try {
            val applicationLog = MyApplication.getLog(this)

            if (applicationLog.isEmpty()) {
                debug("===== APPLICATION LOG =====")
                debug("NO SAVED APPLICATION LOG")
                debug("===== END APPLICATION LOG =====")
                return
            }

            debug("===== APPLICATION LOG FROM MyApplication =====")

            val lines = applicationLog.split("\n")

            for (line in lines) {
                debug("[APP] $line")
            }

            debug("===== END APPLICATION LOG =====")
        } catch (e: Throwable) {
            debug(
                "ERROR reading MyApplication log: " +
                    "${e.javaClass.name}: ${e.message}"
            )
        }
    }

    private fun checkAndInitializeSdk(): Boolean {
        return try {
            debug("===== SDK STATE CHECK =====")
            debug("APP VERSION = $APP_VERSION")

            val currentApplication = ThingSmartSdk.getApplication()

            debug(
                "ThingSmartSdk.getApplication() = " +
                    if (currentApplication == null) "NULL" else "NOT_NULL"
            )

            if (currentApplication != null) {
                debug(
                    "SDK Application class = " +
                        currentApplication.javaClass.name
                )
                debug(
                    "SDK Application hash = " +
                        System.identityHashCode(currentApplication)
                )
                debug(
                    "Current Application hash = " +
                        System.identityHashCode(application)
                )

                if (currentApplication === application) {
                    debug("SDK Application = SAME INSTANCE")
                } else {
                    debug("SDK Application = DIFFERENT INSTANCE")
                }

                debug("SDK already has Application context")
                debug("SDK STATE RESULT = READY")
                debug("===== SDK STATE CHECK END =====")
                return true
            }

            debug("SDK Application is NULL")
            debug("Attempting diagnostic SDK initialization")

            val myApplication = application as? MyApplication

            if (myApplication == null) {
                debug("ERROR: application is NOT MyApplication")
                debug(
                    "Actual Application = " +
                        application.javaClass.name
                )
                debug("SDK STATE RESULT = NOT_READY")
                debug("===== SDK STATE CHECK END =====")
                return false
            }

            debug("Application is MyApplication")
            debug("Calling MyApplication.initializeTuyaSdk()")

            val result = MyApplication.initializeTuyaSdk(
                application as Application
            )

            debug(
                "MyApplication.initializeTuyaSdk() result = $result"
            )

            debug(
                "MyApplication.sdkInitialized = " +
                    MyApplication.sdkInitialized
            )

            val afterApplication = ThingSmartSdk.getApplication()

            debug(
                "AFTER DIAGNOSTIC INIT: " +
                    "ThingSmartSdk.getApplication() = " +
                    if (afterApplication == null) "NULL" else "NOT_NULL"
            )

            if (afterApplication != null) {
                debug(
                    "AFTER INIT Application class = " +
                        afterApplication.javaClass.name
                )
                debug(
                    "AFTER INIT Application hash = " +
                        System.identityHashCode(afterApplication)
                )
                debug(
                    "EXPECTED Application hash = " +
                        System.identityHashCode(application)
                )

                if (afterApplication === application) {
                    debug("AFTER INIT = SAME APPLICATION INSTANCE")
                } else {
                    debug("AFTER INIT = DIFFERENT APPLICATION INSTANCE")
                }

                debug("SDK STATE RESULT = READY")
                debug("===== SDK STATE CHECK END =====")
                return true
            }

            debug(
                "ERROR: SDK Application STILL NULL " +
                    "after diagnostic initialization"
            )
            debug("SDK STATE RESULT = NOT_READY")
            debug("===== SDK STATE CHECK END =====")
            false
        } catch (e: Throwable) {
            debug("SDK STATE CHECK ERROR")
            debugException(e)
            debug("SDK STATE RESULT = NOT_READY")
            false
        }
    }

    private fun debug(message: String) {
        Log.d(TAG, message)
        debugLog.append(message)
        debugLog.append("\n")

        if (::debugText.isInitialized) {
            runOnUiThread {
                updateDebugView()
            }
        }
    }

    private fun debugException(throwable: Throwable) {
        debug("===== FULL THROWABLE =====")

        var current: Throwable? = throwable
        var level = 0

        while (current != null && level <= 10) {
            debug("----- CAUSE LEVEL $level -----")
            debug("Exception Type: ${current.javaClass.name}")
            debug("Exception Message: ${current.message}")
            debug("Localized Message: ${current.localizedMessage}")
            debug("StackTrace:")

            for (element in current.stackTrace) {
                debug("  at $element")
            }

            if (current.suppressed.isNotEmpty()) {
                debug("Suppressed:")

                for (suppressed in current.suppressed) {
                    debug(
                        "  ${suppressed.javaClass.name}: " +
                            "${suppressed.message}"
                    )

                    for (element in suppressed.stackTrace) {
                        debug("    at $element")
                    }
                }
            }

            current = current.cause
            level++

            if (current != null) {
                debug(">>> NEXT CAUSE <<<")
            }
        }

        if (level > 10) {
            debug("Cause chain limit reached.")
        }

        debug("===== END FULL THROWABLE =====")
    }

    private fun updateDebugView() {
        if (::debugText.isInitialized) {
            debugText.text = debugLog.toString()
        }
    }

    private fun loadDeviceList() {
        try {
            debug("loadDeviceList() START")
            debug("APP VERSION = $APP_VERSION")
            debug("Checking ThingHomeSdk User Instance...")

            try {
                val sdkApplication = ThingSmartSdk.getApplication()
                val networkSdk =
                    com.thingclips.smart.android.network.ThingSmartNetWork.mSdk

                debug(
                    "RUNTIME SDK CHECK: " +
                        "ThingSmartSdk.getApplication()=" +
                        if (sdkApplication == null) "NULL" else "NOT_NULL" +
                        ", ThingSmartNetWork.mSdk=" +
                        networkSdk
                )

                if (sdkApplication == null) {
                    debug("ERROR: SDK Application is NULL")
                    debug("loadDeviceList() ABORTED")
                    statusText.text =
                        "SDK ERROR\n\n" +
                        "ThingSmartSdk Application is NULL"
                    return
                }
            } catch (e: Throwable) {
                debug(
                    "RUNTIME SDK CHECK ERROR: " +
                        "${e.javaClass.name}: ${e.message}"
                )
                debug("loadDeviceList() ABORTED")
                return
            }

            val user = ThingHomeSdk.getUserInstance().user
            debug("getUserInstance() SUCCESS")

            if (user == null) {
                debug("USER = NULL")
                statusText.text =
                    "Status: Not Logged In\n\n" +
                    "Tuya SDK initialized successfully.\n" +
                    "Please implement login."
                return
            }

            debug("USER FOUND")
            debug("Username: ${user.username}")

            statusText.text =
                "Logged in\n" +
                "User: ${user.username}\n\n" +
                "Loading Home list..."

            debug("Querying Home List...")

            ThingHomeSdk
                .getHomeManagerInstance()
                .queryHomeList(
                    object : IThingGetHomeListCallback {
                        override fun onSuccess(
                            homeBeans: List<HomeBean>?
                        ) {
                            debug("Home List SUCCESS")

                            val count = homeBeans?.size ?: 0
                            debug("Home Count: $count")

                            if (homeBeans.isNullOrEmpty()) {
                                runOnUiThread {
                                    statusText.text = "No Home found."
                                }
                                return
                            }

                            runOnUiThread {
                                statusText.text =
                                    "Logged in\n" +
                                    "User: ${user.username}\n\n" +
                                    "Home count: $count"
                            }

                            loadHomeDevices(homeBeans)
                        }

                        override fun onError(
                            errorCode: String?,
                            errorMsg: String?
                        ) {
                            debug("Home List ERROR")
                            debug("Code: $errorCode")
                            debug("Message: $errorMsg")

                            runOnUiThread {
                                statusText.text =
                                    "Failed to query Home list\n\n" +
                                    "Code: $errorCode\n" +
                                    "Message: $errorMsg"
                            }
                        }
                    }
                )

            debug("queryHomeList() REQUEST SENT")
        } catch (e: Throwable) {
            MyApplication.saveCrash(this, e)
            debug("ERROR in loadDeviceList()")
            debugException(e)

            statusText.text =
                "SDK ERROR\n\n" +
                "${e.javaClass.name}\n\n" +
                "${e.message}"
        }
    }

    private fun loadHomeDevices(homeBeans: List<HomeBean>) {
        debug("loadHomeDevices() START")

        for (home in homeBeans) {
            try {
                val homeId = home.homeId

                debug("Loading Home: ${home.name}")
                debug("Home ID: $homeId")

                val homeClient =
                    ThingHomeSdk.newHomeInstance(homeId)

                homeClient.getHomeDetail(
                    object : IThingHomeResultCallback {
                        override fun onSuccess(bean: HomeBean?) {
                            debug("Home Detail SUCCESS")
                            debug("Home Name: ${bean?.name}")

                            runOnUiThread {
                                addHomeHeader(bean)

                                val devices = bean?.deviceList
                                val count = devices?.size ?: 0

                                debug("Device Count: $count")

                                if (devices.isNullOrEmpty()) {
                                    addText("No devices found.")
                                } else {
                                    addText("Device count: $count")

                                    for (device in devices) {
                                        debug("Device: ${device.name}")
                                        debug("Device ID: ${device.devId}")
                                        debug("Category: ${device.category}")
                                        addDevice(device)
                                    }
                                }
                            }
                        }

                        override fun onError(
                            errorCode: String?,
                            errorMsg: String?
                        ) {
                            debug("Home Detail ERROR")
                            debug("Code: $errorCode")
                            debug("Message: $errorMsg")
                        }
                    }
                )
            } catch (e: Throwable) {
                debug("ERROR loading home")
                debugException(e)
            }
        }
    }

    private fun addHomeHeader(home: HomeBean?) {
        val text = TextView(this).apply {
            text =
                "\n" +
                "━━━━━━━━━━━━━━━━━━━━\n" +
                "HOME\n" +
                "━━━━━━━━━━━━━━━━━━━━\n" +
                "Name : ${home?.name}\n" +
                "ID   : ${home?.homeId}\n" +
                "━━━━━━━━━━━━━━━━━━━━"
            textSize = 18f
            setPadding(10, 30, 10, 20)
        }

        container.addView(text)
    }

    private fun addDevice(device: DeviceBean) {
        val deviceText = TextView(this).apply {
            text =
                "━━━━━━━━━━━━━━━━━━━━\n" +
                "DEVICE\n" +
                "━━━━━━━━━━━━━━━━━━━━\n" +
                "Name       : ${device.name}\n" +
                "Device ID  : ${device.devId}\n" +
                "Product ID : ${device.productId}\n" +
                "Category   : ${device.category}\n" +
                "Online     : ${device.isOnline}\n\n" +
                "▶ Tap to open Camera"

            textSize = 16f
            setPadding(30, 25, 30, 25)
            isClickable = true

            setOnClickListener {
                debug("Device Clicked: ${device.name}")
                openCamera(device.devId, device.name)
            }
        }

        container.addView(deviceText)
    }

    private fun addText(message: String) {
        val text = TextView(this).apply {
            text = message
            textSize = 16f
            setPadding(20, 20, 20, 20)
        }

        container.addView(text)
    }

    private fun openCamera(
        deviceId: String,
        deviceName: String
    ) {
        try {
            debug("Opening CameraActivity")

            val intent = Intent(
                this,
                CameraActivity::class.java
            )

            intent.putExtra("DEVICE_ID", deviceId)
            intent.putExtra("DEVICE_NAME", deviceName)

            startActivity(intent)
        } catch (e: Throwable) {
            debug("ERROR opening CameraActivity")
            debugException(e)
        }
    }
}