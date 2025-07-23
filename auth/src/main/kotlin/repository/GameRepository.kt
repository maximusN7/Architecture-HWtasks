package org.example.repository

import org.example.model.Game
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameRepository : JpaRepository<Game, Long> {

    fun findByGameId(id: Long): Game?
}
