package com.example.ui.screens

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AIISGViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LiveIntercomScreen(
    viewModel: AIISGViewModel,
    modifier: Modifier = Modifier
) {
    val chatMessages by viewModel.allAgentChats.collectAsState()
    var selectedChannel by remember { mutableStateOf("ALL") }
    var ceoMessageInput by remember { mutableStateOf("") }
    val dateFormatter = remember { SimpleDateFormat("HH:mm:ss", Locale.US) }

    val filteredMessages = if (selectedChannel == "ALL") chatMessages else chatMessages.filter { it.department == selectedChannel }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "AGENT INTERCOM & LIVE COLLABORATION",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonCyan,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "P2P Agent Mesh Feed • CEO Channel Broadcast",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(EmeraldGreen.copy(alpha = 0.2f))
                    .border(0.8.dp, EmeraldGreen, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "LIVE MESH ACTIVE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Department Channels Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("ALL", "SOFTWARE", "SECURITY", "FINANCE", "EXECUTIVE").forEach { ch ->
                val isSelected = selectedChannel == ch
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else SurfaceCard)
                        .border(1.dp, if (isSelected) NeonCyan else BorderSubtle, RoundedCornerShape(6.dp))
                        .clickable { selectedChannel = ch }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("intercom_channel_$ch")
                ) {
                    Text(
                        text = ch,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) NeonCyan else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Message Feed
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredMessages) { msg ->
                val isCeo = msg.isCeoDirective
                val timeStr = dateFormatter.format(Date(msg.timestampEpochMs))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCeo) CyberGold.copy(alpha = 0.15f) else SurfaceCard
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            if (isCeo) CyberGold else BorderSubtle,
                            RoundedCornerShape(12.dp)
                        )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(if (isCeo) CyberGold else NeonCyan.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = msg.senderAgentName.firstOrNull()?.toString() ?: "A",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (isCeo) Color.Black else NeonCyan
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${msg.senderAgentName} → ${msg.recipientAgentName}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCeo) CyberGold else TextPrimary
                                )
                            }

                            Text(
                                text = "[$timeStr]",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                color = TextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = msg.message,
                            fontSize = 11.sp,
                            color = TextPrimary,
                            lineHeight = 15.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Channel: ${msg.department}", fontSize = 9.sp, color = TextMuted)
                            if (isCeo) {
                                Text(text = "CEO PRIORITY DIRECTIVE", fontSize = 9.sp, fontWeight = FontWeight.Black, color = CyberGold)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // CEO Intercom Broadcast Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceCard)
                .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = ceoMessageInput,
                onValueChange = { ceoMessageInput = it },
                placeholder = {
                    Text("Broadcast CEO directive into agent intercom...", fontSize = 11.sp, color = TextMuted)
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier.weight(1f).testTag("ceo_intercom_input")
            )

            IconButton(
                onClick = {
                    if (ceoMessageInput.isNotBlank()) {
                        viewModel.sendCeoIntercomMessage(ceoMessageInput, selectedChannel)
                        ceoMessageInput = ""
                    }
                },
                modifier = Modifier.testTag("send_ceo_intercom_btn")
            ) {
                Icon(Icons.Default.Send, contentDescription = "Broadcast", tint = CyberGold)
            }
        }
    }
}
