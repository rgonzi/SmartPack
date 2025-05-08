package com.inovatech.smartpack.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import com.inovatech.smartpack.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.inovatech.smartpack.model.Role
import com.inovatech.smartpack.model.User
import com.inovatech.smartpack.ui.theme.BlueSecondary

@Composable
fun UserListItem(
    user: User,
    onClick: (User) -> Unit,
) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color = BlueSecondary)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .clickable { onClick(user) }
            .padding(horizontal = 8.dp), colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            //Id + nom
            Text(
                text = "#${user.id}: ${user.name.orEmpty()} - ",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            //Email
            Text(text = user.email!!, color = Color.White)
            //Mostrem una petita icona per saber si és transportista o no
            if (user.role == Role.ROLE_DELIVERYMAN) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_delivery_truck),
                    contentDescription = null,
                    tint = Color.Green,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}