package com.example.task2.domain.mappers

import com.example.task2.domain.models.GroupOutput
import com.example.task2.infrastructure.entities.Groupp

fun Groupp.toGroupOutput() = GroupOutput(
    id = this.id!!,
    name = this.name,
    description = this.description
)