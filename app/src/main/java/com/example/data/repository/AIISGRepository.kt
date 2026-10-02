package com.example.data.repository

import com.example.data.local.*
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class AIISGRepository(private val db: AIISGDatabase) {

    val allAgentsFlow: Flow<List<AgentEntity>> = db.agentDao().getAllAgentsFlow()
    val allTasksFlow: Flow<List<TaskEntity>> = db.taskDao().getAllTasksFlow()
    val activeTasksFlow: Flow<List<TaskEntity>> = db.taskDao().getActiveTasksFlow()
    val allProjectsFlow: Flow<List<ProjectEntity>> = db.projectDao().getAllProjectsFlow()
    val allRecordsFlow: Flow<List<AccountingRecordEntity>> = db.accountingDao().getAllRecordsFlow()
    val recentAuditLogsFlow: Flow<List<AuditLogEntity>> = db.auditLogDao().getRecentLogsFlow()
    val allMeetingsFlow: Flow<List<MeetingRecordEntity>> = db.meetingDao().getAllMeetingsFlow()
    val allReportsFlow: Flow<List<ReportEntity>> = db.reportDao().getAllReportsFlow()
    val allProposalsFlow: Flow<List<AgentProposalEntity>> = db.agentProposalDao().getAllProposalsFlow()

    // Advanced Enterprise Flows
    val allMarketplaceTemplatesFlow: Flow<List<MarketplaceTemplateEntity>> = db.marketplaceDao().getAllTemplatesFlow()
    val allPlaybooksFlow: Flow<List<PlaybookEntity>> = db.playbookDao().getAllPlaybooksFlow()
    val allLessonsFlow: Flow<List<LessonLearnedEntity>> = db.lessonLearnedDao().getAllLessonsFlow()
    val allKnowledgeNodesFlow: Flow<List<KnowledgeNodeEntity>> = db.knowledgeDao().getAllNodesFlow()
    val allCustomerTicketsFlow: Flow<List<CustomerTicketEntity>> = db.customerDao().getAllTicketsFlow()
    val allProcurementProposalsFlow: Flow<List<ProcurementProposalEntity>> = db.procurementDao().getAllProposalsFlow()
    val allPolicyRulesFlow: Flow<List<PolicyRuleEntity>> = db.policyDao().getAllRulesFlow()
    val allSaaSProjectsFlow: Flow<List<SaaSFactoryProjectEntity>> = db.saasFactoryDao().getAllProjectsFlow()
    val recentModelRouteLogsFlow: Flow<List<ModelRouteLogEntity>> = db.modelRouteDao().getRecentLogsFlow()
    val allAgentChatsFlow: Flow<List<AgentChatMessageEntity>> = db.agentChatDao().getAllMessagesFlow()
    val allNotificationsFlow: Flow<List<NotificationItemEntity>> = db.notificationDao().getAllNotificationsFlow()
    val unreadNotificationsCountFlow: Flow<Int> = db.notificationDao().getUnreadCountFlow()

    suspend fun initializeSeedsIfEmpty() {
        val count = db.agentDao().getAgentCount()
        if (count == 0) {
            seedInitialAgents()
            seedInitialTasks()
            seedInitialProjects()
            seedInitialAccounting()
            seedInitialReports()
            seedInitialMeetings()
            seedInitialProposals()
            seedAdvancedEnterpriseData()
            logAudit("SYSTEM", "AIISG Core", "INITIALIZATION", "System", "Autonomous Company initialized with 15 agents & Enterprise Architecture", true, "INFO")
        }
    }

    private suspend fun seedInitialAgents() {
        val initialAgents = listOf(
            AgentEntity(
                id = "agent_01_commander",
                name = "Astra Commander",
                title = "Central Commander",
                department = "EXECUTIVE",
                floor = 1,
                currentRoom = "COMMANDER_ROOM",
                status = "WORKING",
                skillsCsv = "Orchestration, Task Delegation, Dependency Tracking, Progress Auditing",
                toolsCsv = "Planner, Scheduler, System Monitor, Agent Dispatcher",
                priority = "CRITICAL",
                completedTasksCount = 42,
                failedTasksCount = 0,
                idleMinutes = 2,
                activeMinutes = 180,
                resourceUsagePercent = 45,
                avatarColorHex = "#00E5FF",
                posX = 0.72f, posY = 0.25f, targetX = 0.72f, targetY = 0.25f,
                version = "v2.4", certificationScore = 100, totalAiCostUsd = 12.40
            ),
            AgentEntity(
                id = "agent_02_ops",
                name = "Marcus Vance",
                title = "Operations Manager",
                department = "EXECUTIVE",
                floor = 1,
                currentRoom = "COMMANDER_ROOM",
                status = "ONLINE",
                skillsCsv = "Workflow Optimization, Capacity Management, Resource Allocation",
                toolsCsv = "Resource Monitor, Queue Balancer, Metrics Dashboard",
                priority = "HIGH",
                completedTasksCount = 28,
                failedTasksCount = 1,
                idleMinutes = 15,
                activeMinutes = 120,
                resourceUsagePercent = 28,
                avatarColorHex = "#38BDF8",
                posX = 0.68f, posY = 0.28f, targetX = 0.68f, targetY = 0.28f,
                version = "v1.8", certificationScore = 96, totalAiCostUsd = 5.20
            ),
            AgentEntity(
                id = "agent_03_pm",
                name = "Elena Rostova",
                title = "Project Manager",
                department = "EXECUTIVE",
                floor = 1,
                currentRoom = "COMMANDER_ROOM",
                status = "COLLABORATING",
                skillsCsv = "Milestone Tracking, Sprint Management, Requirement Scoping",
                toolsCsv = "Gantt Engine, Jira Integrator, Sprint Auditor",
                priority = "HIGH",
                completedTasksCount = 35,
                failedTasksCount = 0,
                idleMinutes = 8,
                activeMinutes = 150,
                resourceUsagePercent = 32,
                avatarColorHex = "#818CF8",
                posX = 0.76f, posY = 0.28f, targetX = 0.76f, targetY = 0.28f,
                version = "v1.9", certificationScore = 98, totalAiCostUsd = 6.80
            ),
            AgentEntity(
                id = "agent_04_sr_eng",
                name = "Kaelen Chen",
                title = "Senior Software Engineer",
                department = "SOFTWARE",
                floor = 1,
                currentRoom = "SOFTWARE_DEPT",
                status = "WORKING",
                skillsCsv = "System Architecture, Kotlin, Clean Code, Microservices, Security",
                toolsCsv = "Compiler, Git Engine, Profiler, Architecture Linter",
                priority = "HIGH",
                completedTasksCount = 54,
                failedTasksCount = 2,
                idleMinutes = 4,
                activeMinutes = 240,
                resourceUsagePercent = 65,
                avatarColorHex = "#3B82F6",
                posX = 0.22f, posY = 0.62f, targetX = 0.22f, targetY = 0.62f,
                version = "v3.1", certificationScore = 99, totalAiCostUsd = 16.50
            ),
            AgentEntity(
                id = "agent_05_python",
                name = "Pythius Bot",
                title = "Python Developer",
                department = "SOFTWARE",
                floor = 1,
                currentRoom = "SOFTWARE_DEPT",
                status = "TESTING",
                skillsCsv = "Python 3.12, FastAPI, PyTorch, PyTest, Pandas, AsyncIO",
                toolsCsv = "Python REPL, PyTest Engine, Virtualenv Manager, OpenAPI Builder",
                priority = "NORMAL",
                completedTasksCount = 47,
                failedTasksCount = 1,
                idleMinutes = 6,
                activeMinutes = 210,
                resourceUsagePercent = 58,
                avatarColorHex = "#10B981",
                posX = 0.26f, posY = 0.68f, targetX = 0.26f, targetY = 0.68f,
                version = "v2.0", certificationScore = 97, totalAiCostUsd = 11.30
            ),
            AgentEntity(
                id = "agent_06_web",
                name = "Zara Webb",
                title = "Web Developer",
                department = "SOFTWARE",
                floor = 1,
                currentRoom = "SOFTWARE_DEPT",
                status = "WORKING",
                skillsCsv = "React, TypeScript, CSS Grid, Vite, Tailwind, State Management",
                toolsCsv = "Node Environment, Chromium Headless, Webpack/Vite, Lighthouse",
                priority = "NORMAL",
                completedTasksCount = 39,
                failedTasksCount = 2,
                idleMinutes = 12,
                activeMinutes = 190,
                resourceUsagePercent = 52,
                avatarColorHex = "#06B6D4",
                posX = 0.18f, posY = 0.67f, targetX = 0.18f, targetY = 0.67f,
                version = "v2.2", certificationScore = 95, totalAiCostUsd = 9.40
            ),
            AgentEntity(
                id = "agent_07_qa",
                name = "Veritas QA",
                title = "QA / Testing Engineer",
                department = "SOFTWARE",
                floor = 1,
                currentRoom = "SOFTWARE_DEPT",
                status = "TESTING",
                skillsCsv = "Automated Testing, Regression Suites, Boundary Testing, Mocking",
                toolsCsv = "JUnit Runner, Selenium/Playwright, Mockito, Coverage Inspector",
                priority = "HIGH",
                completedTasksCount = 61,
                failedTasksCount = 0,
                idleMinutes = 5,
                activeMinutes = 220,
                resourceUsagePercent = 48,
                avatarColorHex = "#14B8A6",
                posX = 0.28f, posY = 0.63f, targetX = 0.28f, targetY = 0.63f,
                version = "v2.5", certificationScore = 100, totalAiCostUsd = 8.10
            ),
            AgentEntity(
                id = "agent_08_research",
                name = "Dr. Aris Thorne",
                title = "Research Analyst",
                department = "RESEARCH",
                floor = 1,
                currentRoom = "RESEARCH_LAB",
                status = "THINKING",
                skillsCsv = "Market Intelligence, Competitor Analysis, AI Model Benchmarking",
                toolsCsv = "Semantic Search, Academic Corpus, Web Scraper, Synthesis Core",
                priority = "NORMAL",
                completedTasksCount = 22,
                failedTasksCount = 0,
                idleMinutes = 20,
                activeMinutes = 110,
                resourceUsagePercent = 38,
                avatarColorHex = "#8B5CF6",
                posX = 0.72f, posY = 0.64f, targetX = 0.72f, targetY = 0.64f,
                version = "v1.4", certificationScore = 94, totalAiCostUsd = 7.60
            ),
            AgentEntity(
                id = "agent_09_data",
                name = "Cipher Matrix",
                title = "Data Analyst",
                department = "RESEARCH",
                floor = 1,
                currentRoom = "RESEARCH_LAB",
                status = "ONLINE",
                skillsCsv = "SQL, Time Series, Statistical Modeling, Tableau, Predictive ML",
                toolsCsv = "DuckDB, Apache Arrow, Plotly Engine, Regression Models",
                priority = "NORMAL",
                completedTasksCount = 31,
                failedTasksCount = 1,
                idleMinutes = 14,
                activeMinutes = 135,
                resourceUsagePercent = 42,
                avatarColorHex = "#A855F7",
                posX = 0.78f, posY = 0.66f, targetX = 0.78f, targetY = 0.66f,
                version = "v1.7", certificationScore = 95, totalAiCostUsd = 8.90
            ),
            AgentEntity(
                id = "agent_10_writer",
                name = "Lyra Scribus",
                title = "Writer / Documentation Agent",
                department = "CREATIVE",
                floor = 2,
                currentRoom = "CREATIVE_STUDIO",
                status = "WORKING",
                skillsCsv = "Technical Writing, API Documentation, Release Notes, Whitepapers",
                toolsCsv = "Markdown Parser, Spell Checker, Docusaurus Generator, PDF Exporter",
                priority = "NORMAL",
                completedTasksCount = 44,
                failedTasksCount = 0,
                idleMinutes = 11,
                activeMinutes = 160,
                resourceUsagePercent = 25,
                avatarColorHex = "#EC4899",
                posX = 0.22f, posY = 0.82f, targetX = 0.22f, targetY = 0.82f,
                version = "v2.1", certificationScore = 97, totalAiCostUsd = 4.80
            ),
            AgentEntity(
                id = "agent_11_designer",
                name = "Aura Pixel",
                title = "UI/UX Designer",
                department = "CREATIVE",
                floor = 2,
                currentRoom = "CREATIVE_STUDIO",
                status = "COLLABORATING",
                skillsCsv = "Material Design 3, Design Tokens, User Flow, Accessibility, Prototyping",
                toolsCsv = "Vector Studio, Color Palette Synthesizer, WCAG Contrast Checker",
                priority = "NORMAL",
                completedTasksCount = 33,
                failedTasksCount = 0,
                idleMinutes = 9,
                activeMinutes = 175,
                resourceUsagePercent = 34,
                avatarColorHex = "#F43F5E",
                posX = 0.28f, posY = 0.78f, targetX = 0.28f, targetY = 0.78f,
                version = "v1.8", certificationScore = 96, totalAiCostUsd = 5.90
            ),
            AgentEntity(
                id = "agent_12_sec_guard",
                name = "Aegis Sentinel",
                title = "Security Guardian",
                department = "SECURITY",
                floor = 2,
                currentRoom = "SECURITY_CENTER",
                status = "WORKING",
                skillsCsv = "Policy Enforcement, Anomaly Detection, Access Control, Incident Response",
                toolsCsv = "Firewall Watcher, Key Vault Guard, Syslog Monitor, Kill-Switch Controller",
                priority = "CRITICAL",
                completedTasksCount = 88,
                failedTasksCount = 0,
                idleMinutes = 0,
                activeMinutes = 300,
                resourceUsagePercent = 30,
                avatarColorHex = "#EF4444",
                posX = 0.24f, posY = 0.50f, targetX = 0.24f, targetY = 0.50f,
                version = "v3.0", certificationScore = 100, totalAiCostUsd = 14.10
            ),
            AgentEntity(
                id = "agent_13_sec_test",
                name = "Specter Red",
                title = "Authorized Security Testing Agent",
                department = "SECURITY",
                floor = 2,
                currentRoom = "SECURITY_CENTER",
                status = "TESTING",
                skillsCsv = "Vulnerability Assessment, Static Code Analysis, OWASP Top 10, Dependency Audit",
                toolsCsv = "SAST Scanner, Dependency Check, Sandboxed Fuzzer, Report Compiler",
                priority = "HIGH",
                completedTasksCount = 26,
                failedTasksCount = 0,
                idleMinutes = 18,
                activeMinutes = 140,
                resourceUsagePercent = 46,
                avatarColorHex = "#F97316",
                posX = 0.26f, posY = 0.55f, targetX = 0.26f, targetY = 0.55f,
                version = "v2.0", certificationScore = 98, totalAiCostUsd = 9.80
            ),
            AgentEntity(
                id = "agent_14_acct_mgr",
                name = "Sterling Croft",
                title = "Accounting Manager",
                department = "FINANCE",
                floor = 2,
                currentRoom = "ACCOUNTING_SUITE",
                status = "WORKING",
                skillsCsv = "Financial Reporting, GAAP Compliance, Budget Variance, Reconciliation",
                toolsCsv = "Ledger Reconciler, Balance Calculator, Tax Estimator, Compliance Auditor",
                priority = "HIGH",
                completedTasksCount = 52,
                failedTasksCount = 0,
                idleMinutes = 7,
                activeMinutes = 195,
                resourceUsagePercent = 36,
                avatarColorHex = "#EAB308",
                posX = 0.74f, posY = 0.48f, targetX = 0.74f, targetY = 0.48f,
                version = "v2.3", certificationScore = 99, totalAiCostUsd = 7.40
            ),
            AgentEntity(
                id = "agent_15_billing",
                name = "Penny Ledger",
                title = "Invoice & Billing Agent",
                department = "FINANCE",
                floor = 2,
                currentRoom = "ACCOUNTING_SUITE",
                status = "ONLINE",
                skillsCsv = "Invoicing, Accounts Receivable/Payable, Discrepancy Flagging, Audit Trail",
                toolsCsv = "Invoice Generator, Receipt Matcher, Discrepancy Radar, Stripe Sync",
                priority = "NORMAL",
                completedTasksCount = 49,
                failedTasksCount = 0,
                idleMinutes = 10,
                activeMinutes = 180,
                resourceUsagePercent = 29,
                avatarColorHex = "#F59E0B",
                posX = 0.76f, posY = 0.54f, targetX = 0.76f, targetY = 0.54f,
                version = "v1.6", certificationScore = 97, totalAiCostUsd = 6.20
            )
        )
        db.agentDao().insertAgents(initialAgents)
    }

    private suspend fun seedInitialTasks() {
        val initialTasks = listOf(
            TaskEntity(
                id = "TASK-101",
                title = "Build Automated Financial Discrepancy Radar",
                description = "Develop automated verification routine to cross-reference invoices against purchase orders and bank statements.",
                creatorAgentId = "agent_01_commander",
                assignedAgentId = "agent_05_python",
                department = "SOFTWARE",
                priority = "HIGH",
                status = "RUNNING",
                loopStage = "EXECUTE",
                retryCount = 0,
                evidenceLog = "Python virtual environment configured. FastAPI router created. Unit tests passing 8/8.",
                resultSummary = "Core verification engine running in sandbox.",
                requiresOwnerAuth = false,
                triggerSource = "Owner Command: 'Show me today\\'s accounting.'",
                commanderReasoning = "Discrepancy prevention essential for GAAP compliance before end of quarter.",
                modelUsed = "gemini-pro"
            ),
            TaskEntity(
                id = "TASK-102",
                title = "OWASP Dependency Vulnerability Scan",
                description = "Execute static scan across all Python and TypeScript packages to ensure zero CVE vulnerabilities.",
                creatorAgentId = "agent_12_sec_guard",
                assignedAgentId = "agent_13_sec_test",
                department = "SECURITY",
                priority = "CRITICAL",
                status = "RUNNING",
                loopStage = "OBSERVE",
                retryCount = 0,
                evidenceLog = "Scanner verified 142 dependencies. Zero high severity vulnerabilities found.",
                resultSummary = "Security integrity clean at SHA-256 baseline.",
                requiresOwnerAuth = false,
                triggerSource = "Security Guardian Continuous Daemon",
                commanderReasoning = "Zero-trust policy requires daily dependency scan prior to staging.",
                modelUsed = "air-gapped-crypto"
            ),
            TaskEntity(
                id = "TASK-103",
                title = "Autonomous Web Portal Frontend Polish",
                description = "Implement Material 3 responsive dashboard with real-time WebSocket state synchronization.",
                creatorAgentId = "agent_03_pm",
                assignedAgentId = "agent_06_web",
                department = "SOFTWARE",
                priority = "NORMAL",
                status = "VERIFIED",
                loopStage = "COMPLETE",
                retryCount = 0,
                evidenceLog = "Vite build succeeded in 842ms. Lighthouse accessibility score 100/100.",
                resultSummary = "Clean modern responsive UI deployed to internal staging.",
                requiresOwnerAuth = false,
                triggerSource = "Sprint Planning Milestone 4",
                commanderReasoning = "Customer UX benchmark requires 100% WCAG accessibility compliance.",
                modelUsed = "gemini-flash"
            ),
            TaskEntity(
                id = "TASK-104",
                title = "Monthly Software License Rebalance",
                description = "Reconcile cloud compute expenses against departmental budget allocations.",
                creatorAgentId = "agent_14_acct_mgr",
                assignedAgentId = "agent_15_billing",
                department = "FINANCE",
                priority = "HIGH",
                status = "PLANNED",
                loopStage = "PLAN",
                retryCount = 0,
                evidenceLog = "Ledger extracted. Ready for discrepancy checking.",
                resultSummary = "Planned for 14:00 execution.",
                requiresOwnerAuth = true,
                isAuthorized = false,
                triggerSource = "Fiscal Month-End Rebalance Rule",
                commanderReasoning = "Disbursement threshold exceeds \$5,000, gating under CEO authorization rule.",
                modelUsed = "gemini-flash"
            )
        )
        db.taskDao().insertTasks(initialTasks)
    }

    private suspend fun seedInitialProjects() {
        val initialProjects = listOf(
            ProjectEntity(
                id = "PROJ-AIISG-CORE",
                name = "AIISG Autonomous Core Engine",
                code = "ACE-01",
                department = "SOFTWARE",
                status = "IN_PROGRESS",
                progressPercent = 78,
                leadAgentId = "agent_04_sr_eng",
                description = "Central distributed task scheduling and universal self-healing execution loop.",
                gitCommitHash = "7f3b9c1",
                repoBranch = "main",
                testCoveragePercent = 94,
                activeFile = "engine/universal_loop.py",
                estimatedAiCostUsd = 24.50
            ),
            ProjectEntity(
                id = "PROJ-SEC-FORTRESS",
                name = "Project Aegis Guard",
                code = "SEC-02",
                department = "SECURITY",
                status = "TESTING",
                progressPercent = 88,
                leadAgentId = "agent_12_sec_guard",
                description = "Real-time tamper-resistant audit logging and zero-trust permission enforcement.",
                gitCommitHash = "a18e42f",
                repoBranch = "security-v2",
                testCoveragePercent = 98,
                activeFile = "guardian/tamper_log.py",
                estimatedAiCostUsd = 12.80
            ),
            ProjectEntity(
                id = "PROJ-FIN-INTELLIGENCE",
                name = "Genie Ledger Reconciler",
                code = "FIN-03",
                department = "FINANCE",
                status = "IN_PROGRESS",
                progressPercent = 65,
                leadAgentId = "agent_14_acct_mgr",
                description = "Automated invoice generator, balance auditor, and human-authorized transfer gate.",
                gitCommitHash = "9d41b63",
                repoBranch = "finance-automation",
                testCoveragePercent = 91,
                activeFile = "accounting/reconciliation.py",
                estimatedAiCostUsd = 8.40
            )
        )
        db.projectDao().insertProjects(initialProjects)
    }

    private suspend fun seedInitialAccounting() {
        val records = listOf(
            AccountingRecordEntity(
                id = "INV-2026-081",
                type = "INVOICE",
                title = "AI Enterprise Office Deployment - Tier 1",
                counterparty = "Apex Global Technologies",
                amount = 45000.0,
                currency = "USD",
                status = "APPROVED",
                department = "FINANCE",
                category = "Enterprise Revenue",
                requiresHumanAuth = false,
                notes = "Milestone 1 completed and verified."
            ),
            AccountingRecordEntity(
                id = "BILL-2026-019",
                type = "BILL",
                title = "Dedicated Cloud GPU Cluster Hosting",
                counterparty = "Neural Cloud Infrastructure Corp",
                amount = 8240.0,
                currency = "USD",
                status = "PENDING_APPROVAL",
                department = "SOFTWARE",
                category = "Cloud Infrastructure",
                requiresHumanAuth = true,
                notes = "Requires CEO confirmation for outward payment."
            ),
            AccountingRecordEntity(
                id = "EXP-2026-042",
                type = "EXPENSE",
                title = "SSL Wildcard & Domain Renewals",
                counterparty = "CyberTrust Registry",
                amount = 450.0,
                currency = "USD",
                status = "RECONCILED",
                department = "SECURITY",
                category = "Security Licenses",
                requiresHumanAuth = false,
                notes = "Pre-approved operational expenditure."
            ),
            AccountingRecordEntity(
                id = "INV-2026-082",
                type = "INVOICE",
                title = "Autonomous Research & Market Intelligence Contract",
                counterparty = "Vanguard Innovations Ltd",
                amount = 28500.0,
                currency = "USD",
                status = "DRAFT",
                department = "RESEARCH",
                category = "Research Services",
                requiresHumanAuth = false,
                notes = "Drafting final deliverables."
            )
        )
        db.accountingDao().insertRecords(records)
    }

    private suspend fun seedInitialReports() {
        val reports = listOf(
            ReportEntity(
                id = "REP-0900-TODAY",
                type = "DAILY_MORNING_0900",
                title = "AIISG Morning Executive Briefing (09:00 AM)",
                verifiedContent = """
                    [09:00 VERIFIED MORNING BRIEFING]
                    Company: Artificial Intelligent Genie Private Limited
                    Timezone: Asia/Dubai (Local: Active)
                    Status: All 15 Primary Agents Online & Healthy
                    
                    1. OVERNIGHT PROGRESS:
                    - Software Dept ran 12 automated regressions (100% pass).
                    - Security Guardian blocked 0 unauthorized access attempts; zero policy breaches.
                    - Memory storage verified clean at 14.2 MB.
                    
                    2. TODAY'S OBJECTIVES:
                    - Senior Software Engineer & Python Dev: Deliver Financial Discrepancy Radar.
                    - Security: Complete OWASP dependency audit.
                    - Finance: Reconcile monthly cloud bills ($8,240.00 pending CEO authorization).
                    - Creative: Finalize documentation for API v2.
                    
                    3. SYSTEM HEALTH:
                    - CPU Load: 24% | RAM: 1.4 GB / 8 GB | Disk: 18% used.
                    - Task Queue: 4 Active Tasks | 0 Blocked.
                """.trimIndent(),
                recipientEmail = "001ylfamily@gmail.com",
                sentStatus = "DELIVERED",
                keyMetricsSummary = "15 Online | 4 Tasks Active | 0 Critical Vulnerabilities"
            ),
            ReportEntity(
                id = "REP-2100-YESTERDAY",
                type = "DAILY_EVENING_2100",
                title = "AIISG Evening Comprehensive Audit (21:00 PM)",
                verifiedContent = """
                    [21:00 VERIFIED EVENING REPORT]
                    Company: Artificial Intelligent Genie Private Limited
                    
                    1. COMPLETED WORK VERIFICATION:
                    - Tasks Completed: 18 verified with evidence hashes.
                    - Code Commits: 7 commits pushed across 3 active repositories.
                    - Test Coverage: Maintained at 94.2% system-wide.
                    
                    2. FINANCIAL SUMMARY:
                    - Accounts Receivable: $73,500.00
                    - Accounts Payable: $8,690.00
                    - Discrepancies detected: 0
                    
                    3. AGENT WORKLOAD AUDIT:
                    - Top Contributor: Veritas QA (61 verifications)
                    - Active Hours: Average 3.2 hours autonomous compute per agent.
                """.trimIndent(),
                recipientEmail = "001ylfamily@gmail.com",
                sentStatus = "DELIVERED",
                keyMetricsSummary = "18 Tasks Completed | $73.5K Receivable | Zero Discrepancies"
            )
        )
        db.reportDao().insertReports(reports)
    }

    private suspend fun seedInitialMeetings() {
        val meetings = listOf(
            MeetingRecordEntity(
                id = "MEET-ALL-HANDS-W39",
                title = "AIISG All-Hands Strategic Weekly Conference",
                type = "WEEKLY_ALL_HANDS",
                status = "CONCLUDED",
                agenda = "1. Completed Work Review\n2. Security State\n3. Financial Health\n4. Agent Creation Lab Proposals\n5. Next-Week Milestones",
                transcript = """
                    [Astra Commander]: Good morning team. All 15 agents present. Let us review the sprint deliverables.
                    [Kaelen Chen - Sr Eng]: Software department has unified the universal execution loop. All task transitions now enforce independent verification before 'COMPLETE' can be emitted.
                    [Aegis Sentinel - Security]: Zero breaches. Access controls strictly isolate destructive and financial operations to explicit CEO token authorization.
                    [Sterling Croft - Finance]: Q3 enterprise revenue is tracking 18% above projections. Cloud bills are awaiting owner approval.
                    [Astra Commander]: Excellent. Let us submit the Agent Creation Lab proposal for a specialized Cloud DevOps agent to manage auto-scaling.
                """.trimIndent(),
                summary = "All 15 agents coordinated successfully. Zero fake progress policy strictly enforced. Proposed creating new DevOps Agent.",
                decisionsCsv = "Enforce 100% test verification on all commits, Gate financial payments with CEO biometric/touch authorization, Expand Floor 2 Agent Lab",
                actionItemsCsv = "QA agent to add load testing suite, Python Dev to finish Discrepancy Radar, Designer to refine Floor 2 layout"
            )
        )
        db.meetingDao().insertMeetings(meetings)
    }

    private suspend fun seedInitialProposals() {
        val proposal = AgentProposalEntity(
            id = "PROP-001",
            proposedRole = "Cloud DevOps & Kubernetes Architect",
            proposedTitle = "DevOps Engineer",
            department = "SOFTWARE",
            justification = "Workload analysis indicates Software Engineering agents spend 22% of cycles configuring Docker containers and CI/CD pipelines.",
            recommendedSkillsCsv = "Kubernetes, Docker, Terraform, Prometheus, CI/CD Pipelines, Bash Scripting",
            toolsCsv = "Kubectl, Helm, Docker Daemon, Terraform CLI, Grafana Exporter",
            validationStatus = "PROPOSED",
            createdByAgentId = "agent_01_commander",
            targetFloor = 2
        )
        db.agentProposalDao().insertProposal(proposal)
    }

    private suspend fun seedAdvancedEnterpriseData() {
        // 1. Marketplace Templates
        val templates = listOf(
            MarketplaceTemplateEntity(
                id = "TMPL-FLUTTER-ARCH",
                title = "Flutter & Mobile Systems Architect",
                role = "Senior Mobile Architect",
                department = "SOFTWARE",
                version = "v1.4",
                description = "Cross-platform mobile client builder with offline-first synchronization and Riverpod architecture.",
                requiredToolsCsv = "Flutter SDK, Dart Analyzer, Gradle Builder, Fastlane",
                certificationScore = 98,
                isInstalled = false,
                targetFloor = 3
            ),
            MarketplaceTemplateEntity(
                id = "TMPL-PEN-TESTER",
                title = "Automated Penetration Testing Agent",
                role = "Offensive Security Specialist",
                department = "SECURITY",
                version = "v2.1",
                description = "Ethical red-team scanner evaluating network ingress, TLS suites, JWT tampering, and API boundaries.",
                requiredToolsCsv = "Nmap Sandbox, Burp Suite Engine, SAST Parser, CVE Matcher",
                certificationScore = 99,
                isInstalled = false,
                targetFloor = 2
            ),
            MarketplaceTemplateEntity(
                id = "TMPL-GROWTH-MKT",
                title = "Autonomous B2B Growth Marketer",
                role = "Growth & SEO Specialist",
                department = "CUSTOMER",
                version = "v1.1",
                description = "Lead generation engine, competitor keyword research, cold outreach draft generation.",
                requiredToolsCsv = "SERP Analyzer, LinkedIn Scraper, Email Warmup Engine",
                certificationScore = 93,
                isInstalled = false,
                targetFloor = 3
            ),
            MarketplaceTemplateEntity(
                id = "TMPL-CLOUD-SRE",
                title = "Site Reliability & Chaos Engineer",
                role = "SRE Guardian",
                department = "SOFTWARE",
                version = "v3.0",
                description = "Automated uptime monitoring, canary deployments, auto-healing restarts, and latency alarms.",
                requiredToolsCsv = "Prometheus Scraper, Chaos Monkey, PagerDuty Webhook, Docker CLI",
                certificationScore = 100,
                isInstalled = true,
                targetFloor = 2
            )
        )
        db.marketplaceDao().insertTemplates(templates)

        // 2. Playbooks (Reusable workflows)
        val playbooks = listOf(
            PlaybookEntity(
                id = "PB-SENTRY-REGRESSION",
                title = "Playbook: Continuous Zero-Regression Validation",
                category = "QUALITY_ASSURANCE",
                triggerWorkflow = "On Code Commit to Main Branch",
                stepsCsv = "Run Static Linter, Compile Sandboxed Binary, Execute 48 Unit Tests, Verify OWASP Dependency Free, Emit SHA256 Signature",
                successRatePercent = 99,
                derivedFromTaskId = "TASK-101",
                version = "v2.1",
                approvalStatus = "APPROVED"
            ),
            PlaybookEntity(
                id = "PB-DISCREPANCY-RESOLVE",
                title = "Playbook: Financial Variance Triangulation",
                category = "FINANCE",
                triggerWorkflow = "On Ledger Imbalance > $0.00",
                stepsCsv = "Extract Stripe Receipts, Compare Invoiced POs, Fetch Bank Statement JSON, Flag Discrepancy Line Item, Queue CEO Authorization",
                successRatePercent = 100,
                derivedFromTaskId = "TASK-104",
                version = "v1.5",
                approvalStatus = "APPROVED"
            ),
            PlaybookEntity(
                id = "PB-INCIDENT-LOCKDOWN",
                title = "Playbook: Suspicious Ingress Emergency Quarantine",
                category = "SECURITY",
                triggerWorkflow = "On Abnormal API Key Rate or Unverified Origin",
                stepsCsv = "Revoke Active Session Token, Rotate Internal Service Keys, Pause Outward Networking, Notify CEO & Guardian, Record Forensic Hash",
                successRatePercent = 98,
                derivedFromTaskId = "TASK-102",
                version = "v3.0",
                approvalStatus = "APPROVED"
            )
        )
        db.playbookDao().insertPlaybooks(playbooks)

        // 3. Lessons Learned
        val lessons = listOf(
            LessonLearnedEntity(
                id = "LL-001",
                taskOrErrorTitle = "Memory Leak in Persistent WebSocket Daemon",
                rootCause = "Unclosed Coroutine channel in high-frequency office ticker.",
                safeFixApplied = "Bound ticker lifecycle directly to CoroutineScope(SupervisorJob() + Dispatchers.Default).",
                preventionGuideline = "All streaming loops must employ explicit cancellation checks.",
                severity = "HIGH"
            ),
            LessonLearnedEntity(
                id = "LL-002",
                taskOrErrorTitle = "Outward Payment API Timing Anomaly",
                rootCause = "Automated bill settlement attempted before human approval confirmation.",
                safeFixApplied = "Enforced hard gating check in Database DAO: requiresHumanAuth = true blocks commit until isApproved = true.",
                preventionGuideline = "Zero financial disbursements may execute without cryptographic CEO confirmation.",
                severity = "CRITICAL"
            )
        )
        db.lessonLearnedDao().insertLessons(lessons)

        // 4. Company Knowledge Graph Nodes
        val nodes = listOf(
            KnowledgeNodeEntity(
                id = "KN-AIISG-SYS",
                nodeType = "SYSTEM",
                title = "AIISG Autonomous Operating Infrastructure",
                description = "Primary operating matrix coordinating 15 autonomous agents across 3 virtual office floors.",
                connectedNodeIdsCsv = "KN-ASTRA-CMD, KN-AEGIS-SEC, KN-ACE-CORE",
                tagsCsv = "Core, Sovereign, Infrastructure"
            ),
            KnowledgeNodeEntity(
                id = "KN-ASTRA-CMD",
                nodeType = "AGENT",
                title = "Central Commander Astra",
                description = "Master orchestration AI responsible for objective decomposition and universal loop enforcement.",
                connectedNodeIdsCsv = "KN-AIISG-SYS, KN-ACE-CORE, KN-DECISION-01",
                tagsCsv = "Executive, Orchestration, Commander"
            ),
            KnowledgeNodeEntity(
                id = "KN-AEGIS-SEC",
                nodeType = "AGENT",
                title = "Aegis Sentinel (Security Guardian)",
                description = "Zero-trust policy enforcement and tamper-proof SHA-256 audit logger.",
                connectedNodeIdsCsv = "KN-AIISG-SYS, KN-POLICY-ENGINE",
                tagsCsv = "Security, Firewall, Guardian"
            ),
            KnowledgeNodeEntity(
                id = "KN-ACE-CORE",
                nodeType = "PROJECT",
                title = "Autonomous Core Engine (ACE-01)",
                description = "Distributed coroutine task engine powering continuous multi-agent execution loops.",
                connectedNodeIdsCsv = "KN-ASTRA-CMD, KN-CUST-APEX",
                tagsCsv = "Software, HighAvailability"
            ),
            KnowledgeNodeEntity(
                id = "KN-CUST-APEX",
                nodeType = "CUSTOMER",
                title = "Apex Global Technologies",
                description = "Tier 1 Enterprise customer utilizing AIISG Sovereign Autonomous Office.",
                connectedNodeIdsCsv = "KN-ACE-CORE, KN-DECISION-01",
                tagsCsv = "Enterprise, Tier1, Revenue"
            ),
            KnowledgeNodeEntity(
                id = "KN-DECISION-01",
                nodeType = "DECISION",
                title = "CEO Mandate: Zero Fake Progress Policy",
                description = "Strict rule requiring cryptographic evidence before marking any task as complete.",
                connectedNodeIdsCsv = "KN-ASTRA-CMD, KN-AIISG-SYS",
                tagsCsv = "Governance, Quality"
            )
        )
        db.knowledgeDao().insertNodes(nodes)

        // 5. Customer / CRM Records
        val tickets = listOf(
            CustomerTicketEntity(
                id = "CRM-1001",
                customerName = "David Vance (CTO)",
                company = "Apex Global Technologies",
                title = "Request for Dedicated Floor 3 Neural Cluster",
                priority = "HIGH",
                status = "PENDING_APPROVAL",
                stage = "CONTRACT",
                draftResponse = "Hello David, our Agent Creation Lab has prepared the Floor 3 expansion cluster specs with 99.99% uptime guarantee. Formal SLA attached.",
                requiresApproval = true,
                isApproved = false,
                dealValue = 45000.0
            ),
            CustomerTicketEntity(
                id = "CRM-1002",
                customerName = "Samantha Miller (VP Eng)",
                company = "Helios FinTech International",
                title = "API Rate Limit Elevation for Ledger Sync",
                priority = "NORMAL",
                status = "OPEN",
                stage = "SUPPORT",
                draftResponse = "We have provisioned the requested throughput limit of 10,000 req/sec under our verified security token protocol.",
                requiresApproval = false,
                isApproved = true,
                dealValue = 18000.0
            )
        )
        db.customerDao().insertTickets(tickets)

        // 6. Autonomous Procurement Proposals
        val procurements = listOf(
            ProcurementProposalEntity(
                id = "PROC-2026-01",
                itemTitle = "High-Density GPU Cluster (8x H100)",
                category = "CLOUD_COMPUTE",
                vendorA = "AWS EC2 p5.48xlarge ($12,400/mo)",
                vendorB = "Lambda Cloud Dedicated ($8,240/mo)",
                recommendedVendor = "Lambda Cloud Dedicated (Saves 33% at identical SLA)",
                estimatedMonthlyCost = 8240.0,
                justification = "Required for local training and model routing on Floor 3 Neural Lab.",
                status = "PENDING_CEO_APPROVAL"
            ),
            ProcurementProposalEntity(
                id = "PROC-2026-02",
                itemTitle = "Zero-Trust Enterprise Identity Bridge",
                category = "SECURITY_TOOL",
                vendorA = "Okta Workforce Identity ($450/mo)",
                vendorB = "Cloudflare Zero Trust Access ($280/mo)",
                recommendedVendor = "Cloudflare Zero Trust Access",
                estimatedMonthlyCost = 280.0,
                justification = "Enforces hardware security keys (FIDO2) for all external companion endpoints.",
                status = "APPROVED"
            )
        )
        db.procurementDao().insertProposals(procurements)

        // 7. Policy Rules (Company Constitution)
        val policies = listOf(
            PolicyRuleEntity(
                id = "POL-001",
                title = "Pre-Approved Internal Development Autonomy",
                category = "DEVELOPMENT",
                ruleType = "AUTONOMOUS_ALLOWED",
                conditionText = "Software agents may compile code, create sandbox branches, and run test suites without individual prompts.",
                requiresApproval = false,
                enforcementLevel = "STRICT"
            ),
            PolicyRuleEntity(
                id = "POL-002",
                title = "Outward Financial Transfer Gate",
                category = "FINANCE",
                ruleType = "MANDATORY_APPROVAL",
                conditionText = "Any outward payment, contract signing, or balance disbursement > $0.00 mandates explicit CEO confirmation.",
                requiresApproval = true,
                enforcementLevel = "STRICT"
            ),
            PolicyRuleEntity(
                id = "POL-003",
                title = "External Customer Communication Gating",
                category = "CUSTOMER",
                ruleType = "MANDATORY_APPROVAL",
                conditionText = "Commercial contracts and sensitive outbound customer email responses require CEO review prior to dispatch.",
                requiresApproval = true,
                enforcementLevel = "STRICT"
            ),
            PolicyRuleEntity(
                id = "POL-004",
                title = "Production Credential Storage Isolation",
                category = "SECURITY",
                ruleType = "ACCESS_CONTROL",
                conditionText = "API keys, private tokens, and cloud credentials must never appear in raw text; accessible only via encrypted vault.",
                requiresApproval = false,
                enforcementLevel = "STRICT"
            )
        )
        db.policyDao().insertRules(policies)

        // 8. Autonomous SaaS Factory Project
        val saasProject = SaaSFactoryProjectEntity(
            id = "SAAS-01",
            productName = "GeniePulse AI — Autonomous Observability SaaS",
            currentStage = "DEVELOPMENT",
            progressPercent = 68,
            ideaBrief = "Autonomous SaaS product delivering real-time telemetry, dead-man alarms, and auto-healing infrastructure agents for enterprise clients.",
            researchSummary = "Competitor analysis against Datadog and Dynatrace shows high demand for autonomous root-cause remediation rather than passive dashboards.",
            specDocument = "Specification Document v2.4 verified. Microservices: Ingestion (Rust), Processor (Python FastAPI), Dashboard (React/Compose).",
            designTokens = "Material Design 3 tokens finalized. Primary NeonCyan, Dark obsidian surfaces, 8dp grid spacing.",
            repoUrl = "github.com/aiisg-internal/geniepulse-core.git",
            qaStatus = "VERIFIED (182 integration tests passing, zero regressions)",
            secStatus = "AUDITED (SAST Clean, 0 CVEs)",
            releaseNotes = "Beta release ready for closed enterprise preview."
        )
        db.saasFactoryDao().insertProject(saasProject)

        // 9. Model Routing Logs
        val modelLogs = listOf(
            ModelRouteLogEntity(
                taskTitle = "Routine Task Status Ingestion",
                selectedModel = "gemini-flash",
                reason = "Simple parsing objective. Routed to high-speed lower cost model.",
                latencyMs = 120,
                costUsd = 0.0004
            ),
            ModelRouteLogEntity(
                taskTitle = "FastAPI Discrepancy Radar Algorithm Synthesis",
                selectedModel = "gemini-pro",
                reason = "Complex architectural logic & mathematical verification require strong reasoning model.",
                latencyMs = 640,
                costUsd = 0.0042
            ),
            ModelRouteLogEntity(
                taskTitle = "Audit Log SHA-256 Cryptographic Hash Generation",
                selectedModel = "air-gapped-crypto",
                reason = "Zero network egress allowed for tamper-resistant cryptographic seal.",
                latencyMs = 15,
                costUsd = 0.0000
            )
        )
        for (ml in modelLogs) {
            db.modelRouteDao().insertLog(ml)
        }

        // 10. Agent Intercom Chats
        val initialChats = listOf(
            AgentChatMessageEntity(
                id = "CHAT-01",
                senderAgentId = "agent_04_sr_eng",
                senderAgentName = "Kaelen Chen",
                recipientAgentId = "agent_07_qa",
                recipientAgentName = "Veritas QA",
                department = "SOFTWARE",
                message = "Veritas, boundary condition suite on LedgerRadar AST has been pushed. Please execute regression verification."
            ),
            AgentChatMessageEntity(
                id = "CHAT-02",
                senderAgentId = "agent_07_qa",
                senderAgentName = "Veritas QA",
                recipientAgentId = "agent_04_sr_eng",
                recipientAgentName = "Kaelen Chen",
                department = "SOFTWARE",
                message = "Running 14 test suites in isolated sandbox... All assertions PASSED in 0.28s. Evidence hash SHA256-7F3B9C1 generated."
            ),
            AgentChatMessageEntity(
                id = "CHAT-03",
                senderAgentId = "agent_12_sec_guard",
                senderAgentName = "Aegis Sentinel",
                recipientAgentId = "agent_01_commander",
                recipientAgentName = "Astra Commander",
                department = "SECURITY",
                message = "Commander, daily OWASP dependency audit complete. Zero CVEs detected across all 152 microservices."
            ),
            AgentChatMessageEntity(
                id = "CHAT-04",
                senderAgentId = "agent_14_acct_mgr",
                senderAgentName = "Sterling Croft",
                recipientAgentId = "OWNER_CEO",
                recipientAgentName = "Owner / CEO",
                department = "FINANCE",
                message = "CEO, outward cloud cluster disbursement of $8,240.00 is queued. Requires PIN or biometric authorization."
            )
        )
        db.agentChatDao().insertMessages(initialChats)

        // 11. Initial Notifications
        val initialNotifications = listOf(
            NotificationItemEntity(
                id = "NOTIF-01",
                title = "Financial Authorization Required",
                message = "Bill BILL-2026-019 for Dedicated Cloud GPU ($8,240.00) awaits CEO sign-off.",
                category = "FINANCIAL",
                priority = "HIGH",
                isRead = false,
                targetScreen = "ACCOUNTING"
            ),
            NotificationItemEntity(
                id = "NOTIF-02",
                title = "SaaS Factory Milestone Reached",
                message = "GeniePulse AI has completed QA testing. Ready for Security Audit phase.",
                category = "FACTORY",
                priority = "NORMAL",
                isRead = false,
                targetScreen = "SAAS_FACTORY"
            ),
            NotificationItemEntity(
                id = "NOTIF-03",
                title = "Customer Contract Ready",
                message = "Apex Global Technologies contract response draft requires CEO approval.",
                category = "CRM",
                priority = "HIGH",
                isRead = false,
                targetScreen = "CRM"
            )
        )
        db.notificationDao().insertNotifications(initialNotifications)
    }

    suspend fun insertChatMessage(message: AgentChatMessageEntity) {
        db.agentChatDao().insertMessage(message)
    }

    suspend fun markNotificationRead(id: String) {
        db.notificationDao().markAsRead(id)
    }

    suspend fun logAudit(
        agentId: String,
        agentName: String,
        action: String,
        tool: String,
        target: String,
        isSuccess: Boolean,
        severity: String = "INFO",
        error: String? = null
    ) {
        val hash = UUID.randomUUID().toString().substring(0, 8).uppercase()
        val log = AuditLogEntity(
            agentId = agentId,
            agentName = agentName,
            action = action,
            tool = tool,
            target = target,
            result = if (isSuccess) "VERIFIED: Execution completed successfully" else "FAILED: ${error ?: "Execution error"}",
            isSuccess = isSuccess,
            errorMessage = error ?: "",
            severity = severity,
            verificationHash = "SHA256-$hash"
        )
        db.auditLogDao().insertLog(log)
    }

    // Emergency Stop
    suspend fun triggerEmergencyStop() {
        db.agentDao().pauseAllAgents()
        db.taskDao().pauseActiveTasks()
        logAudit(
            agentId = "OWNER_CEO",
            agentName = "Owner / CEO",
            action = "EMERGENCY_STOP",
            tool = "KillSwitch",
            target = "All Autonomous Services & Active Agents",
            isSuccess = true,
            severity = "CRITICAL"
        )
    }

    suspend fun resumeOperations() {
        db.agentDao().updateAgentStatus("agent_01_commander", "WORKING")
        db.agentDao().updateAgentStatus("agent_12_sec_guard", "WORKING")
        logAudit(
            agentId = "OWNER_CEO",
            agentName = "Owner / CEO",
            action = "RESUME_SERVICES",
            tool = "Commander",
            target = "Core Operations",
            isSuccess = true,
            severity = "INFO"
        )
    }

    suspend fun insertTask(task: TaskEntity) {
        db.taskDao().insertTask(task)
        logAudit(task.creatorAgentId, "Task Dispatcher", "DISPATCH_TASK", "TaskQueue", task.id, true)
    }

    suspend fun updateTask(task: TaskEntity) {
        db.taskDao().updateTask(task)
    }

    suspend fun updateAgent(agent: AgentEntity) {
        db.agentDao().updateAgent(agent)
    }

    suspend fun approveAccountingRecord(id: String) {
        db.accountingDao().approveRecord(id)
        logAudit("OWNER_CEO", "Owner / CEO", "FINANCIAL_AUTHORIZATION", "LedgerGate", id, true, "SECURITY")
    }

    suspend fun insertAgent(agent: AgentEntity) {
        db.agentDao().insertAgent(agent)
        logAudit("AGENT_LAB", "Agent Creation Lab", "DEPLOY_AGENT", "IncubationChamber", agent.id, true, "INFO")
    }

    suspend fun updateProposal(proposal: AgentProposalEntity) {
        db.agentProposalDao().updateProposal(proposal)
    }

    suspend fun insertReport(report: ReportEntity) {
        db.reportDao().insertReport(report)
        logAudit("COMMANDER", "Central Commander", "GENERATE_REPORT", "ReportEngine", report.id, true, "INFO")
    }

    suspend fun insertMeeting(meeting: MeetingRecordEntity) {
        db.meetingDao().insertMeeting(meeting)
        logAudit("COMMANDER", "Central Commander", "CONVENE_MEETING", "ConferenceRoom", meeting.id, true, "INFO")
    }

    suspend fun installMarketplaceTemplate(template: MarketplaceTemplateEntity) {
        val newAgentId = "agent_mkt_" + UUID.randomUUID().toString().substring(0, 4).lowercase()
        val newAgent = AgentEntity(
            id = newAgentId,
            name = template.title.split(" ").firstOrNull() ?: "Specialist",
            title = template.role,
            department = template.department,
            floor = template.targetFloor,
            currentRoom = if (template.targetFloor == 3) "EXPANSION_DEPT_A" else "SOFTWARE_DEPT",
            status = "ONLINE",
            skillsCsv = template.description,
            toolsCsv = template.requiredToolsCsv,
            avatarColorHex = "#A855F7",
            version = template.version,
            certificationScore = template.certificationScore
        )
        db.agentDao().insertAgent(newAgent)
        db.marketplaceDao().updateTemplate(template.copy(isInstalled = true))
        logAudit("MARKETPLACE", "Marketplace Engine", "INSTALL_AGENT_TEMPLATE", "AgentRegistry", template.id, true)
    }

    suspend fun approveCustomerTicket(id: String) {
        db.customerDao().approveTicketResponse(id)
        logAudit("OWNER_CEO", "Owner / CEO", "APPROVE_CUSTOMER_COMM", "CRM_Gate", id, true, "SECURITY")
    }

    suspend fun approveProcurement(id: String) {
        db.procurementDao().approveProposal(id)
        logAudit("OWNER_CEO", "Owner / CEO", "APPROVE_PROCUREMENT", "ProcurementGateway", id, true, "SECURITY")
    }

    suspend fun insertSaaSProject(project: SaaSFactoryProjectEntity) {
        db.saasFactoryDao().insertProject(project)
        logAudit("SAAS_FACTORY", "Autonomous SaaS Factory", "CREATE_PRODUCT", "Pipeline", project.productName, true)
    }

    suspend fun advanceSaaSStage(project: SaaSFactoryProjectEntity) {
        val nextStage = when (project.currentStage) {
            "IDEA" -> "RESEARCH"
            "RESEARCH" -> "SPEC"
            "SPEC" -> "DESIGN"
            "DESIGN" -> "DEVELOPMENT"
            "DEVELOPMENT" -> "QA"
            "QA" -> "SECURITY"
            "SECURITY" -> "DEPLOYED"
            "DEPLOYED" -> "DOCUMENTED"
            else -> "DOCUMENTED"
        }
        val progress = when (nextStage) {
            "IDEA" -> 10
            "RESEARCH" -> 25
            "SPEC" -> 40
            "DESIGN" -> 55
            "DEVELOPMENT" -> 70
            "QA" -> 85
            "SECURITY" -> 92
            "DEPLOYED" -> 98
            else -> 100
        }
        val updated = project.copy(currentStage = nextStage, progressPercent = progress)
        db.saasFactoryDao().updateProject(updated)
        logAudit("SAAS_FACTORY", "Autonomous SaaS Factory", "ADVANCE_STAGE", "ProductPipeline", "${project.productName} -> $nextStage", true)
    }

    suspend fun logModelRoute(taskTitle: String, model: String, reason: String, latencyMs: Long, costUsd: Double) {
        val entry = ModelRouteLogEntity(
            taskTitle = taskTitle,
            selectedModel = model,
            reason = reason,
            latencyMs = latencyMs,
            costUsd = costUsd
        )
        db.modelRouteDao().insertLog(entry)
    }

    suspend fun updateCustomerTicketFollowUp(id: String, dateText: String) {
        val ticket = db.customerDao().getAllTicketsFlow()
        // Log follow-up scheduled
        logAudit("CUSTOMER_DEPT", "CRM Manager", "SCHEDULE_FOLLOW_UP", "CalendarSync", "Ticket $id: Follow-up set for $dateText", true)
    }

    suspend fun updatePlaybookRevision(playbook: PlaybookEntity) {
        db.playbookDao().updatePlaybook(playbook)
        logAudit("LEARNING_ENGINE", "Autonomous Playbook Engine", "VERSION_UPDATE", "PlaybookRegistry", "${playbook.title} updated to ${playbook.version}", true)
    }

    suspend fun recordHumanFeedback(title: String, feedbackText: String, approved: Boolean) {
        logAudit(
            agentId = "OWNER_CEO",
            agentName = "Owner / CEO",
            action = if (approved) "HUMAN_FEEDBACK_APPROVED" else "HUMAN_FEEDBACK_PROPOSED",
            tool = "HumanFeedbackLoop",
            target = title,
            isSuccess = true,
            severity = "INFO"
        )
    }
}
