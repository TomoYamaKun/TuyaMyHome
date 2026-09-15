// app/src/main/java/com/tuya/myhome/papa/CameraActivity.kt
// ver 1.01-07
//
// Camera Activity
// 現段階：MainActivityからDevice IDを受け取り、
//          Camera SDK / P2P実装前の動作確認を行う

package com.tuya.myhome.papa

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CameraActivity : AppCompatActivity() {

// ============================================================
// UI
// ============================================================

private lateinit var titleTextView: TextView

private lateinit var deviceInfoTextView: TextView

private lateinit var statusTextView: TextView


// ============================================================
// Device Information
// ============================================================

private var deviceId: String = ""

private var deviceName: String = ""


// ============================================================
// Activity Create
// ============================================================

override fun onCreate(
    savedInstanceState: Bundle?
) {

    super.onCreate(
        savedInstanceState
    )


    // --------------------------------------------------------
    // Receive Device Information
    // --------------------------------------------------------

    deviceId =
        intent.getStringExtra(
            "DEVICE_ID"
        ) ?: ""


    deviceName =
        intent.getStringExtra(
            "DEVICE_NAME"
        ) ?: ""


    // --------------------------------------------------------
    // Create Layout
    // --------------------------------------------------------

    createLayout()


    // --------------------------------------------------------
    // Display Device Information
    // --------------------------------------------------------

    showDeviceInformation()


    // --------------------------------------------------------
    // Future:
    // Initialize Camera P2P
    // --------------------------------------------------------

    initializeCamera()
}


// ============================================================
// Create UI
// ============================================================

private fun createLayout() {

    val rootLayout =
        LinearLayout(this).apply {

            orientation =
                LinearLayout.VERTICAL

            gravity =
                Gravity.CENTER_HORIZONTAL

            setPadding(
                30,
                40,
                30,
                40
            )
        }


    // --------------------------------------------------------
    // Title
    // --------------------------------------------------------

    titleTextView =
        TextView(this).apply {

            text =
                "Tuya Camera"

            textSize =
                24f

            gravity =
                Gravity.CENTER

            setPadding(
                10,
                20,
                10,
                40
            )
        }


    // --------------------------------------------------------
    // Device Information
    // --------------------------------------------------------

    deviceInfoTextView =
        TextView(this).apply {

            textSize =
                16f

            setPadding(
                20,
                20,
                20,
                40
            )
        }


    // --------------------------------------------------------
    // Camera Preview Placeholder
    // --------------------------------------------------------

    val previewPlaceholder =
        TextView(this).apply {

            text =
                "CAMERA PREVIEW\n\n" +
                "IPC SDK Ready\n" +
                "P2P implementation pending"

            textSize =
                18f

            gravity =
                Gravity.CENTER

            setBackgroundColor(
                0xFF202020.toInt()
            )

            setTextColor(
                0xFFFFFFFF.toInt()
            )
        }


    val previewParams =
        LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0
        ).apply {

            weight =
                1f
        }


    // --------------------------------------------------------
    // Status
    // --------------------------------------------------------

    statusTextView =
        TextView(this).apply {

            textSize =
                16f

            gravity =
                Gravity.CENTER

            setPadding(
                10,
                30,
                10,
                20
            )
        }


    // --------------------------------------------------------
    // Add Views
    // --------------------------------------------------------

    rootLayout.addView(
        titleTextView
    )


    rootLayout.addView(
        deviceInfoTextView
    )


    rootLayout.addView(
        previewPlaceholder,
        previewParams
    )


    rootLayout.addView(
        statusTextView
    )


    setContentView(
        rootLayout
    )
}


// ============================================================
// Show Device Information
// ============================================================

private fun showDeviceInformation() {

    deviceInfoTextView.text =
        "━━━━━━━━━━━━━━━━━━━━\n" +
        "CAMERA DEVICE\n" +
        "━━━━━━━━━━━━━━━━━━━━\n" +
        "Name      : $deviceName\n" +
        "Device ID : $deviceId\n" +
        "━━━━━━━━━━━━━━━━━━━━"
}


// ============================================================
// Initialize Camera
// ============================================================

private fun initializeCamera() {

    if (
        deviceId.isEmpty()
    ) {

        statusTextView.text =
            "ERROR\n" +
            "DEVICE_ID was not received."

        return
    }


    statusTextView.text =
        "Device ID received successfully.\n" +
        "Camera IPC SDK loaded.\n" +
        "Ready for P2P implementation."
}


// ============================================================
// Activity Resume
// ============================================================

override fun onResume() {

    super.onResume()

    // Future:
    // Resume Camera Preview
}


// ============================================================
// Activity Pause
// ============================================================

override fun onPause() {

    super.onPause()

    // Future:
    // Stop Camera Preview
}


// ============================================================
// Activity Destroy
// ============================================================

override fun onDestroy() {

    super.onDestroy()

    // Future:
    // Release P2P Camera Resources
}

}