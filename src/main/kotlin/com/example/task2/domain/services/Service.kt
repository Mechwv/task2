package com.example.task2.domain.services

import com.example.task2.domain.models.GroupFull
import com.example.task2.domain.models.GroupInput
import com.example.task2.domain.models.ParticipantInput
import com.example.task2.domain.models.TossResult
import com.example.task2.infrastructure.entities.Groupp

interface Service {
    fun createGroup(groupInput: GroupInput): Int
    fun getGroups(): MutableList<Groupp>
    fun getGroupById(id: Int): GroupFull?
    fun updateGroupById(id: Int, groupInput: GroupInput): Groupp?
    fun deleteGroupById(id: Int): Unit?
    fun addParticipantToGroupById(id: Int, participantInput: ParticipantInput): Int?
    fun deletePartFromGroupById(groupId: Int, participantId: Int): Groupp?
    fun toss(id: Int): TossResult?
    fun getRecipient(groupId: Int, participantId: Int): Any?
}