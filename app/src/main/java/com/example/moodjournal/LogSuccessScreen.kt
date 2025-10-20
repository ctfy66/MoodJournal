package com.example.moodjournal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LogSuccessScreen(
    onGoToDashboard: () -> Unit
) {
    val darkBackground = Color(0xFF0A0C1E)
    val purpleButton = Color(0xFF7C3AED)
    val cyanCheckmark = Color(0xFF00E5FF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Title at top
            Text(
                text = "情绪日记",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            // Success Message
            Text(
                text = "保存成功！",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 60.dp)
            )

            // Checkmark Icon
            Canvas(
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 80.dp)
            ) {
                val path = Path().apply {
                    // Draw checkmark
                    moveTo(size.width * 0.25f, size.height * 0.5f)
                    lineTo(size.width * 0.45f, size.height * 0.7f)
                    lineTo(size.width * 0.75f, size.height * 0.3f)
                }
                
                drawPath(
                    path = path,
                    color = cyanCheckmark,
                    style = Stroke(
                        width = 20f,
                        cap = StrokeCap.Round
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Go to Dashboard Button
            Button(
                onClick = onGoToDashboard,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleButton
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "返回主页",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
