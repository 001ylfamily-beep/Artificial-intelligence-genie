package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
fun SecurityCenterScreen(
    viewModel: AIISGViewModel,
    modifier: Modifier = Modifier
) {
    val auditLogs by viewModel.auditLogs.collectAsState()
    val isScanning by viewModel.securityScanRunning.collectAsState()
    val isEmergencyStopped by viewModel.isEmergencyStopped.collectAsState()
    val dateFormatter = remember { SimpleDateFormat("HH:mm:ss", Locale.US) }

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
                    text = "SECURITY OPERATIONS CENTER (SOC)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonCyan,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Aegis Sentinel Watchtower • Tamper-Resistant SHA-256 Audit Trail",
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
                    text = "ZERO BREACHES",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Guardian Sentinel Status Card
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, if (isEmergencyStopped) CrimsonAlert else EmeraldGreen.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isEmergencyStopped) CrimsonAlert.copy(alpha = 0.2f) else EmeraldGreen.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = if (isEmergencyStopped) CrimsonAlert else EmeraldGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isEmergencyStopped) "EMERGENCY CONTAINMENT ACTIVE" else "AEGIS SENTINEL: ARMED & PATROLLING",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isEmergencyStopped) CrimsonAlert else TextPrimary
                        )
                        Text(
                            text = "Credential storage encrypted • Zero-trust authorization active",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }

                Button(
                    onClick = { viewModel.runSecurityScan() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isScanning) CyberGold else NeonCyan,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp).testTag("trigger_security_scan_button")
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isScanning) "SCANNING..." else "VULN SCAN",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Audit Trail Feed
        Text(
            text = "LIVE AUDIT LOG & CRYPTOGRAPHIC VERIFICATION HASHES:",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = CyberGold,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(auditLogs) { log ->
                val timeStr = dateFormatter.format(Date(log.timestampEpochMs))
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().border(0.6.dp, BorderSubtle, RoundedCornerShape(8.dp))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "[$timeStr]",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${log.agentName} (${log.action})",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(
                                        when (log.severity) {
                                            "CRITICAL" -> CrimsonAlert.copy(alpha = 0.2f)
                                            "SECURITY" -> CyberGold.copy(alpha = 0.2f)
                                            else -> NeonCyan.copy(alpha = 0.15f)
                                        }
                                    )
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = log.severity,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (log.severity) {
                                        "CRITICAL" -> CrimsonAlert
                                        "SECURITY" -> CyberGold
                                        else -> NeonCyan
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${log.tool} → ${log.target}: ${log.result}",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Proof Hash: ${log.verificationHash}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}
