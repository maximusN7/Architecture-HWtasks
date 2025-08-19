package org.example.controller

import org.example.clients.KafkaClient
import org.example.model.MoveRequest
import org.example.model.RotateRequest
import org.example.model.ShootRequest
import org.example.service.ApiService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ApiController(
    private val apiService: ApiService,
    private val kafkaClient: KafkaClient
) {

    @RequestMapping("/move")
    fun moveObject(@RequestBody request: MoveRequest): ResponseEntity<Any> {
        val message = apiService.mapToMessage(request)
        kafkaClient.sendMessageToGameServer(message)

        return ResponseEntity.ok("")
    }

    @RequestMapping("/rotate")
    fun rotateObject(@RequestBody request: RotateRequest): ResponseEntity<Any> {
        val message = apiService.mapToMessage(request)
        kafkaClient.sendMessageToGameServer(message)

        return ResponseEntity.ok("")
    }

    @RequestMapping("/shoot")
    fun shootFromObject(@RequestBody request: ShootRequest): ResponseEntity<Any> {
        val message = apiService.mapToMessage(request)
        kafkaClient.sendMessageToGameServer(message)

        return ResponseEntity.ok("")
    }
}
