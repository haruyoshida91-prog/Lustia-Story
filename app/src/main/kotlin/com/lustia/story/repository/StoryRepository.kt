package com.lustia.story.repository

import com.google.gson.Gson
import com.lustia.story.data.Story

class StoryRepository {

    fun loadStory(): Story? {
        // For now, return a sample story
        // In production, this would load from JSON files, databases, or network
        return createSampleStory()
    }

    private fun createSampleStory(): Story {
        val scenes = mapOf(
            "intro" to com.lustia.story.data.Scene(
                id = "intro",
                text = "Welcome to Lustia Story. You are the Dungeon Boss, a powerful entity guarding your realm. An intruder has breached your sanctum. What do you do?",
                choices = listOf(
                    com.lustia.story.data.Choice(
                        id = "choice_confront",
                        text = "Confront the intruder directly",
                        nextSceneId = "confrontation"
                    ),
                    com.lustia.story.data.Choice(
                        id = "choice_observe",
                        text = "Observe from the shadows first",
                        nextSceneId = "observation"
                    )
                )
            ),
            "confrontation" to com.lustia.story.data.Scene(
                id = "confrontation",
                text = "You emerge from the darkness, your form towering over the intruder. They freeze in fear...",
                choices = listOf(
                    com.lustia.story.data.Choice(
                        id = "choice_intimidate",
                        text = "Intimidate them into submission",
                        nextSceneId = "ending_victory"
                    ),
                    com.lustia.story.data.Choice(
                        id = "choice_seduce",
                        text = "Use your charms to distract them",
                        nextSceneId = "ending_victory"
                    )
                )
            ),
            "observation" to com.lustia.story.data.Scene(
                id = "observation",
                text = "From the shadows, you study the intruder. They seem unprepared and nervous. An opportunity presents itself...",
                choices = listOf(
                    com.lustia.story.data.Choice(
                        id = "choice_ambush",
                        text = "Ambush them while they're distracted",
                        nextSceneId = "ending_victory"
                    )
                )
            ),
            "ending_victory" to com.lustia.story.data.Scene(
                id = "ending_victory",
                text = "The intruder is defeated. Your realm remains secure. Another day passes in your dungeon...",
                sceneType = com.lustia.story.data.SceneType.VICTORY,
                choices = listOf(
                    com.lustia.story.data.Choice(
                        id = "choice_restart",
                        text = "Play Again",
                        nextSceneId = "intro"
                    )
                )
            )
        )

        return Story(
            id = "lustia_001",
            title = "Lustia Story",
            description = "A story of a dungeon boss defending their realm",
            scenes = scenes,
            startSceneId = "intro"
        )
    }
}
