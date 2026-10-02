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
import com.example.data.local.AccountingRecordEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.AIISGViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AccountingScreen(
    viewModel: AIISGViewModel,
    modifier: Modifier = Modifier
) {
    val records by viewModel.accountingRecords.collectAsState()
    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    val totalReceivables = records.filter { it.type == "INVOICE" }.sumOf { it.amount }
    val totalPayables = records.filter { it.type == "BILL" }.sumOf { it.amount }
    val pendingAuthCount = records.count { it.status == "PENDING_APPROVAL" }

    var recordToApprove by remember { mutableStateOf<AccountingRecordEntity?>(null) }

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
                    text = "ACCOUNTING & FINANCE SUITE",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonCyan,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Floor 2 • Human Authorization Gateway Mandate",
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
                    text = "Zero Discrepancies",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberGold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Balance Summary Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).border(0.8.dp, BorderSubtle, RoundedCornerShape(10.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Accounts Receivable", fontSize = 10.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = currencyFormatter.format(totalReceivables),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = EmeraldGreen
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).border(0.8.dp, BorderSubtle, RoundedCornerShape(10.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Accounts Payable", fontSize = 10.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = currencyFormatter.format(totalPayables),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = CyberGold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Pending CEO Action Notice
        if (pendingAuthCount > 0) {
            Card(
                colors = CardDefaults.cardColors(containerColor = CyberGold.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberGold, RoundedCornerShape(10.dp))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = CyberGold, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "$pendingAuthCount outward bill payment requires explicit CEO approval under zero-trust policy.",
                        fontSize = 11.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Ledger Records List
        Text(
            text = "VERIFIED FINANCIAL LEDGER & INVOICES:",
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
            items(records) { item ->
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
                                    text = "Entity: ${item.counterparty} • ${item.category}",
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }

                            Text(
                                text = currencyFormatter.format(item.amount),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = if (item.type == "INVOICE") EmeraldGreen else CyberGold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        when (item.status) {
                                            "APPROVED", "RECONCILED" -> EmeraldGreen.copy(alpha = 0.2f)
                                            "PENDING_APPROVAL" -> CrimsonAlert.copy(alpha = 0.2f)
                                            else -> NeonCyan.copy(alpha = 0.2f)
                                        }
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = item.status,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (item.status) {
                                        "APPROVED", "RECONCILED" -> EmeraldGreen
                                        "PENDING_APPROVAL" -> CrimsonAlert
                                        else -> NeonCyan
                                    }
                                )
                            }

                            if (item.status == "PENDING_APPROVAL") {
                                Button(
                                    onClick = { recordToApprove = item },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CyberGold,
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(30.dp).testTag("approve_btn_${item.id}")
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("AUTHORIZE PAYMENT", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Text(
                                    text = item.notes.ifBlank { "Reconciled against verified bank feed." },
                                    fontSize = 9.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }
            }
        }

        // Approval Modal with Zero-Trust PIN/Biometric Authentication
        if (recordToApprove != null) {
            val rec = recordToApprove!!
            com.example.ui.components.PinAuthDialog(
                actionTitle = "Disbursement Approval: ${rec.id}",
                actionDetail = "Authorize outward payment of ${currencyFormatter.format(rec.amount)} ${rec.currency} to ${rec.counterparty}.",
                onPinVerified = {
                    viewModel.approveAccounting(rec.id)
                    recordToApprove = null
                },
                onDismiss = { recordToApprove = null }
            )
        }
    }
}
