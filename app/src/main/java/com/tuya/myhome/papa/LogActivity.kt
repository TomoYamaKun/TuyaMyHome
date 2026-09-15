package com.tuya.myhome.papa

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LogActivity : AppCompatActivity() {

private lateinit var logText: TextView


override fun onCreate(
    savedInstanceState: Bundle?
) {

    super.onCreate(
        savedInstanceState
    )


    createLayout()
}


override fun onResume() {

    super.onResume()

    reloadLog()
}


// ============================================================
// Create Layout
// ============================================================

private fun createLayout() {

    val root =
        LinearLayout(this).apply {

            orientation =
                LinearLayout.VERTICAL

            setPadding(
                20,
                20,
                20,
                20
            )
        }


    val title =
        TextView(this).apply {

            text =
                "DEBUG LOG\n(Tap log to copy all)"

            textSize =
                20f

            setPadding(
                10,
                10,
                10,
                20
            )
        }


    val reloadButton =
        Button(this).apply {

            text =
                "Reload"

            setOnClickListener {

                reloadLog()
            }
        }


    val clearButton =
        Button(this).apply {

            text =
                "Clear Log"

            setOnClickListener {

                MyApplication.clearLog(
                    this@LogActivity
                )

                reloadLog()
            }
        }


    val scrollView =
        ScrollView(this)


    logText =
        TextView(this).apply {

            textSize =
                13f

            setTextIsSelectable(
                true
            )

            setPadding(
                20,
                20,
                20,
                20
            )

            setBackgroundColor(
                0xFF202020.toInt()
            )

            setTextColor(
                0xFF00FF00.toInt()
            )


            setOnClickListener {

                copyLogToClipboard()
            }
        }


    scrollView.addView(
        logText
    )


    root.addView(
        title
    )

    root.addView(
        reloadButton
    )

    root.addView(
        clearButton
    )

    root.addView(
        scrollView,
        LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0,
            1f
        )
    )


    setContentView(
        root
    )
}


// ============================================================
// Reload Log
// ============================================================

private fun reloadLog() {

    logText.text =
        MyApplication.getLog(
            this
        )
}


// ============================================================
// Copy Log
// ============================================================

private fun copyLogToClipboard() {

    val text =
        logText.text.toString()


    val clipboard =
        getSystemService(
            Context.CLIPBOARD_SERVICE
        ) as ClipboardManager


    val clip =
        ClipData.newPlainText(
            "Tuya Debug Log",
            text
        )


    clipboard.setPrimaryClip(
        clip
    )


    Toast.makeText(
        this,
        "Debug log copied to clipboard",
        Toast.LENGTH_SHORT
    ).show()
}

}