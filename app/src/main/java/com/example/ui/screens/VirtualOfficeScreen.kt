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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AgentEntity
import com.example.domain.OfficeDirectory
import com.example.domain.OfficeRoom
import com.example.ui.components.OfficeCanvas
import com.example.ui.theme.*
import com.example.ui.viewmodel.AIISGViewModel

@Composable
fun VirtualOfficeScreen(
    viewModel: AIISGViewModel,
    modifier: Modifier = Modifier
) {
    val agents by viewModel.allAgents.collectAsState()
    val floor by viewModel.currentFloor.collectAsState()
    val selectedAgent by viewModel.selectedAgent.collectAsState()
    val selectedRoom by viewModel.selectedRoom.collectAsState()
    val isIsometric3D by viewModel.isIsometric3DView.collectAsState()

    var showRoomDialog by remember { mutableStateOf(false) }
    var showAgentDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg)
    ) {
        // Floor selector tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                Triple(1, "Floor 1", "Ops & Eng"),
                Triple(2, "Floor 2", "Security & Lab"),
                Triple(3, "Floor 3", "Expansion")
            ).forEach { (fNum, title, sub) ->
                val isSelected = floor == fNum
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) NeonCyan.copy(alpha = 0.2f) else SurfaceCard
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = if (isSelected) 1.5.dp else 0.8.dp,
                            color = if (isSelected) NeonCyan else BorderSubtle,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { viewModel.selectFloor(fNum) }
                        .testTag("floor_tab_$fNum")
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) NeonCyan else TextPrimary
                        )
                        Text(
                            text = sub,
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Live Office Telemetry Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val workingCount = agents.count { it.status == "WORKING" || it.status == "TESTING" }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(StatusWorking)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$workingCount Active Working",
                    fontSize = 11.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = "${agents.size} Agents Registered",
                fontSize = 11.sp,
                color = TextSecondary
            )

            Text(
                text = "Tap any room or agent to inspect",
                fontSize = 11.sp,
                color = CyberGold
            )
        }

        // Main Virtual Office Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceDark)
                .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
        ) {
            OfficeCanvas(
                floor = floor,
                agents = agents,
                selectedRoom = selectedRoom,
                selectedAgent = selectedAgent,
                onRoomClick = { room ->
                    viewModel.selectRoom(room)
                    showRoomDialog = true
                },
                onAgentClick = { agent ->
                    viewModel.selectAgent(agent)
                    showAgentDialog = true
                },
                isIsometric3D = isIsometric3D
            )
        }

        // Room Inspector Modal
        if (showRoomDialog && selectedRoom != null) {
            val room = selectedRoom!!
            val roomAgents = agents.filter { it.currentRoom == room.id }

            AlertDialog(
                onDismissRequest = { showRoomDialog = false },
                containerColor = SurfaceDark,
                titleContentColor = NeonCyan,
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MeetingRoom, contentDescription = null, tint = NeonCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = room.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = room.description, fontSize = 12.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Agents Currently Present (${roomAgents.size}):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        if (roomAgents.isEmpty()) {
                            Text(text = "No agents stationed in this department right now.", fontSize = 11.sp, color = TextMuted)
                        } else {
                            roomAgents.forEach { ag ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SurfaceCard)
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(text = ag.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = ag.title, fontSize = 10.sp, color = TextSecondary)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (ag.status == "WORKING") StatusWorking.copy(alpha = 0.2f) else StatusThinking.copy(alpha = 0.2f)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = ag.status,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (ag.status == "WORKING") StatusWorking else StatusThinking
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showRoomDialog = false }) {
                        Text("Close", color = NeonCyan)
                    }
                }
            )
        }

        // Agent Deep Dive Modal
        if (showAgentDialog && selectedAgent != null) {
            val agent = selectedAgent!!
            AlertDialog(
                onDismissRequest = { showAgentDialog = false },
                containerColor = SurfaceDark,
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(android.graphics.Color.parseColor(agent.avatarColorHex))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = agent.name.firstOrNull()?.toString() ?: "A",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = agent.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = agent.title, fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                },
                text = {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        item {
                            // Department & Status
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Department: ${agent.department}", fontSize = 11.sp, color = TextSecondary)
                                Text(
                                    text = "Status: ${agent.status}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (agent.status == "WORKING") StatusWorking else StatusThinking
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            // Current Task
                            Text(text = "Current Assignment:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(
                                text = agent.currentTaskTitle ?: "Waiting in standby / Autonomous monitoring",
                                fontSize = 11.sp,
                                color = if (agent.currentTaskTitle != null) NeonCyan else TextMuted
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            // Performance Metrics
                            Text(text = "Verified Performance:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Completed Tasks: ${agent.completedTasksCount}", fontSize = 11.sp, color = EmeraldGreen)
                                Text(text = "Failed Tasks: ${agent.failedTasksCount}", fontSize = 11.sp, color = CrimsonAlert)
                                Text(text = "Resource Load: ${agent.resourceUsagePercent}%", fontSize = 11.sp, color = CyberGold)
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            // Skills & Tools
                            Text(text = "Authorized Skills:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = agent.skillsCsv, fontSize = 10.sp, color = TextSecondary)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "Authorized Tools:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = agent.toolsCsv, fontSize = 10.sp, color = TextSecondary)
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAgentDialog = false }) {
                        Text("Dismiss", color = NeonCyan)
                    }
                }
            )
        }
    }
}
