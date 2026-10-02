package com.example.domain

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

enum class OfficeFloor(val level: Int, val title: String, val subtitle: String) {
    FLOOR_1(1, "Floor 1: Operational Core", "Executive, Software Engineering & Research Hub"),
    FLOOR_2(2, "Floor 2: Security & Creation", "Security Ops, Accounting Suite, Creative & Agent Lab"),
    FLOOR_3(3, "Floor 3: Advanced Expansion", "Unlocked via Agent Creation Lab Scale-out")
}

data class OfficeRoom(
    val id: String,
    val name: String,
    val code: String,
    val floor: Int,
    val bounds: Rect, // Normalized 0..1 coordinates (left, top, right, bottom)
    val colorHex: String,
    val primaryDept: String,
    val description: String
) {
    val center: Offset
        get() = Offset((bounds.left + bounds.right) / 2f, (bounds.top + bounds.bottom) / 2f)
}

object OfficeDirectory {
    val rooms = listOf(
        // FLOOR 1
        OfficeRoom(
            id = "CEO_OFFICE",
            name = "CEO Executive Suite",
            code = "CEO-01",
            floor = 1,
            bounds = Rect(0.04f, 0.05f, 0.46f, 0.32f),
            colorHex = "#FFB703",
            primaryDept = "EXECUTIVE",
            description = "Owner / CEO Command Station with live holographic tactical controls."
        ),
        OfficeRoom(
            id = "COMMANDER_ROOM",
            name = "Central Commander Center",
            code = "CMD-01",
            floor = 1,
            bounds = Rect(0.54f, 0.05f, 0.96f, 0.32f),
            colorHex = "#00E5FF",
            primaryDept = "EXECUTIVE",
            description = "Autonomous company orchestration core and task dispatch center."
        ),
        OfficeRoom(
            id = "SOFTWARE_DEPT",
            name = "Software Engineering Lab",
            code = "ENG-01",
            floor = 1,
            bounds = Rect(0.04f, 0.38f, 0.46f, 0.72f),
            colorHex = "#3B82F6",
            primaryDept = "SOFTWARE",
            description = "Python & Web developer workstations with continuous build terminals."
        ),
        OfficeRoom(
            id = "RESEARCH_LAB",
            name = "Research & Data Analytics",
            code = "RES-01",
            floor = 1,
            bounds = Rect(0.54f, 0.38f, 0.96f, 0.72f),
            colorHex = "#8B5CF6",
            primaryDept = "RESEARCH",
            description = "Market intelligence corpus, web scraping nodes, and predictive modeling."
        ),
        OfficeRoom(
            id = "RECEPTION",
            name = "Reception & Common Lounge",
            code = "REC-01",
            floor = 1,
            bounds = Rect(0.20f, 0.78f, 0.80f, 0.95f),
            colorHex = "#64748B",
            primaryDept = "OPERATIONS",
            description = "Visitor portal, coffee lounge, and agent rest area."
        ),

        // FLOOR 2
        OfficeRoom(
            id = "MEETING_ROOM",
            name = "All-Hands Conference Hall",
            code = "CONF-02",
            floor = 2,
            bounds = Rect(0.20f, 0.05f, 0.80f, 0.32f),
            colorHex = "#10B981",
            primaryDept = "EXECUTIVE",
            description = "Weekly company-wide meeting arena with round table & live agenda screen."
        ),
        OfficeRoom(
            id = "SECURITY_CENTER",
            name = "Security Operations Center",
            code = "SOC-02",
            floor = 2,
            bounds = Rect(0.04f, 0.38f, 0.46f, 0.65f),
            colorHex = "#EF4444",
            primaryDept = "SECURITY",
            description = "Guardian watchtower, audit verification, and kill-switch relays."
        ),
        OfficeRoom(
            id = "ACCOUNTING_SUITE",
            name = "Accounting & Finance Suite",
            code = "FIN-02",
            floor = 2,
            bounds = Rect(0.54f, 0.38f, 0.96f, 0.65f),
            colorHex = "#F59E0B",
            primaryDept = "FINANCE",
            description = "Invoicing ledger, banking reconciliation, and human authorization gateway."
        ),
        OfficeRoom(
            id = "CREATIVE_STUDIO",
            name = "Creative & UI/UX Studio",
            code = "CRT-02",
            floor = 2,
            bounds = Rect(0.04f, 0.70f, 0.46f, 0.95f),
            colorHex = "#EC4899",
            primaryDept = "CREATIVE",
            description = "Material Design token systems, technical writing, and asset synthesis."
        ),
        OfficeRoom(
            id = "AGENT_LAB",
            name = "Agent Creation Lab",
            code = "LAB-02",
            floor = 2,
            bounds = Rect(0.54f, 0.70f, 0.96f, 0.95f),
            colorHex = "#06B6D4",
            primaryDept = "LAB",
            description = "Incubation chambers where agents propose and synthesize new specialist agents."
        ),

        // FLOOR 3 (Expansion)
        OfficeRoom(
            id = "EXPANSION_DEPT_A",
            name = "DevOps & Cloud Cluster",
            code = "OPS-03",
            floor = 3,
            bounds = Rect(0.04f, 0.10f, 0.46f, 0.48f),
            colorHex = "#6366F1",
            primaryDept = "SOFTWARE",
            description = "High density container cluster orchestration and multi-cloud bridges."
        ),
        OfficeRoom(
            id = "EXPANSION_DEPT_B",
            name = "Deep Learning Neural Lab",
            code = "AI-03",
            floor = 3,
            bounds = Rect(0.54f, 0.10f, 0.96f, 0.48f),
            colorHex = "#14B8A6",
            primaryDept = "RESEARCH",
            description = "Local model fine-tuning and autonomous tool synthesis."
        ),
        OfficeRoom(
            id = "EXPANSION_DEPT_C",
            name = "Global Expansion War Room",
            code = "WAR-03",
            floor = 3,
            bounds = Rect(0.15f, 0.55f, 0.85f, 0.90f),
            colorHex = "#8B5CF6",
            primaryDept = "EXECUTIVE",
            description = "International expansion, legal compliance, and multi-tenant scaling."
        )
    )

    fun getRoomsForFloor(floor: Int): List<OfficeRoom> = rooms.filter { it.floor == floor }

    fun getRoom(roomId: String): OfficeRoom? = rooms.find { it.id == roomId }
}
