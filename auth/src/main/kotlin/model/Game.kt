package org.example.model

import jakarta.persistence.*

@Entity
@Table(name = "games")
data class Game(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val gameId: Long = 0,
    @Column(unique = true)
    val gameName: String,
    @ElementCollection
    @CollectionTable(
        name = "game_participants",
        joinColumns = [JoinColumn(name = "game_id")]
    )
    @Column(name = "participant_name")
    val participantsNames: List<String> = emptyList()
)
