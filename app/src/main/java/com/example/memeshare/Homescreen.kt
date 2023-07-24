package com.example.memeshare

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class Homescreen : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)

        val btn = findViewById<Button>(R.id.button)
        val intent = Intent(this,MainActivity::class.java)

        btn.setOnClickListener{
            val biometricManager = BiometricManager.from(this)
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                    Toast.makeText(applicationContext,"Device doesn't have fingerprint",Toast.LENGTH_SHORT).show()
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                    Toast.makeText(applicationContext,"Not working",Toast.LENGTH_SHORT).show()
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                    Toast.makeText(applicationContext,"No fingerprint assigned",Toast.LENGTH_SHORT).show()
            }

            executor = ContextCompat.getMainExecutor(this)
            biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(applicationContext,
                            "Authentication error: $errString", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        Toast.makeText(applicationContext,
                            "Authentication succeeded!", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(intent)
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(applicationContext, "Authentication failed",
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                })

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Use your fingerprint to authenticate")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use password")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }


    }


}