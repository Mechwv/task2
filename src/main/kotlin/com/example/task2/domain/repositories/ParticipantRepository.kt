package com.example.task2.domain.repositories

import com.example.task2.infrastructure.entities.Participant
import org.springframework.data.jpa.repository.JpaRepository

interface ParticipantRepository: JpaRepository<Participant, Int>