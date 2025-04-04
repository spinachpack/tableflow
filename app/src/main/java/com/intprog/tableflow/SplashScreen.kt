package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

//        Slight delay before starting

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SignupScreen::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}