package com.example.task2.infrastructure.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class Participant(
    @Id @GeneratedValue val id: Int?,
    val name: String,
    val wish: String?,
    var recipient: Int?
)
