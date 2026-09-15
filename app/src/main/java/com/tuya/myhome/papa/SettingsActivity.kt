package com.tuya.myhome.papa

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

private lateinit var appKeyEdit: EditText

private lateinit var appSecretEdit: EditText

private lateinit var projectIdEdit: EditText

private lateinit var statusText: TextView


override fun onCreate(
    savedInstanceState: Bundle?
) {

    super.onCreate(
        savedInstanceState
    )


    createLayout()


    loadSettings()
}


// ============================================================
// Create Layout
// ============================================================

private fun createLayout() {

    val scrollView =
        ScrollView(this)


    val layout =
        LinearLayout(this).apply {

            orientation =
                LinearLayout.VERTICAL

            setPadding(
                30,
                30,
                30,
                30
            )
        }


    val title =
        TextView(this).apply {

            text =
                "Tuya SDK Settings"

            textSize =
                24f

            setPadding(
                10,
                10,
                10,
                30
            )
        }


    // --------------------------------------------------------
    // App Key
    // --------------------------------------------------------

    val appKeyLabel =
        createLabel(
            "App Key"
        )


    appKeyEdit =
        EditText(this).apply {

            hint =
                "Tuya App Key"

            inputType =
                InputType.TYPE_CLASS_TEXT
        }


    // --------------------------------------------------------
    // App Secret
    // --------------------------------------------------------

    val appSecretLabel =
        createLabel(
            "App Secret"
        )


    appSecretEdit =
        EditText(this).apply {

            hint =
                "Tuya App Secret"

            inputType =
                InputType.TYPE_CLASS_TEXT or
                InputType.TYPE_TEXT_VARIATION_PASSWORD
        }


    // --------------------------------------------------------
    // Project ID
    // --------------------------------------------------------

    val projectIdLabel =
        createLabel(
            "Project ID (Optional)"
        )


    projectIdEdit =
        EditText(this).apply {

            hint =
                "Tuya Cloud Project ID"

            inputType =
                InputType.TYPE_CLASS_TEXT
        }


    // --------------------------------------------------------
    // Save Button
    // --------------------------------------------------------

    val saveButton =
        Button(this).apply {

            text =
                "Save Settings"

            setOnClickListener {

                saveSettings()
            }
        }


    // --------------------------------------------------------
    // Initialize Button
    // --------------------------------------------------------

    val initializeButton =
        Button(this).apply {

            text =
                "Initialize Tuya SDK"

            setOnClickListener {

                initializeSdk()
            }
        }


    // --------------------------------------------------------
    // Status
    // --------------------------------------------------------

    statusText =
        TextView(this).apply {

            text =
                ""

            textSize =
                16f

            setPadding(
                10,
                30,
                10,
                10
            )
        }


    // --------------------------------------------------------
    // Add Views
    // --------------------------------------------------------

    layout.addView(
        title
    )

    layout.addView(
        appKeyLabel
    )

    layout.addView(
        appKeyEdit
    )

    layout.addView(
        appSecretLabel
    )

    layout.addView(
        appSecretEdit
    )

    layout.addView(
        projectIdLabel
    )

    layout.addView(
        projectIdEdit
    )

    layout.addView(
        saveButton
    )

    layout.addView(
        initializeButton
    )

    layout.addView(
        statusText
    )


    scrollView.addView(
        layout
    )


    setContentView(
        scrollView
    )
}


// ============================================================
// Create Label
// ============================================================

private fun createLabel(
    textValue: String
): TextView {

    return TextView(this).apply {

        text =
            textValue

        textSize =
            16f

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
        .apply()


    MyApplication.addLog(
        this,
        "Settings saved"
    )


    statusText.text =
        "Settings saved successfully.\n\n" +
        "App Key: " +
        if (appKey.isEmpty()) "NOT SET" else "SET" +
        "\nApp Secret: " +
        if (appSecret.isEmpty()) "NOT SET" else "SET" +
        "\nProject ID: " +
        if (projectId.isEmpty()) "NOT SET" else "SET"
}


// ============================================================
// Initialize SDK
// ============================================================

private fun initializeSdk() {

    saveSettings()


    statusText.text =
        "Initializing Tuya SDK..."


    val result =
        MyApplication.initializeTuyaSdk(
            application
        )


    if (
        result
    ) {

        statusText.text =
            "Tuya SDK initialization SUCCESS"

    } else {

        statusText.text =
            "Tuya SDK initialization FAILED\n\n" +
            "Check Log screen."
    }
}

}