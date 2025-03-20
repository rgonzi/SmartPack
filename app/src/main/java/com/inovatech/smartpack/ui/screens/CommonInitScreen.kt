package com.inovatech.smartpack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inovatech.smartpack.R
import com.inovatech.smartpack.ui.theme.Background
import com.inovatech.smartpack.ui.theme.BlueTertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonInitScreen(
    title: String,
    nomBotoPrincipal: String = "Continuar",
    onNextClick: () -> Unit,
    onCancelClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Scaffold(containerColor = Color.Transparent, modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.app_name),
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onCancelClick, content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        })
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.Black
                    )
                )
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(), onClick = onNextClick
                    ) {
                        Text(nomBotoPrincipal, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray, contentColor = Color.White
                        ), modifier = Modifier.fillMaxWidth(), onClick = onCancelClick
                    ) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 32.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    title, fontWeight = FontWeight.Bold, fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                content()
            }
        }
    }
}