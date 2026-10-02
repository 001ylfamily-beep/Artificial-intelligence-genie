package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.ui.theme.*
import com.example.ui.viewmodel.AIISGViewModel

@Composable
fun CommanderScreen(
    viewModel: AIISGViewModel,
    modifier: Modifier = Modifier
) {
    var commandInput by remember { mutableStateOf("") }
    val commanderSpeech by viewModel.commanderSpeechOutput.collectAsState()
    val isListening by viewModel.isListening.collectAsState()
    val agents by viewModel.allAgents.collectAsState()
    val activeTasks by viewModel.activeTasks.collectAsState()

    var showCreateTaskModal by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(16.dp)
    ) {
        // Central Commander Holographic Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NeonCyan.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NeonCyan.copy(alpha = 0.15f))
                                .border(1.5.dp, NeonCyan, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = "Commander AI",
                                tint = NeonCyan,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "CENTRAL COMMANDER ASTRA",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = NeonCyan,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Autonomous Orchestration Core • Verifiable Execution",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(EmeraldGreen.copy(alpha = 0.2f))
                            .border(0.8.dp, EmeraldGreen, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "ORCHESTRATING",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Commander Live Voice Transcript
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SurfaceDark)
                        .border(0.8.dp, BorderSubtle, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Audio Speech Out",
                            tint = CyberGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "\"$commanderSpeech\"",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Voice/Text Command Chips
        Text(
            text = "CEO VOICE DIRECTIVES (TAP TO DISPATCH):",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = CyberGold,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val suggestions = listOf(
                "Commander, start the website project.",
                "Show me what the developers are doing.",
                "Run defensive security scan.",
                "Generate today's evening accounting report.",
                "Start the weekly meeting.",
                "Create a new agent proposal.",
                "Ask research team to investigate."
            )
            items(suggestions) { cmd ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.clickable {
                        viewModel.dispatchVoiceCommand(cmd)
                    }
                ) {
                    Text(
                        text = cmd,
                        fontSize = 11.sp,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Command Bar (Text input + Mic toggle + Send)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceCard)
                .border(1.dp, if (isListening) NeonCyan else BorderSubtle, RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    viewModel.toggleListening()
                    if (!isListening) {
                        viewModel.dispatchVoiceCommand("Commander, start the website project.")
                    }
                },
                modifier = Modifier.testTag("voice_command_mic")
            ) {
                Icon(
                    imageVector = if (isListening) Icons.Default.Mic else Icons.Default.MicNone,
                    contentDescription = "Voice Input",
                    tint = if (isListening) NeonCyan else TextSecondary
                )
            }

            TextField(
                value = commandInput,
                onValueChange = { commandInput = it },
                placeholder = {
                    Text("Type command (e.g., 'Deploy Discrepancy Radar')...", fontSize = 12.sp, color = TextMuted)
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("commander_command_input")
            )

            IconButton(
                onClick = {
                    if (commandInput.isNotBlank()) {
                        viewModel.dispatchVoiceCommand(commandInput)
                        commandInput = ""
                    }
                },
                modifier = Modifier.testTag("send_command_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send Command",
                    tint = NeonCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Directives
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val quickDirectives = listOf(
                "Ek SaaS product banao",
                "Commander, start the website project",
                "Run security scan",
                "Deploy Discrepancy Radar",
                "Check financial ledger"
            )
            items(quickDirectives) { directive ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceCard,
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, BorderSubtle),
                    modifier = Modifier.clickable {
                        viewModel.dispatchVoiceCommand(directive)
                    }
                ) {
                    Text(
                        text = "💬 $directive",
                        fontSize = 10.sp,
                        color = CyberGold,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Operations Telemetry & Quick Task Action
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ACTIVE FLEET WORKLOAD (${activeTasks.size} Tasks Running)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )

            Button(
                onClick = { showCreateTaskModal = true },
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color(0xFF003844)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp).testTag("commander_add_task_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("DISPATCH TASK", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Active Tasks List
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(activeTasks) { task ->
                val assignedAgent = agents.find { it.id == task.assignedAgentId }
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().border(0.8.dp, BorderSubtle, RoundedCornerShape(10.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${task.id}: ${task.title}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        when (task.priority) {
                                            "CRITICAL" -> CrimsonAlert.copy(alpha = 0.2f)
                                            "HIGH" -> CyberGold.copy(alpha = 0.2f)
                                            else -> NeonCyan.copy(alpha = 0.2f)
                                        }
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = task.priority,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (task.priority) {
                                        "CRITICAL" -> CrimsonAlert
                                        "HIGH" -> CyberGold
                                        else -> NeonCyan
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = task.description, fontSize = 11.sp, color = TextSecondary, maxLines = 2)

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Assigned: ${assignedAgent?.name ?: task.assignedAgentId} (${task.department})",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                            Text(
                                text = "Loop Stage: ${task.loopStage}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = StatusWorking
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal to create task
    if (showCreateTaskModal) {
        var taskTitle by remember { mutableStateOf("") }
        var taskDesc by remember { mutableStateOf("") }
        var selectedDept by remember { mutableStateOf("SOFTWARE") }
        var selectedPriority by remember { mutableStateOf("HIGH") }

        AlertDialog(
            onDismissRequest = { showCreateTaskModal = false },
            containerColor = SurfaceDark,
            title = {
                Text("Dispatch Autonomous Task", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        label = { Text("Task Title", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("new_task_title_input")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = taskDesc,
                        onValueChange = { taskDesc = it },
                        label = { Text("Task Description / Specifications", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("new_task_desc_input")
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Target Department:", fontSize = 11.sp, color = TextSecondary)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("SOFTWARE", "RESEARCH", "SECURITY", "FINANCE").forEach { dept ->
                            FilterChip(
                                selected = selectedDept == dept,
                                onClick = { selectedDept = dept },
                                label = { Text(dept, fontSize = 9.sp) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (taskTitle.isNotBlank()) {
                            val defaultAgent = when (selectedDept) {
                                "SOFTWARE" -> "agent_05_python"
                                "RESEARCH" -> "agent_08_research"
                                "SECURITY" -> "agent_12_sec_guard"
                                else -> "agent_14_acct_mgr"
                            }
                            viewModel.createNewTask(
                                title = taskTitle,
                                desc = taskDesc.ifBlank { "Standard autonomous execution directive." },
                                agentId = defaultAgent,
                                priority = selectedPriority,
                                dept = selectedDept
                            )
                            showCreateTaskModal = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color(0xFF003844)),
                    modifier = Modifier.testTag("submit_create_task_button")
                ) {
                    Text("Dispatch to Commander")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateTaskModal = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }
}
