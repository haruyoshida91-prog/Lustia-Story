package com.lustia.story.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameState(
    val currentSceneId: String,
    val playerStats: MutableMap<String, Int>,
    val inventory: MutableList<String>,
    val visitedScenes: MutableSet<String>,
    val currentEncounter: Encounter? = null,
    val money: Int = 0,
    val experience: Int = 0
) : Parcelable {
    fun clone(): GameState = copy(
        playerStats = playerStats.toMutableMap(),
        inventory = inventory.toMutableList(),
        visitedScenes = visitedScenes.toMutableSet()
    )
}

@Parcelize
data class Encounter(
    val antagonistId: String,
    val antagonistName: String,
    val antagonistStats: Map<String, Int>,
    val playerHealth: Int,
    val antagonistHealth: Int,
    val round: Int = 1
) : Parcelable

@Parcelize
data class PlayerCharacter(
    val name: String,
    val role: String,
    val stats: Map<String, Int>,
    val level: Int = 1,
    val experience: Int = 0
) : Parcelable
