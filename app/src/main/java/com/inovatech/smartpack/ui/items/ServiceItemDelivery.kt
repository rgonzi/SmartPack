package com.inovatech.smartpack.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inovatech.smartpack.R
import com.inovatech.smartpack.model.Package
import com.inovatech.smartpack.model.Service
import com.inovatech.smartpack.model.ServiceStatus
import com.inovatech.smartpack.ui.theme.*

@Composable
fun ServiceItemDelivery(
    service: Service,
    isExpanded: Boolean,
    onClick: () -> Unit,
    onNavToDetail: () -> Unit,
    onStatusChange: (ServiceStatus) -> Unit,
    onConfirmDelivery: (String) -> Unit
) {
    var showConfirmDeliveryDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(color = BlueSecondary)
            .border(1.dp, Color.Black, RoundedCornerShape(24.dp))
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
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

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.packageToDeliver.recipientName,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = service.packageToDeliver.recipientAddress,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    maxLines = 2
                )
            }

            IconButton(onClick = onNavToDetail) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusButton("Entregat", color = StatusDelivered) {
                    showConfirmDeliveryDialog = true
                }
                StatusButton("Absent", color = StatusNotDelivered) {
                    onStatusChange(ServiceStatus.NO_ENTREGAT)
                }
                StatusButton("Retornat", color = StatusReturned) {
                    onStatusChange(ServiceStatus.RETORNAT)
                }
            }
        }
    }

    if (showConfirmDeliveryDialog) {
        ConfirmDeliveryDialog(
            onDismiss = { showConfirmDeliveryDialog = false },
            onConfirm = {
                onConfirmDelivery(it)
                showConfirmDeliveryDialog = false
            }
        )
    }

}

@Composable
fun ConfirmDeliveryDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var recipientPhone by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirma el telèfon del destinatari:", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                OutlinedTextField(
                    value = recipientPhone,
                    onValueChange = { recipientPhone = it },
                    label = { Text("Telèfon del destinatari") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    keyboardActions = KeyboardActions(onDone = { onConfirm(recipientPhone) }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(recipientPhone) }) { Text("Confirmar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel·lar") }
        }
    )
}


@Composable
fun StatusButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text, color = Color.White, fontSize = 13.sp)
    }
}

@Preview
@Composable
fun ServiceItemDeliveryPreview() {
    ServiceItemDelivery(
        service = Service(
            id = 1, packageToDeliver = Package(
                id = 1,
                details = "Entregar només de 14h a 18h, sinó entregar a un punt de recollida.",
                recipientName = "Roger González",
                recipientAddress = "Carrer Pla de l'Estany, 27 08295 Sant Vicenç de Castellet",
                recipientPhone = "666555444"
            ), status = ServiceStatus.ORDENAT
        ), isExpanded = true,
        onClick = { },
        onNavToDetail = { },
        onStatusChange = { },
        onConfirmDelivery = { }
    )
}