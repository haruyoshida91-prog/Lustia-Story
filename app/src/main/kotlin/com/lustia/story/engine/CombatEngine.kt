package com.lustia.story.engine

import com.lustia.story.data.Encounter
import com.lustia.story.data.GameState
import kotlin.random.Random

class CombatEngine {

    fun createEncounter(
        antagonistId: String,
        antagonistName: String,
        antagonistStats: Map<String, Int>,
        playerStats: Map<String, Int>
    ): Encounter {
        val playerHealth = (playerStats["health"] ?: 100)
        val antagonistHealth = (antagonistStats["health"] ?: 100)

        return Encounter(
            antagonistId = antagonistId,
            antagonistName = antagonistName,
            antagonistStats = antagonistStats,
            playerHealth = playerHealth,
            antagonistHealth = antagonistHealth
        )
    }

    fun playerAttack(encounter: Encounter, gameState: GameState): CombatResult {
        val playerAttack = (gameState.playerStats["strength"] ?: 10) + Random.nextInt(-5, 10)
        val antagonistDefense = (encounter.antagonistStats["defense"] ?: 5) + Random.nextInt(-3, 5)

        val damage = (playerAttack - antagonistDefense).coerceAtLeast(1)
        val newAntagonistHealth = (encounter.antagonistHealth - damage).coerceAtLeast(0)

        val isVictory = newAntagonistHealth <= 0

        return CombatResult(
            damageDealt = damage,
            newPlayerHealth = encounter.playerHealth,
            newAntagonistHealth = newAntagonistHealth,
            combatOver = isVictory,
            playerWon = isVictory,
            message = if (isVictory) "Victory!" else "Hit for $damage damage!"
        )
    }

    fun antagonistAttack(encounter: Encounter, gameState: GameState): CombatResult {
        val antagonistAttack = (encounter.antagonistStats["strength"] ?: 10) + Random.nextInt(-5, 10)
        val playerDefense = (gameState.playerStats["defense"] ?: 5) + Random.nextInt(-3, 5)

        val damage = (antagonistAttack - playerDefense).coerceAtLeast(1)
        val newPlayerHealth = (encounter.playerHealth - damage).coerceAtLeast(0)

        val isDefeat = newPlayerHealth <= 0

        return CombatResult(
            damageDealt = damage,
            newPlayerHealth = newPlayerHealth,
            newAntagonistHealth = encounter.antagonistHealth,
            combatOver = isDefeat,
            playerWon = false,
            message = if (isDefeat) "You have been defeated..." else "You take $damage damage!"
        )
    }
}

data class CombatResult(
    val damageDealt: Int,
    val newPlayerHealth: Int,
    val newAntagonistHealth: Int,
    val combatOver: Boolean,
    val playerWon: Boolean,
    val message: String
)
