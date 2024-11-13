package com.example.agoraofficialapplication


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    private val appId = "2053d317382a4137a4a1755b22a109ba"
    private val channelName = "abckol"
    private val token = "40c9bd6c12bf4dd5aad0cb9639a31a0b"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(appId, channelName, token)
        }
    }
}