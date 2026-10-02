package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.sp
import com.example.data.local.AgentEntity
import com.example.domain.OfficeDirectory
import com.example.domain.OfficeRoom

@OptIn(ExperimentalTextApi::class)
@Composable
fun OfficeCanvas(
    floor: Int,
    agents: List<AgentEntity>,
    selectedRoom: OfficeRoom?,
    selectedAgent: AgentEntity?,
    onRoomClick: (OfficeRoom) -> Unit,
    onAgentClick: (AgentEntity) -> Unit,
    isIsometric3D: Boolean = false,
    modifier: Modifier = Modifier
) {
    val rooms = OfficeDirectory.getRoomsForFloor(floor)
    val textMeasurer = rememberTextMeasurer()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(floor, rooms, agents, isIsometric3D) {
                detectTapGestures { tapOffset ->
                    val w = size.width.toFloat()
                    val h = size.height.toFloat()
                    val normX = tapOffset.x / w
                    val normY = tapOffset.y / h

                    // Check agent clicks first (touch radius ~36dp normalized)
                    val clickedAgent = agents.find { agent ->
                        val dx = (agent.posX * w) - tapOffset.x
                        val dy = (agent.posY * h) - tapOffset.y
                        kotlin.math.sqrt(dx * dx + dy * dy) < 52f
                    }
                    if (clickedAgent != null) {
                        onAgentClick(clickedAgent)
                        return@detectTapGestures
                    }

                    // Check room clicks
                    val clickedRoom = rooms.find { r ->
                        normX >= r.bounds.left && normX <= r.bounds.right &&
                                normY >= r.bounds.top && normY <= r.bounds.bottom
                    }
                    if (clickedRoom != null) {
                        onRoomClick(clickedRoom)
                    }
                }
            }
    ) {
        val w = size.width
        val h = size.height

        // 1. Draw Floor Grid (Standard or 3D Isometric Hologram)
        if (isIsometric3D) {
            drawIsometricCyberGrid(w, h, pulseAlpha)
        } else {
            drawFloorGrid(w, h)
        }

        // 2. Draw Rooms
        for (room in rooms) {
            val isSelected = room.id == selectedRoom?.id
            val left = room.bounds.left * w
            val top = room.bounds.top * h
            val rw = (room.bounds.right - room.bounds.left) * w
            val rh = (room.bounds.bottom - room.bounds.top) * h

            val roomColor = try {
                Color(android.graphics.Color.parseColor(room.colorHex))
            } catch (e: Exception) {
                Color(0xFF00E5FF)
            }

            if (isIsometric3D) {
                // 3D Isometric Extrusion Wall Base Drop
                val depth = 14f
                drawRoundRect(
                    color = Color(0xFF060B14),
                    topLeft = Offset(left + 6f, top + depth),
                    size = Size(rw, rh),
                    cornerRadius = CornerRadius(16f, 16f)
                )
                // Side wall depth fill
                drawRoundRect(
                    color = roomColor.copy(alpha = 0.25f),
                    topLeft = Offset(left, top + depth),
                    size = Size(rw, rh),
                    cornerRadius = CornerRadius(16f, 16f)
                )
            }

            // Room floor fill
            drawRoundRect(
                color = roomColor.copy(alpha = if (isSelected) 0.22f else if (isIsometric3D) 0.12f else 0.08f),
                topLeft = Offset(left, top),
                size = Size(rw, rh),
                cornerRadius = CornerRadius(16f, 16f)
            )

            // Room wall border
            drawRoundRect(
                color = if (isSelected) roomColor else roomColor.copy(alpha = if (isIsometric3D) 0.65f else 0.45f),
                topLeft = Offset(left, top),
                size = Size(rw, rh),
                cornerRadius = CornerRadius(16f, 16f),
                style = Stroke(width = if (isSelected) 3.5f else 1.8f)
            )

            // Interior workstation furniture representation
            drawRoomFurniture(room, left, top, rw, rh, roomColor)

            // Room header tag
            val roomTitle = if (isIsometric3D) "[3D] ${room.code}: ${room.name}" else "${room.code}: ${room.name}"
            val titleLayout = textMeasurer.measure(
                text = AnnotatedString(roomTitle),
                style = TextStyle(
                    color = roomColor,
                    fontSize = 11.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            )
            drawRect(
                color = Color(0xFF0A101D).copy(alpha = 0.90f),
                topLeft = Offset(left + 8f, top + 6f),
                size = Size(titleLayout.size.width + 12f, titleLayout.size.height + 6f)
            )
            drawText(
                textMeasurer = textMeasurer,
                text = roomTitle,
                topLeft = Offset(left + 14f, top + 9f),
                style = TextStyle(color = roomColor, fontSize = 11.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            )
        }

        // 3. Draw Agents on this floor
        val floorAgents = agents.filter { it.floor == floor }
        for (agent in floorAgents) {
            val ax = agent.posX * w
            val ay = agent.posY * h
            val isAgentSelected = agent.id == selectedAgent?.id

            val agentColor = try {
                Color(android.graphics.Color.parseColor(agent.avatarColorHex))
            } catch (e: Exception) {
                Color(0xFF00E5FF)
            }

            val statusColor = when (agent.status) {
                "WORKING" -> Color(0xFF10B981)
                "THINKING" -> Color(0xFF38BDF8)
                "TESTING" -> Color(0xFFA855F7)
                "WAITING" -> Color(0xFFF59E0B)
                "ERROR" -> Color(0xFFEF4444)
                "PAUSED" -> Color(0xFF6B7280)
                else -> Color(0xFF10B981)
            }

            if (isIsometric3D) {
                // 3D Shadow Ellipse
                drawOval(
                    color = Color.Black.copy(alpha = 0.5f),
                    topLeft = Offset(ax - 18f, ay + 12f),
                    size = Size(36f, 14f)
                )
                // Holographic vertical anchor line
                drawLine(
                    color = statusColor.copy(alpha = 0.4f),
                    start = Offset(ax, ay + 14f),
                    end = Offset(ax, ay - 24f),
                    strokeWidth = 1f
                )
            }

            // Outer pulse halo if working/testing
            if (agent.status == "WORKING" || agent.status == "TESTING") {
                drawCircle(
                    color = statusColor.copy(alpha = pulseAlpha * 0.4f),
                    radius = 24f,
                    center = Offset(ax, ay)
                )
            }

            // Selection indicator
            if (isAgentSelected) {
                drawCircle(
                    color = Color.White,
                    radius = 26f,
                    center = Offset(ax, ay),
                    style = Stroke(width = 2.5f)
                )
            }

            // Agent circular avatar core
            drawCircle(
                color = Color(0xFF0A111F),
                radius = 16f,
                center = Offset(ax, ay)
            )
            drawCircle(
                color = agentColor,
                radius = 16f,
                center = Offset(ax, ay),
                style = Stroke(width = 2.2f)
            )

            // Small status dot
            drawCircle(
                color = statusColor,
                radius = 4.5f,
                center = Offset(ax + 11f, ay - 11f)
            )

            // Agent initial
            val initial = agent.name.firstOrNull()?.toString() ?: "A"
            drawText(
                textMeasurer = textMeasurer,
                text = initial,
                topLeft = Offset(ax - 5f, ay - 9f),
                style = TextStyle(color = Color.White, fontSize = 12.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Black)
            )

            // Name pill below agent
            val shortName = agent.name.split(" ").firstOrNull() ?: agent.name
            val nameLayout = textMeasurer.measure(
                text = AnnotatedString(shortName),
                style = TextStyle(color = Color(0xFFE2E8F0), fontSize = 9.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
            )
            drawRect(
                color = Color(0xCC050A14),
                topLeft = Offset(ax - nameLayout.size.width / 2f - 4f, ay + 19f),
                size = Size(nameLayout.size.width + 8f, nameLayout.size.height + 2f)
            )
            drawText(
                textMeasurer = textMeasurer,
                text = shortName,
                topLeft = Offset(ax - nameLayout.size.width / 2f, ay + 20f),
                style = TextStyle(color = Color(0xFFE2E8F0), fontSize = 9.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
            )
        }
    }
}

private fun DrawScope.drawFloorGrid(w: Float, h: Float) {
    val step = 36f
    var x = 0f
    while (x < w) {
        drawLine(
            color = Color(0xFF1E293B).copy(alpha = 0.25f),
            start = Offset(x, 0f),
            end = Offset(x, h),
            strokeWidth = 0.8f
        )
        x += step
    }
    var y = 0f
    while (y < h) {
        drawLine(
            color = Color(0xFF1E293B).copy(alpha = 0.25f),
            start = Offset(0f, y),
            end = Offset(w, y),
            strokeWidth = 0.8f
        )
        y += step
    }
}

private fun DrawScope.drawIsometricCyberGrid(w: Float, h: Float, pulseAlpha: Float) {
    val step = 42f
    // Diagonal 30-degree isometric lines
    var x = -w
    while (x < w * 2) {
        drawLine(
            color = Color(0xFF00E5FF).copy(alpha = 0.12f * pulseAlpha),
            start = Offset(x, 0f),
            end = Offset(x + h * 0.7f, h),
            strokeWidth = 0.9f
        )
        drawLine(
            color = Color(0xFF00E5FF).copy(alpha = 0.12f * pulseAlpha),
            start = Offset(x, 0f),
            end = Offset(x - h * 0.7f, h),
            strokeWidth = 0.9f
        )
        x += step
    }
}

private fun DrawScope.drawRoomFurniture(room: OfficeRoom, left: Float, top: Float, rw: Float, rh: Float, color: Color) {
    when (room.id) {
        "MEETING_ROOM" -> {
            val cx = left + rw / 2f
            val cy = top + rh / 2f + 10f
            drawCircle(
                color = color.copy(alpha = 0.2f),
                radius = 32f,
                center = Offset(cx, cy)
            )
            drawCircle(
                color = color.copy(alpha = 0.6f),
                radius = 32f,
                center = Offset(cx, cy),
                style = Stroke(width = 1.5f)
            )
        }
        "CEO_OFFICE", "COMMANDER_ROOM" -> {
            drawRoundRect(
                color = color.copy(alpha = 0.2f),
                topLeft = Offset(left + rw * 0.25f, top + rh * 0.45f),
                size = Size(rw * 0.5f, rh * 0.25f),
                cornerRadius = CornerRadius(8f, 8f)
            )
        }
        else -> {
            drawRoundRect(
                color = color.copy(alpha = 0.15f),
                topLeft = Offset(left + rw * 0.15f, top + rh * 0.45f),
                size = Size(rw * 0.32f, rh * 0.22f),
                cornerRadius = CornerRadius(6f, 6f)
            )
            drawRoundRect(
                color = color.copy(alpha = 0.15f),
                topLeft = Offset(left + rw * 0.55f, top + rh * 0.45f),
                size = Size(rw * 0.32f, rh * 0.22f),
                cornerRadius = CornerRadius(6f, 6f)
            )
        }
    }
}
