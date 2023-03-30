package com.example.task2.application.controllers

import com.example.task2.domain.mappers.toGroupOutput
import com.example.task2.domain.models.*
import com.example.task2.infrastructure.entities.Groupp
import com.example.task2.infrastructure.services.ServiceImpl
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/group")
class Controller(
    val service: ServiceImpl
) {
    @PostMapping
    fun addGroup(@RequestBody body: GroupInput) = service.createGroup(body)

    @GetMapping
    fun getGroups() = service.getGroups().map { it.toGroupOutput() }

    @Operation(summary = "getGroup",
        responses = [
            ApiResponse(description = "Successful Operation", responseCode = "200", content = [Content(mediaType = "application/json", schema = Schema(implementation = GroupFull::class))]),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(hidden = true))]),
        ]
    )
    @GetMapping("/{id}")
    fun getGroup(@PathVariable id: Int) = service.getGroupById(id)?.let {
        ResponseEntity.ok(it)
    } ?: ResponseEntity("Группа не найдена", HttpStatus.NOT_FOUND)

    @PutMapping("/{id}")
    fun updateGroup(@PathVariable id: Int, @RequestBody groupInput: GroupInput) = service.updateGroupById(id, groupInput)?.let {
        ResponseEntity.ok().build()
    } ?: ResponseEntity("Группа не найдена", HttpStatus.NOT_FOUND)

    @DeleteMapping("/{id}")
    fun deleteGroup(@PathVariable id: Int) = service.deleteGroupById(id)?.let {
        ResponseEntity.ok().build()
    } ?: ResponseEntity("Группа не найдена", HttpStatus.NOT_FOUND)

    @Operation(summary = "addParticipantToGroup",
        responses = [
            ApiResponse(description = "Successful Operation", responseCode = "200", content = [Content(mediaType = "application/json", schema = Schema(implementation = Int::class))]),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(hidden = true))]),
        ]
    )
    @PostMapping("/{id}/participant")
    fun addParticipantToGroup(@PathVariable id: Int, @RequestBody participantInput: ParticipantInput) = service.addParticipantToGroupById(id, participantInput)?.let {
        ResponseEntity.ok(it)
    } ?: ResponseEntity("Группа не найдена", HttpStatus.NOT_FOUND)

    @DeleteMapping("/{groupId}/participant/{participantId}")
    fun deletePartFromGroup(@PathVariable groupId: Int, @PathVariable participantId: Int) = service.deletePartFromGroupById(groupId, participantId)?.let {
        ResponseEntity.ok().build()
    } ?: ResponseEntity("Группа не найдена", HttpStatus.NOT_FOUND)

    @Operation(summary = "addParticipantToGroup",
        responses = [
            ApiResponse(description = "Successful Operation", responseCode = "200", content = [Content(mediaType = "application/json", schema = Schema(implementation = ParticipantFull::class))]),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(hidden = true))]),
        ]
    )
    @PostMapping("/{id}/toss")
    fun toss(@PathVariable id: Int) = service.toss(id)?.let {
        when (it) {
            is TossSuccess -> ResponseEntity.ok(it.res)
            is TossError -> ResponseEntity("Проведение жеребьевки невозможно", HttpStatus.CONFLICT)
            else -> ResponseEntity("Группа не найдена", HttpStatus.NOT_FOUND)
        }
    } ?: ResponseEntity("Группа не найдена", HttpStatus.NOT_FOUND)

    @Operation(summary = "getRecipient",
        responses = [
            ApiResponse(description = "Successful Operation", responseCode = "200", content = [Content(mediaType = "application/json", schema = Schema(implementation = ParticipantOutput::class))]),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content(schema = Schema(hidden = true))]),
        ]
    )
    @GetMapping("/{groupId}/participant/{participantId}/recipient")
    fun getRecipient(@PathVariable groupId: Int, @PathVariable participantId: Int) = service.getRecipient(groupId, participantId)?.let {
        when (it) {
            is RecepientFindSuccess -> ResponseEntity.ok(it.res)
            is RecepientFindError -> ResponseEntity("Участник еще не дарит подарок", HttpStatus.NOT_FOUND)
            else -> ResponseEntity("Группа не найдена", HttpStatus.NOT_FOUND)
        }
    } ?: ResponseEntity("Группа не найдена", HttpStatus.NOT_FOUND)
}















