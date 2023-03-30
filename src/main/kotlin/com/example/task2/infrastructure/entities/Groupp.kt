package com.example.task2.infrastructure.entities

import jakarta.persistence.*

@Entity
data class Groupp(
    @Id @GeneratedValue val id: Int?,
    var name: String,
    var description: String?,
    @ElementCollection(fetch = FetchType.EAGER)
    val participants: MutableList<Int>
)



