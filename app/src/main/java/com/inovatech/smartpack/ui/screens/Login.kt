package com.inovatech.smartpack.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import com.inovatech.smartpack.R

@Serializable
object Login

@Composable
fun LoginScreen() {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.logo_blanc),
            contentDescription = "Logo SmartPack",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        //Email
        Spacer(modifier = Modifier.weight(1f))
        //Password
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier.padding(16.dp),
            onClick = { /*TODO*/ }
        ) {
            Text("Iniciar sessió")
        }
        Button(
            modifier = Modifier.padding(16.dp),
            onClick = { /*TODO*/ }
        ) {
            Text("Registrar-se")
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}