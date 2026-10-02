package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.ui.theme.*
import com.example.ui.viewmodel.AIISGViewModel

@Composable
fun DevelopmentWorkspaceScreen(
    viewModel: AIISGViewModel,
    modifier: Modifier = Modifier
) {
    val projects by viewModel.allProjects.collectAsState()
    val workspaceState by viewModel.workspaceState.collectAsState()

    var selectedProjectId by remember { mutableStateOf("PROJ-AIISG-CORE") }
    val currentProject = projects.find { it.id == selectedProjectId } ?: projects.firstOrNull()

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
                    text = "PYTHON & WEB DEVELOPMENT WORKSPACE",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonCyan,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Automated Sandboxed Builds • Version Controlled",
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
                    text = "${projects.size} Projects Active",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Project selector chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(projects) { proj ->
                val isSelected = proj.id == selectedProjectId
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) NeonCyan.copy(alpha = 0.2f) else SurfaceCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NeonCyan else BorderSubtle),
                    modifier = Modifier.clickable { selectedProjectId = proj.id }
                ) {
                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        Text(
                            text = proj.name,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) NeonCyan else TextPrimary
                        )
                        Text(
                            text = "Branch: ${proj.repoBranch} • Commit: ${proj.gitCommitHash}",
                            fontSize = 9.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (currentProject != null) {
            // Project Metadata Strip
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().border(0.8.dp, BorderSubtle, RoundedCornerShape(10.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${currentProject.code}: ${currentProject.name}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = currentProject.description,
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Coverage: ${currentProject.testCoveragePercent}%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen
                        )
                        Text(
                            text = "Progress: ${currentProject.progressPercent}%",
                            fontSize = 10.sp,
                            color = NeonCyan
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Code Preview & Terminal Box
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Editor Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Code, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "sandbox/${currentProject.activeFile}",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { viewModel.runWorkspaceCode() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (workspaceState.isRunning) CyberGold else EmeraldGreen,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp).testTag("run_build_test_button")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (workspaceState.isRunning) "COMPILING..." else "TEST & RUN",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Code Editor Block
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ObsidianBg)
                            .border(0.8.dp, BorderSubtle, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = workspaceState.codeSnippet,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = TextPrimary,
                            lineHeight = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Terminal Console Output
                    Text(
                        text = "TERMINAL EXECUTION LOGS:",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF030712))
                            .border(0.8.dp, BorderSubtle, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = workspaceState.terminalOutput,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            color = NeonCyan,
                            lineHeight = 13.sp
                        )
                    }
                }
            }
        }
    }
}
