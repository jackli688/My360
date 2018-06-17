package com.jackli.www.rocket

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.widget.Button


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startFloatWindow = findViewById(R.id.start_float_window) as Button
        startFloatWindow.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this@MainActivity)) {
                    startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")), 12345)
                } else {
                    openFloatWindow()
                }
            } else {
                openFloatWindow()
            }
        }
    }

    private fun openFloatWindow() {
        val intent = Intent(this@MainActivity, FloatWindowService::class.java)
        startService(intent)
        finish()
    }
}
