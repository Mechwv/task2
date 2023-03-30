package com.example.task2.infrastructure.services

import com.example.task2.domain.models.*
import com.example.task2.domain.repositories.GroupRepository
import com.example.task2.domain.repositories.ParticipantRepository
import com.example.task2.domain.services.Service
import com.example.task2.domain.utils.Utils
import com.example.task2.infrastructure.entities.Groupp
import com.example.task2.infrastructure.entities.Participant
import org.springframework.data.repository.findByIdOrNull

@org.springframework.stereotype.Service
class ServiceImpl(
    private val groupRepository: GroupRepository,
    private val participantRepository: ParticipantRepository,
): Service {
    override fun createGroup(groupInput: GroupInput): Int = groupRepository.save(
        Groupp(
            id = null,
            name = groupInput.name,
            description = groupInput.description,
            participants = mutableListOf()
        )
    ).id!!

    override fun getGroups(): MutableList<Groupp> = groupRepository.findAll()

    override fun getGroupById(id: Int) = groupRepository.findByIdOrNull(id)?.let { gp ->
        val participants = mutableListOf<Participant>()

        for (e in gp.participants) {
            participants.add(participantRepository.findById(e).get())
        }

        val gpFull = GroupFull(
            id = gp.id!!,
            name = gp.name,
            description = gp.description,
            participants = participants
        )
        gpFull
    }

    override fun updateGroupById(id: Int, groupInput: GroupInput) = groupRepository.findByIdOrNull(id)?.let { gp ->
        gp.name = groupInput.name
        gp.description = groupInput.description
        groupRepository.save(gp)
    }

    override fun deleteGroupById(id: Int) = groupRepository.findByIdOrNull(id)?.let { gp ->
        groupRepository.deleteById(id)
    }

    override fun addParticipantToGroupById(id: Int, participantInput: ParticipantInput) =
        groupRepository.findByIdOrNull(id)?.let { gp ->
            val partId = participantRepository.save(Participant(
                id = null,
                name = participantInput.name,
                wish = participantInput.wish,
                recipient = null
            )).id!!
            gp.participants.add(partId)
            groupRepository.save(gp)
            partId
        }

    override fun deletePartFromGroupById(groupId: Int, participantId: Int) = groupRepository.findByIdOrNull(groupId)?.let { gp ->
        participantRepository.deleteById(participantId)
        gp.participants.remove(participantId)
        groupRepository.save(gp)
    }

    override fun toss(id: Int) = groupRepository.findByIdOrNull(id)?.let { gp ->
        if (Utils.tossCheck(gp.participants)) {
            val participants = mutableListOf<Participant>()
            val partFull = mutableListOf<ParticipantFull>()

            for (e in gp.participants) {
                participants.add(participantRepository.findById(e).get())
            }

            for (i in 0 until participants.size - 1) {
                println(i + 1)
                val tempPart = participants[i + 1]
                partFull.add(ParticipantFull(
                    id = participants[i].id!!,
                    name = participants[i].name,
                    wish = participants[i].wish,
                    recipient = ParticipantOutput(
                        id = tempPart.id!!,
                        name = tempPart.name,
                        wish = tempPart.wish,
                    )
                ))
                participants[i].recipient = tempPart.id
                participantRepository.save(participants[i])
            }
            val tempPart = participants[participants.indices.last]
            partFull.add(
                ParticipantFull(
                    id = tempPart.id!!,
                    name = tempPart.name,
                    wish = tempPart.wish,
                    recipient = ParticipantOutput(
                        id = participants[0].id!!,
                        name = participants[0].name,
                        wish = participants[0].wish,
                    )
                )
            )
            tempPart.recipient = participants[0].id
            participantRepository.save(tempPart)
            return@let TossSuccess(partFull)
        }
        return@let TossError()
    }
    override fun getRecipient(groupId: Int, participantId: Int) = groupRepository.findByIdOrNull(groupId)?.let { gp ->
        val partRecipientId = participantRepository.findById(participantId).get().recipient
        if (partRecipientId!= null) {
            val partRecipient = participantRepository.findById(partRecipientId).get()
            return@let RecepientFindSuccess(ParticipantOutput(
                id = partRecipient.id!!,
                name = partRecipient.name,
                wish = partRecipient.wish
            ))
        } else {
            return@let RecepientFindError()
        }
    }

}