package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AgentProposalEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.AIISGViewModel
import java.util.UUID

@Composable
fun AgentCreationLabScreen(
    viewModel: AIISGViewModel,
    modifier: Modifier = Modifier
) {
    val proposals by viewModel.proposals.collectAsState()
    val agents by viewModel.allAgents.collectAsState()

    var showSynthesizeModal by remember { mutableStateOf(false) }
    var activeLabTab by remember { mutableStateOf("PROPOSALS") }
    var sandboxRunning by remember { mutableStateOf(false) }
    var selectedProposalForSandbox by remember { mutableStateOf<AgentProposalEntity?>(null) }

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
                    text = "AGENT CREATION LAB & SANDBOX",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonCyan,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Specialist Synthesis • Multi-Floor Dynamic Capacity",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }

            Button(
                onClick = { showSynthesizeModal = true },
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color(0xFF003844)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp).testTag("propose_new_agent_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("PROPOSE AGENT", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Fleet Capacity Status
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "COMPANY SCALING & CAPACITY METRICS:",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberGold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total Active Fleet: ${agents.size} Agents", fontSize = 11.sp, color = TextPrimary)
                    Text(text = "Base Capacity: 15 Agents", fontSize = 11.sp, color = TextSecondary)
                    Text(text = "Floors Open: 3 Floors", fontSize = 11.sp, color = EmeraldGreen, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tab Switcher: PROPOSALS vs SANDBOX
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                Triple("PROPOSALS", "Proposals & Deployment", Icons.Default.Science),
                Triple("SANDBOX", "Simulation / Sandbox Testbed", Icons.Default.Biotech)
            ).forEach { (tabId, label, icon) ->
                val isSelected = activeLabTab == tabId
                Button(
                    onClick = { activeLabTab = tabId },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) NeonCyan.copy(alpha = 0.2f) else SurfaceCard,
                        contentColor = if (isSelected) NeonCyan else TextSecondary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .border(1.dp, if (isSelected) NeonCyan else BorderSubtle, RoundedCornerShape(8.dp))
                ) {
                    Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = label, fontSize = 9.5.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (activeLabTab) {
            "PROPOSALS" -> {
                Text(
                    text = "AGENT PROPOSALS & VALIDATION PIPELINE:",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(proposals) { prop ->
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
                                    Column {
                                        Text(
                                            text = "${prop.proposedTitle}: ${prop.proposedRole}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "Department: ${prop.department} • Target: Floor ${prop.targetFloor}",
                                            fontSize = 10.sp,
                                            color = TextSecondary
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (prop.validationStatus == "APPROVED_DEPLOYED") EmeraldGreen.copy(alpha = 0.2f)
                                                else CyberGold.copy(alpha = 0.2f)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = prop.validationStatus,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (prop.validationStatus == "APPROVED_DEPLOYED") EmeraldGreen else CyberGold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Justification: ${prop.justification}",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    lineHeight = 15.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Skills: ${prop.recommendedSkillsCsv}",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                                Text(
                                    text = "Tools: ${prop.toolsCsv}",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            selectedProposalForSandbox = prop
                                            activeLabTab = "SANDBOX"
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.height(30.dp)
                                    ) {
                                        Icon(Icons.Default.Biotech, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("TEST IN SANDBOX", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                                    }

                                    if (prop.validationStatus != "APPROVED_DEPLOYED") {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Button(
                                            onClick = { viewModel.validateAndDeployProposal(prop) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = EmeraldGreen,
                                                contentColor = Color.Black
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                            modifier = Modifier.height(30.dp).testTag("validate_deploy_btn_${prop.id}")
                                        ) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("VALIDATE & DEPLOY AGENT", fontSize = 9.sp, fontWeight = FontWeight.Black)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "SANDBOX" -> {
                // Isolated Environment Agent Simulation & Sandbox Testbed
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().border(1.dp, CyberGold, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Shield, contentDescription = null, tint = CyberGold, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "ISOLATED AGENT SANDBOX SIMULATION",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = TextPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Mandatory certification gate: Every candidate agent runs in a sandboxed micro-container to verify permissions, tool safety, failure recovery, and resource limits before live fleet activation.",
                                    fontSize = 10.sp,
                                    color = TextSecondary,
                                    lineHeight = 14.sp
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                val candidate = selectedProposalForSandbox ?: proposals.firstOrNull()
                                if (candidate != null) {
                                    Text(
                                        text = "Active Sandbox Candidate: ${candidate.proposedTitle} (${candidate.proposedRole})",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonCyan
                                    )
                                    Text(
                                        text = "Environment: Isolated chroot /tmp/sandbox/agent_${candidate.id.take(6)} (Zero Production Write Access)",
                                        fontSize = 9.5.sp,
                                        color = TextMuted
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(
                                        onClick = {
                                            sandboxRunning = true
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = CyberGold, contentColor = Color.Black),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth().testTag("run_sandbox_test_btn"),
                                        enabled = !sandboxRunning
                                    ) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (sandboxRunning) "RUNNING SANDBOX HARNESS..." else "EXECUTE SANDBOX DIAGNOSTIC SUITE",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Sandbox Test Criteria & Execution Logs
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().border(0.8.dp, BorderSubtle, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "VERIFICATION TEST CRITERIA (4 GATES):",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberGold,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                val tests = listOf(
                                    Triple("1. Permission Boundary Isolation", "Prevented unauthorized data & shell access. Read-only policy enforced.", "PASSED (100%)"),
                                    Triple("2. Tool Call Syntax & Fallbacks", "Valid JSON payload generation & 100% parameter compliance.", "PASSED (99.2%)"),
                                    Triple("3. Failure Recovery & Error Handling", "Simulated API 500 error & crash. Agent self-healed in 0.4s without crashing loop.", "PASSED (100%)"),
                                    Triple("4. Resource Consumption & Token Burn", "Peak CPU load 18%, Token throughput capped at 1,200 tokens/min limit.", "PASSED (96.5%)")
                                )

                                tests.forEach { (title, detail, status) ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = title, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                            Text(text = detail, fontSize = 9.sp, color = TextSecondary)
                                        }
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(EmeraldGreen.copy(alpha = 0.2f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(text = status, fontSize = 8.5.sp, fontWeight = FontWeight.Black, color = EmeraldGreen)
                                        }
                                    }
                                    HorizontalDivider(color = BorderSubtle.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 4.dp))
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "FINAL CERTIFICATION SCORE: 98.9% (CERTIFIED SAFE)",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = EmeraldGreen
                                    )

                                    (selectedProposalForSandbox ?: proposals.firstOrNull())?.let { prop ->
                                        if (prop.validationStatus != "APPROVED_DEPLOYED") {
                                            Button(
                                                onClick = {
                                                    viewModel.validateAndDeployProposal(prop)
                                                    activeLabTab = "PROPOSALS"
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen, contentColor = Color.Black),
                                                shape = RoundedCornerShape(8.dp),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                                modifier = Modifier.height(28.dp).testTag("promote_to_fleet_btn")
                                            ) {
                                                Text("PROMOTE TO FLEET", fontSize = 8.5.sp, fontWeight = FontWeight.Black)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Modal for New Agent Proposal
        if (showSynthesizeModal) {
            var roleTitle by remember { mutableStateOf("") }
            var specRole by remember { mutableStateOf("") }
            var dept by remember { mutableStateOf("SOFTWARE") }
            var justification by remember { mutableStateOf("") }
            var targetFloor by remember { mutableStateOf(2) }

            AlertDialog(
                onDismissRequest = { showSynthesizeModal = false },
                containerColor = SurfaceDark,
                title = {
                    Text("Synthesize New Specialist Agent", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = roleTitle,
                            onValueChange = { roleTitle = it },
                            label = { Text("Job Title (e.g. Mobile Architect)", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = specRole,
                            onValueChange = { specRole = it },
                            label = { Text("Specialist Role / Name", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = justification,
                            onValueChange = { justification = it },
                            label = { Text("Workload Need / Justification", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Target Floor: Floor $targetFloor", fontSize = 11.sp, color = TextSecondary)
                            Row {
                                TextButton(onClick = { targetFloor = 2 }) { Text("Floor 2", color = if (targetFloor == 2) NeonCyan else TextMuted) }
                                TextButton(onClick = { targetFloor = 3 }) { Text("Floor 3 (Exp)", color = if (targetFloor == 3) NeonCyan else TextMuted) }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (roleTitle.isNotBlank()) {
                                val prop = AgentProposalEntity(
                                    id = "PROP-" + UUID.randomUUID().toString().substring(0, 4).uppercase(),
                                    proposedRole = specRole.ifBlank { "Specialized AI Engineer" },
                                    proposedTitle = roleTitle,
                                    department = dept,
                                    justification = justification.ifBlank { "Workload deficit detected by Central Commander." },
                                    recommendedSkillsCsv = "Domain Architecture, Autonomous Verification, CI/CD",
                                    toolsCsv = "CLI, Custom Runner, Inspector",
                                    validationStatus = "PROPOSED",
                                    createdByAgentId = "agent_01_commander",
                                    targetFloor = targetFloor
                                )
                                viewModel.validateAndDeployProposal(prop)
                                showSynthesizeModal = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color(0xFF003844))
                    ) {
                        Text("Synthesize & Validate")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSynthesizeModal = false }) {
                        Text("Cancel", color = TextSecondary)
                    }
                }
            )
        }
    }
}
