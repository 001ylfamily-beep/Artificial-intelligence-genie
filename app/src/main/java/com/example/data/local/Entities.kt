package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agents")
data class AgentEntity(
    @PrimaryKey val id: String,
    val name: String,
    val title: String,
    val department: String, // EXECUTIVE, SOFTWARE, RESEARCH, CREATIVE, SECURITY, FINANCE, LAB, CUSTOMER
    val floor: Int, // 1, 2, 3...
    val currentRoom: String,
    val status: String, // ONLINE, WORKING, THINKING, WAITING, COLLABORATING, IN_MEETING, TESTING, BLOCKED, ERROR, PAUSED, OFFLINE, COMPLETED
    val skillsCsv: String,
    val toolsCsv: String,
    val currentTaskId: String? = null,
    val currentTaskTitle: String? = null,
    val priority: String = "NORMAL",
    val completedTasksCount: Int = 0,
    val failedTasksCount: Int = 0,
    val idleMinutes: Int = 10,
    val activeMinutes: Int = 45,
    val resourceUsagePercent: Int = 24,
    val avatarColorHex: String = "#00D4FF",
    val posX: Float = 0.5f,
    val posY: Float = 0.5f,
    val targetX: Float = 0.5f,
    val targetY: Float = 0.5f,
    val lastActiveEpochMs: Long = System.currentTimeMillis(),
    val version: String = "v1.0",
    val certificationScore: Int = 98, // Verified certification score
    val totalAiCostUsd: Double = 4.25
)

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val creatorAgentId: String,
    val assignedAgentId: String,
    val department: String,
    val priority: String, // CRITICAL, HIGH, NORMAL, LOW
    val status: String, // PLANNED, RUNNING, BLOCKED, FAILED, VERIFIED, COMPLETED, PAUSED
    val loopStage: String, // RECEIVE, UNDERSTAND, PLAN, BREAK_DOWN, ASSIGN, EXECUTE, OBSERVE, VERIFY, TEST, FIX, RETEST, DOCUMENT, REPORT, COMPLETE
    val retryCount: Int = 0,
    val maxRetries: Int = 3,
    val createdAtEpochMs: Long = System.currentTimeMillis(),
    val updatedAtEpochMs: Long = System.currentTimeMillis(),
    val deadlineText: String = "Today, 18:00",
    val evidenceLog: String = "",
    val resultSummary: String = "",
    val requiresOwnerAuth: Boolean = false,
    val isAuthorized: Boolean = true,
    // Explainable Command History Lineage
    val triggerSource: String = "Owner Command Direct",
    val commanderReasoning: String = "High priority operational milestone.",
    val modelUsed: String = "gemini-flash"
)

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val code: String,
    val department: String,
    val status: String, // PLANNING, IN_PROGRESS, TESTING, VERIFYING, COMPLETED, PAUSED
    val progressPercent: Int,
    val leadAgentId: String,
    val description: String,
    val gitCommitHash: String,
    val repoBranch: String = "main",
    val testCoveragePercent: Int = 92,
    val activeFile: String = "main.py",
    val lastUpdatedEpochMs: Long = System.currentTimeMillis(),
    val estimatedAiCostUsd: Double = 18.50
)

@Entity(tableName = "accounting_records")
data class AccountingRecordEntity(
    @PrimaryKey val id: String,
    val type: String, // INVOICE, BILL, INCOME, EXPENSE
    val title: String,
    val counterparty: String,
    val amount: Double,
    val currency: String = "USD",
    val status: String, // DRAFT, PENDING_APPROVAL, APPROVED, RECONCILED, FLAGGED_DISCREPANCY
    val department: String,
    val category: String,
    val createdEpochMs: Long = System.currentTimeMillis(),
    val requiresHumanAuth: Boolean = false,
    val notes: String = ""
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestampEpochMs: Long = System.currentTimeMillis(),
    val agentId: String,
    val agentName: String,
    val action: String,
    val tool: String,
    val target: String,
    val result: String,
    val isSuccess: Boolean = true,
    val errorMessage: String = "",
    val severity: String = "INFO", // INFO, WARNING, CRITICAL, SECURITY
    val verificationHash: String = ""
)

@Entity(tableName = "meeting_records")
data class MeetingRecordEntity(
    @PrimaryKey val id: String,
    val title: String,
    val type: String, // WEEKLY_ALL_HANDS, EMERGENCY_SESSION, DEPARTMENTAL
    val timestampEpochMs: Long = System.currentTimeMillis(),
    val status: String, // SCHEDULED, IN_PROGRESS, CONCLUDED
    val agenda: String,
    val transcript: String,
    val summary: String,
    val decisionsCsv: String,
    val actionItemsCsv: String
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey val id: String,
    val type: String, // DAILY_MORNING_0900, DAILY_EVENING_2100, WEEKLY_MEETING_SUMMARY, SECURITY_AUDIT
    val title: String,
    val timestampEpochMs: Long = System.currentTimeMillis(),
    val verifiedContent: String,
    val recipientEmail: String,
    val sentStatus: String = "DELIVERED",
    val keyMetricsSummary: String = ""
)

@Entity(tableName = "agent_proposals")
data class AgentProposalEntity(
    @PrimaryKey val id: String,
    val proposedRole: String,
    val proposedTitle: String,
    val department: String,
    val justification: String,
    val recommendedSkillsCsv: String,
    val toolsCsv: String,
    val validationStatus: String, // PROPOSED, VALIDATING, APPROVED_DEPLOYED, REJECTED
    val createdByAgentId: String,
    val targetFloor: Int = 2
)

// --- ADVANCED ENTERPRISE ENTITIES ---

@Entity(tableName = "marketplace_templates")
data class MarketplaceTemplateEntity(
    @PrimaryKey val id: String,
    val title: String,
    val role: String,
    val department: String,
    val version: String,
    val description: String,
    val requiredToolsCsv: String,
    val certificationScore: Int,
    val isInstalled: Boolean = false,
    val author: String = "AIISG Labs",
    val targetFloor: Int = 2
)

@Entity(tableName = "playbooks")
data class PlaybookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val triggerWorkflow: String,
    val stepsCsv: String,
    val successRatePercent: Int,
    val derivedFromTaskId: String,
    val version: String = "v1.2",
    val approvalStatus: String = "APPROVED" // APPROVED, DRAFT
)

@Entity(tableName = "lessons_learned")
data class LessonLearnedEntity(
    @PrimaryKey val id: String,
    val taskOrErrorTitle: String,
    val rootCause: String,
    val safeFixApplied: String,
    val preventionGuideline: String,
    val recordedEpochMs: Long = System.currentTimeMillis(),
    val severity: String = "MEDIUM"
)

@Entity(tableName = "knowledge_nodes")
data class KnowledgeNodeEntity(
    @PrimaryKey val id: String,
    val nodeType: String, // PROJECT, AGENT, FILE, DECISION, CUSTOMER, SYSTEM
    val title: String,
    val description: String,
    val connectedNodeIdsCsv: String,
    val tagsCsv: String,
    val lastUpdatedEpochMs: Long = System.currentTimeMillis()
)

@Entity(tableName = "customer_tickets")
data class CustomerTicketEntity(
    @PrimaryKey val id: String,
    val customerName: String,
    val company: String,
    val title: String,
    val priority: String,
    val status: String, // OPEN, PENDING_APPROVAL, RESOLVED
    val stage: String, // LEAD, PROPOSAL, CONTRACT, SUPPORT
    val draftResponse: String,
    val requiresApproval: Boolean = true,
    val isApproved: Boolean = false,
    val dealValue: Double = 0.0
)

@Entity(tableName = "procurement_proposals")
data class ProcurementProposalEntity(
    @PrimaryKey val id: String,
    val itemTitle: String,
    val category: String, // CLOUD_COMPUTE, SECURITY_TOOL, DESIGN_ASSET, API_CREDITS
    val vendorA: String,
    val vendorB: String,
    val recommendedVendor: String,
    val estimatedMonthlyCost: Double,
    val justification: String,
    val status: String = "PENDING_CEO_APPROVAL" // PENDING_CEO_APPROVAL, APPROVED, REJECTED
)

@Entity(tableName = "policy_rules")
data class PolicyRuleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val ruleType: String, // AUTONOMOUS_ALLOWED, MANDATORY_APPROVAL, ACCESS_CONTROL
    val conditionText: String,
    val requiresApproval: Boolean,
    val enforcementLevel: String = "STRICT" // STRICT, AUDIT_ONLY
)

@Entity(tableName = "saas_factory_projects")
data class SaaSFactoryProjectEntity(
    @PrimaryKey val id: String,
    val productName: String,
    val currentStage: String, // IDEA, RESEARCH, SPEC, DESIGN, DEVELOPMENT, QA, SECURITY, DEPLOYED, DOCUMENTED
    val progressPercent: Int,
    val ideaBrief: String,
    val researchSummary: String,
    val specDocument: String,
    val designTokens: String,
    val repoUrl: String,
    val qaStatus: String,
    val secStatus: String,
    val releaseNotes: String
)

@Entity(tableName = "model_route_logs")
data class ModelRouteLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val taskTitle: String,
    val selectedModel: String, // gemini-flash, gemini-pro, air-gapped-crypto
    val reason: String,
    val latencyMs: Long,
    val costUsd: Double,
    val timestampEpochMs: Long = System.currentTimeMillis()
)

@Entity(tableName = "agent_chats")
data class AgentChatMessageEntity(
    @PrimaryKey val id: String,
    val senderAgentId: String,
    val senderAgentName: String,
    val recipientAgentId: String,
    val recipientAgentName: String,
    val department: String,
    val message: String,
    val timestampEpochMs: Long = System.currentTimeMillis(),
    val isCeoDirective: Boolean = false
)

@Entity(tableName = "notifications")
data class NotificationItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val category: String, // FINANCIAL, SECURITY, CRM, FACTORY, SYSTEM
    val priority: String, // CRITICAL, HIGH, NORMAL
    val timestampEpochMs: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val targetScreen: String = "OFFICE"
)
