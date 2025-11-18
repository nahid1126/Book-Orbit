package com.nahid.book_orbit.ui.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nahid.book_orbit.R

@Composable
fun AnimatedProgressDialog(modifier: Modifier = Modifier.size(100.dp)) {
    Dialog(onDismissRequest = {

    }, properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false)) {
        Box(
            modifier = Modifier.fillMaxSize().padding(all = 24.dp),
            contentAlignment = Alignment.Center,
        ) {
            val preloaderLottieComposition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(
                    R.raw.loading
                )
            )

            val preloaderProgress by animateLottieCompositionAsState(
                preloaderLottieComposition,
                iterations = LottieConstants.IterateForever,
                isPlaying = true
            )

            LottieAnimation(
                composition = preloaderLottieComposition,
                progress = preloaderProgress,
                modifier = modifier
            )
        }
    }
}