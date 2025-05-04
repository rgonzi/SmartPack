package com.inovatech.smartpack.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.inovatech.smartpack.model.Company
import com.inovatech.smartpack.ui.theme.BlueSecondary

@Composable
fun CompanyListItem(
    company: Company,
    onClick: (Company) -> Unit,
) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = BlueSecondary)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .clickable { onClick(company) }
            .padding(horizontal = 8.dp), colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                //Id + nom
                Text(
                    text = "#${company.id}: ${company.name} - ",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                //Email
                Text(text = company.nif, color = Color.White)
            }
            Text(text = "${company.email} - ${company.phone}", color = Color.White)
        }
    }
}