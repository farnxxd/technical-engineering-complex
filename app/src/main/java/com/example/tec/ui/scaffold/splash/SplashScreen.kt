package com.example.tec.ui.scaffold.splash

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tec.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    finishedListener: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val fadeOutProgress = remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        delay(1000)
        fadeOutProgress.floatValue = 1f
    }

    val content = animateFloatAsState(
        targetValue = if (fadeOutProgress.floatValue > 0f) 0f else 1f,
        animationSpec = spring(Spring.DampingRatioNoBouncy, Spring.StiffnessVeryLow),
        label = "splash",
        finishedListener = { finishedListener(true) }
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .alpha(content.value)
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {}
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.Center)
                .size(300.dp)
        )
        Text(text = fadeOutProgress.toString(), modifier = Modifier.padding(top = 100.dp))
    }
}