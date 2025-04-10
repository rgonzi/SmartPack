package com.inovatech.smartpack.ui.screens.deliveryman

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inovatech.smartpack.R
import com.inovatech.smartpack.model.Package
import com.inovatech.smartpack.model.Service
import com.inovatech.smartpack.model.ServiceStatus
import com.inovatech.smartpack.model.uiState.DeliveryManUiState
import com.inovatech.smartpack.ui.theme.BlueSecondary
import kotlinx.serialization.Serializable

@Serializable
data class ServiceItemDetail(val serviceId: Long)

@Composable
fun ServiceItemDetailScreen(
    viewModel: DeliveryManHomeViewModel,
    uiState: DeliveryManUiState,
    serviceId: Long,
    launchSnackbar: (String) -> Unit,
    onBackPressed: () -> Unit,
) {
    LaunchedEffect(uiState.msg) {
        if (!uiState.isLoading && uiState.msg != null) {
            launchSnackbar(uiState.msg)
            viewModel.resetMsg()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Detall del servei",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        ServiceItemDetailDelivery(service = uiState.assignedServices.find { it.id == serviceId }!!)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            ),
            onClick = { onBackPressed() }
        ) {
            Text("Tornar")
        }

    }
}


@Composable
fun ServiceItemDetailDelivery(
    service: Service,
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(color = BlueSecondary)
            .border(1.dp, Color.Black, RoundedCornerShape(24.dp))
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_package_box),
                    contentDescription = "Paquet",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                        .padding(4.dp),
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Destinatari:", fontWeight = FontWeight.Bold, color = Color.White)
                            Text(
                                text = service.packageToDeliver.recipientName, color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Telèfon:", fontWeight = FontWeight.Bold, color = Color.White)
                            Text(
                                text = service.packageToDeliver.recipientPhone,
                                color = Color.White.copy(alpha = 0.9f),
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            //Pes
                            Row {
                                Text(
                                    text = "Pes: ",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${service.packageToDeliver.weight} Kg",
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Detalls:", fontWeight = FontWeight.Bold, color = Color.White)
                            Text(
                                text = if (service.packageToDeliver.details.isEmpty()) {
                                    "- No hi ha detalls -"
                                } else {
                                    service.packageToDeliver.details
                                }, color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                    Text("Adreça: ", fontWeight = FontWeight.Bold, color = Color.White)
                    Text(
                        text = service.packageToDeliver.recipientAddress,
                        color = Color.White.copy(alpha = 0.9f),
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(8.dp))


                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Estat: ", fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    text = service.status.toString(),
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Preview
@Composable
fun ServiceItemDetailPreview() {
    ServiceItemDetailDelivery(
        Service(
            id = 1, packageToDeliver = Package(
                id = 1,
                details = "Entregar només de 14h a 18h, sinó entregar a un punt de recollida.",
                recipientName = "Roger González",
                recipientAddress = "Carrer Pla de l'Estany, 27 08295 Sant Vicenç de Castellet",
                recipientPhone = "666555444"
            ), status = ServiceStatus.TRANSIT
        )
    )
}