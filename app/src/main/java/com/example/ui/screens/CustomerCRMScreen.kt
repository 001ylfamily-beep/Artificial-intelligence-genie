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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CustomerTicketEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.AIISGViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CustomerCRMScreen(
    viewModel: AIISGViewModel,
    modifier: Modifier = Modifier
) {
    val tickets by viewModel.customerTickets.collectAsState()
    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    var selectedTicketForApproval by remember { mutableStateOf<CustomerTicketEntity?>(null) }
    var selectedTicketForFollowUp by remember { mutableStateOf<CustomerTicketEntity?>(null) }
    var activeCrmTab by remember { mutableStateOf("TICKETS") } // TICKETS vs DIRECTORY

    val scheduledFollowUps = remember { mutableStateMapOf<String, String>() }

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
                    text = "CUSTOMER & CRM DEPARTMENT",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonCyan,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Sales Pipeline • Support Tickets • Follow-up Scheduling",
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
                    text = "${tickets.size} Active Records",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberGold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // CRM Tab Switcher: TICKETS vs DIRECTORY
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                Triple("TICKETS", "Pipeline & Tickets", Icons.Default.ConfirmationNumber),
                Triple("DIRECTORY", "Customer Accounts", Icons.Default.Business)
            ).forEach { (tabId, label, icon) ->
                val isSelected = activeCrmTab == tabId
                Button(
                    onClick = { activeCrmTab = tabId },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) NeonCyan.copy(alpha = 0.2f) else SurfaceCard,
                        contentColor = if (isSelected) NeonCyan else TextSecondary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .border(1.dp, if (isSelected) NeonCyan else BorderSubtle, RoundedCornerShape(8.dp))
                ) {
                    Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = label, fontSize = 9.5.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Pipeline Stages Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("LEAD", "PROPOSAL", "CONTRACT", "SUPPORT").forEach { stage ->
                val countInStage = tickets.count { it.stage == stage }
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).border(0.8.dp, BorderSubtle, RoundedCornerShape(8.dp))
                ) {
                    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = stage, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "$countInStage", fontSize = 12.sp, fontWeight = FontWeight.Black, color = NeonCyan)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (activeCrmTab == "TICKETS") {
            Text(
                text = "CUSTOMER TICKETS & OUTBOUND GATE:",
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
                items(tickets) { item ->
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
                                    Text(
                                        text = "${item.id}: ${item.title}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "${item.customerName} • ${item.company}",
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                }

                                if (item.dealValue > 0) {
                                    Text(
                                        text = currencyFormatter.format(item.dealValue),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = EmeraldGreen
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceDark)
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Draft Outbound Response:",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyberGold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.draftResponse,
                                        fontSize = 10.sp,
                                        color = TextPrimary,
                                        lineHeight = 14.sp
                                    )
                                }
                            }

                            // Follow-up status tag if scheduled
                            val followUp = scheduledFollowUps[item.id]
                            if (followUp != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Event, contentDescription = null, tint = CyberGold, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Follow-up Scheduled: $followUp",
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyberGold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Stage: ${item.stage} • Priority: ${item.priority}",
                                    fontSize = 9.sp,
                                    color = TextMuted
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    // Schedule Follow-Up Button
                                    OutlinedButton(
                                        onClick = { selectedTicketForFollowUp = item },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                        modifier = Modifier.height(30.dp)
                                    ) {
                                        Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("SCHEDULE", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                                    }

                                    // Sensitive Outbound Gate
                                    if (item.requiresApproval && !item.isApproved) {
                                        Button(
                                            onClick = { selectedTicketForApproval = item },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = CyberGold,
                                                contentColor = Color.Black
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                            modifier = Modifier.height(30.dp).testTag("approve_comm_${item.id}")
                                        ) {
                                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("APPROVE & SEND", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    } else {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("DISPATCHED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Customer Accounts Directory
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val accounts = listOf(
                    Triple("Apex Global Technologies", "Enterprise Tier • Contract $48,000/yr", "Health: 98% • 2 Active Tickets"),
                    Triple("Horizon Quantum Labs", "Growth Tier • Contract $24,500/yr", "Health: 100% • 1 Active Lead"),
                    Triple("Omnicorp Retail Systems", "Pilot Tier • Pipeline $12,000", "Health: 92% • Contract Stage")
                )

                items(accounts) { (name, tier, health) ->
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
                                Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(EmeraldGreen.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("ACTIVE ACCOUNT", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = tier, fontSize = 10.sp, color = CyberGold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = health, fontSize = 9.5.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }

        // Outbound Approval Modal
        if (selectedTicketForApproval != null) {
            val ticket = selectedTicketForApproval!!
            AlertDialog(
                onDismissRequest = { selectedTicketForApproval = null },
                containerColor = SurfaceDark,
                title = {
                    Text("CEO Outbound Communication Approval", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CyberGold)
                },
                text = {
                    Column {
                        Text(text = "Recipient: ${ticket.customerName} (${ticket.company})", fontSize = 11.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = ticket.draftResponse, fontSize = 11.sp, color = TextPrimary)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Approval ensures no sensitive internal architecture or unvetted commercial commitments are transmitted without CEO authorization.",
                            fontSize = 9.sp,
                            color = TextMuted
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.approveCustomerTicket(ticket.id)
                            selectedTicketForApproval = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberGold, contentColor = Color.Black)
                    ) {
                        Text("Approve & Dispatch")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { selectedTicketForApproval = null }) {
                        Text("Cancel", color = TextSecondary)
                    }
                }
            )
        }

        // Follow-Up Scheduling Modal
        if (selectedTicketForFollowUp != null) {
            val ticket = selectedTicketForFollowUp!!
            var selectedDateOption by remember { mutableStateOf("Tomorrow at 10:00 AM") }

            AlertDialog(
                onDismissRequest = { selectedTicketForFollowUp = null },
                containerColor = SurfaceDark,
                title = {
                    Text("Schedule Follow-Up: ${ticket.company}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                },
                text = {
                    Column {
                        Text(text = "Select scheduled follow-up interval for ${ticket.customerName}:", fontSize = 11.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(10.dp))
                        listOf("Tomorrow at 10:00 AM", "In 3 Business Days", "Next Monday Morning", "End of Quarter Review").forEach { opt ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedDateOption = opt }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedDateOption == opt,
                                    onClick = { selectedDateOption = opt },
                                    colors = RadioButtonDefaults.colors(selectedColor = NeonCyan)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = opt, fontSize = 11.sp, color = if (selectedDateOption == opt) NeonCyan else TextPrimary)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.scheduleFollowUp(ticket.id, selectedDateOption)
                            scheduledFollowUps[ticket.id] = selectedDateOption
                            selectedTicketForFollowUp = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color(0xFF003844))
                    ) {
                        Text("Confirm Schedule")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { selectedTicketForFollowUp = null }) {
                        Text("Cancel", color = TextSecondary)
                    }
                }
            )
        }
    }
}
