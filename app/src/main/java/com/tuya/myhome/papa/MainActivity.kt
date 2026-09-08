//app/src/main/java/com/tuya/myhome/papa/MainActivity.kt
//ver 1.01-00

package com.tuya.myhome.papa

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tuya.myhome.papa.config.TuyaConfigManager

class MainActivity : AppCompatActivity() {

    private lateinit var configManager: TuyaConfigManager

    private lateinit var editAppKey: EditText
    private lateinit var editAppSecret: EditText
    private lateinit var editDataCenter: EditText
    private lateinit var editSha256: EditText

    private lateinit var buttonSave: Button
    private lateinit var textStatus: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        configManager = TuyaConfigManager(this)

        initializeViews()

        loadConfig()

        setupListeners()
    }


    private fun initializeViews() {

        editAppKey =
            findViewById(R.id.editAppKey)

        editAppSecret =
            findViewById(R.id.editAppSecret)

        editDataCenter =
            findViewById(R.id.editDataCenter)

        editSha256 =
            findViewById(R.id.editSha256)

        buttonSave =
            findViewById(R.id.buttonSave)

        textStatus =
            findViewById(R.id.textStatus)
    }


    private fun loadConfig() {

        editAppKey.setText(
            configManager.getAppKey()
        )

        editAppSecret.setText(
            configManager.getAppSecret()
        )

        editDataCenter.setText(
            configManager.getDataCenter()
        )

        editSha256.setText(
            configManager.getSha256()
        )


        if (configManager.isConfigured()) {

            textStatus.text =
                "保存済みのTuya設定を読み込みました"

        } else {

            textStatus.text =
                "Tuya接続情報を入力してください"
        }
    }


    private fun setupListeners() {

        buttonSave.setOnClickListener {

            saveConfig()
        }
    }


    private fun saveConfig() {

        val appKey =
            editAppKey.text.toString().trim()

        val appSecret =
            editAppSecret.text.toString().trim()

        val dataCenter =
            editDataCenter.text.toString().trim()

        val sha256 =
            editSha256.text.toString().trim()


        if (appKey.isEmpty()) {

            showError(
                "App Key / Client ID を入力してください"
            )

            return
        }


        if (appSecret.isEmpty()) {

            showError(
                "App Secret / Client Secret を入力してください"
            )

            return
        }


        if (dataCenter.isEmpty()) {

            showError(
                "Data Center を入力してください"
            )

            return
        }


        val result =
            configManager.saveConfig(
                appKey = appKey,
                appSecret = appSecret,
                dataCenter = dataCenter,
                sha256 = sha256
            )


        if (result) {

            textStatus.text =
                "設定を保存しました"

            Toast.makeText(
                this,
                "Tuya設定を保存しました",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            showError(
                "設定の保存に失敗しました"
            )
        }
    }


    private fun showError(
        message: String
    ) {

        textStatus.text =
            "エラー: $message"

        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}