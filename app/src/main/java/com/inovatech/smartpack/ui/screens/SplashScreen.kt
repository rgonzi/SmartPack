package com.inovatech.smartpack.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inovatech.smartpack.R
import kotlinx.serialization.Serializable

@Serializable
data object Splash

/**
 * Composable que defineix una pantalla Splash que es mostrarà a l'iniciar l'aplicació.
 * Dona marge perquè es puguin fer les configuracions inicials abans de mostrar la pantalla
 * de Login
 */
@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_transparent),
            contentDescription = "Splash",
            contentScale = ContentScale.FillWidth
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}