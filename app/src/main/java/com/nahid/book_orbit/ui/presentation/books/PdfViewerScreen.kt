package com.nahid.book_orbit.ui.presentation.books

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

private const val TAG = "PdfViewerScreen"
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PdfWebViewScreen(pdfUrl: String) {
    AndroidView(factory = { ctx ->
        WebView(ctx).apply {
            val fileId = pdfUrl.substringAfter("d/").substringBefore("/view")
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.builtInZoomControls = true

            loadUrl("https://drive.google.com/file/d/$fileId/preview")
        }
    }, modifier = Modifier.fillMaxSize())
}

