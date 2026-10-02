package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.NotificationItemEntity
import com.example.ui.theme.*

@Composable
fun EmergencyStopHeader(
    isEmergencyStopped: Boolean,
    currentFloor: Int,
    simulationSpeed: Float = 1f,
    isIsometric3D: Boolean = false,
    unreadNotifications: Int = 0,
    notificationsList: List<NotificationItemEntity> = emptyList(),
    onFloorChange: (Int) -> Unit,
    onSpeedChange: (Float) -> Unit = {},
    onToggle3D: () -> Unit = {},
    onNotificationClick: (NotificationItemEntity) -> Unit = {},
    onEmergencyStop: () -> Unit,
    onResume: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "alertPulse")
    val alertAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alertAlpha"
    )

    var showNotificationsDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceDark)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Company Branding
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF003844))
                        .border(1.5.dp, NeonCyan, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AIISG Genie",
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "AIISG",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = NeonCyan,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CyberGold.copy(alpha = 0.2f))
                                .border(0.8.dp, CyberGold, RoundedCornerShape(4.dp))
                                .padding(horizontal = 5.dp, vertical = 1.5.dp)
                        ) {
                            Text(
                                text = "CEO",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberGold
                            )
                        }
                    }
                    Text(
                        text = "Autonomous AI Office",
                        fontSize = 9.sp,
                        color = TextSecondary
                    )
                }
            }

            // Center: 3D Hologram Toggle & Simulation Speed
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // 3D Isometric View Toggle
                IconButton(
                    onClick = onToggle3D,
                    modifier = Modifier.size(32.dp).testTag("toggle_3d_btn")
                ) {
                    Icon(
                        imageVector = if (isIsometric3D) Icons.Default.ViewInAr else Icons.Default.Layers,
                        contentDescription = "Toggle 3D View",
                        tint = if (isIsometric3D) CyberGold else TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Speed Selector Chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SurfaceCard)
                        .border(0.8.dp, BorderSubtle, RoundedCornerShape(6.dp))
                        .clickable {
                            val nextSpeed = when (simulationSpeed) {
                                1f -> 2f
                                2f -> 5f
                                else -> 1f
                            }
                            onSpeedChange(nextSpeed)
                        }
                        .padding(horizontal = 7.dp, vertical = 4.dp)
                        .testTag("speed_controller_chip")
                ) {
                    Text(
                        text = "${simulationSpeed.toInt()}x Speed",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                }

                // Notification Bell with Badge
                Box(
                    modifier = Modifier.clickable { showNotificationsDialog = true }
                ) {
                    IconButton(
                        onClick = { showNotificationsDialog = true },
                        modifier = Modifier.size(32.dp).testTag("notification_bell_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = if (unreadNotifications > 0) CyberGold else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    if (unreadNotifications > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(CrimsonAlert),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$unreadNotifications",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Right: Emergency Stop or Resume Button
            if (!isEmergencyStopped) {
                Button(
                    onClick = onEmergencyStop,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CrimsonAlert,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier
                        .testTag("emergency_stop_button")
                        .height(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Emergency Stop",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "STOP",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }
            } else {
                Button(
                    onClick = onResume,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier
                        .testTag("resume_operations_button")
                        .height(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Resume Operations",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "RESUME",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Emergency Alert Bar if stopped
        AnimatedVisibility(visible = isEmergencyStopped) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CrimsonAlert.copy(alpha = alertAlpha * 0.25f))
                    .border(1.dp, CrimsonAlert, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Safe Lock",
                        tint = CrimsonAlert,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "EMERGENCY SAFETY LOCK ENGAGED: Autonomous tasks, browser agents, & computer controls frozen.",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }

    // In-App Notification Center Dialog
    if (showNotificationsDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationsDialog = false },
            containerColor = SurfaceDark,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = CyberGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Live Notification Center", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
            },
            text = {
                if (notificationsList.isEmpty()) {
                    Text("No active alerts. All company nodes nominal.", fontSize = 11.sp, color = TextMuted)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().heightIn(max = 340.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(notificationsList) { notif ->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (!notif.isRead) SurfaceCard else ObsidianBg
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(0.6.dp, if (!notif.isRead) CyberGold else BorderSubtle, RoundedCornerShape(8.dp))
                                    .clickable {
                                        onNotificationClick(notif)
                                        showNotificationsDialog = false
                                    }
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = notif.title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(
                                                    if (notif.priority == "HIGH") CrimsonAlert.copy(alpha = 0.2f) else NeonCyan.copy(alpha = 0.2f)
                                                )
                                                .padding(horizontal = 5.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = notif.priority,
                                                fontSize = 7.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (notif.priority == "HIGH") CrimsonAlert else NeonCyan
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = notif.message, fontSize = 10.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationsDialog = false }) {
                    Text("Close", color = NeonCyan)
                }
            }
        )
    }
}
