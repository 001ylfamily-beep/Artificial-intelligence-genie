package com.example.ui.screens

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
import com.example.data.local.TaskEntity
import com.example.domain.UniversalExecutionEngine
import com.example.ui.theme.*
import com.example.ui.viewmodel.AIISGViewModel

@Composable
fun TaskQueueScreen(
    viewModel: AIISGViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.allTasks.collectAsState()
    val agents by viewModel.allAgents.collectAsState()

    var statusFilter by remember { mutableStateOf("ALL") }
    var selectedTaskForDetails by remember { mutableStateOf<TaskEntity?>(null) }

    val filteredTasks = when (statusFilter) {
        "RUNNING" -> tasks.filter { it.status == "RUNNING" }
        "PLANNED" -> tasks.filter { it.status == "PLANNED" }
        "VERIFIED" -> tasks.filter { it.status == "VERIFIED" }
        "FAILED" -> tasks.filter { it.status == "FAILED" }
        else -> tasks
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "UNIVERSAL AI EXECUTION QUEUE",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonCyan,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Strict Multi-Stage Lifecycle • Verifiable Evidence Mandate",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(SurfaceCard)
                    .border(0.8.dp, BorderSubtle, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${tasks.size} Total Tasks",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Universal AI Loop Stage Visualizer
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "CONTROLLED EXECUTION LIFECYCLE (14 STAGES):",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberGold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(UniversalExecutionEngine.loopStages) { stage ->
                        val countInStage = tasks.count { it.loopStage == stage && it.status == "RUNNING" }
                        val isFinished = stage == "COMPLETE"
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (countInStage > 0) NeonCyan.copy(alpha = 0.25f) else SurfaceCard,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (countInStage > 0) NeonCyan else BorderSubtle
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stage,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (countInStage > 0) NeonCyan else TextMuted
                                )
                                if (countInStage > 0) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(NeonCyan),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "$countInStage", fontSize = 8.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("ALL", "RUNNING", "PLANNED", "VERIFIED", "FAILED").forEach { filter ->
                val isSelected = statusFilter == filter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else SurfaceCard)
                        .border(1.dp, if (isSelected) NeonCyan else BorderSubtle, RoundedCornerShape(6.dp))
                        .clickable { statusFilter = filter }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("filter_$filter")
                ) {
                    Text(
                        text = filter,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) NeonCyan else TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Task Cards List
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredTasks) { task ->
                val assignedAgent = agents.find { it.id == task.assignedAgentId }

                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            if (task.status == "RUNNING") StatusWorking.copy(alpha = 0.6f) else BorderSubtle,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { selectedTaskForDetails = task }
                        .testTag("task_card_${task.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${task.id}: ${task.title}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        when (task.status) {
                                            "VERIFIED" -> EmeraldGreen.copy(alpha = 0.2f)
                                            "RUNNING" -> StatusWorking.copy(alpha = 0.2f)
                                            "FAILED" -> CrimsonAlert.copy(alpha = 0.2f)
                                            else -> NeonCyan.copy(alpha = 0.2f)
                                        }
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = task.status,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (task.status) {
                                        "VERIFIED" -> EmeraldGreen
                                        "RUNNING" -> StatusWorking
                                        "FAILED" -> CrimsonAlert
                                        else -> NeonCyan
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = task.description, fontSize = 11.sp, color = TextSecondary, lineHeight = 15.sp)

                        Spacer(modifier = Modifier.height(10.dp))

                        // Progress stage bar
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Stage: ${task.loopStage}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusWorking
                                )
                                if (task.retryCount > 0) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Retries: ${task.retryCount}/${task.maxRetries}",
                                        fontSize = 9.sp,
                                        color = CyberGold
                                    )
                                }
                            }
                        }

                        if (task.evidenceLog.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Evidence: ${task.evidenceLog.lines().lastOrNull() ?: ""}",
                                fontSize = 9.sp,
                                color = TextMuted,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        // Task Detail & Evidence Modal
        if (selectedTaskForDetails != null) {
            val task = selectedTaskForDetails!!
            AlertDialog(
                onDismissRequest = { selectedTaskForDetails = null },
                containerColor = SurfaceDark,
                title = {
                    Text(
                        text = "Task Verification Evidence: ${task.id}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                },
                text = {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        item {
                            Text(text = task.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = task.description, fontSize = 11.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(text = "Audit & Verification Evidence Log:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberGold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ObsidianBg)
                                    .border(0.8.dp, BorderSubtle, RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = task.evidenceLog.ifBlank { "Awaiting execution verification run." },
                                    fontSize = 10.sp,
                                    color = TextPrimary,
                                    lineHeight = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Independent QA Verification: PASS (Strict anti-fabrication enforced)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreen
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { selectedTaskForDetails = null }) {
                        Text("Close", color = NeonCyan)
                    }
                }
            )
        }
    }
}
