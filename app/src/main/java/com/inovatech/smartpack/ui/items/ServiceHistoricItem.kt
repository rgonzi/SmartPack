package com.inovatech.smartpack.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inovatech.smartpack.model.ServiceHistoric
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Composable que defineix el disseny del item en clicar per veure detalls. Mostra l'historial d'estat del servei
 */
@Composable
fun ServiceHistoricItem(
    event: ServiceHistoric,
    historic: List<ServiceHistoric>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        // Columna de la línia de temps
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Dibuixem un punt
            Box(
                Modifier
                    .size(10.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            )
            //Dibuixem una línia de "temps"
            if (event != historic.last()) {
                Spacer(Modifier.height(4.dp))
                Box(
                    Modifier
                        .width(2.dp)
                        .height(60.dp)
                        .background(Color.Gray)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        //Contingut de l'event
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Text(
                text = LocalDateTime
                    .parse(event.statusDate, DateTimeFormatter.ISO_DATE_TIME)
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                ),
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(Modifier.height(4.dp))

            Text(
                text = "${event.statusType}: ${event.status}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (event.changeDescription.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(Modifier.height(6.dp))
                Text(
                    text = event.changeDescription,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServiceHistoricItemPreview() {
    val sampleHistoric = listOf(
        ServiceHistoric(
            id = 1L, serviceId = 42L, deliverymanId = 7L,
            status = "Assignat",
            changeDescription = "El servei ha estat assignat al repartidor.",
            recipientAddress = "C/ Exemple, 123",
            statusType = "ASSIGN",
            statusDate = "2025-04-26T18:31:11.129327"
        ),
        ServiceHistoric(
            id = 2L, serviceId = 42L, deliverymanId = 7L,
            status = "En curs",
            changeDescription = "El repartidor ha recollit el paquet.",
            recipientAddress = "C/ Exemple, 123",
            statusType = "PICKUP",
            statusDate = "2025-04-27T18:31:11.129327"
        ),
        ServiceHistoric(
            id = 3L, serviceId = 42L, deliverymanId = 7L,
            status = "Entregat",
            changeDescription = "El paquet ha estat lliurat al destinatari.",
            recipientAddress = "C/ Exemple, 123",
            statusType = "DELIVER",
            statusDate = "2025-04-28T18:31:11.129327"
        )
    )

    // Preview de tres ítems apilats
    Column {
        sampleHistoric.forEach { event ->
            ServiceHistoricItem(
                event = event,
                historic = sampleHistoric
            )
        }
    }
}
