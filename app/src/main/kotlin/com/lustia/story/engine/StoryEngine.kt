package com.lustia.story.engine

import com.lustia.story.data.Choice
import com.lustia.story.data.GameState
import com.lustia.story.data.Scene
import com.lustia.story.data.Story

class StoryEngine(private val story: Story) {

    fun getCurrentScene(gameState: GameState): Scene? {
        return story.scenes[gameState.currentSceneId]
    }

    fun processChoice(choice: Choice, gameState: GameState): Boolean {
        // Check requirements
        choice.requirementCheck?.let { requirement ->
            val currentValue = gameState.playerStats[requirement.statName] ?: 0
            if (currentValue < requirement.minValue) {
                return false // Choice not available
            }
        }
        return true
    }

    fun getAvailableChoices(gameState: GameState): List<Choice> {
        val scene = getCurrentScene(gameState) ?: return emptyList()
        return scene.choices.filter { choice ->
            processChoice(choice, gameState)
        }
    }

    fun moveToScene(sceneId: String, gameState: GameState) {
        gameState.currentSceneId = sceneId
        gameState.visitedScenes.add(sceneId)

        // Apply scene stat changes
        getCurrentScene(gameState)?.statChanges?.forEach { (stat, change) ->
            val currentValue = gameState.playerStats[stat] ?: 0
            gameState.playerStats[stat] = (currentValue + change).coerceAtLeast(0)
        }
    }

    fun isGameOver(gameState: GameState): Boolean {
        val currentScene = getCurrentScene(gameState) ?: return false
        return currentScene.sceneType.name.contains("GAME_OVER")
    }

    fun isVictory(gameState: GameState): Boolean {
        val currentScene = getCurrentScene(gameState) ?: return false
        return currentScene.sceneType.name == "VICTORY"
    }
}
