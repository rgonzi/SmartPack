package com.inovatech.smartpack.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * @Composable que mostra un LoadingScreen quan isLoading és true.
 * Està embolcallat amb un Box clicable per a que no es pugui interectuar
 * amb la pantalla mentre s'executa.
 */
@Composable
fun LoadingScreen(
    isLoading: Boolean
) {
    if (isLoading) {
        /* Embolcallem el CircularProgressIndicator perquè així no es pugui
        interectuar amb la pantalla mentre s'executa el login */
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)) // Fons semitransparent
                .clickable(enabled = false) {} // Evita interaccions
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}