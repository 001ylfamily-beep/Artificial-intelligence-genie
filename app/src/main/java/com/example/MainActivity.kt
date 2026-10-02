package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.EmergencyStopHeader
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AIISGViewModel

data class NavItem(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val testTag: String
)

class MainActivity : ComponentActivity() {

    private val viewModel: AIISGViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val currentScreen by viewModel.selectedScreen.collectAsState()
                val isEmergencyStopped by viewModel.isEmergencyStopped.collectAsState()
                val currentFloor by viewModel.currentFloor.collectAsState()
                val simulationSpeed by viewModel.simulationSpeedMultiplier.collectAsState()
                val isIsometric3D by viewModel.isIsometric3DView.collectAsState()
                val unreadNotifications by viewModel.unreadNotificationsCount.collectAsState()
                val notificationsList by viewModel.allNotifications.collectAsState()

                // BackHandler returns to Office if in another screen
                BackHandler(enabled = currentScreen != "OFFICE") {
                    viewModel.selectScreen("OFFICE")
                }

                val primaryNavItems = listOf(
                    NavItem("OFFICE", "Office", Icons.Default.CorporateFare, "nav_office"),
                    NavItem("COMMANDER", "Commander", Icons.Default.Psychology, "nav_commander"),
                    NavItem("TASKS", "Tasks", Icons.Default.Assignment, "nav_tasks"),
                    NavItem("AGENTS", "Agents", Icons.Default.Groups, "nav_agents"),
                    NavItem("WORKSPACES", "Code", Icons.Default.Terminal, "nav_workspaces")
                )

                val secondaryNavItems = listOf(
                    NavItem("LIVE_INTERCOM", "Live Intercom", Icons.Default.Forum, "nav_intercom"),
                    NavItem("SAAS_FACTORY", "SaaS Factory", Icons.Default.RocketLaunch, "nav_saas_factory"),
                    NavItem("MARKETPLACE", "Marketplace", Icons.Default.Store, "nav_marketplace"),
                    NavItem("DIGITAL_TWIN", "Digital Twin", Icons.Default.Hub, "nav_digital_twin"),
                    NavItem("CRM", "Customer / CRM", Icons.Default.SupportAgent, "nav_crm"),
                    NavItem("GOVERNANCE", "Forecasting", Icons.Default.TrendingUp, "nav_governance"),
                    NavItem("ACCOUNTING", "Finance Suite", Icons.Default.AccountBalance, "nav_accounting"),
                    NavItem("SECURITY", "Security Center", Icons.Default.Security, "nav_security"),
                    NavItem("LAB", "Creation Lab", Icons.Default.Science, "nav_lab"),
                    NavItem("REPORTS", "Daily Reports", Icons.Default.Analytics, "nav_reports")
                )

                var showMoreNavMenu by remember { mutableStateOf(false) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        EmergencyStopHeader(
                            isEmergencyStopped = isEmergencyStopped,
                            currentFloor = currentFloor,
                            simulationSpeed = simulationSpeed,
                            isIsometric3D = isIsometric3D,
                            unreadNotifications = unreadNotifications,
                            notificationsList = notificationsList,
                            onFloorChange = { viewModel.selectFloor(it) },
                            onSpeedChange = { viewModel.setSimulationSpeed(it) },
                            onToggle3D = { viewModel.toggleIsometricMode() },
                            onNotificationClick = { notif ->
                                viewModel.markNotificationRead(notif.id)
                                viewModel.selectScreen(notif.targetScreen)
                            },
                            onEmergencyStop = { viewModel.triggerEmergencyStop() },
                            onResume = { viewModel.resumeOperations() }
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = SurfaceDark,
                            tonalElevation = 6.dp,
                            modifier = Modifier.testTag("main_navigation_bar")
                        ) {
                            primaryNavItems.forEach { item ->
                                val isSelected = currentScreen == item.id
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = { viewModel.selectScreen(item.id) },
                                    icon = {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.label,
                                            tint = if (isSelected) NeonCyan else TextSecondary
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = item.label,
                                            fontSize = 9.sp,
                                            color = if (isSelected) NeonCyan else TextSecondary
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = NeonCyan.copy(alpha = 0.15f)
                                    ),
                                    modifier = Modifier.testTag(item.testTag)
                                )
                            }

                            // More button for secondary departments & enterprise systems
                            val isMoreSelected = secondaryNavItems.any { it.id == currentScreen }
                            NavigationBarItem(
                                selected = isMoreSelected,
                                onClick = { showMoreNavMenu = true },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Apps,
                                        contentDescription = "More Departments",
                                        tint = if (isMoreSelected) NeonCyan else TextSecondary
                                    )
                                },
                                label = {
                                    Text(
                                        text = "Fleet & Hubs",
                                        fontSize = 8.5.sp,
                                        color = if (isMoreSelected) NeonCyan else TextSecondary
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = NeonCyan.copy(alpha = 0.15f)
                                ),
                                modifier = Modifier.testTag("nav_more")
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(ObsidianBg)
                    ) {
                        when (currentScreen) {
                            "OFFICE" -> VirtualOfficeScreen(viewModel = viewModel)
                            "COMMANDER" -> CommanderScreen(viewModel = viewModel)
                            "TASKS" -> TaskQueueScreen(viewModel = viewModel)
                            "AGENTS" -> AgentDirectoryScreen(viewModel = viewModel)
                            "WORKSPACES" -> DevelopmentWorkspaceScreen(viewModel = viewModel)
                            "LIVE_INTERCOM" -> LiveIntercomScreen(viewModel = viewModel)
                            "SAAS_FACTORY" -> SaaSFactoryScreen(viewModel = viewModel)
                            "MARKETPLACE" -> MarketplaceLearningScreen(viewModel = viewModel)
                            "DIGITAL_TWIN" -> KnowledgeDigitalTwinScreen(viewModel = viewModel)
                            "CRM" -> CustomerCRMScreen(viewModel = viewModel)
                            "GOVERNANCE" -> ForecastingGovernanceScreen(viewModel = viewModel)
                            "ACCOUNTING" -> AccountingScreen(viewModel = viewModel)
                            "SECURITY" -> SecurityCenterScreen(viewModel = viewModel)
                            "LAB" -> AgentCreationLabScreen(viewModel = viewModel)
                            "REPORTS" -> ReportsMeetingsScreen(viewModel = viewModel)
                            else -> VirtualOfficeScreen(viewModel = viewModel)
                        }

                        // More Navigation Modal Menu
                        if (showMoreNavMenu) {
                            AlertDialog(
                                onDismissRequest = { showMoreNavMenu = false },
                                containerColor = SurfaceDark,
                                title = {
                                    Text(
                                        text = "AIISG Enterprise Departments",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonCyan
                                    )
                                },
                                text = {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxWidth().heightIn(max = 440.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(secondaryNavItems) { nav ->
                                            OutlinedButton(
                                                onClick = {
                                                    viewModel.selectScreen(nav.id)
                                                    showMoreNavMenu = false
                                                },
                                                modifier = Modifier.fillMaxWidth().testTag(nav.testTag),
                                                colors = ButtonDefaults.outlinedButtonColors(
                                                    contentColor = if (currentScreen == nav.id) NeonCyan else TextPrimary
                                                ),
                                                shape = RoundedCornerShape(10.dp)
                                            ) {
                                                Icon(imageVector = nav.icon, contentDescription = null, modifier = Modifier.size(18.dp))
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Text(text = nav.label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                            }
                                        }
                                    }
                                },
                                confirmButton = {
                                    TextButton(onClick = { showMoreNavMenu = false }) {
                                        Text("Dismiss", color = NeonCyan)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
