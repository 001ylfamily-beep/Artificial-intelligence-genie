package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SaaSFactoryProjectEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.AIISGViewModel

@Composable
fun SaaSFactoryScreen(
    viewModel: AIISGViewModel,
    modifier: Modifier = Modifier
) {
    val saasProjects by viewModel.saasProjects.collectAsState()
    val pipelineStages = listOf(
        "IDEA", "RESEARCH", "SPEC", "DESIGN", "DEVELOPMENT", "QA", "SECURITY", "DEPLOYED", "DOCUMENTED"
    )

    var showCreateModal by remember { mutableStateOf(false) }

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
                    text = "AUTONOMOUS SAAS PROJECT FACTORY",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonCyan,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Idea → Spec → Code → QA → Security → Production Release",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }

            Button(
                onClick = { showCreateModal = true },
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color(0xFF003844)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp).testTag("start_new_saas_button")
            ) {
                Icon(Icons.Default.RocketLaunch, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("LAUNCH SAAS", fontSize = 9.sp, fontWeight = FontWeight.Black)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Visual Pipeline Tracker
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "FULL AUTOMATED FACTORY PIPELINE (9 STAGES):",
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
                    items(pipelineStages) { stage ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SurfaceCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                        ) {
                            Text(
                                text = stage,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Projects List
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(saasProjects) { proj ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = proj.productName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text(text = "Pipeline Stage: ${proj.currentStage}", fontSize = 10.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(EmeraldGreen.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(text = "${proj.progressPercent}% DONE", fontSize = 9.sp, fontWeight = FontWeight.Black, color = EmeraldGreen)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { proj.progressPercent / 100f },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = NeonCyan,
                            trackColor = SurfaceDark
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Idea Brief: ${proj.ideaBrief}", fontSize = 10.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Market Research: ${proj.researchSummary}", fontSize = 10.sp, color = TextMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Architecture Spec: ${proj.specDocument}", fontSize = 10.sp, color = TextMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "QA Status: ${proj.qaStatus}", fontSize = 10.sp, color = EmeraldGreen)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Security Audit: ${proj.secStatus}", fontSize = 10.sp, color = CyberGold)

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = proj.repoUrl,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                color = TextMuted
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedButton(
                                    onClick = { viewModel.advanceSaaSStage(proj) },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier.height(30.dp).testTag("advance_saas_${proj.id}")
                                ) {
                                    Icon(Icons.Default.FastForward, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("STEP", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                                }

                                Button(
                                    onClick = { viewModel.autoRunFullSaaSPipeline(proj) },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color(0xFF003844)),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(30.dp).testTag("run_all_pipeline_${proj.id}")
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("RUN FULL PIPELINE", fontSize = 8.5.sp, fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Modal for Launching SaaS
        if (showCreateModal) {
            var prodName by remember { mutableStateOf("GenieAPM Cloud Metrics") }
            AlertDialog(
                onDismissRequest = { showCreateModal = false },
                containerColor = SurfaceDark,
                title = {
                    Text("Trigger Autonomous SaaS Factory", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                },
                text = {
                    Column {
                        Text(
                            text = "Directive: 'Ek SaaS product banao'. The Central Commander will orchestrate the full Idea → Specification → Development → Security → Production release pipeline.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick directive preset chip
                        OutlinedButton(
                            onClick = { prodName = "AIISG Cloud Sentinel SaaS" },
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("Preset: 'Ek SaaS product banao'", fontSize = 9.sp, color = CyberGold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = prodName,
                            onValueChange = { prodName = it },
                            label = { Text("Product Concept Name (e.g. GenieAPM)", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth().testTag("saas_name_input")
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (prodName.isNotBlank()) {
                                viewModel.triggerSaaSFactory(prodName)
                                showCreateModal = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color(0xFF003844))
                    ) {
                        Text("Initiate Factory Loop")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateModal = false }) {
                        Text("Cancel", color = TextSecondary)
                    }
                }
            )
        }
    }
}
