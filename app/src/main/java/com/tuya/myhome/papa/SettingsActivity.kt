// /storage/internal_new/project/TuyaMyHome/app/src/main/java/com/tuya/myhome/papa/SettingsActivity.kt
// ver 1.01-12

package com.tuya.myhome.papa

import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.WindowInsets
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.thingclips.smart.android.user.api.ILoginCallback
import com.thingclips.smart.android.user.bean.User
import com.thingclips.smart.home.sdk.ThingHomeSdk

class SettingsActivity : AppCompatActivity() {
private lateinit var appKeyEdit: EditText
private lateinit var appSecretEdit: EditText
private lateinit var projectIdEdit: EditText
private lateinit var countryCodeEdit: EditText
private lateinit var accountEdit: EditText
private lateinit var passwordEdit: EditText
private lateinit var statusText: TextView

override fun onCreate(
    savedInstanceState: Bundle?
) {
    super.onCreate(savedInstanceState)
    createLayout()
    loadSettings()
    MyApplication.addLog(
        this,
        "SettingsActivity opened"
    )
}

// ============================================================
// Create Layout
// ============================================================

private fun createLayout() {
    val scrollView = ScrollView(this)
    val layout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(
            30,
            30,
            30,
            30
        )
    }

    val title = TextView(this).apply {
        text = "Tuya SDK Settings"
        textSize = 24f
        setPadding(
            10,
            10,
            10,
            30
        )
    }

    val appKeyLabel = createLabel(
        "App Key"
    )

    appKeyEdit = EditText(this).apply {
        hint = "Tuya App Key"
        inputType = InputType.TYPE_CLASS_TEXT
    }

    val appSecretLabel = createLabel(
        "App Secret"
    )

    appSecretEdit = EditText(this).apply {
        hint = "Tuya App Secret"
        inputType =
            InputType.TYPE_CLASS_TEXT or
            InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    val projectIdLabel = createLabel(
        "Project ID (Optional)"
    )

    projectIdEdit = EditText(this).apply {
        hint = "Tuya Cloud Project ID"
        inputType = InputType.TYPE_CLASS_TEXT
    }

    val loginTitle = TextView(this).apply {
        text = "Tuya Account Login"
        textSize = 20f
        setPadding(
            10,
            40,
            10,
            10
        )
    }

    val countryCodeLabel = createLabel(
        "Country Code"
    )

    countryCodeEdit = EditText(this).apply {
        hint = "81"
        setText("81")
        inputType = InputType.TYPE_CLASS_PHONE
    }

    val accountLabel = createLabel(
        "Account (Email / Phone)"
    )

    accountEdit = EditText(this).apply {
        hint = "Email address or phone number"
        inputType = InputType.TYPE_CLASS_TEXT
    }

    val passwordLabel = createLabel(
        "Password"
    )

    passwordEdit = EditText(this).apply {
        hint = "Tuya account password"
        inputType =
            InputType.TYPE_CLASS_TEXT or
            InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    val saveButton = Button(this).apply {
        text = "Save Settings"
        setOnClickListener {
            saveSettings()
        }
    }

    val initializeButton = Button(this).apply {
        text = "Initialize Tuya SDK"
        setOnClickListener {
            initializeSdk()
        }
    }

    val loginButton = Button(this).apply {
        text = "Login to Tuya"
        setOnClickListener {
            loginToTuya()
        }
    }

    statusText = TextView(this).apply {
        text = ""
        textSize = 16f
        setPadding(
            10,
            30,
            10,
            10
        )
    }

    layout.addView(title)
    layout.addView(appKeyLabel)
    layout.addView(appKeyEdit)
    layout.addView(appSecretLabel)
    layout.addView(appSecretEdit)
    layout.addView(projectIdLabel)
    layout.addView(projectIdEdit)
    layout.addView(loginTitle)
    layout.addView(countryCodeLabel)
    layout.addView(countryCodeEdit)
    layout.addView(accountLabel)
    layout.addView(accountEdit)
    layout.addView(passwordLabel)
    layout.addView(passwordEdit)
    layout.addView(saveButton)
    layout.addView(initializeButton)
    layout.addView(loginButton)
    layout.addView(statusText)

    scrollView.addView(layout)
    setContentView(scrollView)

    scrollView.setOnApplyWindowInsetsListener { view, insets ->
        val systemBars = insets.getInsets(
            WindowInsets.Type.systemBars()
        )

        view.setPadding(
            view.paddingLeft,
            view.paddingTop,
            view.paddingRight,
            systemBars.bottom + 30
        )

        insets
    }

    scrollView.requestApplyInsets()
}

// ============================================================
// Create Label
// ============================================================

private fun createLabel(
    textValue: String
): TextView {
    return TextView(this).apply {
        text = textValue
        textSize = 16f
        setPadding(
            10,
            25,
            10,
            5
        )
    }
}

// ============================================================
// Load Settings
// ============================================================

private fun loadSettings() {
    appKeyEdit.setText(
        MyApplication.getAppKey(
            this
        )
    )

    appSecretEdit.setText(
        MyApplication.getAppSecret(
            this
        )
    )

    projectIdEdit.setText(
        MyApplication.getProjectId(
            this
        )
    )

    val preferences = getSharedPreferences(
        MyApplication.PREF_NAME,
        MODE_PRIVATE
    )

    countryCodeEdit.setText(
        preferences.getString(
            "tuya_country_code",
            "81"
        )
    )

    accountEdit.setText(
        preferences.getString(
            "tuya_account",
            ""
        )
    )

    passwordEdit.setText(
        preferences.getString(
            "tuya_password",
            ""
        )
    )
}

// ============================================================
// Save Settings
// ============================================================

private fun saveSettings() {
    val appKey =
        appKeyEdit.text
            .toString()
            .trim()

    val appSecret =
        appSecretEdit.text
            .toString()
            .trim()

    val projectId =
        projectIdEdit.text
            .toString()
            .trim()

    val countryCode =
        countryCodeEdit.text
            .toString()
            .trim()

    val account =
        accountEdit.text
            .toString()
            .trim()

    val password =
        passwordEdit.text
            .toString()

    getSharedPreferences(
        MyApplication.PREF_NAME,
        MODE_PRIVATE
    )
        .edit()
        .putString(
            MyApplication.KEY_APP_KEY,
            appKey
        )
        .putString(
            MyApplication.KEY_APP_SECRET,
            appSecret
        )
        .putString(
            MyApplication.KEY_PROJECT_ID,
            projectId
        )
        .putString(
            "tuya_country_code",
            countryCode
        )
        .putString(
            "tuya_account",
            account
        )
        .putString(
            "tuya_password",
            password
        )
        .apply()

    MyApplication.addLog(
        this,
        "SETTINGS SAVE START"
    )

    MyApplication.addLog(
        this,
        "App Key = " +
            if (appKey.isEmpty()) {
                "NOT SET"
            } else {
                "SET"
            }
    )

    MyApplication.addLog(
        this,
        "App Secret = " +
            if (appSecret.isEmpty()) {
                "NOT SET"
            } else {
                "SET"
            }
    )

    MyApplication.addLog(
        this,
        "Project ID = " +
            if (projectId.isEmpty()) {
                "NOT SET"
            } else {
                "SET"
            }
    )

    MyApplication.addLog(
        this,
        "Country Code = " +
            if (countryCode.isEmpty()) {
                "NOT SET"
            } else {
                countryCode
            }
    )

    MyApplication.addLog(
        this,
        "Account = " +
            if (account.isEmpty()) {
                "NOT SET"
            } else {
                "SET"
            }
    )

    MyApplication.addLog(
        this,
        "Password = " +
            if (password.isEmpty()) {
                "NOT SET"
            } else {
                "SET"
            }
    )

    MyApplication.addLog(
        this,
        "SETTINGS SAVE END"
    )

    statusText.text =
        "Settings saved successfully."
}

// ============================================================
// Initialize SDK
// ============================================================

private fun initializeSdk() {
    MyApplication.addLog(
        this,
        "========================================"
    )

    MyApplication.addLog(
        this,
        "SDK INITIALIZE BUTTON CLICKED"
    )

    MyApplication.addLog(
        this,
        "SDK INITIALIZE START"
    )

    saveSettings()

    statusText.text =
        "Initializing Tuya SDK..."

    try {
        MyApplication.addLog(
            this,
            "Calling MyApplication.initializeTuyaSdk()"
        )

        val result =
            MyApplication.initializeTuyaSdk(
                application
            )

        MyApplication.addLog(
            this,
            "initializeTuyaSdk() returned = $result"
        )

        if (result) {
            statusText.text =
                "Tuya SDK initialization SUCCESS"

            MyApplication.addLog(
                this,
                "SDK INITIALIZE RESULT = SUCCESS"
            )
        } else {
            statusText.text =
                "Tuya SDK initialization FAILED\n\n" +
                "Check Log screen."

            MyApplication.addLog(
                this,
                "SDK INITIALIZE RESULT = FAILED"
            )
        }
    } catch (e: Exception) {
        MyApplication.addLog(
            this,
            "SDK INITIALIZE EXCEPTION"
        )

        logException(
            e
        )

        statusText.text =
            "Tuya SDK initialization EXCEPTION\n\n" +
            "Check Log screen."
    }

    MyApplication.addLog(
        this,
        "SDK INITIALIZE END"
    )

    MyApplication.addLog(
        this,
        "========================================"
    )
}

// ============================================================
// Login To Tuya
// ============================================================

private fun loginToTuya() {
    MyApplication.addLog(
        this,
        "========================================"
    )

    MyApplication.addLog(
        this,
        "LOGIN BUTTON CLICKED"
    )

    MyApplication.addLog(
        this,
        "LOGIN START"
    )

    saveSettings()

    val countryCode =
        countryCodeEdit.text
            .toString()
            .trim()

    val account =
        accountEdit.text
            .toString()
            .trim()

    val password =
        passwordEdit.text
            .toString()

    MyApplication.addLog(
        this,
        "LOGIN countryCode = " +
            if (countryCode.isEmpty()) {
                "EMPTY"
            } else {
                countryCode
            }
    )

    MyApplication.addLog(
        this,
        "LOGIN account type = " +
            if (
                Patterns.EMAIL_ADDRESS
                    .matcher(account)
                    .matches()
            ) {
                "EMAIL"
            } else {
                "PHONE"
            }
    )

    MyApplication.addLog(
        this,
        "LOGIN account = " +
            if (account.isEmpty()) {
                "EMPTY"
            } else {
                "SET"
            }
    )

    MyApplication.addLog(
        this,
        "LOGIN password = " +
            if (password.isEmpty()) {
                "EMPTY"
            } else {
                "SET"
            }
    )

    if (countryCode.isEmpty()) {
        MyApplication.addLog(
            this,
            "LOGIN ERROR: Country Code is empty"
        )

        statusText.text =
            "Login failed\n\nCountry Code is empty."

        MyApplication.addLog(
            this,
            "LOGIN END = VALIDATION ERROR"
        )

        MyApplication.addLog(
            this,
            "========================================"
        )

        return
    }

    if (account.isEmpty()) {
        MyApplication.addLog(
            this,
            "LOGIN ERROR: Account is empty"
        )

        statusText.text =
            "Login failed\n\nAccount is empty."

        MyApplication.addLog(
            this,
            "LOGIN END = VALIDATION ERROR"
        )

        MyApplication.addLog(
            this,
            "========================================"
        )

        return
    }

    if (password.isEmpty()) {
        MyApplication.addLog(
            this,
            "LOGIN ERROR: Password is empty"
        )

        statusText.text =
            "Login failed\n\nPassword is empty."

        MyApplication.addLog(
            this,
            "LOGIN END = VALIDATION ERROR"
        )

        MyApplication.addLog(
            this,
            "========================================"
        )

        return
    }

    statusText.text =
        "Logging in to Tuya..."

    val userInstance =
        try {
            MyApplication.addLog(
                this,
                "LOGIN: Getting ThingHomeSdk User Instance"
            )

            val instance =
                ThingHomeSdk.getUserInstance()

            MyApplication.addLog(
                this,
                "LOGIN: getUserInstance() SUCCESS"
            )

            instance
        } catch (e: Exception) {
            MyApplication.addLog(
                this,
                "LOGIN getUserInstance ERROR"
            )

            logException(
                e
            )

            statusText.text =
                "Login failed\n\n" +
                "Could not get Tuya User Instance.\n" +
                "Check Log screen."

            MyApplication.addLog(
                this,
                "LOGIN END = USER INSTANCE ERROR"
            )

            MyApplication.addLog(
                this,
                "========================================"
            )

            return
        }

    if (userInstance == null) {
        MyApplication.addLog(
            this,
            "LOGIN ERROR: User Instance = NULL"
        )

        statusText.text =
            "Login failed\n\n" +
            "Tuya User Instance is NULL."

        MyApplication.addLog(
            this,
            "LOGIN END = USER INSTANCE NULL"
        )

        MyApplication.addLog(
            this,
            "========================================"
        )

        return
    }

    try {
        val alreadyLogin =
            userInstance.isLogin

        MyApplication.addLog(
            this,
            "LOGIN: isLogin = $alreadyLogin"
        )

        if (alreadyLogin) {
            MyApplication.addLog(
                this,
                "LOGIN: User is already logged in"
            )

            statusText.text =
                "Tuya account is already logged in."

            MyApplication.addLog(
                this,
                "LOGIN END = ALREADY LOGGED IN"
            )

            MyApplication.addLog(
                this,
                "========================================"
            )

            return
        }
    } catch (e: Exception) {
        MyApplication.addLog(
            this,
            "LOGIN isLogin check ERROR"
        )

        logException(
            e
        )
    }

    val callback =
        object : ILoginCallback {
            override fun onSuccess(
                user: User?
            ) {
                MyApplication.addLog(
                    this@SettingsActivity,
                    "========================================"
                )

                MyApplication.addLog(
                    this@SettingsActivity,
                    "LOGIN CALLBACK = onSuccess"
                )

                MyApplication.addLog(
                    this@SettingsActivity,
                    "LOGIN RESULT = SUCCESS"
                )

                if (user != null) {
                    MyApplication.addLog(
                        this@SettingsActivity,
                        "User object = NOT_NULL"
                    )

                    MyApplication.addLog(
                        this@SettingsActivity,
                        "Username = " +
                            user.username
                    )
                } else {
                    MyApplication.addLog(
                        this@SettingsActivity,
                        "User object = NULL"
                    )
                }

                MyApplication.addLog(
                    this@SettingsActivity,
                    "LOGIN END = SUCCESS"
                )

                MyApplication.addLog(
                    this@SettingsActivity,
                    "========================================"
                )

                runOnUiThread {
                    statusText.text =
                        "Tuya Login SUCCESS\n\n" +
                        "User logged in successfully."
                }
            }

            override fun onError(
                code: String,
                error: String
            ) {
                MyApplication.addLog(
                    this@SettingsActivity,
                    "========================================"
                )

                MyApplication.addLog(
                    this@SettingsActivity,
                    "LOGIN CALLBACK = onError"
                )

                MyApplication.addLog(
                    this@SettingsActivity,
                    "LOGIN RESULT = FAILED"
                )

                MyApplication.addLog(
                    this@SettingsActivity,
                    "Error Code = $code"
                )

                MyApplication.addLog(
                    this@SettingsActivity,
                    "Error Message = $error"
                )

                MyApplication.addLog(
                    this@SettingsActivity,
                    "LOGIN END = FAILED"
                )

                MyApplication.addLog(
                    this@SettingsActivity,
                    "========================================"
                )

                runOnUiThread {
                    statusText.text =
                        "Tuya Login FAILED\n\n" +
                        "Code: $code\n" +
                        "Error: $error\n\n" +
                        "Check Debug Log."
                }
            }
        }

    try {
        if (
            Patterns.EMAIL_ADDRESS
                .matcher(account)
                .matches()
        ) {
            MyApplication.addLog(
                this,
                "LOGIN API = loginWithEmail()"
            )

            MyApplication.addLog(
                this,
                "LOGIN API CALL START"
            )

            userInstance.loginWithEmail(
                countryCode,
                account,
                password,
                callback
            )

            MyApplication.addLog(
                this,
                "LOGIN API CALL RETURNED"
            )
        } else {
            MyApplication.addLog(
                this,
                "LOGIN API = loginWithPhonePassword()"
            )

            MyApplication.addLog(
                this,
                "LOGIN API CALL START"
            )

            userInstance.loginWithPhonePassword(
                countryCode,
                account,
                password,
                callback
            )

            MyApplication.addLog(
                this,
                "LOGIN API CALL RETURNED"
            )
        }
    } catch (e: Exception) {
        MyApplication.addLog(
            this,
            "LOGIN API EXCEPTION"
        )

        logException(
            e
        )

        statusText.text =
            "Tuya Login EXCEPTION\n\n" +
            "${e.javaClass.simpleName}\n" +
            "${e.message}\n\n" +
            "Check Debug Log."

        MyApplication.addLog(
            this,
            "LOGIN END = EXCEPTION"
        )
    }
}

// ============================================================
// Exception Logging
// ============================================================

private fun logException(
    exception: Exception
) {
    MyApplication.addLog(
        this,
        "Exception Type: " +
            exception.javaClass.name
    )

    MyApplication.addLog(
        this,
        "Exception Message: " +
            (exception.message ?: "NULL")
    )

    var cause =
        exception.cause

    var depth = 0

    while (
        cause != null &&
        depth < 10
    ) {
        MyApplication.addLog(
            this,
            "CAUSE[$depth] " +
                cause.javaClass.name +
                ": " +
                (cause.message ?: "NULL")
        )

        cause =
            cause.cause

        depth++
    }

    val stackTrace =
        exception.stackTrace

    MyApplication.addLog(
        this,
        "STACKTRACE START"
    )

    for (
        element in stackTrace
    ) {
        MyApplication.addLog(
            this,
            "    at $element"
        )
    }

    MyApplication.addLog(
        this,
        "STACKTRACE END"
    )
}

}