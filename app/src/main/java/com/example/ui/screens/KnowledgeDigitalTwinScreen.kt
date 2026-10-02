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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.KnowledgeNodeEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.AIISGViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun KnowledgeDigitalTwinScreen(
    viewModel: AIISGViewModel,
    modifier: Modifier = Modifier
) {
    val knowledgeNodes by viewModel.knowledgeNodes.collectAsState()
    val agents by viewModel.allAgents.collectAsState()
    val projects by viewModel.allProjects.collectAsState()
    val modelRouteLogs by viewModel.modelRouteLogs.collectAsState()

    var activeTab by remember { mutableStateOf("DIGITAL_TWIN") }
    var selectedNodeType by remember { mutableStateOf("ALL") }
    var inspectedNode by remember { mutableStateOf<KnowledgeNodeEntity?>(null) }

    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    val totalAgentCost = agents.sumOf { it.totalAiCostUsd }
    val totalProjectCost = projects.sumOf { it.estimatedAiCostUsd }
    val totalCombinedCost = totalAgentCost + totalProjectCost
    val monthlyBudgetLimit = 150.0

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
                    text = "DIGITAL TWIN & KNOWLEDGE GRAPH",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = NeonCyan,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Live State Aggregator • Multi-Model AI Cost Router",
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
                    text = "Digital Twin 100% Synced",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Switch Tabs: DIGITAL_TWIN, KNOWLEDGE_GRAPH, MODEL_ROUTER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(
                Triple("DIGITAL_TWIN", "Digital Twin", Icons.Default.Hub),
                Triple("KNOWLEDGE_GRAPH", "Knowledge Graph", Icons.Default.AccountTree),
                Triple("MODEL_ROUTER", "Model Router & Costs", Icons.Default.Savings)
            ).forEach { (tabId, label, icon) ->
                val isSelected = activeTab == tabId
                Button(
                    onClick = { activeTab = tabId },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) NeonCyan.copy(alpha = 0.2f) else SurfaceCard,
                        contentColor = if (isSelected) NeonCyan else TextSecondary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .border(1.dp, if (isSelected) NeonCyan else BorderSubtle, RoundedCornerShape(8.dp))
                ) {
                    Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = label, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (activeTab) {
            "DIGITAL_TWIN" -> {
                // Live Digital Twin Overview
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "AIISG SOVEREIGN DIGITAL TWIN STATE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberGold,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(text = "Fleet Capacity", fontSize = 10.sp, color = TextSecondary)
                                        Text(text = "${agents.size} Verified Agents", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    }
                                    Column {
                                        Text(text = "Active Codebases", fontSize = 10.sp, color = TextSecondary)
                                        Text(text = "${projects.size} Repositories", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    }
                                    Column {
                                        Text(text = "Company Floors", fontSize = 10.sp, color = TextSecondary)
                                        Text(text = "3 Floors Online", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Resource consumption bar
                                Text(text = "System Compute & Infrastructure Load:", fontSize = 10.sp, color = TextSecondary)
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { 0.32f },
                                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                    color = NeonCyan,
                                    trackColor = SurfaceDark
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "RAM: 1.8GB / 8GB • CPU: 28%", fontSize = 9.sp, color = TextMuted)
                                    Text(text = "Disk: 18% (Encrypted NVMe)", fontSize = 9.sp, color = TextMuted)
                                }
                            }
                        }
                    }

                    item {
                        // AI Cost Budget Bar
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
                                    Text(text = "AI COST CONTROLLER & BUDGET", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text(
                                        text = "${currencyFormatter.format(totalCombinedCost)} / ${currencyFormatter.format(monthlyBudgetLimit)}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldGreen
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                val progressRatio = (totalCombinedCost / monthlyBudgetLimit).toFloat().coerceIn(0f, 1f)
                                LinearProgressIndicator(
                                    progress = { progressRatio },
                                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                    color = if (progressRatio > 0.85f) CrimsonAlert else EmeraldGreen,
                                    trackColor = SurfaceDark
                                )

                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Budget Utilization: ${(progressRatio * 100).toInt()}%", fontSize = 9.sp, color = TextSecondary)
                                    Text(text = "Auto-Throttling: ARMED (> 95%)", fontSize = 9.sp, color = CyberGold)
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "TOP AGENT AI CONSUMPTION METRICS:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            letterSpacing = 0.5.sp
                        )
                    }

                    items(agents.sortedByDescending { it.totalAiCostUsd }.take(5)) { ag ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().border(0.6.dp, BorderSubtle, RoundedCornerShape(8.dp))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = ag.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Text(text = "${ag.title} • ${ag.department}", fontSize = 9.sp, color = TextSecondary)
                                }
                                Text(
                                    text = currencyFormatter.format(ag.totalAiCostUsd),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonCyan
                                )
                            }
                        }
                    }
                }
            }

            "KNOWLEDGE_GRAPH" -> {
                // Knowledge Graph Nodes & Context Retrieval Search
                var searchQuery by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Query Knowledge Graph for Commander Context...", fontSize = 10.sp) },
                    placeholder = { Text("e.g. LedgerRadar, Aegis, Security, SaaS", fontSize = 10.sp) },
                    modifier = Modifier.fillMaxWidth().testTag("knowledge_search_field"),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp)) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("ALL", "PROJECT", "AGENT", "CUSTOMER", "SYSTEM", "DECISION").forEach { type ->
                        val isSelected = selectedNodeType == type
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else SurfaceCard)
                                .border(1.dp, if (isSelected) NeonCyan else BorderSubtle, RoundedCornerShape(6.dp))
                                .clickable { selectedNodeType = type }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = type,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) NeonCyan else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                val filteredNodes = knowledgeNodes.filter { node ->
                    (selectedNodeType == "ALL" || node.nodeType == selectedNodeType) &&
                    (searchQuery.isBlank() || node.title.contains(searchQuery, ignoreCase = true) || node.description.contains(searchQuery, ignoreCase = true) || node.tagsCsv.contains(searchQuery, ignoreCase = true))
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredNodes) { node ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(0.8.dp, BorderSubtle, RoundedCornerShape(12.dp))
                                .clickable { inspectedNode = node }
                                .testTag("knowledge_node_${node.id}")
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = node.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(NeonCyan.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(text = node.nodeType, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = node.description, fontSize = 10.sp, color = TextSecondary)

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Context Graph Connections: ${node.connectedNodeIdsCsv}",
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = CyberGold
                                )
                            }
                        }
                    }
                }
            }

            "MODEL_ROUTER" -> {
                // Multi-Model AI Router & Logs
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().border(0.8.dp, BorderSubtle, RoundedCornerShape(12.dp))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(text = "MULTI-MODEL INTELLIGENT DISPATCH MATRIX", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberGold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "• Routine parsing & status checks → Gemini Flash ($0.0001/1k)", fontSize = 10.sp, color = TextPrimary)
                                Text(text = "• Complex code & architectural reasoning → Gemini Pro ($0.0025/1k)", fontSize = 10.sp, color = TextPrimary)
                                Text(text = "• Cryptographic proofs & key handling → Air-Gapped Sandbox ($0.00)", fontSize = 10.sp, color = EmeraldGreen)

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "Simulate Task Dispatch to Router:", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.simulateModelRoute("Parse System Telemetry", "gemini-flash", "Routine JSON log parsing", 45L, 0.00012)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                        modifier = Modifier.weight(1f).height(30.dp)
                                    ) {
                                        Text("Flash", fontSize = 8.5.sp, color = NeonCyan)
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.simulateModelRoute("Microservice Refactoring", "gemini-pro", "Complex AST & concurrency reasoning", 320L, 0.00280)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                        modifier = Modifier.weight(1f).height(30.dp)
                                    ) {
                                        Text("Pro", fontSize = 8.5.sp, color = CyberGold)
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.simulateModelRoute("Audit Secret Keystore", "air-gapped-crypto", "Sensitive zero-external crypto vault", 12L, 0.00000)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                        modifier = Modifier.weight(1f).height(30.dp)
                                    ) {
                                        Text("Air-Gapped", fontSize = 8.5.sp, color = EmeraldGreen)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "RECENT MODEL DISPATCH & LATENCY LOGS:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            letterSpacing = 0.5.sp
                        )
                    }

                    items(modelRouteLogs) { log ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().border(0.6.dp, BorderSubtle, RoundedCornerShape(8.dp))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = log.taskTitle, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(
                                                when (log.selectedModel) {
                                                    "gemini-pro" -> CyberGold.copy(alpha = 0.2f)
                                                    "air-gapped-crypto" -> EmeraldGreen.copy(alpha = 0.2f)
                                                    else -> NeonCyan.copy(alpha = 0.2f)
                                                }
                                            )
                                            .padding(horizontal = 5.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = log.selectedModel,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when (log.selectedModel) {
                                                "gemini-pro" -> CyberGold
                                                "air-gapped-crypto" -> EmeraldGreen
                                                else -> NeonCyan
                                            }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = log.reason, fontSize = 9.sp, color = TextSecondary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Latency: ${log.latencyMs}ms", fontSize = 9.sp, color = TextMuted)
                                    Text(text = "Cost: \$${String.format(Locale.US, "%.5f", log.costUsd)}", fontSize = 9.sp, color = TextMuted)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Node Inspection Modal
        if (inspectedNode != null) {
            val node = inspectedNode!!
            AlertDialog(
                onDismissRequest = { inspectedNode = null },
                containerColor = SurfaceDark,
                title = {
                    Text(text = node.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                },
                text = {
                    Column {
                        Text(text = "Type: ${node.nodeType}", fontSize = 11.sp, color = CyberGold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = node.description, fontSize = 11.sp, color = TextPrimary)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Connected Graph Edges: ${node.connectedNodeIdsCsv}", fontSize = 10.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Indexed Tags: ${node.tagsCsv}", fontSize = 9.sp, color = TextMuted)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { inspectedNode = null }) {
                        Text("Close", color = NeonCyan)
                    }
                }
            )
        }
    }
}
