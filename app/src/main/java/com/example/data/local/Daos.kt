package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AgentDao {
    @Query("SELECT * FROM agents ORDER BY floor ASC, id ASC")
    fun getAllAgentsFlow(): Flow<List<AgentEntity>>

    @Query("SELECT * FROM agents WHERE floor = :floor")
    fun getAgentsByFloorFlow(floor: Int): Flow<List<AgentEntity>>

    @Query("SELECT * FROM agents WHERE id = :id LIMIT 1")
    suspend fun getAgentById(id: String): AgentEntity?

    @Query("SELECT COUNT(*) FROM agents")
    suspend fun getAgentCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgents(agents: List<AgentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgent(agent: AgentEntity)

    @Update
    suspend fun updateAgent(agent: AgentEntity)

    @Query("UPDATE agents SET status = :status WHERE id = :id")
    suspend fun updateAgentStatus(id: String, status: String)

    @Query("UPDATE agents SET currentRoom = :room, posX = :x, posY = :y, targetX = :x, targetY = :y WHERE id = :id")
    suspend fun updateAgentLocation(id: String, room: String, x: Float, y: Float)

    @Query("UPDATE agents SET status = 'PAUSED'")
    suspend fun pauseAllAgents()
}

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY createdAtEpochMs DESC")
    fun getAllTasksFlow(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE status = 'RUNNING' OR status = 'PLANNED' ORDER BY priority DESC")
    fun getActiveTasksFlow(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    suspend fun getTaskById(id: String): TaskEntity?

    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun getTaskCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("UPDATE tasks SET status = 'PAUSED' WHERE status = 'RUNNING' OR status = 'PLANNED'")
    suspend fun pauseActiveTasks()
}

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY lastUpdatedEpochMs DESC")
    fun getAllProjectsFlow(): Flow<List<ProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<ProjectEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Update
    suspend fun updateProject(project: ProjectEntity)
}

@Dao
interface AccountingDao {
    @Query("SELECT * FROM accounting_records ORDER BY createdEpochMs DESC")
    fun getAllRecordsFlow(): Flow<List<AccountingRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecords(records: List<AccountingRecordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: AccountingRecordEntity)

    @Update
    suspend fun updateRecord(record: AccountingRecordEntity)

    @Query("UPDATE accounting_records SET status = 'APPROVED' WHERE id = :id")
    suspend fun approveRecord(id: String)
}

@Dao
interface AuditLogDao {
    @Query("SELECT * FROM audit_logs ORDER BY timestampEpochMs DESC LIMIT 200")
    fun getRecentLogsFlow(): Flow<List<AuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: AuditLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogs(logs: List<AuditLogEntity>)
}

@Dao
interface MeetingDao {
    @Query("SELECT * FROM meeting_records ORDER BY timestampEpochMs DESC")
    fun getAllMeetingsFlow(): Flow<List<MeetingRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeeting(meeting: MeetingRecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeetings(meetings: List<MeetingRecordEntity>)

    @Update
    suspend fun updateMeeting(meeting: MeetingRecordEntity)
}

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY timestampEpochMs DESC")
    fun getAllReportsFlow(): Flow<List<ReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReports(reports: List<ReportEntity>)
}

@Dao
interface AgentProposalDao {
    @Query("SELECT * FROM agent_proposals ORDER BY id DESC")
    fun getAllProposalsFlow(): Flow<List<AgentProposalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProposal(proposal: AgentProposalEntity)

    @Update
    suspend fun updateProposal(proposal: AgentProposalEntity)
}

@Dao
interface MarketplaceDao {
    @Query("SELECT * FROM marketplace_templates ORDER BY certificationScore DESC")
    fun getAllTemplatesFlow(): Flow<List<MarketplaceTemplateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplates(templates: List<MarketplaceTemplateEntity>)

    @Update
    suspend fun updateTemplate(template: MarketplaceTemplateEntity)
}

@Dao
interface PlaybookDao {
    @Query("SELECT * FROM playbooks ORDER BY successRatePercent DESC")
    fun getAllPlaybooksFlow(): Flow<List<PlaybookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaybooks(playbooks: List<PlaybookEntity>)

    @Update
    suspend fun updatePlaybook(playbook: PlaybookEntity)
}

@Dao
interface LessonLearnedDao {
    @Query("SELECT * FROM lessons_learned ORDER BY recordedEpochMs DESC")
    fun getAllLessonsFlow(): Flow<List<LessonLearnedEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonLearnedEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: LessonLearnedEntity)
}

@Dao
interface KnowledgeDao {
    @Query("SELECT * FROM knowledge_nodes ORDER BY nodeType ASC")
    fun getAllNodesFlow(): Flow<List<KnowledgeNodeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNodes(nodes: List<KnowledgeNodeEntity>)
}

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customer_tickets ORDER BY priority DESC")
    fun getAllTicketsFlow(): Flow<List<CustomerTicketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTickets(tickets: List<CustomerTicketEntity>)

    @Update
    suspend fun updateTicket(ticket: CustomerTicketEntity)

    @Query("UPDATE customer_tickets SET isApproved = 1, status = 'RESOLVED' WHERE id = :id")
    suspend fun approveTicketResponse(id: String)
}

@Dao
interface ProcurementDao {
    @Query("SELECT * FROM procurement_proposals ORDER BY estimatedMonthlyCost DESC")
    fun getAllProposalsFlow(): Flow<List<ProcurementProposalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProposals(proposals: List<ProcurementProposalEntity>)

    @Update
    suspend fun updateProposal(proposal: ProcurementProposalEntity)

    @Query("UPDATE procurement_proposals SET status = 'APPROVED' WHERE id = :id")
    suspend fun approveProposal(id: String)
}

@Dao
interface PolicyDao {
    @Query("SELECT * FROM policy_rules ORDER BY ruleType ASC")
    fun getAllRulesFlow(): Flow<List<PolicyRuleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRules(rules: List<PolicyRuleEntity>)
}

@Dao
interface SaaSFactoryDao {
    @Query("SELECT * FROM saas_factory_projects ORDER BY progressPercent DESC")
    fun getAllProjectsFlow(): Flow<List<SaaSFactoryProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<SaaSFactoryProjectEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: SaaSFactoryProjectEntity)

    @Update
    suspend fun updateProject(project: SaaSFactoryProjectEntity)
}

@Dao
interface ModelRouteDao {
    @Query("SELECT * FROM model_route_logs ORDER BY timestampEpochMs DESC LIMIT 50")
    fun getRecentLogsFlow(): Flow<List<ModelRouteLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ModelRouteLogEntity)
}

@Dao
interface AgentChatDao {
    @Query("SELECT * FROM agent_chats ORDER BY timestampEpochMs ASC LIMIT 100")
    fun getAllMessagesFlow(): Flow<List<AgentChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<AgentChatMessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: AgentChatMessageEntity)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestampEpochMs DESC")
    fun getAllNotificationsFlow(): Flow<List<NotificationItemEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationItemEntity>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)
}
