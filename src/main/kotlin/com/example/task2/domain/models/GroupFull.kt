package com.example.task2.domain.models

import com.example.task2.infrastructure.entities.Participant

data class GroupFull(
    val id: Int,
    val name: String,
    val description: String?,
    val participants: MutableList<Participant>
)