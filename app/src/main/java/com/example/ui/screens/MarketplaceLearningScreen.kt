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
import com.example.data.local.MarketplaceTemplateEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.AIISGViewModel

@Composable
fun MarketplaceLearningScreen(
    viewModel: AIISGViewModel,
    modifier: Modifier = Modifier
) {
    val templates by viewModel.marketplaceTemplates.collectAsState()
    val playbooks by viewModel.playbooks.collectAsState()
    val lessons by viewModel.lessonsLearned.collectAsState()

    var activeTab by remember { mutableStateOf("MARKETPLACE") }
    var selectedTemplateForDetails by remember { mutableStateOf<MarketplaceTemplateEntity?>(null) }

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
                    text = "AGENT MARKETPLACE & PLAYBOOKS",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonCyan,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Certified Specialist Templates • Continuous Learning Engine",
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
                    text = "${templates.size} Templates Available",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberGold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Switch Tabs: MARKETPLACE, PLAYBOOKS, LESSONS, FEEDBACK
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf(
                Triple("MARKETPLACE", "Market", Icons.Default.Store),
                Triple("PLAYBOOKS", "Playbooks", Icons.Default.MenuBook),
                Triple("LESSONS", "Lessons", Icons.Default.Lightbulb),
                Triple("FEEDBACK", "Feedback & Versions", Icons.Default.RateReview)
            ).forEach { (tabId, label, icon) ->
                val isSelected = activeTab == tabId
                Button(
                    onClick = { activeTab = tabId },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) NeonCyan.copy(alpha = 0.2f) else SurfaceCard,
                        contentColor = if (isSelected) NeonCyan else TextSecondary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
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

        when (activeTab) {
            "MARKETPLACE" -> {
                // Agent Templates List
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(templates) { item ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(0.8.dp, BorderSubtle, RoundedCornerShape(12.dp))
                                .clickable { selectedTemplateForDetails = item }
                                .testTag("template_card_${item.id}")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = item.title,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = item.version,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = NeonCyan
                                            )
                                        }
                                        Text(
                                            text = "${item.role} • Dept: ${item.department} • Floor ${item.targetFloor}",
                                            fontSize = 10.sp,
                                            color = TextSecondary
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(EmeraldGreen.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "CERTIFIED ${item.certificationScore}%",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = EmeraldGreen
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = item.description,
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    lineHeight = 15.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Required Tools: ${item.requiredToolsCsv}",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )

                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Author: ${item.author}",
                                        fontSize = 9.sp,
                                        color = TextMuted
                                    )

                                    if (item.isInstalled) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "INSTALLED IN FLEET",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = EmeraldGreen
                                            )
                                        }
                                    } else {
                                        Button(
                                            onClick = { viewModel.installMarketplaceTemplate(item) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = NeonCyan,
                                                contentColor = Color(0xFF003844)
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                            modifier = Modifier.height(32.dp).testTag("install_btn_${item.id}")
                                        ) {
                                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("INSTALL & CERTIFY", fontSize = 10.sp, fontWeight = FontWeight.Black)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            "PLAYBOOKS" -> {
                // Playbooks List
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(playbooks) { pb ->
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
                                    Text(
                                        text = pb.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(EmeraldGreen.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "${pb.successRatePercent}% SUCCESS",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldGreen
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Trigger: ${pb.triggerWorkflow}",
                                    fontSize = 10.sp,
                                    color = CyberGold,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Automated Protocol Steps:\n${pb.stepsCsv.split(", ").joinToString("\n") { "→ $it" }}",
                                    fontSize = 10.sp,
                                    color = TextSecondary,
                                    lineHeight = 14.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Derived From: ${pb.derivedFromTaskId}", fontSize = 9.sp, color = TextMuted)
                                    Text(text = "Status: ${pb.approvalStatus} (${pb.version})", fontSize = 9.sp, color = NeonCyan)
                                }
                            }
                        }
                    }
                }
            }

            "LESSONS" -> {
                // Lessons Learned List
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(lessons) { item ->
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
                                    Text(
                                        text = item.taskOrErrorTitle,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (item.severity == "CRITICAL") CrimsonAlert.copy(alpha = 0.2f) else CyberGold.copy(alpha = 0.2f)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = item.severity,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (item.severity == "CRITICAL") CrimsonAlert else CyberGold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Root Cause: ${item.rootCause}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Safe Fix Applied: ${item.safeFixApplied}",
                                    fontSize = 11.sp,
                                    color = EmeraldGreen
                                )

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Prevention Rule: ${item.preventionGuideline}",
                                    fontSize = 10.sp,
                                    color = CyberGold
                                )
                            }
                        }
                    }
                }
            }

            "FEEDBACK" -> {
                var targetWorkflow by remember { mutableStateOf("DevOps Zero-Downtime Deployment") }
                var feedbackNote by remember { mutableStateOf("") }
                var feedbackSubmitted by remember { mutableStateOf(false) }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Feedback, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "HUMAN FEEDBACK → WORKFLOW IMPROVEMENT",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Owner feedback directly recalibrates autonomous playbooks and triggers version increment proposals with complete approval history.",
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = targetWorkflow,
                                    onValueChange = { targetWorkflow = it },
                                    label = { Text("Target Workflow or Specialist Agent", fontSize = 10.sp) },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = feedbackNote,
                                    onValueChange = { feedbackNote = it },
                                    label = { Text("Improvement Directive / Correction", fontSize = 10.sp) },
                                    placeholder = { Text("e.g. Enforce memory check < 80% before rolling deploy", fontSize = 10.sp) },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 2
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Button(
                                    onClick = {
                                        if (feedbackNote.isNotBlank()) {
                                            viewModel.submitHumanFeedback(targetWorkflow, feedbackNote, approved = true)
                                            feedbackSubmitted = true
                                            feedbackNote = ""
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color(0xFF003844)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.align(Alignment.End).testTag("submit_feedback_btn")
                                ) {
                                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("SUBMIT & TRIGGER REVISION", fontSize = 10.sp, fontWeight = FontWeight.Black)
                                }

                                if (feedbackSubmitted) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "✓ Feedback applied! Workflow revision proposal generated with cryptographic audit record.",
                                        fontSize = 10.sp,
                                        color = EmeraldGreen,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "VERSION APPROVAL HISTORY & REVISIONS:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            letterSpacing = 0.5.sp
                        )
                    }

                    items(playbooks) { pb ->
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
                                    Column {
                                        Text(text = pb.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        Text(text = "Current: ${pb.version} • Derived From: ${pb.derivedFromTaskId}", fontSize = 10.sp, color = TextSecondary)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(EmeraldGreen.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(text = pb.approvalStatus, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Version History: v1.0 (Initial Seed) → v1.1 (Human Feedback: Added error fallback) → ${pb.version} (Active Approved)",
                                    fontSize = 9.sp,
                                    color = CyberGold,
                                    lineHeight = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
