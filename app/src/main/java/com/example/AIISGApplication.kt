package com.example

import android.app.Application
import com.example.data.local.AIISGDatabase
import com.example.data.repository.AIISGRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AIISGApplication : Application() {
    val database by lazy { AIISGDatabase.getDatabase(this) }
    val repository by lazy { AIISGRepository(database) }

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            repository.initializeSeedsIfEmpty()
        }
    }
}
