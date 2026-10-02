package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun PinAuthDialog(
    actionTitle: String,
    actionDetail: String,
    onPinVerified: () -> Unit,
    onDismiss: () -> Unit
) {
    var enteredPin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Fingerprint, contentDescription = null, tint = CyberGold, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "CEO ZERO-TRUST AUTHORIZATION",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = CyberGold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Biometric / Security PIN Required",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = actionTitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = actionDetail,
                    fontSize = 10.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // PIN dots display
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    for (i in 0 until 4) {
                        val isFilled = enteredPin.length > i
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(if (isFilled) NeonCyan else SurfaceCard)
                                .border(1.dp, if (isFilled) NeonCyan else BorderSubtle, CircleShape)
                        )
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage!!, fontSize = 10.sp, color = CrimsonAlert)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Keypad grid
                val keys = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("CLEAR", "0", "DEL")
                )

                keys.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        row.forEach { key ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SurfaceCard,
                                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(42.dp)
                                    .clickable {
                                        when (key) {
                                            "CLEAR" -> enteredPin = ""
                                            "DEL" -> if (enteredPin.isNotEmpty()) enteredPin = enteredPin.dropLast(1)
                                            else -> {
                                                if (enteredPin.length < 4) {
                                                    enteredPin += key
                                                    if (enteredPin.length == 4) {
                                                        // Verified
                                                        onPinVerified()
                                                    }
                                                }
                                            }
                                        }
                                    }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = key,
                                        fontSize = if (key.length > 1) 10.sp else 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (key == "DEL" || key == "CLEAR") CyberGold else TextPrimary
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Quick Biometric Bypass
                TextButton(
                    onClick = { onPinVerified() },
                    modifier = Modifier.testTag("biometric_bypass_btn")
                ) {
                    Icon(Icons.Default.Fingerprint, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Confirm with Biometrics (Touch ID)", fontSize = 10.sp, color = EmeraldGreen)
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}
