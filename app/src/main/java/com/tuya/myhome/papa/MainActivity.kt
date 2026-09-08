package com.tuya.myhome.papa

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.home.sdk.bean.HomeBean
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback
import com.tuya.smart.sdk.bean.DeviceBean

class MainActivity : AppCompatActivity() {

    private lateinit var container: LinearLayout
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createLayout()

        loadDeviceList()
    }

    private fun createLayout() {

        val scrollView = ScrollView(this)

        container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(30, 30, 30, 30)
        }

        statusText = TextView(this).apply {
            text = "Tuya SDK Initialized\nChecking devices..."
            textSize = 18f
            setPadding(10, 10, 10, 30)
        }

        container.addView(statusText)

        scrollView.addView(container)

        setContentView(scrollView)
    }


    private fun loadDeviceList() {

        val user = TuyaHomeSdk.getUserInstance().user

        if (user == null) {

            statusText.text =
                "Status: Not Logged In\n\n" +
                "Please login first."

            return
        }

        statusText.text =
            "Logged in\n" +
            "Loading Home list..."


        TuyaHomeSdk.getHomeManagerInstance()
            .queryHomeList(object : ITuyaGetHomeListCallback {

                override fun onSuccess(homeBeans: List<HomeBean>?) {

                    if (homeBeans.isNullOrEmpty()) {

                        runOnUiThread {
                            statusText.text =
                                "No Home found."
                        }

                        return
                    }

                    runOnUiThread {

                        statusText.text =
                            "Home count: ${homeBeans.size}\n"
                    }

                    loadHomeDevices(homeBeans)
                }


                override fun onError(
                    errorCode: String?,
                    errorMsg: String?
                ) {

                    runOnUiThread {

                        statusText.text =
                            "Failed to query Home list\n\n" +
                            "Code: $errorCode\n" +
                            "Message: $errorMsg"
                    }
                }
            })
    }


    private fun loadHomeDevices(
        homeBeans: List<HomeBean>
    ) {

        for (home in homeBeans) {

            val homeId = home.homeId

            val homeClient =
                TuyaHomeSdk.newHomeInstance(homeId)


            homeClient.getHomeDetail(
                object :
                    com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback {

                    override fun onSuccess(
                        bean: HomeBean?
                    ) {

                        runOnUiThread {

                            addHomeHeader(bean)

                            val devices =
                                bean?.deviceList

                            if (devices.isNullOrEmpty()) {

                                addText(
                                    "No devices found."
                                )

                            } else {

                                for (device in devices) {

                                    addDevice(device)
                                }
                            }
                        }
                    }


                    override fun onError(
                        errorCode: String?,
                        errorMsg: String?
                    ) {

                        runOnUiThread {

                            addText(
                                "Failed Home Detail\n" +
                                "$errorMsg ($errorCode)"
                            )
                        }
                    }
                })
        }
    }


    private fun addHomeHeader(
        home: HomeBean?
    ) {

        val text = TextView(this).apply {

            text =
                "\n━━━━━━━━━━━━━━━━━━\n" +
                "HOME\n" +
                "Name : ${home?.name}\n" +
                "ID   : ${home?.homeId}\n" +
                "━━━━━━━━━━━━━━━━━━"

            textSize = 18f

            setPadding(
                10,
                30,
                10,
                20
            )
        }

        container.addView(text)
    }


    private fun addDevice(
        device: DeviceBean
    ) {

        val deviceText =
            TextView(this).apply {

                text =
                    "📱 ${device.name}\n" +
                    "Device ID : ${device.devId}\n" +
                    "Product ID: ${device.productId}\n" +
                    "Category  : ${device.category}\n" +
                    "Online    : ${device.isOnline}\n\n" +
                    "Tap to open"

                textSize = 16f

                setPadding(
                    30,
                    25,
                    30,
                    25
                )

                isClickable = true

                setOnClickListener {

                    openCamera(
                        device.devId,
                        device.name
                    )
                }
            }


        container.addView(
            deviceText,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    }


    private fun addText(
        message: String
    ) {

        val text = TextView(this).apply {

            text = message

            textSize = 16f

            setPadding(
                20,
                20,
                20,
                20
            )
        }

        container.addView(text)
    }


    private fun openCamera(
        deviceId: String,
        deviceName: String
    ) {

        val intent =
            Intent(
                this,
                CameraActivity::class.java
            )

        intent.putExtra(
            "DEVICE_ID",
            deviceId
        )

        intent.putExtra(
            "DEVICE_NAME",
            deviceName
        )

        startActivity(intent)
    }
}