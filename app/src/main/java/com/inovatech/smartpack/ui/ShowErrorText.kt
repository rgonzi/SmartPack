import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun ShowErrorText(
    text: String,
) {
    Text(
        text = text,
        color = Color.Red,
        fontSize = 12.sp,
        textAlign = TextAlign.Start
    )
}