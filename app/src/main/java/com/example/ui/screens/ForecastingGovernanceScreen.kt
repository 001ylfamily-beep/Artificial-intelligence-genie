package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.local.ProcurementProposalEntity
import com.example.data.local.TaskEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.AIISGViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ForecastingGovernanceScreen(
    viewModel: AIISGViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.allTasks.collectAsState()
    val procurements by viewModel.procurementProposals.collectAsState()
    val policies by viewModel.policyRules.collectAsState()
    val drStatus by viewModel.disasterRecoveryStatus.collectAsState()

    var activeSubTab by remember { mutableStateOf("ATTENTION") }
    var selectedTaskForLineage by remember { mutableStateOf<TaskEntity?>(null) }
    var selectedProcurementForApproval by remember { mutableStateOf<ProcurementProposalEntity?>(null) }

    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale.US) }

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
                    text = "BI FORECASTING & GOVERNANCE",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonCyan,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Predictive Intelligence • Company Constitution • Procurement",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(CyberGold.copy(alpha = 0.2f))
                    .border(0.8.dp, CyberGold, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "AI FORECAST MODE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberGold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Switch Tabs: ATTENTION, FORECAST, PROCUREMENT, POLICY, LINEAGE
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(
                Triple("ATTENTION", "Attention", Icons.Default.PriorityHigh),
                Triple("FORECAST", "Forecasting", Icons.Default.TrendingUp),
                Triple("PROCUREMENT", "Procurement", Icons.Default.ShoppingCart),
                Triple("POLICY", "Constitution", Icons.Default.Gavel),
                Triple("LINEAGE", "Explain Lineage", Icons.Default.Timeline)
            ).forEach { (tabId, label, icon) ->
                val isSelected = activeSubTab == tabId
                Button(
                    onClick = { activeSubTab = tabId },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) NeonCyan.copy(alpha = 0.2f) else SurfaceCard,
                        contentColor = if (isSelected) NeonCyan else TextSecondary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .border(1.dp, if (isSelected) NeonCyan else BorderSubtle, RoundedCornerShape(8.dp))
                ) {
                    Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = label, fontSize = 8.5.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (activeSubTab) {
            "ATTENTION" -> {
                // "What Needs Attention?" Dashboard
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().border(1.dp, CyberGold.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = CyberGold, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "CRITICAL ACTION ITEMS (CEO ATTENTION)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "1. Pending Outward Cloud GPU Settlement (\$8,240.00) awaits verification.", fontSize = 11.sp, color = TextSecondary)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "2. Apex Global Technologies contract proposal requires outbound communication authorization.", fontSize = 11.sp, color = TextSecondary)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "3. Autonomous Procurement: Lambda Cloud Cluster proposal pending sign-off.", fontSize = 11.sp, color = TextSecondary)
                            }
                        }
                    }

                    item {
                        // Disaster Recovery Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().border(0.8.dp, BorderSubtle, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "DISASTER RECOVERY MODE (DR)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CrimsonAlert)
                                    Button(
                                        onClick = { viewModel.triggerDisasterRecoverySimulation() },
                                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonAlert, contentColor = Color.White),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                        modifier = Modifier.height(28.dp).testTag("trigger_dr_sim_btn")
                                    ) {
                                        Text("TEST DR RESTORE", fontSize = 8.sp, fontWeight = FontWeight.Black)
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "State: $drStatus", fontSize = 10.sp, color = TextPrimary)
                            }
                        }
                    }
                }
            }

            "FORECAST" -> {
                // Predictive Forecasting
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().border(1.dp, CyberGold, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "[PREDICTIVE MODEL PROJECTION — NOT FACT]",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CyberGold,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "All figures below are statistical extrapolations synthesized by Data Analyst Cipher Matrix from current enterprise contracts and compute burnout.",
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().border(0.8.dp, BorderSubtle, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "REVENUE & EXPENSE FORECAST (Q4 2026)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(text = "Projected Inflow", fontSize = 10.sp, color = TextSecondary)
                                        Text(text = "\$142,500.00", fontSize = 14.sp, fontWeight = FontWeight.Black, color = EmeraldGreen)
                                    }
                                    Column {
                                        Text(text = "Projected Infrastructure", fontSize = 10.sp, color = TextSecondary)
                                        Text(text = "\$24,600.00", fontSize = 14.sp, fontWeight = FontWeight.Black, color = CyberGold)
                                    }
                                    Column {
                                        Text(text = "Net Margin", fontSize = 10.sp, color = TextSecondary)
                                        Text(text = "+82.7%", fontSize = 14.sp, fontWeight = FontWeight.Black, color = NeonCyan)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().border(0.8.dp, BorderSubtle, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "CAPACITY & BOTTLENECK PREDICTION", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "• Software Engineering department will reach 88% task queue saturation within 14 days.\n• Recommendation: Approve Floor 3 DevOps Specialist to balance build pipelines.\n• Storage consumption stable at 14.2 MB/month.",
                                    fontSize = 10.sp,
                                    color = TextSecondary,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }

            "PROCUREMENT" -> {
                // Autonomous Procurement Proposals
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(procurements) { prop ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().border(0.8.dp, BorderSubtle, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = prop.itemTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = "Category: ${prop.category}", fontSize = 10.sp, color = TextSecondary)
                                    }
                                    Text(
                                        text = "${currencyFormatter.format(prop.estimatedMonthlyCost)}/mo",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = CyberGold
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Vendor A: ${prop.vendorA}", fontSize = 10.sp, color = TextMuted)
                                Text(text = "Vendor B: ${prop.vendorB}", fontSize = 10.sp, color = TextMuted)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Recommended: ${prop.recommendedVendor}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Justification: ${prop.justification}", fontSize = 10.sp, color = TextSecondary)

                                Spacer(modifier = Modifier.height(10.dp))
                                if (prop.status == "PENDING_CEO_APPROVAL") {
                                    Button(
                                        onClick = { selectedProcurementForApproval = prop },
                                        colors = ButtonDefaults.buttonColors(containerColor = CyberGold, contentColor = Color.Black),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.align(Alignment.End).height(30.dp).testTag("procure_approve_${prop.id}")
                                    ) {
                                        Text("AUTHORIZE PURCHASE", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Row(
                                        modifier = Modifier.align(Alignment.End),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "PURCHASE AUTHORIZED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "POLICY" -> {
                // Company Constitution / Policy Rules
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(policies) { rule ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().border(0.8.dp, BorderSubtle, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = rule.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (rule.requiresApproval) CrimsonAlert.copy(alpha = 0.2f) else EmeraldGreen.copy(alpha = 0.2f)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (rule.requiresApproval) "CEO APPROVAL MANDATORY" else "AUTONOMOUS ALLOWED",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (rule.requiresApproval) CrimsonAlert else EmeraldGreen
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = rule.conditionText, fontSize = 11.sp, color = TextSecondary)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "Category: ${rule.category} • Enforcement: ${rule.enforcementLevel}", fontSize = 9.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }

            "LINEAGE" -> {
                // Explainable Command History Lineage
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(tasks) { task ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(0.8.dp, BorderSubtle, RoundedCornerShape(12.dp))
                                .clickable { selectedTaskForLineage = task }
                                .testTag("lineage_card_${task.id}")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "${task.id}: ${task.title}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text(text = "Tap to Trace Lineage", fontSize = 9.sp, color = CyberGold)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Origin: ${task.triggerSource}",
                                    fontSize = 10.sp,
                                    color = NeonCyan
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Commander Reasoning: ${task.commanderReasoning}",
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Traceable Lineage Inspection Modal
        if (selectedTaskForLineage != null) {
            val task = selectedTaskForLineage!!
            AlertDialog(
                onDismissRequest = { selectedTaskForLineage = null },
                containerColor = SurfaceDark,
                title = {
                    Text("Explainable Lineage: ${task.id}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                },
                text = {
                    Column {
                        Text(text = "1. TRIGGER SOURCE:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberGold)
                        Text(text = task.triggerSource, fontSize = 11.sp, color = TextPrimary)
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "2. COMMANDER REASONING & DISPATCH:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberGold)
                        Text(text = task.commanderReasoning, fontSize = 11.sp, color = TextPrimary)
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "3. AI MODEL ROUTED:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberGold)
                        Text(text = task.modelUsed, fontSize = 11.sp, color = NeonCyan)
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "4. ASSIGNED AGENT:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberGold)
                        Text(text = "${task.assignedAgentId} (${task.department})", fontSize = 11.sp, color = TextPrimary)
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "5. VERIFICATION EVIDENCE LOG:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CyberGold)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(ObsidianBg)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = task.evidenceLog.ifBlank { "Verified clean execution." },
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TextSecondary
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { selectedTaskForLineage = null }) {
                        Text("Close", color = NeonCyan)
                    }
                }
            )
        }

        // Procurement Authorization Modal
        if (selectedProcurementForApproval != null) {
            val prop = selectedProcurementForApproval!!
            AlertDialog(
                onDismissRequest = { selectedProcurementForApproval = null },
                containerColor = SurfaceDark,
                title = {
                    Text("CEO Procurement Authorization", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CyberGold)
                },
                text = {
                    Column {
                        Text(text = prop.itemTitle, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "Cost: ${currencyFormatter.format(prop.estimatedMonthlyCost)}/month", fontSize = 12.sp, color = NeonCyan)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Recommended: ${prop.recommendedVendor}", fontSize = 11.sp, color = EmeraldGreen)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Justification: ${prop.justification}", fontSize = 10.sp, color = TextSecondary)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.approveProcurement(prop.id)
                            selectedProcurementForApproval = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberGold, contentColor = Color.Black)
                    ) {
                        Text("Authorize Purchase")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { selectedProcurementForApproval = null }) {
                        Text("Cancel", color = TextSecondary)
                    }
                }
            )
        }
    }
}
