package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        AgentEntity::class,
        TaskEntity::class,
        ProjectEntity::class,
        AccountingRecordEntity::class,
        AuditLogEntity::class,
        MeetingRecordEntity::class,
        ReportEntity::class,
        AgentProposalEntity::class,
        MarketplaceTemplateEntity::class,
        PlaybookEntity::class,
        LessonLearnedEntity::class,
        KnowledgeNodeEntity::class,
        CustomerTicketEntity::class,
        ProcurementProposalEntity::class,
        PolicyRuleEntity::class,
        SaaSFactoryProjectEntity::class,
        ModelRouteLogEntity::class,
        AgentChatMessageEntity::class,
        NotificationItemEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AIISGDatabase : RoomDatabase() {
    abstract fun agentDao(): AgentDao
    abstract fun taskDao(): TaskDao
    abstract fun projectDao(): ProjectDao
    abstract fun accountingDao(): AccountingDao
    abstract fun auditLogDao(): AuditLogDao
    abstract fun meetingDao(): MeetingDao
    abstract fun reportDao(): ReportDao
    abstract fun agentProposalDao(): AgentProposalDao
    abstract fun marketplaceDao(): MarketplaceDao
    abstract fun playbookDao(): PlaybookDao
    abstract fun lessonLearnedDao(): LessonLearnedDao
    abstract fun knowledgeDao(): KnowledgeDao
    abstract fun customerDao(): CustomerDao
    abstract fun procurementDao(): ProcurementDao
    abstract fun policyDao(): PolicyDao
    abstract fun saasFactoryDao(): SaaSFactoryDao
    abstract fun modelRouteDao(): ModelRouteDao
    abstract fun agentChatDao(): AgentChatDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AIISGDatabase? = null

        fun getDatabase(context: Context): AIISGDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AIISGDatabase::class.java,
                    "aiisg_autonomous_office.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
