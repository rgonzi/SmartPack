package com.inovatech.smartpack.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.inovatech.smartpack.model.Vehicle
import com.inovatech.smartpack.ui.theme.BlueSecondary

@Composable
fun VehicleListItem(
    vehicle: Vehicle,
    onClick: (Vehicle) -> Unit,
) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = BlueSecondary)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .clickable { onClick(vehicle) }
            .padding(horizontal = 8.dp), colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            //Id + Marca + Model
            Text(
                text = "#${vehicle.id}: ${vehicle.brand} - ${vehicle.model} - ",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            //Matrícula
            Text(text = vehicle.plate, color = Color.White)
        }
    }
}