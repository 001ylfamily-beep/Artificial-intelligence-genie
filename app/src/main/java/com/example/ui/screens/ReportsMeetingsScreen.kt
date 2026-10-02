package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MeetingRecordEntity
import com.example.data.local.ReportEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.AIISGViewModel

@Composable
fun ReportsMeetingsScreen(
    viewModel: AIISGViewModel,
    modifier: Modifier = Modifier
) {
    val reports by viewModel.reports.collectAsState()
    val meetings by viewModel.meetings.collectAsState()

    var selectedTab by remember { mutableStateOf("REPORTS") }
    var selectedMeetingForInspection by remember { mutableStateOf<MeetingRecordEntity?>(null) }
    var selectedReportForInspection by remember { mutableStateOf<ReportEntity?>(null) }

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
                    text = "REPORTS & ALL-AGENT MEETINGS",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonCyan,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Scheduled 09:00 / 21:00 Briefings • All-Hands Transcripts",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { viewModel.triggerReportGeneration("MORNING") },
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceCard, contentColor = NeonCyan),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp).testTag("gen_report_btn")
                ) {
                    Text("+ 09:00 Report", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { viewModel.triggerMeetingSession() },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen, contentColor = Color.Black),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp).testTag("convene_meeting_btn")
                ) {
                    Text("CONVENE MEETING", fontSize = 9.sp, fontWeight = FontWeight.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Switch Tabs: REPORTS vs MEETINGS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("REPORTS", "MEETINGS").forEach { tab ->
                val isSelected = selectedTab == tab
                Button(
                    onClick = { selectedTab = tab },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) NeonCyan.copy(alpha = 0.2f) else SurfaceCard,
                        contentColor = if (isSelected) NeonCyan else TextSecondary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, if (isSelected) NeonCyan else BorderSubtle, RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = if (tab == "REPORTS") "DAILY REPORTS (09:00 & 21:00)" else "WEEKLY ALL-HANDS SESSIONS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedTab == "REPORTS") {
            // Reports List
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(reports) { report ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(0.8.dp, BorderSubtle, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = report.title,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(EmeraldGreen.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "EMAIL SENT",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldGreen
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "Delivered to: ${report.recipientEmail}", fontSize = 10.sp, color = CyberGold)

                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SurfaceDark)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = report.verifiedContent.lines().take(4).joinToString("\n"),
                                    fontSize = 10.sp,
                                    color = TextSecondary,
                                    lineHeight = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            val context = LocalContext.current
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        val sendIntent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TITLE, report.title)
                                            putExtra(Intent.EXTRA_SUBJECT, report.title)
                                            putExtra(Intent.EXTRA_TEXT, report.verifiedContent)
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(sendIntent, "Export & Share Report"))
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color(0xFF003844)),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(30.dp).testTag("export_share_report_${report.id}")
                                ) {
                                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("EXPORT / SHARE", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Meetings List
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(meetings) { meet ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(0.8.dp, BorderSubtle, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = meet.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(NeonCyan.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = meet.status,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonCyan
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "Summary: ${meet.summary}", fontSize = 11.sp, color = TextSecondary)

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Key Decisions: ${meet.decisionsCsv}", fontSize = 10.sp, color = EmeraldGreen)
                            Text(text = "Action Items: ${meet.actionItemsCsv}", fontSize = 10.sp, color = CyberGold)

                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SurfaceDark)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "Transcript snippet:\n" + meet.transcript.lines().take(3).joinToString("\n"),
                                    fontSize = 10.sp,
                                    color = TextMuted,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
