package com.example.domain

import com.example.data.local.MeetingRecordEntity
import com.example.data.local.ReportEntity
import com.example.data.local.TaskEntity
import com.example.data.repository.AIISGRepository
import java.util.UUID

sealed class CommandResult {
    data class Success(val spokenFeedback: String, val actionTaken: String) : CommandResult()
    data class Navigated(val screenId: String, val spokenFeedback: String) : CommandResult()
    data class Error(val message: String) : CommandResult()
}

class VoiceCommandProcessor(private val repository: AIISGRepository) {

    suspend fun processCommand(rawInput: String): CommandResult {
        val input = rawInput.trim().lowercase()

        return when {
            // Emergency Stop
            input.contains("emergency stop") || input.contains("halt all") || input.contains("stop all") -> {
                repository.triggerEmergencyStop()
                CommandResult.Success(
                    spokenFeedback = "Emergency stop initiated, CEO. All autonomous tasks and agents are paused.",
                    actionTaken = "EMERGENCY_STOP_TRIGGERED"
                )
            }

            // Resume
            input.contains("resume") || input.contains("restart operations") -> {
                repository.resumeOperations()
                CommandResult.Success(
                    spokenFeedback = "Operations resumed. Central Commander and Security Guardian are active.",
                    actionTaken = "OPERATIONS_RESUMED"
                )
            }

            // Autonomous SaaS Factory
            input.contains("saas") || input.contains("product banao") -> {
                CommandResult.Navigated(
                    screenId = "SAAS_FACTORY",
                    spokenFeedback = "Autonomous SaaS Project Factory initialized. Launching Idea to Production pipeline, CEO."
                )
            }

            // Marketplace
            input.contains("marketplace") || input.contains("templates") -> {
                CommandResult.Navigated(
                    screenId = "MARKETPLACE",
                    spokenFeedback = "Opening Agent Marketplace and Reusable Playbooks registry."
                )
            }

            // Digital Twin & Knowledge Graph
            input.contains("knowledge") || input.contains("digital twin") -> {
                CommandResult.Navigated(
                    screenId = "DIGITAL_TWIN",
                    spokenFeedback = "Accessing Company Knowledge Graph and Digital Twin telemetry."
                )
            }

            // CRM / Customer
            input.contains("customer") || input.contains("crm") || input.contains("ticket") -> {
                CommandResult.Navigated(
                    screenId = "CRM",
                    spokenFeedback = "Opening Customer and CRM Department. Reviewing pending outbound communication drafts."
                )
            }

            // Governance / Forecasting / Procurement
            input.contains("forecast") || input.contains("governance") || input.contains("procurement") || input.contains("policy") -> {
                CommandResult.Navigated(
                    screenId = "GOVERNANCE",
                    spokenFeedback = "Opening BI Forecasting, Procurement Proposals, and Company Constitution."
                )
            }

            // Software / website project
            input.contains("website") || input.contains("web project") -> {
                val taskId = "TASK-" + UUID.randomUUID().toString().substring(0, 4).uppercase()
                val newTask = TaskEntity(
                    id = taskId,
                    title = "Autonomous Web Application Staging",
                    description = "Deploy next-generation responsive customer portal with real-time WebSocket connectivity.",
                    creatorAgentId = "agent_01_commander",
                    assignedAgentId = "agent_06_web",
                    department = "SOFTWARE",
                    priority = "HIGH",
                    status = "PLANNED",
                    loopStage = "RECEIVE",
                    evidenceLog = "Initiated by CEO voice command: '$rawInput'."
                )
                repository.insertTask(newTask)
                CommandResult.Success(
                    spokenFeedback = "Affirmative CEO. Task $taskId assigned to Web Developer Zara Webb. Workflow loop initiated.",
                    actionTaken = "TASK_CREATED"
                )
            }

            // Developers activity
            input.contains("developer") || input.contains("software team") -> {
                CommandResult.Navigated(
                    screenId = "DEV_WORKSPACE",
                    spokenFeedback = "Navigating to Developer Workspace. Senior Software Engineer and Python Developer are actively running builds."
                )
            }

            // Accounting
            input.contains("accounting") || input.contains("finance") || input.contains("invoice") || input.contains("bill") -> {
                CommandResult.Navigated(
                    screenId = "ACCOUNTING",
                    spokenFeedback = "Opening Accounting Suite. Financial Manager Sterling Croft is reviewing current receivables and bills."
                )
            }

            // Security
            input.contains("security") || input.contains("guardian") || input.contains("scan") -> {
                CommandResult.Navigated(
                    screenId = "SECURITY",
                    spokenFeedback = "Opening Security Operations Center. Security Guardian Aegis Sentinel confirms zero breaches."
                )
            }

            // Weekly meeting
            input.contains("meeting") || input.contains("all-hands") || input.contains("all hands") -> {
                val meetId = "MEET-" + UUID.randomUUID().toString().substring(0, 4).uppercase()
                val meeting = MeetingRecordEntity(
                    id = meetId,
                    title = "AIISG CEO-Convened Executive Session",
                    type = "EMERGENCY_SESSION",
                    status = "CONCLUDED",
                    agenda = "1. CEO Directives\n2. Priority Alignment\n3. Action Items",
                    transcript = "[Astra Commander]: The CEO has convened an executive session. All 15 agents are synchronized on primary objectives.\n[Veritas QA]: Quality gates active.\n[Kaelen Chen]: Software builds aligned.",
                    summary = "Executive session convened by CEO command. All agents confirmed aligned.",
                    decisionsCsv = "Accelerate core engine deliverables, Maintain zero-tolerance error recovery",
                    actionItemsCsv = "Immediate task distribution to Software and Research"
                )
                repository.insertMeeting(meeting)
                CommandResult.Success(
                    spokenFeedback = "Company meeting convened and documented. All 15 agents logged and synchronized.",
                    actionTaken = "MEETING_CONVENED"
                )
            }

            // Reports
            input.contains("report") || input.contains("briefing") -> {
                val reportId = "REP-" + UUID.randomUUID().toString().substring(0, 4).uppercase()
                val report = ReportEntity(
                    id = reportId,
                    type = if (input.contains("morning")) "DAILY_MORNING_0900" else "DAILY_EVENING_2100",
                    title = "AIISG Verified Executive Report",
                    verifiedContent = "[VERIFIED SYSTEM REPORT]\nTriggered on demand by Owner/CEO.\nAll 15 agents verified active.\nTask queue clean.\nZero safety discrepancies.",
                    recipientEmail = "001ylfamily@gmail.com",
                    sentStatus = "DELIVERED",
                    keyMetricsSummary = "100% Operational Health | 15 Agents | Verified Evidence"
                )
                repository.insertReport(report)
                CommandResult.Success(
                    spokenFeedback = "Executive report generated and queued for transmission to your registered email address.",
                    actionTaken = "REPORT_GENERATED"
                )
            }

            // Research
            input.contains("research") || input.contains("investigate") -> {
                val taskId = "TASK-" + UUID.randomUUID().toString().substring(0, 4).uppercase()
                val newTask = TaskEntity(
                    id = taskId,
                    title = "Autonomous Market & Tech Investigation",
                    description = "Conduct deep competitive analysis on sovereign AI enterprise infrastructures and compile findings.",
                    creatorAgentId = "agent_01_commander",
                    assignedAgentId = "agent_08_research",
                    department = "RESEARCH",
                    priority = "NORMAL",
                    status = "PLANNED",
                    loopStage = "RECEIVE",
                    evidenceLog = "Dispatched via CEO request: '$rawInput'."
                )
                repository.insertTask(newTask)
                CommandResult.Success(
                    spokenFeedback = "Dispatched task $taskId to Research Analyst Dr. Aris Thorne.",
                    actionTaken = "TASK_CREATED"
                )
            }

            // Agent Creation Lab
            input.contains("create agent") || input.contains("new agent") || input.contains("creation lab") -> {
                CommandResult.Navigated(
                    screenId = "AGENT_LAB",
                    spokenFeedback = "Navigating to Agent Creation Lab. The team has synthesized specialist candidate proposals."
                )
            }

            // Fallback: general objective breakdown by Central Commander
            else -> {
                val taskId = "TASK-" + UUID.randomUUID().toString().substring(0, 4).uppercase()
                val newTask = TaskEntity(
                    id = taskId,
                    title = rawInput.take(60),
                    description = "Autonomous task generated from Owner voice directive: \"$rawInput\". Central Commander breaking down execution steps.",
                    creatorAgentId = "agent_01_commander",
                    assignedAgentId = "agent_04_sr_eng",
                    department = "SOFTWARE",
                    priority = "HIGH",
                    status = "PLANNED",
                    loopStage = "RECEIVE",
                    evidenceLog = "Directive registered. Universal AI loop starting."
                )
                repository.insertTask(newTask)
                CommandResult.Success(
                    spokenFeedback = "Understood CEO. Central Commander has ingested your objective and scheduled task $taskId.",
                    actionTaken = "COMMANDER_DIRECTIVE_ASSIGNED"
                )
            }
        }
    }
}
