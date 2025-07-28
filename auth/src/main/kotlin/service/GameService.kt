package org.example.service

import org.example.model.Game
import org.example.repository.GameRepository
import org.example.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class GameService(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
) {

    fun registerGame(gameName: String, participants: List<String>): Long {
        participants.filter { username ->
            userRepository.findByUsername(username) != null
        }

        val game = Game(
            gameName = gameName,
            participantsNames = participants,
        )

        return gameRepository.save(game).gameId
    }

    fun isUserParticipantOfGame(username: String, gameId: Long): Boolean {
        val game = gameRepository.findByGameId(gameId) ?: return false

        return game.participantsNames.contains(username)
    }
}
