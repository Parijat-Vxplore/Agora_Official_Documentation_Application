package com.example.agoraofficialapplication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun MainScreen(appId: String, channelName: String, token: String) {
    var hasPermissions by remember { mutableStateOf(false) }

    if (hasPermissions) {
        VideoCallScreen(appId, channelName, token)
    } else {
        RequestPermissions {
            hasPermissions = true
        }
    }
}
