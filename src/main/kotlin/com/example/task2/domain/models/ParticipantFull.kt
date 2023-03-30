package com.example.task2.domain.models

data class ParticipantFull(
    val id: Int,
    val name: String,
    val wish: String?,
    val recipient: ParticipantOutput
)