package com.example.scrollapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.TimeUnit
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiObject
import android.support.test.uiautomator.UiObjectNotFoundException
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity // Use AppCompatActivity for Kotlin

class MultiAppScroll : AppCompatActivity() {

    private val TIKTOK_PACKAGE = "com.tiktok.android"
    private val INSTAGRAM_PACKAGE = "com.instagram.android"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val context = this
        val statusText = findViewById<TextView>(R.id.status_text) // Update status text

        while (true) {
            openApp(context, TIKTOK_PACKAGE)

            try {
                TimeUnit.SECONDS.sleep(30) // Delay for 30 seconds
                performBasicSwipe(context)
                statusText.text = "Scrolling in TikTok..."
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: UiObjectNotFoundException) {
                println("UI element not found for swipe in TikTok")
            }

            closeApp(context)

            openApp(context, INSTAGRAM_PACKAGE)

            try {
                TimeUnit.SECONDS.sleep(30) // Delay for 30 seconds
                performBasicSwipe(context)
                statusText.text = "Scrolling in Instagram..."
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: UiObjectNotFoundException) {
                println("UI element not found for swipe in Instagram")
            }

            closeApp(context)
        }
    }

    private fun openApp(context: Context, packageName: String) {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            context.startActivity(launchIntent)
        } else {
            println("App not found: $packageName")
        }
    }

    private fun closeApp(context: Context) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    private fun performBasicSwipe(context: Context) throws UiObjectNotFoundException {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val screenWidth = device.displayWidth
        val screenHeight = device.displayHeight
        val startX = (screenWidth * 0.75).toInt() // Start from 75% of screen width
        val startY = (screenHeight / 2).toInt() // Start from the middle of the screen
        val endX = (screenWidth * 0.25).toInt() // End at 25% of screen width
        val endY = startY // End at the same vertical position
        device.swipe(startX, startY, endX, endY, 1000) // Swipe duration in milliseconds
    }
}
