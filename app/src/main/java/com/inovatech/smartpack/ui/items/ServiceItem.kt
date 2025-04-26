package com.inovatech.smartpack.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inovatech.smartpack.R
import com.inovatech.smartpack.model.Package
import com.inovatech.smartpack.model.Service
import com.inovatech.smartpack.model.ServiceStatus
import com.inovatech.smartpack.ui.theme.BlueSecondary
import com.inovatech.smartpack.ui.theme.OrangePrimary

@Composable
fun ServiceItem(
    service: Service,
    isExpanded: Boolean = false,
    onClick: () -> Unit = {},
    onNavToDetail: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onModifyServiceClick: () -> Unit = {},
) {
    //TODO Fer que també es pugui modificar el servei quan està en ORDENAT o TRANSIT
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
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Color.White.copy(alpha = 0.4f))
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Estat: ", fontWeight = FontWeight.Bold, color = Color.White)
            Text(
                text = service.status.toString(), color = Color.White.copy(alpha = 0.9f)
            )
        }

        if (isExpanded && (service.status == ServiceStatus.ORDENAT ||
                    service.status == ServiceStatus.TRANSIT)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = onModifyServiceClick,
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Modificar", color = Color.White, fontSize = 13.sp)
                }

                if (service.status == ServiceStatus.ORDENAT) {
                    Button(
                        onClick = onDeleteClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Eliminar", color = Color.White, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ServiceItemTRANSITPreview() {
    ServiceItem(
        service = Service(
            id = 1, packageToDeliver = Package(
                id = 1,
                details = "Entregar només de 14h a 18h, sinó entregar a un punt de recollida.",
                recipientName = "Roger González",
                recipientAddress = "Carrer Pla de l'Estany, 27 08295 Sant Vicenç de Castellet",
                recipientPhone = "666555444"
            ), status = ServiceStatus.TRANSIT
        ), isExpanded = true, onClick = {}, onDeleteClick = {}, onModifyServiceClick = {})
}

@Preview
@Composable
fun ServiceItemORDENATPreview() {
    ServiceItem(
        service = Service(
            id = 1, packageToDeliver = Package(
                id = 1,
                details = "Entregar només de 14h a 18h, sinó entregar a un punt de recollida.",
                recipientName = "Roger González",
                recipientAddress = "Carrer Pla de l'Estany, 27 08295 Sant Vicenç de Castellet",
                recipientPhone = "666555444"
            ), status = ServiceStatus.ORDENAT
        ), isExpanded = true, onClick = {}, onDeleteClick = {}, onModifyServiceClick = {})
}

@Preview
@Composable
fun ServiceItemENTREGATPreview() {
    ServiceItem(
        service = Service(
            id = 1, packageToDeliver = Package(
                id = 1,
                details = "Entregar només de 14h a 18h, sinó entregar a un punt de recollida.",
                recipientName = "Roger González",
                recipientAddress = "Carrer Pla de l'Estany, 27 08295 Sant Vicenç de Castellet",
                recipientPhone = "666555444"
            ), status = ServiceStatus.ENTREGAT
        ), isExpanded = true, onClick = {}, onDeleteClick = {}, onModifyServiceClick = {})
}