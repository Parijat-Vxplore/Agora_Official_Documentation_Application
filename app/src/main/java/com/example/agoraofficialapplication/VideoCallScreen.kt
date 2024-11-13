package com.example.agoraofficialapplication

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.SurfaceView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas


@Composable
fun VideoCallScreen(appId: String, channelName: String, token: String) {
    val context = LocalContext.current
    val remoteUid = remember { mutableStateOf<Int?>(null) }
    Log.d("ParijatTAg", "VideoCallScreen: Entered")
//    val rtcEngine = remember {
//        RtcEngine.create(context, appId, object : IRtcEngineEventHandler() {
//            override fun onUserJoined(uid: Int, elapsed: Int) {
//                remoteUid.value = uid
//            }
//
//            override fun onUserOffline(uid: Int, reason: Int) {
//                remoteUid.value = null
//            }
//        })
//    }

    val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        // Monitor remote users in the channel and obtain their uid
        override fun onUserJoined(uid: Int, elapsed: Int) {
            Handler(Looper.getMainLooper()).post {
                // After obtaining uid, set up the remote video view
                remoteUid.value = uid
            }
        }
    }
    val config = RtcEngineConfig()
    config.mContext = context
    config.mAppId = appId
    config.mEventHandler = mRtcEventHandler

    Log.d("ParijatTAg", "VideoCallScreen: config ready")
// Create and initialize an RtcEngine instance
    val rtcEngine = RtcEngine.create(config)
    Log.d("ParijatTAg", "VideoCallScreen: config set")

    DisposableEffect(Unit) {
        rtcEngine.apply {
            enableVideo()
            setupLocalVideo(VideoCanvas(SurfaceView(context).apply {
                setZOrderMediaOverlay(true)
            }, VideoCanvas.RENDER_MODE_HIDDEN, 0))
            joinChannel(token, channelName, null, 0)
        }

        onDispose {
            rtcEngine.leaveChannel()
            RtcEngine.destroy()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Log.d("ParijatTAg", "VideoCallScreen: Inside the box")
        // Local video view
        AndroidView(
            factory = { ctx ->
                SurfaceView(ctx).apply {
                    Log.d("ParijatTAg", "VideoCallScreen: Local video view")
                    rtcEngine.setupLocalVideo(VideoCanvas(this, VideoCanvas.RENDER_MODE_HIDDEN, 0))
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Remote video view
        remoteUid.value?.let { uid ->
            AndroidView(
                factory = { ctx ->
                    SurfaceView(ctx).apply {
                        rtcEngine.setupRemoteVideo(VideoCanvas(this, VideoCanvas.RENDER_MODE_HIDDEN, uid))
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
