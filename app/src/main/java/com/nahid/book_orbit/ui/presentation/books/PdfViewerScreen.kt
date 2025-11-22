package com.nahid.book_orbit.ui.presentation.books

import android.annotation.SuppressLint
import android.content.Context
import android.gesture.GestureLibraries.fromFile
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.compose.LottieCompositionSpec.File
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.io.outputStream

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PdfWebViewScreen(pdfUrl: String) {
    AndroidView(factory = { ctx ->
        WebView(ctx).apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.builtInZoomControls = true

            loadUrl("https://docs.google.com/gview?embedded=true&url=$pdfUrl")
        }
    }, modifier = Modifier.fillMaxSize())
}

